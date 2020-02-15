package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.JobServiceIDEnum;
import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.ScheduleCartableJobServicePresenter;
import avida.ican.Farzin.Presenter.ScheduleMessageJobServicePresenter;
import avida.ican.Farzin.Presenter.Service.CheckServerAviableJobService;
import avida.ican.Farzin.Presenter.Service.ParentJobService.CartableParentJobService;
import avida.ican.Farzin.Presenter.Service.ParentJobService.MessageParentJobService;
import avida.ican.Ican.App;

import static avida.ican.Ican.App.setServiceContext;

/**
 * Created by AtrasVida on 2018-06-25 at 4:28 PM
 */

public class FarzinBroadcastReceiver extends BroadcastReceiver {
    //public static HashMap<String, Stack<Intent>> serviceIntents = new HashMap<>();
    public int SERVICECOUNT = 0;
    private Context context;
    private CheckServerAviableJobService checkServerAviableJobService;
    private ScheduleCartableJobServicePresenter cartableJobServicePresenter;
    private ScheduleMessageJobServicePresenter messageJobServicePresenter;
    private JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;
    private JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;
    private CartableParentJobService cartableParentJobService;
    private MessageParentJobService messageParentJobService;
    private static final String SERVICETAG = "IcanJobService";
    private boolean isServiceRun = false;

    public FarzinBroadcastReceiver() {
        this.isServiceRun = false;
    }

    @Override
    public void onReceive(final Context context, Intent mintent) {
        this.context = context;
        SERVICECOUNT = 0;
        setServiceContext(context);
        stopServices();
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        runCheckServerAviableJob();
        isServiceRun = false;
        if (farzinPrefrences.isMetaDataForFirstTimeSync() && farzinPrefrences.isDataForFirstTimeSync() && farzinPrefrences.getUserName() != "-1") {
            runServices();
            CustomLogger.setLog("------- ***initBroadcastReceiver in if tag for run service*** -------");
        }
        CustomLogger.setLog("------- ***initBroadcastReceiver end line onReceive*** -------");
    }

    private void runCheckServerAviableJob() {
        getCheckServerAviableJobService().initJob();
    }

    public void runServices() {
        setServiceContext(context);
        SERVICECOUNT = 0;
        if (!isServiceRun) {
            runCheckServerAviableJob();
            getCartableParentJobService().initJob();
            getMessageParentJobService().initJob();
            isServiceRun = true;
            CustomLogger.setLog("------- ***||||| initBroadcastReceiver runServices ||||*** -------");
        }
    }

    public void stopServices() {
        isServiceRun = false;
        cancellJob(context, JobServiceIDEnum.DOCUMENT_OPRERATORS_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.IMPORT_DOCUMENT_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.DOCUMENT_ATTACHFILE_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.GET_CONFIRMATION_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.GET_CARTABLE_DOCUMENT_SERVICE_JOBID.getStringValue());

        cancellJob(context, JobServiceIDEnum.SEND_MESSAGE_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.GET_RECIEVE_MESSAGE_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.GET_SENT_MESSAGE_SERVICE_JOBID.getStringValue());

        cancellJob(context, JobServiceIDEnum.CARTABLE_PARENT_SERVICE_JOBID.getStringValue());
        cancellJob(context, JobServiceIDEnum.MESSAGE_PARENT_SERVICE_JOBID.getStringValue());
    }

    public void runBaseService() {
        if (!isServiceRun) {
            restartServices();
        }
    }

    public void restartServices() {
        stopServices();
        runServices();
    }

    private CheckServerAviableJobService getCheckServerAviableJobService() {
        return checkServerAviableJobService = new CheckServerAviableJobService().getInstance(context);
    }

    private MessageParentJobService getMessageParentJobService() {
        return messageParentJobService = new MessageParentJobService().getInstance(context);
    }

    private CartableParentJobService getCartableParentJobService() {
        return cartableParentJobService = new CartableParentJobService().getInstance(context);
    }


    @SuppressLint("LongLogTag")
    public void cancellJob(Context context, String jobID) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        dispatcher.cancel(jobID);
        Log.d(SERVICETAG, "Service is  cancell, Job " + jobID);
    }


    public int getServiceSyncCount() {
        return SERVICECOUNT;
    }

    /* private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }*/


}

