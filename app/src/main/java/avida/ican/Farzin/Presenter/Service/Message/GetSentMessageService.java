package avida.ican.Farzin.Presenter.Service.Message;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-08-08 at 4:00 PM
 */

public class GetSentMessageService extends Service {

    private final long DELAY = TimeValue.SecondsInMilli() * 40;
    private final long LOWDELAY = TimeValue.SecondsInMilli() * 2;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 30;
    private MessageListListener messageListListener;
    private Context context;
    private Handler handler = new Handler();
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private FarzinMessageQuery farzinMessageQuery;
    private int pageNumber = 1;
    private Status status = Status.UnRead;
    private int Count = 1;
    private final int MaxCount = 50;
    private final int MinCount = 1;
    private long tempDelay = LOWDELAY;


    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
            Count = MinCount;
            callDataFinish();
        } else {
            Count = MaxCount;
        }
        pageNumber = 1;
        getMessageFromServerPresenter = new GetMessageFromServerPresenter();
        farzinMessageQuery = new FarzinMessageQuery();
        messageListListener = new MessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                if (messageList.size() == 0) {
                    reGetMessage();
                } else {
                    SaveMessage(messageList);
                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    }, FAILED_DELAY);

                } else {
                    reGetMessage();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    }, FAILED_DELAY);

                } else {
                    reGetMessage();
                }
            }
        };
        GetMessage(pageNumber, Count);

        return Service.START_STICKY;
    }

    private void GetMessage(int pageNumber, int count) {
        Log.i("pageNumber", "GetSentMessage pageNumber= " + pageNumber);
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            getFarzinPrefrences().putMessageSentLastCheckDate(CustomFunction.getCurentDateTime().toString());
            reGetMessage();
        } else {
            if (!getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                getMessageFromServerPresenter.GetSentMessageList(pageNumber, count, messageListListener);
            } else {
                CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getMessageSentLastCheckDate(), tempDelay);
                getFarzinPrefrences().putMessageSentLastCheckDate(CustomFunction.getCurentDateTime().toString());
                if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                    getMessageFromServerPresenter.GetSentMessageList(pageNumber, count, messageListListener);
                } else {
                    reGetMessage();
                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void SaveMessage(final ArrayList<StructureMessageRES> messageList) {
        final StructureMessageRES structureMessageRES = messageList.get(0);

        AsyncTask<List<StructureReceiverRES>, Void, Void> asyncTask = new AsyncTask<List<StructureReceiverRES>, Void, Void>() {
            @Override
            protected Void doInBackground(List<StructureReceiverRES>[] lists) {
                int count = 0;
                for (int i = 0; i < lists.length; i++) {
                    if (lists[0].get(i).isRead()) {
                        count++;
                    }
                }
                if (count == lists.length) {
                    status = avida.ican.Farzin.Model.Enum.Status.READ;
                } else {
                    status = avida.ican.Farzin.Model.Enum.Status.UnRead;
                }
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureMessageRES.getReceivers());

        farzinMessageQuery.SaveMessage(structureMessageRES, Type.SENDED, status, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(final StructureMessageDB structureMessageDB) {
                if (structureMessageDB.getId() > 0) {
                    if (App.fragmentMessageList != null) {
                        if (App.CurentActivity != null) {
                            App.CurentActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    App.fragmentMessageList.UpdateSendMessageData();
                                    // UpdateAllNewMessageStatusToUnreadStatus();
                                }
                            });
                        }
                    }
                }
                try {
                    messageList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (messageList.size() == 0) {
                    pageNumber = pageNumber + 1;
                    GetMessage(pageNumber, Count);
                } else {
                    SaveMessage(messageList);
                }

            }

            @Override
            public void onExisting() {
                ShowToast("Duplicate message");
                reGetMessage();
            }

            @Override
            public void onFailed(String message) {
                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            messageList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveMessage(messageList);
                    }
                }, FAILED_DELAY);

                ShowToast("save message onFailed");
            }

            @Override
            public void onCancel() {
                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            messageList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveMessage(messageList);
                    }
                }, FAILED_DELAY);
                ShowToast("save message onCancel");
            }
        });
    }


    private void reGetMessage() {
        ShowToast("re Get Message");
        callDataFinish();
        pageNumber = 1;
        Count = MinCount;
        if (App.activityStacks == null) {
            tempDelay = App.DELAYWhenAppClose;
        } else {
            if (getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                tempDelay = DELAY;
            } else {
                tempDelay = LOWDELAY;
            }
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetMessage(pageNumber, Count);
            }
        }, tempDelay);
    }

    private void callDataFinish() {
        getFarzinPrefrences().putSendMessageForFirstTimeSync(true);
        if (BaseActivity.dialogMataDataSync != null) {
            BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncSendMessage);
        }
    }

    private void ShowToast(final String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (App.isTestMod) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void threadSleep() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (App.isTestMod) {
            Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        }
        //timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
