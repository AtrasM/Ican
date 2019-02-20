package avida.ican.Farzin.Presenter.Service.Cartable;

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
import android.widget.Toast;

import java.util.ArrayList;

import avida.ican.Farzin.FarzinCartableNotificationReceiver;
import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.GetCartableDocumentFromServerPresenter;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
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
 * Created by AtrasVida on 2018-09-11 at 13:06 PM
 */

public class GetCartableDocumentService extends Service {
    private final long DELAY = TimeValue.SecondsInMilli() * 30;
    private final long LOWDELAY = TimeValue.SecondsInMilli() * 5;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private CartableDocumentListListener cartableDocumentListListener;
    private Context context;
    private Handler handler = new Handler();
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private Status status = Status.IsNew;
    private int Count = 10;
    private final int MaxCount = 50;
    private final int MinCount = 10;
    private int notifyID = NotificationChanelEnum.Cartable.getValue();
    private Intent NotificationIntent;
    private static int existCont = 0;
    private static int dataSize = 0;
    private static int newCount = 0;
    private long tempDelay = LOWDELAY;

    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
            Count = MinCount;
            callDataFinish();
        } else {
            Count = MaxCount;
        }
        getCartableDocumentFromServerPresenter = new GetCartableDocumentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentListListener = new CartableDocumentListListener() {
            @Override
            public void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {
                existCont = 0;
                dataSize = inboxCartableDocumentList.size();
                if (inboxCartableDocumentList.size() == 0) {
                    reGetData(MinCount);
                } else {
                    SaveData(inboxCartableDocumentList);

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
                    reGetData(Count);
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    }, FAILED_DELAY);
                } else {
                    reGetData(Count);
                }
            }
        };
        GetCartableDocument(Count);

        return Service.START_STICKY;
    }

    private void GetCartableDocument(int count) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            getFarzinPrefrences().putCartableLastCheckDate(CustomFunction.getCurentDateTime().toString());
            reGetData(Count);
        } else {
            if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                getCartableDocumentFromServerPresenter.GetCartableDocumentList(count, cartableDocumentListListener);
            } else {
                CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getCartableLastCheckDate(), tempDelay);
                getFarzinPrefrences().putCartableLastCheckDate(CustomFunction.getCurentDateTime().toString());
                if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                    getCartableDocumentFromServerPresenter.GetCartableDocumentList(count, cartableDocumentListListener);
                } else {
                    reGetData(Count);
                }
            }
        }


    }

    private void SaveData(final ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        final StructureInboxDocumentRES structureInboxDocumentRES = inboxCartableDocumentList.get(0);

        if (structureInboxDocumentRES.isRead()) {
            status = Status.READ;
        } else {
           /* if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
                status = Status.IsNew;
            } else {
                status = Status.UnRead;
            }*/
            status = Status.UnRead;
        }

        farzinCartableQuery.saveCartableDocument(structureInboxDocumentRES, Type.RECEIVED, status, new CartableDocumentQuerySaveListener() {

            @Override
            public void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB) {
                if(structureInboxDocumentDB==null || structureInboxDocumentDB.getId()<=0){
                    existCont++;
                }else{
                    newCount++;
                }

                try {
                    inboxCartableDocumentList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (inboxCartableDocumentList.size() == 0) {
                    if (existCont == dataSize) {
                        reGetData(MinCount);
                    } else {
                        GetCartableDocument(Count);
                        CallMulltiMessageNotification();
                    }

                    //reGetData();
                } else {
                    SaveData(inboxCartableDocumentList);
                }
            }

            @Override
            public void onExisting() {
                ShowToast("Duplicate ducument");
                //CallMulltiMessageNotification();
                existCont++;
                inboxCartableDocumentList.remove(0);
                if (inboxCartableDocumentList.size() == 0) {
                    if (existCont == dataSize) {
                        reGetData(MinCount);
                    } else {
                        GetCartableDocument(Count);
                        CallMulltiMessageNotification();
                    }

                    //reGetData();
                } else {
                    SaveData(inboxCartableDocumentList);
                }
                //reGetData(MinCount);
            }

            @Override
            public void onFailed(final String message) {
                ShowToast("save document onFailed");
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed(message);
                        }
                    }, FAILED_DELAY);
                } else {
                    reGetData(Count);
                }

            }

            @Override
            public void onCancel() {
                ShowToast("save document onCancel");
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    }, FAILED_DELAY);
                } else {
                    reGetData(Count);
                }
            }
        });

    }

    private void reGetData(int count) {
        ShowToast("re Get cartable document");
        callDataFinish();
        Count = count;
        if (App.activityStacks == null) {
            tempDelay = App.DELAYWhenAppClose;
        } else {
            if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                tempDelay = DELAY;
            } else {
                tempDelay = LOWDELAY;
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetCartableDocument(Count);
            }
        }, tempDelay);


    }

    private void callDataFinish() {
        getFarzinPrefrences().putCartableDocumentForFirstTimeSync(true);
        if (BaseActivity.dialogMataDataSync != null) {
            BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncCartableDocument);
        }
    }

    private void CallMulltiMessageNotification() {
        if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
            return;
        } /*else {
            if (App.networkStatus != null) {
                if (App.networkStatus == NetworkStatus.Syncing) {
                    return;
                }
            }
        }*/
        ShowToast("new CartableDocument");
        // long cartableDocumentCount = new FarzinCartableQuery().getCartableDocumentCount();
        String title = Resorse.getString(context, R.string.Cartable);
        String message = newCount + " " + Resorse.getString(context, R.string.NewCartableDocument);
        callNotification(title, "" + message, GetNotificationPendingIntent(GetMultiCartableDocumentIntent()));
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


    void callNotification(final String title, final String message, final PendingIntent pendingIntent) {
        try {
            String CHANNEL_ID = "Ican_Notification_CHID";// The id of the channel.
            CharSequence name = "Ican_Farzin";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;


            Notification notification =
                    new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setDeleteIntent(getDeleteIntent())
                            .setSmallIcon(R.drawable.ic_notification_inbox)
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
          /*  if (App.fragmentCartable != null && !App.fragmentCartable.isHidden()) {
                new FarzinCartableQuery().UpdateAllNewCartableDocumentStatusToUnreadStatus();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public PendingIntent getDeleteIntent() {

        Intent intentCancell = new Intent(context, FarzinCartableNotificationReceiver.class);
        intentCancell.putExtra(PutExtraEnum.ID.name(), notifyID);
        PendingIntent pendingIntentCancell = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intentCancell, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntentCancell;
    }

    private Intent GetMultiCartableDocumentIntent() {
        NotificationIntent = new Intent(context, FarzinNotificationClickManager.class);
        NotificationIntent.putExtra(PutExtraEnum.Notification.getValue(), PutExtraEnum.MultyCartableDocument.getValue());
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

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
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


}
