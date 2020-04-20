package avida.ican.Farzin.Chat.SignalR;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Ican.App;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ErrorCallback;
import microsoft.aspnet.signalr.client.Logger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

public class SignalRSingleton {

    private static SignalRSingleton mInstance = null;
    private HubConnection mHubConnection;
    private HubProxy chatHubProxy;
    private HubProxy fnsHubProxy;
    private static FarzinPrefrences farzinPrefrences;
    private static SignalRService mService;
    private static boolean mBound = false;

    public static SignalRSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new SignalRSingleton();
            farzinPrefrences = new FarzinPrefrences().init();
            startSignalRService();
        }

        return mInstance;
    }

    @SuppressLint("StaticFieldLeak")
    private static void startSignalRService() {
     /*   Intent intent = new Intent();
        intent.setClass(App.getAppContext(), SignalRService.class);
        App.CurentActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);*/
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                Intent intent = new Intent();
                intent.setClass(App.getAppContext(), SignalRService.class);
                App.getAppContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                App.getFarzinBroadCastReceiver().runChatMessageQueueJobService();
                super.onPostExecute(aVoid);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private static final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public SignalRService getService() {
        return mService;
    }

    public boolean getBound() {
        return mBound;
    }

    public void setBound(boolean bound) {
        mBound = bound;
    }

    public void setmHubConnection() {
        String query = "userID=" + farzinPrefrences.getRoleIDToken() + "&actorID=" + farzinPrefrences.getActorIDToken() + "&mainActorID=" + farzinPrefrences.getActorIDToken() + "&clientType=MOBILE&ApplicationName=CHAT_NOTIFICATION&appVersion=" + App.getAppVersionName();
        String serverUrl = farzinPrefrences.getServerUrl() + "FarzinSoft/";
        Logger logger = (s, logLevel) -> s.toString();
        mHubConnection = new microsoft.aspnet.signalr.client.hubs.HubConnection(serverUrl, query, true, logger);
    }

    public void setChatHubProxy() {
        String chatHubName = "ChatRoom";
        chatHubProxy = getHubConnection().createHubProxy(chatHubName);
    }

    public void setFnsHubProxy() {
        String fnsHubName = "FNS";
        fnsHubProxy = getHubConnection().createHubProxy(fnsHubName);
    }

    /**
     * method for clients (activities)
     *
     * @return
     */
    public SignalRFuture<Void> sendMessage(String message, String roomID, String randomID) {
        if (chatHubProxy == null) {
            setChatHubProxy();
        }
        return chatHubProxy.invoke("SendMessage", roomID, message.trim(), randomID);
       /* SignalRFuture<Void> signalRFuture = chatHubProxy.invoke("SendMessage", roomID, message.trim(), randomID);
        signalRFuture.done(new Action<Void>() {
            @Override
            public void run(Void aVoid) throws Exception {
            }
        }).onError(new ErrorCallback() {
            @Override
            public void onError(Throwable throwable) {

            }
        }).onCancelled(new Runnable() {
            @Override
            public void run() {

            }
        });
        return signalRFuture;*/
    }

    public void validation() {
        if (fnsHubProxy == null) {
            setFnsHubProxy();
        }
        fnsHubProxy.on("validation", message -> {
            Log.d("<Debug", "new message received in `on`" + message);
        }, String.class);
    }

    public HubProxy getChatHubProxy() {
        if (chatHubProxy == null) {
            setChatHubProxy();
        }
        return chatHubProxy;
    }

    public HubProxy getFnsHubProxy() {
        if (fnsHubProxy == null) {
            setFnsHubProxy();
        }
        return fnsHubProxy;
    }

    public HubConnection getHubConnection() {
        if (mHubConnection == null) {
            setmHubConnection();
        }
        return mHubConnection;
    }

}
