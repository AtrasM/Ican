package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.acra.ACRA;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Presenter.Service.Cartable.DocumentAttachFileQueueJobService;
import avida.ican.Farzin.Presenter.Service.Cartable.DocumentOpreratorsQueueJobService;
import avida.ican.Farzin.Presenter.Service.Cartable.GetCartableDocumentJobService;
import avida.ican.Farzin.Presenter.Service.Cartable.GetConfirmationListJobService;
import avida.ican.Farzin.Presenter.Service.Cartable.ImportDocumentJobService;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.DOCUMENT_ATTACHFILE_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.DOCUMENT_OPRERATORS_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.GET_CARTABLE_DOCUMENT_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.GET_CONFIRMATION_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.IMPORT_DOCUMENT_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.AttachFile;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.DocumentOprator;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.GetCartable;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.GetConfirmation;
import static avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum.ImportDocument;

public class ScheduleCartableJobServicePresenter {
    private static final String TAG = "IcanJobService";
    private Context context;

    private final int maxTry = 3;
    private static int tryCount = 0;
    // private static ScheduleCartableJobServicePresenter mInstance = null;
    private JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;

/*    public static ScheduleCartableJobServicePresenter getInstance(Context context, JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
        if (mInstance == null) {
            mInstance = new ScheduleCartableJobServicePresenter();
            tryCount = 0;
        }
        mInstance.context = context;
        //App.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
        mInstance.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
        return mInstance;
    }*/

    public ScheduleCartableJobServicePresenter(Context context, JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
        tryCount = 0;
        this.context = context;
        this.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
    }

    private ScheduleCartableJobServicePresenter() {
    }

    public void runCustomJob(Class<? extends JobService> serviceClass, int startTimeAsSeccond, int endTimeAsSeccond, String jobID) {
        try {
            initJob(serviceClass, startTimeAsSeccond, endTimeAsSeccond, jobID);
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }

    }


    public void runCustomJob(CartableJobServiceNameEnum jobService) {
        try {
            int defTime = 3;
            if (jobService == DocumentOprator) {
                initJob(new DocumentOpreratorsQueueJobService().getInstance(context, jobServiceCartableSchedulerListener).getClass(), defTime, defTime, DOCUMENT_OPRERATORS_SERVICE_JOBID.getStringValue());
            } else if (jobService == GetConfirmation) {
                initJob(new GetConfirmationListJobService().getInstance(context, jobServiceCartableSchedulerListener).getClass(), defTime, defTime, GET_CONFIRMATION_SERVICE_JOBID.getStringValue());
            } else if (jobService == ImportDocument) {
                initJob(new ImportDocumentJobService().getInstance(context, jobServiceCartableSchedulerListener).getClass(), defTime, defTime, IMPORT_DOCUMENT_SERVICE_JOBID.getStringValue());
            } else if (jobService == AttachFile) {
                //error handler cannot run in thread
                initJob(new DocumentAttachFileQueueJobService().getInstance(context, jobServiceCartableSchedulerListener).getClass(), defTime, defTime, DOCUMENT_ATTACHFILE_SERVICE_JOBID.getStringValue());
            } else if (jobService == GetCartable) {
                initJob(new GetCartableDocumentJobService().getInstance(context, jobServiceCartableSchedulerListener).getClass(), defTime, defTime, GET_CARTABLE_DOCUMENT_SERVICE_JOBID.getStringValue());
            } else {
                jobServiceCartableSchedulerListener.onFinish();
            }
        } catch (Exception e) {
            if (tryCount <= maxTry) {
                tryCount++;
                runCustomJob(jobService);
            } else {
                tryCount = 0;
                jobServiceCartableSchedulerListener.onFinish();
                ACRA.getErrorReporter().handleSilentException(e);
            }
            e.printStackTrace();

        }
    }


    @SuppressLint("LongLogTag")
    private void initJob(Class<? extends JobService> serviceClass, int startTimeAsSeccond, int endTimeAsSeccond, String jobID) {
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        cancellJob(jobID, dispatcher);
        Job myJob = dispatcher.newJobBuilder()
                .setService(serviceClass) // the JobService that will be called
                .setTag(jobID)// uniquely identifies the job
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(startTimeAsSeccond, endTimeAsSeccond))
                .build();
        dispatcher.mustSchedule(myJob);
        Log.d(TAG, "Service is  running, Job " + jobID + " Scheduled.");
        CustomLogger.setLog("Service is  Scheduled Cartable Child as " + startTimeAsSeccond + " seccond, Job " + jobID);

    }

    @SuppressLint("LongLogTag")
    public void cancellJob(String jobID, FirebaseJobDispatcher dispatcher) {
        dispatcher.cancel(jobID);
        Log.d(TAG, "Service is  cancell, Job " + jobID);
    }
}
