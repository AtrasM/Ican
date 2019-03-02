package avida.ican.Farzin.Presenter.Service.Message;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import avida.ican.Farzin.FarzinMessageNotificationReceiver;
import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityWriteMessage;
import avida.ican.Farzin.View.FarzinNotificationClickManager;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-07-23 at 5:35 PM
 */

public class GetRecieveMessageService extends Service {
    private final long DELAY = TimeValue.SecondsInMilli() * 40;
    private final long LOWDELAY = TimeValue.SecondsInMilli() * 2;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private MessageListListener messageListListener;
    private Context context;
    private Handler handler = new Handler();
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private FarzinMessageQuery farzinMessageQuery;
    private int pageNumber = 1;
    private Status status = Status.IsNew;
    private int Count = 1;
    private final int MaxCount = 50;
    private final int MinCount = 1;
    private int notifyID = NotificationChanelEnum.Message.getValue();
    private Intent NotificationIntent;
    private static int newCount = 0;
    private long tempDelay = LOWDELAY;
    private boolean canShowNotification = true;

    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
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
                    canShowNotification = true;
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

        if (!canGetData()) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reGetMessage();
                }
            }, DELAY);
        } else {
            Log.i("pageNumber", "GetRecieveMessage pageNumber= " + pageNumber);
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                getFarzinPrefrences().putMessageRecieveLastCheckDate(CustomFunction.getCurentDateTime().toString());
                reGetMessage();
            } else {
                if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
                    getMessageFromServerPresenter.GetRecieveMessageList(pageNumber, count, messageListListener);
                } else {
                    CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getMessageRecieveLastCheckDate(), tempDelay);
                    getFarzinPrefrences().putMessageRecieveLastCheckDate(CustomFunction.getCurentDateTime().toString());
                    if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                        getMessageFromServerPresenter.GetRecieveMessageList(pageNumber, count, messageListListener);
                    } else {
                        reGetMessage();
                    }
                }

            }
        }
    }


    private void SaveMessage(final ArrayList<StructureMessageRES> messageList) {

        final StructureMessageRES structureMessageRES = messageList.get(0);

        if (structureMessageRES.isRead()) {
            status = Status.READ;
        } else {
            status = Status.UnRead;
        }

        ArrayList<StructureReceiverRES> receiversRES = new ArrayList<>();
        StructureUserAndRoleDB UserAndRoleDB = new FarzinMetaDataQuery(context).getUserInfo(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID());
        StructureReceiverRES receiverRES = new StructureReceiverRES(UserAndRoleDB.getRole_ID(), UserAndRoleDB.getUser_ID(), UserAndRoleDB.getLastName(), false, "");
        receiversRES.add(receiverRES);
        structureMessageRES.setReceivers(receiversRES);
        farzinMessageQuery.SaveMessage(structureMessageRES, Type.RECEIVED, status, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(StructureMessageDB structureMessageDB) {

                if (structureMessageDB.getId() > 0) {
                    newCount++;
                    if (App.fragmentMessageList != null) {
                        final ArrayList<StructureMessageDB> structureMessagesDB = new ArrayList<>();
                        structureMessagesDB.add(structureMessageDB);
                        if (App.CurentActivity != null) {
                            App.getHandlerMainThread().post(new Runnable() {
                                @Override
                                public void run() {
                                    if (App.fragmentMessageList != null) {
                                        App.fragmentMessageList.AddReceiveNewMessage(structureMessagesDB);
                                    }

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
                    CallMulltiMessageNotification();
                } else {
                    SaveMessage(messageList);
                }


            }

            @Override
            public void onExisting() {
                ShowToast("Duplicate message");
                reGetMessage();
                CallMulltiMessageNotification();

            }

            @Override
            public void onFailed(String message) {
                handler.postDelayed(new Runnable() {
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
                handler.postDelayed(new Runnable() {
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

    private void CallMulltiMessageNotification() {
        if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
            newCount = 0;
            return;
        }
        if (!canShowNotification) {
            return;
        }
        if (newCount > 0) {
            String title = Resorse.getString(context, R.string.newMessage);
            String message = newCount + " " + Resorse.getString(context, R.string.NewMessageContent);
            callNotification(title, "" + message, GetNotificationPendingIntent(GetMultiMessageIntent()));
        }
        newCount = 0;
    }


    private void reGetMessage() {
        ShowToast("re Get Message");
        callDataFinish();
        pageNumber = 1;
        Count = MinCount;
        if (App.activityStacks == null) {
            tempDelay = App.DELAYWhenAppClose;
        } else {
            if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
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
        getFarzinPrefrences().putReceiveMessageForFirstTimeSync(true);
        if (BaseActivity.dialogMataDataSync != null) {
            canShowNotification = false;
            BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncReceiveMessage);
        }
    }

    private boolean canGetData() {
        boolean can = false;
        if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync() && !getFarzinPrefrences().isDataForFirstTimeSync()) {
            can = false;
        } else {
            can = true;
        }
        return can;
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

    void callNotification(final String title, final String message, final PendingIntent pendingIntent) {
        try {
            String CHANNEL_ID = "Ican_Notification_CHID";// The id of the channel.
            CharSequence name = "Ican_Farzin";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;


            //CustomFunction.DismissNotification(context, notifyID);


            Notification notification =
                    new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setDeleteIntent(getDeleteIntent())
                            .setSmallIcon(R.drawable.ic_notification_message)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setChannelId(CHANNEL_ID).build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = null;
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                // Sets whether notifications posted to this channel should display notification lights
                mChannel.enableLights(true);
                // Sets whether notification posted to this channel should vibrate.
                mChannel.enableVibration(true);
                // Sets the notification light color for notifications posted to this channel
                mChannel.setLightColor(R.color.colorPrimaryDark);
                // Sets whether notifications posted to this channel appear on the lockscreen or not
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(mChannel);
            }

            // Issue the notification.
            mNotificationManager.notify(notifyID, notification);
           /* if (App.fragmentMessageList != null && !App.fragmentMessageList.isHidden()) {
                new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
                ShowToast("fragment message list is show");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public PendingIntent getDeleteIntent() {

        Intent intentCancell = new Intent(context, FarzinMessageNotificationReceiver.class);
        intentCancell.putExtra(PutExtraEnum.ID.name(), notifyID);
        PendingIntent pendingIntentCancell = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intentCancell, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntentCancell;
    }

    private Intent GetSimpleMessageIntent(int MessageID) {
        NotificationIntent = new Intent(context, FarzinActivityWriteMessage.class);
        NotificationIntent.putExtra(PutExtraEnum.ID.name(), -1);
        return NotificationIntent;
    }

    private Intent GetMultiMessageIntent() {
        NotificationIntent = new Intent(context, FarzinNotificationClickManager.class);
        NotificationIntent.putExtra(PutExtraEnum.Notification.getValue(), PutExtraEnum.MultyMessage.getValue());
        NotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return NotificationIntent;
    }

    private PendingIntent GetNotificationPendingIntent(Intent intent) {
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        return pendingIntent;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
