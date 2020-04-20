package avida.ican.Farzin.Presenter.Service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Presenter.CheckServerAviablePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.acra.ACRA;

import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.CHECK_SERVER_AVIABLE_JOBID;
import static com.firebase.jobdispatcher.FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS;

/**
 * Created by AtrasVida on 2018-12-18 at 6:36 PM
 */

public class CheckServerAviableJobService extends JobService {
    private final int DELAYWhenAppClose = 40;
    private final int DELAY = 15;
    private ListenerNetwork listenerNetwork;
    private CheckServerAviablePresenter checkServerAviablePresenter;
    private int tempDelay;
    private JobParameters job;
    private static final String TAG = "CheckServerAviableJobService";
    private Context context;

    public CheckServerAviableJobService() {
    }

    public CheckServerAviableJobService getInstance(Context context) {
        this.context = context;
        App.setServiceContext(context);
        return this;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onStartJob(final JobParameters params) {
        try {
            CustomLogger.setLog("Service is onStart, Job " + CHECK_SERVER_AVIABLE_JOBID.getStringValue());
            context = this;
            App.setServiceContext(context);
            this.job = params;

            new Thread(() -> {
                initPresenterAndListener();
                Check();
            }).start();
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
        CustomLogger.setLog("Service was  Stop, Job " + CHECK_SERVER_AVIABLE_JOBID.getStringValue());
        return true;
    }

    private void initPresenterAndListener() {
        checkServerAviablePresenter = new CheckServerAviablePresenter();
        listenerNetwork = new ListenerNetwork() {
            @Override
            public void onConnected() {

                if (App.networkStatus != null) {
                    if (App.networkStatus == NetworkStatus.Syncing) {
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.Syncing();
                        }
                    } else {
                        App.networkStatus = NetworkStatus.Connected;
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.Connected();
                        }
                    }

                }
                reCheck();
            }

            @Override
            public void disConnected() {
                if (App.networkStatus != null) {
                    App.networkStatus = NetworkStatus.WatingForNetwork;
                    if (App.netWorkStatusListener != null) {
                        App.netWorkStatusListener.WatingForNetwork();
                    }
                }
                reCheck();
            }

            @Override
            public void onFailed() {
                if (App.networkStatus != null) {
                    App.networkStatus = NetworkStatus.WatingForNetwork;
                    if (App.netWorkStatusListener != null) {
                        App.netWorkStatusListener.WatingForNetwork();
                    }
                }
                reCheck();
            }
        };
    }

    private void Check() {
        checkServerAviablePresenter.CallRequest(listenerNetwork);
    }

    private void reCheck() {
        CustomLogger.setLog("App.networkStatus status= " + App.networkStatus + ", Job " + CHECK_SERVER_AVIABLE_JOBID.getStringValue());
        jobFinished(job, false);
        initJob();
    }

    @SuppressLint("LongLogTag")
    public void initJob() {
        cancellJob();
        if (App.activityStacks == null) {
            tempDelay = DELAYWhenAppClose;
        } else {
            tempDelay = DELAY;
        }
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        Job myJob = dispatcher.newJobBuilder()
                .setService(CheckServerAviableJobService.class) // the JobService that will be called
                .setTag(CHECK_SERVER_AVIABLE_JOBID.getStringValue())// uniquely identifies the job
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(tempDelay, tempDelay))
                .build();

        int jobStatus = dispatcher.schedule(myJob);
        CustomLogger.setLog("Service is Scheduled as " + tempDelay + " seccond, Job " + CHECK_SERVER_AVIABLE_JOBID.getStringValue() + " Scheduled " + (jobStatus == SCHEDULE_RESULT_SUCCESS ? "Success" : "Fail"));
    }

    @SuppressLint("LongLogTag")
    public void cancellJob() {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        dispatcher.cancel(CHECK_SERVER_AVIABLE_JOBID.getStringValue());
        CustomLogger.setLog("Service is  cancell, Job " + CHECK_SERVER_AVIABLE_JOBID.getStringValue());
    }

}
