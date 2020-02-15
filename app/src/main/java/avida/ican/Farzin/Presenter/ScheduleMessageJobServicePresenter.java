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
import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Presenter.Service.Message.GetRecieveMessageJobService;
import avida.ican.Farzin.Presenter.Service.Message.GetSentMessageJobService;
import avida.ican.Farzin.Presenter.Service.Message.SendMessageJobService;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.GET_RECIEVE_MESSAGE_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.GET_SENT_MESSAGE_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.SEND_MESSAGE_SERVICE_JOBID;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.GetRecieveMessage;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.GetSentMessage;
import static avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum.SendMessage;

public class ScheduleMessageJobServicePresenter {
    private static final String TAG = "IcanJobService";
    private Context context;
    private final int maxTry = 3;
    private static int tryCount = 0;
    //private static volatile ScheduleMessageJobServicePresenter mInstance = null;
    private JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;

  /*  public  ScheduleMessageJobServicePresenter getInstance(Context context, JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener) {
        if (mInstance == null) {
            mInstance = new ScheduleMessageJobServicePresenter();
            tryCount = 0;
        }
        mInstance.jobServiceMessageSchedulerListener = jobServiceMessageSchedulerListener;
        mInstance.context = context;
        return mInstance;
    }*/

    public ScheduleMessageJobServicePresenter(Context context, JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener) {
        tryCount = 0;
        this.context = context;
        this.jobServiceMessageSchedulerListener = jobServiceMessageSchedulerListener;
    }

    public ScheduleMessageJobServicePresenter() {
    }

    public void runCustomJob(Class<? extends JobService> serviceClass, int startTimeAsSeccond, int endTimeAsSeccond, String jobID) {
        try {
            initJob(serviceClass, startTimeAsSeccond, endTimeAsSeccond, jobID);
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
    }

    public void runCustomJob(MessageJobServiceNameEnum jobService) {
        try {
            int defTime = 3;
            if (jobService == GetRecieveMessage) {
                initJob(new GetRecieveMessageJobService().getInstance(context, jobServiceMessageSchedulerListener).getClass(), defTime, defTime, GET_RECIEVE_MESSAGE_SERVICE_JOBID.getStringValue());
            } else if (jobService == GetSentMessage) {
                initJob(new GetSentMessageJobService().getInstance(context, jobServiceMessageSchedulerListener).getClass(), defTime, defTime, GET_SENT_MESSAGE_SERVICE_JOBID.getStringValue());
            } else if (jobService == SendMessage) {
                initJob(new SendMessageJobService().getInstance(context, jobServiceMessageSchedulerListener).getClass(), defTime, defTime, SEND_MESSAGE_SERVICE_JOBID.getStringValue());
            } else {
                jobServiceMessageSchedulerListener.onFinish();
            }
        } catch (Exception e) {
            if (tryCount <= maxTry) {
                tryCount++;
                runCustomJob(jobService);
            } else {
                tryCount = 0;
                jobServiceMessageSchedulerListener.onFinish();
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
        CustomLogger.setLog("Service is  Scheduled Message Child as " + startTimeAsSeccond + " seccond, Job " + jobID);

    }

    @SuppressLint("LongLogTag")
    public void cancellJob(String jobID, FirebaseJobDispatcher dispatcher) {
        dispatcher.cancel(jobID);
        Log.d(TAG, "Service is  cancell, Job " + jobID);
    }

}
