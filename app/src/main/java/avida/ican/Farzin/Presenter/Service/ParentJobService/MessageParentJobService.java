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
import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.ScheduleMessageJobServicePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.Failed;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.CARTABLE_PARENT_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.MESSAGE_PARENT_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.Finish;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.GetRecieveMessage;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.GetSentMessage;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.SendMessage;
import static com.firebase.jobdispatcher.FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS;

/**
 * Created by AtrasVida on 2019-11-16 at 11:02 AM
 */

public class MessageParentJobService extends JobService {
    private JobParameters job;
    private static final String TAG = "MessageParentJobService";
    private Context context;
    private static JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;
    private ScheduleMessageJobServicePresenter scheduleMessageJobServicePresenter;
    private final int maxTry = 3;
    private static int tryCount = 0;

    public MessageParentJobService getInstance(Context context) {
        this.context = context;
        return this;
    }

    public MessageParentJobService() {
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onStartJob(final JobParameters params) {

        try {
            CustomLogger.setLog("Service is  onStart, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue());
            context = this;
            tryCount = 0;
            App.setServiceContext(context);
            this.job = params;

            //scheduleMessageJobServicePresenter = ScheduleMessageJobServicePresenter.getInstance(context, initListener());
            scheduleMessageJobServicePresenter = new ScheduleMessageJobServicePresenter(context, initListener());
            if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
                scheduleMessageJobServicePresenter.runCustomJob(GetRecieveMessage);
            } else if (!getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                scheduleMessageJobServicePresenter.runCustomJob(GetSentMessage);
            } else {
                scheduleMessageJobServicePresenter.runCustomJob(SendMessage);
            }
            CustomLogger.setLog("Service onStart end block, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue());

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
        CustomLogger.setLog("Service was Stop, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue());

        return true;
    }

    private JobServiceMessageSchedulerListener initListener() {
        if (jobServiceMessageSchedulerListener == null) {
            jobServiceMessageSchedulerListener = new JobServiceMessageSchedulerListener() {
                @Override
                public void onSuccess(MessageJobServiceNameEnum jobServiceNameEnum) {
                    if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
                        checkJobService(GetRecieveMessage.ordinal());
                    } else if (!getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                        checkJobService(GetSentMessage.ordinal());
                    } else {
                        checkJobService(jobServiceNameEnum.ordinal() + 1);
                    }
                }

                @Override
                public void onFailed(MessageJobServiceNameEnum jobServiceNameEnum) {
                    if (tryCount <= maxTry) {
                        tryCount++;
                        checkJobService(jobServiceNameEnum.ordinal());
                    } else {
                        tryCount = 0;
                        checkJobService(jobServiceNameEnum.ordinal() + 1);
                    }
                }

                @Override
                public void onCancel(MessageJobServiceNameEnum jobServiceNameEnum) {
                    checkJobService(jobServiceNameEnum.ordinal() + 1);
                }

                @Override
                public void onFinish() {
                    reSchedule();
                }
            };
        }
        //jobServiceMessageSchedulerListener = jobServiceMessageSchedulerListener;
        return jobServiceMessageSchedulerListener;
    }

    private void checkJobService(int i) {

        if (i == GetRecieveMessage.ordinal()) {
            scheduleMessageJobServicePresenter.runCustomJob(GetRecieveMessage);
        } else if (i == GetSentMessage.ordinal()) {
            scheduleMessageJobServicePresenter.runCustomJob(GetSentMessage);
        } else if (i == SendMessage.ordinal()) {
            scheduleMessageJobServicePresenter.runCustomJob(SendMessage);
        } else if (i == Finish.ordinal()) {
            jobServiceMessageSchedulerListener.onFinish();
        } else if (i == Failed.ordinal()) {
            jobServiceMessageSchedulerListener.onFinish();
        } else {
            jobServiceMessageSchedulerListener.onFinish();
        }
    }

    private void reSchedule() {
        jobFinished(job, false);
        CustomLogger.setLog("Service is  Finished, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue());
        initJob();
    }

    @SuppressLint("LongLogTag")
    public void initJob() {
        final int DELAYWhenAppClose = 105;
        int tempDelay;
        if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync() || !getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
            tempDelay = 4;
        } else if (App.activityStacks == null) {
            tempDelay = DELAYWhenAppClose;
        } else {
            tempDelay = 65;
        }
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        cancellJob(dispatcher);
        Job myJob = dispatcher.newJobBuilder()
                .setService(MessageParentJobService.class) // the JobService that will be called
                .setTag(MESSAGE_PARENT_SERVICE_JOBID.getStringValue())// uniquely identifies the job
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(tempDelay, tempDelay))
                .build();

        int jobStatus = dispatcher.schedule(myJob);
        CustomLogger.setLog("Service is Scheduled as " + tempDelay + " seccond, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue() + " Scheduled " + (jobStatus == SCHEDULE_RESULT_SUCCESS ? "Success" : "Fail error code= " + jobStatus));
        CustomLogger.setLog("App.Context= " + App.getAppContext());
    }

    @SuppressLint("LongLogTag")
    public void cancellJob(FirebaseJobDispatcher dispatcher) {
        dispatcher.cancel(MESSAGE_PARENT_SERVICE_JOBID.getStringValue());
        CustomLogger.setLog("Service is  cancell, Job " + MESSAGE_PARENT_SERVICE_JOBID.getStringValue());
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
