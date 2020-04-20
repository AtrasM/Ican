package avida.ican.Farzin.Chat.SignalR;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;

import java.util.concurrent.ExecutionException;

import avida.ican.Farzin.Chat.RoomMessage.FarzinChatRoomMessagePresenter;
import avida.ican.Farzin.Model.Enum.JobServiceIDEnum;
import avida.ican.Farzin.Model.Structure.Bundle.chat.StructureChatHubProxyONBND;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Farzin.View.Enum.Chat.ChatConnectionStateEnum;
import avida.ican.Farzin.View.Enum.Chat.ChatMessageActionsEnum;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Farzin.View.Enum.Chat.ChatQueueResponse;
import avida.ican.Farzin.View.FarzinActivityChatRoomMessage;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ChatRoomMessageDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.ToastEnum;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.StateChangedCallback;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class SignalRService extends Service {
    private Handler mHandler; // to display Toast message
    private final IBinder mBinder = new LocalBinder(); // Binder given to clients
    private SignalRSingleton mInstance;
    private String LogTag = "ChatService";
    private FarzinChatRoomMessagePresenter farzinChatRoomMessagePresenter;
    private String SERVICETAG = "SignalRService";
    private Context context;
    private static ChatConnectionStateEnum connectionStateEnum = ChatConnectionStateEnum.NoAction;

    public SignalRService() {

    }

    @Override
    public void onCreate() {
        mInstance = SignalRSingleton.getInstance();
        mHandler = new Handler(Looper.getMainLooper());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        startSignalR();
        context = this;
        return result;
    }

    @Override
    public void onDestroy() {
        try {
            mInstance.getHubConnection().stop();
            cancellJob(context, JobServiceIDEnum.SEND_CHAT_MESSAGE_QUEUE_SERVICE_JOBID.getStringValue());
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the communication channel to the service.
        startSignalR();
        return mBinder;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public SignalRService getService() {
            // Return this instance of SignalRService so clients can call public methods
            return SignalRService.this;
        }
    }

    private void startSignalR() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        mInstance.setmHubConnection();
        mInstance.setChatHubProxy();
        mInstance.setFnsHubProxy();
        initHubConnectionStateListener();
        ClientTransport clientTransport = new ServerSentEventsTransport(mInstance.getHubConnection().getLogger());
        SignalRFuture<Void> signalRFuture = mInstance.getHubConnection().start(clientTransport).done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
                connectionStateEnum = ChatConnectionStateEnum.Connected;
                sendConnectionStatus(connectionStateEnum);
                App.ShowMessage().ShowToast("SignalR Done Connecting!", ToastEnum.TOAST_LONG_TIME);
            }
        });

        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mInstance.validation();
        initPresenter();


        //__________________________________________________________________________________

        mInstance.getChatHubProxy().on("receiveMessage", (chatRoomMessageModelRES, chatRoomModelRES) -> {
            App.getHandlerMainThread().post(() -> {
                farzinChatRoomMessagePresenter.saveInputMessage(chatRoomMessageModelRES, chatRoomModelRES);
                StructureChatHubProxyONBND structureChatHubProxyONBND = new StructureChatHubProxyONBND(chatRoomMessageModelRES, chatRoomModelRES, ChatMessageActionsEnum.ReceiveMessage);
                sendDataToActivityChatMessage(structureChatHubProxyONBND);
                sendDataToActivityChatRoom(chatRoomModelRES.getRoomType());
            });
        }, StructureChatRoomMessageModelRES.class, StructureChatRoomModelRES.class);

        //--------------------------------------------------

        mInstance.getChatHubProxy().on("showSendMessage", (chatRoomMessageModelRES, chatRoomModelRES, tempMessageID) -> {
            App.getHandlerMainThread().post(() -> {

                chatRoomMessageModelRES.setCurrentUserIsWriter(true);
                farzinChatRoomMessagePresenter.saveInputMessage(chatRoomMessageModelRES, tempMessageID);
                farzinChatRoomMessagePresenter.updateChatRoomLastMessageContent(chatRoomModelRES.getChatRoomID(), chatRoomMessageModelRES.getMessageContent(), chatRoomModelRES.getRoomType());
                StructureChatHubProxyONBND structureChatHubProxyONBND = new StructureChatHubProxyONBND(chatRoomMessageModelRES, chatRoomModelRES, tempMessageID, ChatMessageActionsEnum.ShowSendMessage);
                sendDataToActivityChatMessage(structureChatHubProxyONBND);
                sendDataToActivityChatRoom(chatRoomModelRES.getRoomType());
                sendDataToChatMessageQueueService(ChatQueueResponse.ShowSendMessage, tempMessageID);
            });

        }, StructureChatRoomMessageModelRES.class, StructureChatRoomModelRES.class, String.class);

        mInstance.getChatHubProxy().on("showDontSendMessage", (chatRoomModelRES, tempMessageID) -> {
            App.getHandlerMainThread().post(() -> {
                sendDataToChatMessageQueueService(ChatQueueResponse.Failed, tempMessageID);
            });

        }, StructureChatRoomModelRES.class, String.class);


        //__________________________________________________________________________________

    }


    private void initHubConnectionStateListener() {
        /*mInstance.getHubConnection().error(error -> App.ShowMessage().ShowToast(error.getMessage(), ToastEnum.TOAST_LONG_TIME));
        mInstance.getHubConnection().connected(() -> Log.i(LogTag, "mHubConnection State= connected"));*/
        mInstance.getHubConnection().closed(() -> {
            Log.i(LogTag, "mHubConnection State= closed , DISCONNECTED!");
            if (mInstance.getHubConnection().getState().toString() == "Disconnected") {
                new reconnectPulling().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }


        });
       /* mInstance.getHubConnection().reconnecting(() -> {
            Log.i(LogTag, "mHubConnection State= reconnecting");

        });
        mInstance.getHubConnection().reconnected(() -> Log.i(LogTag, "mHubConnection State= reconnected"));*/
        mInstance.getHubConnection().stateChanged((connectionState, connectionState1) -> {
            Log.i(LogTag, "mHubConnection State= stateChanged: " + connectionState.ordinal());
            switch (connectionState) {
                case Connecting:
                case Reconnecting: {
                    connectionStateEnum = ChatConnectionStateEnum.Connecting;
                    break;
                }
                case Connected: {
                    connectionStateEnum = ChatConnectionStateEnum.Connected;
                    break;
                }
                case Disconnected: {
                    connectionStateEnum = ChatConnectionStateEnum.Disconnected;
                    break;
                }
            }
            sendConnectionStatus(connectionStateEnum);
        });

    }

    private class reconnectPulling extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    if (mInstance.getHubConnection().getState().toString() != "Connected") {
                        mInstance.getHubConnection().start();
                        Log.d(LogTag, "Reconnect To Server...");
                        Thread.sleep(TimeValue.SecondsInMilli() * 5);
                    }
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }

            }
            return null;
        }
    }


    private void initPresenter() {
        farzinChatRoomMessagePresenter = new FarzinChatRoomMessagePresenter(new ChatRoomMessageDataListener() {
            @Override
            public void downloadedReplyData(StructureChatRoomMessageDB structureChatRoomMessageDB) {

            }

            @Override
            public void inputMessage(StructureChatRoomMessageDB structureChatRoomMessageDB) {
                App.getHandlerMainThread().post(() -> App.ShowMessage().ShowToast("new Chat Message.MessageContent= " + structureChatRoomMessageDB.getMessageContent(), ToastEnum.TOAST_LONG_TIME));
             /*   if (structureChatRoomMessageDB.getChatRoomIDString().equals(adapterChatRoomMessage.getLastMessage().getChatRoomIDString())) {
                    App.getHandlerMainThread().post(() -> {
                        msgInput.getInputEditText().setText("");
                        adapterChatRoomMessage.addDataToFirst(structureChatRoomMessageDB);
                        rcvChatRoomMessage.smoothScrollToPosition(0);
                    });
                }*/

            }

            @Override
            public void newData(StructureChatRoomMessageDB structureChatRoomMessageDB) {
            }

            @Override
            public void noData() {


            }
        });
    }

    private void sendDataToActivityChatMessage(StructureChatHubProxyONBND chatHubProxyONBND) {
        // Bundle bundleObject = new Bundle();
        Intent messageIntent = new Intent();
        messageIntent.setAction(ChatPutExtraEnum.MessageReceiver.getValue());
        FarzinActivityChatRoomMessage.chatHubProxyONBND = chatHubProxyONBND;
        //bundleObject.putSerializable(ChatPutExtraEnum.MessageBND.getValue(), chatHubProxyONBND);
        // messageIntent.putExtras(bundleObject);
        sendBroadcast(messageIntent);

    }

    private void sendDataToActivityChatRoom(int chatRoomTypeEnum) {
        Intent roomIntent = new Intent();
        roomIntent.setAction(ChatPutExtraEnum.RoomReceiver.getValue());
        roomIntent.putExtra(ChatPutExtraEnum.UpdateRoom.getValue(), chatRoomTypeEnum);
        sendBroadcast(roomIntent);

    }

    private void sendDataToChatMessageQueueService(ChatQueueResponse chatQueueResponse, String tempID) {
        Intent roomIntent = new Intent();
        roomIntent.setAction(ChatPutExtraEnum.QueueResponseReceiver.getValue());
        roomIntent.putExtra(ChatPutExtraEnum.QueueServiceResponse.getValue(), chatQueueResponse.getValue());
        roomIntent.putExtra(ChatPutExtraEnum.MessageTempID.getValue(), tempID);
        sendBroadcast(roomIntent);
    }

    private void sendConnectionStatus(ChatConnectionStateEnum chatConnectionStateEnum) {
        Intent connectionIntent = new Intent();
        connectionIntent.setAction(ChatPutExtraEnum.ConnectionReceiver.getValue());
        connectionIntent.putExtra(ChatPutExtraEnum.ConnectionStatus.getValue(), chatConnectionStateEnum);
        sendBroadcast(connectionIntent);
    }

    @SuppressLint("LongLogTag")
    public void cancellJob(Context context, String jobID) {
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(App.getAppContext()));
        dispatcher.cancel(jobID);
        Log.d(SERVICETAG, "Service is  cancell, Job " + jobID);
    }
}
