package avida.ican.Farzin.Chat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import org.acra.ACRA;


import avida.ican.Farzin.Chat.SignalR.SignalRSingleton;
import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Farzin.View.Enum.Chat.ChatQueueResponse;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.SignalRFuture;

import static avida.ican.Farzin.Model.Enum.JobServiceIDEnum.SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID;
import static com.firebase.jobdispatcher.FirebaseJobDispatcher.SCHEDULE_RESULT_SUCCESS;

/**
 * Created by AtrasVida on 2020-04-11 at 14:52 PM
 */

public class ChatMessageQueueJobService extends JobService {

    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 15;
    private Context context;
    private int tryCount = 0;
    private final static int MaxTry = 3;
    private FarzinChatQuery farzinChatQuery;
    private JobParameters job;
    private SignalRSingleton signalRInstance;
    private BroadcastReceiver responseReceiver;

    public ChatMessageQueueJobService() {
    }

    public ChatMessageQueueJobService getInstance(Context context) {
        this.context = context;
        //this.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
        return this;
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        context = this;
        App.setServiceContext(context);
        this.job = job;
        try {
            farzinChatQuery = new FarzinChatQuery();
            new Thread(() -> {
                signalRInstance = SignalRSingleton.getInstance();
                startJob();
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
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void send(final StructureChatRoomMessageDB chatRoomMessageDB) {
        tryCount++;
        SignalRFuture<Void> data = signalRInstance.sendMessage(chatRoomMessageDB.getMessageContent(), chatRoomMessageDB.getChatRoomIDString(), chatRoomMessageDB.getMessageIDString());
        data.done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {
                tryCount++;
                tryAgain(chatRoomMessageDB);
            }
        }).onCancelled(new Runnable() {
            @Override
            public void run() {
                tryCount++;
                tryAgain(chatRoomMessageDB);
            }
        });
    }

    private void tryAgain(StructureChatRoomMessageDB structureChatRoomMessageDBS) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                send(structureChatRoomMessageDBS);
            }

            @Override
            public void disConnected() {
                tryCount++;
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    reSchedule();
                } else {
                    App.getHandlerMainThread().postDelayed(() -> tryAgain(structureChatRoomMessageDBS), FAILED_DELAY);
                }
            }

            @Override

            public void onFailed() {
                reSchedule();
            }
        });
    }

    private void initResponseReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatPutExtraEnum.QueueResponseReceiver.getValue());
        responseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null) {
                    if (intent.getAction().equals(ChatPutExtraEnum.QueueResponseReceiver.getValue())) {
                        String chatQueueResponse = intent.getStringExtra(ChatPutExtraEnum.QueueServiceResponse.getValue());
                        String messageTempID = intent.getStringExtra(ChatPutExtraEnum.QueueServiceResponse.getValue());

                       /* if (chatQueueResponse.equals(ChatQueueResponse.ShowSendMessage.getValue())) {
1
                        } else*/
                        if (chatQueueResponse.equals(ChatQueueResponse.Failed.getValue())) {
                            StructureChatRoomMessageDB chatRoomMessageDB = new FarzinChatQuery().getChatMessage(messageTempID);
                            if (chatRoomMessageDB != null) {
                                farzinChatQuery.setErrorCountChatMessageQueue(messageTempID, chatRoomMessageDB.getErrorCount() + 1);
                            }
                        }
                        checkData(false);

                    }

                }
            }
        };
        registerReceiver(responseReceiver, filter);
    }

    private void startJob() {
        try {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                reSchedule();
            } else {
                checkData(true);
            }

        } catch (Exception e) {
            reSchedule();
            e.printStackTrace();
        }
    }

    private void checkData(boolean initReceiver) {
        StructureChatRoomMessageDB chatRoomMessageDB = new FarzinChatQuery().getFirstChatRoomMessageQueue();
        if (chatRoomMessageDB != null && chatRoomMessageDB.getMessageIDString().length() > 0) {
            if (initReceiver) {
                initResponseReceiver();
            }
            send(chatRoomMessageDB);
        } else {
            reSchedule();
        }
    }

    private void reSchedule() {
        tryCount = 0;
        try {
            jobFinished(job, false);
            CustomLogger.setLog("Service is Finished, Job " + SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue());
            initJob();
        } catch (Exception e) {
            CustomLogger.setLog(e.toString());
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }

    }

    @SuppressLint("LongLogTag")
    public void initJob() {

        final int DELAYWhenAppClose = 95;
        int tempDelay;
        if (App.activityStacks == null) {
            tempDelay = DELAYWhenAppClose;
        } else {
            tempDelay = 10;
        }
        // Create a new dispatcher using the Google Play driver.
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        cancellJob(dispatcher);
        Job myJob = dispatcher.newJobBuilder()
                .setService(ChatMessageQueueJobService.class) // the JobService that will be called
                .setTag(SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue())// uniquely identifies the job
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(tempDelay, tempDelay))
                .build();

        int jobStatus = dispatcher.schedule(myJob);
        CustomLogger.setLog("Service is Scheduled as " + tempDelay + " seccond, Job " + SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue() + " Scheduled " + (jobStatus == SCHEDULE_RESULT_SUCCESS ? "Success" : "Fail error code= " + jobStatus));
        CustomLogger.setLog("App.Context= " + App.getAppContext());

    }

    @SuppressLint("LongLogTag")
    public void cancellJob(FirebaseJobDispatcher dispatcher) {
        dispatcher.cancel(SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue());
        CustomLogger.setLog("Service is  cancell, Job " + SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue());

    }

    @Override
    public void onDestroy() {

        try {
            if (responseReceiver != null) {
                unregisterReceiver(responseReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}