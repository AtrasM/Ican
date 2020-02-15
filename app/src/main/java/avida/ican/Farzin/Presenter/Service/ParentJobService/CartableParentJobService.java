package avida.ican.Farzin.Presenter.Service.ParentJobService;

import android.annotation.SuppressLint;
import android.content.Context;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.acra.ACRA;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.ScheduleCartableJobServicePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.AttachFile;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.DocumentOprator;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.Failed;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.Finish;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.GetCartable;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.GetConfirmation;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.ImportDocument;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.CARTABLE_PARENT_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.CHECK_SERVER_AVIABLE_JOBID;
import static com.firebase.jobdispatcher.FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS;

/**
 * Created by AtrasVida on 2019-11-13 at 11:02 AM
 */

public class CartableParentJobService extends JobService {
    private static final String TAG = "CartableParentJobService";
    private Context context;
    private JobParameters job;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;
    private ScheduleCartableJobServicePresenter scheduleCartableJobServicePresenter;
    private final int maxTry = 3;
    private static int tryCount = 0;

    public CartableParentJobService getInstance(Context context) {
        this.context = context;
        return this;
    }

    public CartableParentJobService() {
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onStartJob(final JobParameters params) {

        try {
            CustomLogger.setLog("Service is onStart, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue());
            context = this;
            tryCount = 0;
            App.setServiceContext(context);
            this.job = params;

            //scheduleCartableJobServicePresenter = ScheduleCartableJobServicePresenter.getInstance(context, initListener());
            scheduleCartableJobServicePresenter = new ScheduleCartableJobServicePresenter(context, initListener());
            if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                scheduleCartableJobServicePresenter.runCustomJob(GetCartable);
            } else {
                scheduleCartableJobServicePresenter.runCustomJob(DocumentOprator);
            }
            CustomLogger.setLog("Service is onStart end block, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }

        return false;
    }


    @SuppressLint("LongLogTag")
    @Override
    public boolean onStopJob(final JobParameters params) {
        CustomLogger.setLog("Service was  Stop, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue());

        return true;
    }

    private JobServiceCartableSchedulerListener initListener() {
        if (jobServiceCartableSchedulerListener == null) {
            jobServiceCartableSchedulerListener = new JobServiceCartableSchedulerListener() {
                @Override
                public void onSuccess(CartableJobServiceNameEnum jobServiceNameEnum) {
                    checkJobService(jobServiceNameEnum.ordinal() + 1);
                }

                @Override
                public void onFailed(CartableJobServiceNameEnum jobServiceNameEnum) {
                    if (tryCount <= maxTry) {
                        tryCount++;
                        checkJobService(jobServiceNameEnum.ordinal());
                    } else {
                        tryCount = 0;
                        checkJobService(jobServiceNameEnum.ordinal() + 1);
                    }
                }

                @Override
                public void onCancel(CartableJobServiceNameEnum jobServiceNameEnum) {
                    checkJobService(jobServiceNameEnum.ordinal() + 1);
                }

                @Override
                public void onFinish() {
                    reSchedule();
                }
            };
        }
        //App.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
        return jobServiceCartableSchedulerListener;
    }

    private void checkJobService(int i) {
        if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
            scheduleCartableJobServicePresenter.runCustomJob(GetCartable);
        } else {
            if (i == DocumentOprator.ordinal()) {
                scheduleCartableJobServicePresenter.runCustomJob(DocumentOprator);
            } else if (i == GetConfirmation.ordinal()) {
                scheduleCartableJobServicePresenter.runCustomJob(GetConfirmation);
            } else if (i == ImportDocument.ordinal()) {
                scheduleCartableJobServicePresenter.runCustomJob(ImportDocument);
            } else if (i == AttachFile.ordinal()) {
                scheduleCartableJobServicePresenter.runCustomJob(AttachFile);
            } else if (i == GetCartable.ordinal()) {
                scheduleCartableJobServicePresenter.runCustomJob(GetCartable);
            } else if (i == Finish.ordinal()) {
                jobServiceCartableSchedulerListener.onFinish();
            } else if (i == Failed.ordinal()) {
                jobServiceCartableSchedulerListener.onFinish();
            } else {
                jobServiceCartableSchedulerListener.onFinish();
            }
        }
    }

    private void reSchedule() {
        jobFinished(job, false);
        CustomLogger.setLog("Service is Finished, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue());
        initJob();
    }

    @SuppressLint("LongLogTag")
    public void initJob() {

        final int DELAYWhenAppClose = 95;
        int tempDelay;
        if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
            tempDelay = 2;
        } else if (App.activityStacks == null) {
            tempDelay = DELAYWhenAppClose;
        } else {
            tempDelay = 55;
        }
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        cancellJob(dispatcher);
        Job myJob = dispatcher.newJobBuilder()
                .setService(CartableParentJobService.class) // the JobService that will be called
                .setTag(CARTABLE_PARENT_SERVICE_JOBID.getStringValue())// uniquely identifies the job
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(tempDelay, tempDelay))
                .build();

        int jobStatus = dispatcher.schedule(myJob);
        CustomLogger.setLog("Service is Scheduled as " + tempDelay + " seccond, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue() + " Scheduled " + (jobStatus == SCHEDULE_RESULT_SUCCESS ? "Success" : "Fail error code= " + jobStatus));
        CustomLogger.setLog("App.Context= " + App.getAppContext());

    }

    @SuppressLint("LongLogTag")
    public void cancellJob(FirebaseJobDispatcher dispatcher) {
        dispatcher.cancel(CARTABLE_PARENT_SERVICE_JOBID.getStringValue());
        CustomLogger.setLog("Service is  cancell, Job " + CARTABLE_PARENT_SERVICE_JOBID.getStringValue());

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
