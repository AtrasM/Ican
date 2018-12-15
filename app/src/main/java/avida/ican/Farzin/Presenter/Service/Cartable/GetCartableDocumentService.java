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

import avida.ican.Farzin.FarzinMessageNotificationReceiver;
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
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinNotificationClickManager;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-09-11 at 13:06 PM
 */

public class GetCartableDocumentService extends Service {
    private final long DELAY = TimeValue.MinutesInMilli() * 35;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 30;
    private CartableDocumentListListener cartableDocumentListListener;
    private Context context;
    private Handler handler = new Handler();
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private Status status = Status.IsNew;
    private int Count = 1;
    private final int MaxCount = 50;
    private final int MinCount = 1;
    private int notifyID = 2;
    private Intent NotificationIntent;
    private static int existCont = 0;
    private static int dataSize = 0;

    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        Count = MaxCount;
        getCartableDocumentFromServerPresenter = new GetCartableDocumentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentListListener = new CartableDocumentListListener() {
            @Override
            public void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {
                existCont = 0;
                dataSize = inboxCartableDocumentList.size();
                if (inboxCartableDocumentList.size() == 0) {
                    if(BaseActivity.dialogMataDataSync!=null){
                        BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncCartableDocument);
                    }

                    reGetData(MaxCount);
                } else {
                    SaveData(inboxCartableDocumentList);

                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
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
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    },FAILED_DELAY);
                } else {
                    reGetData(Count);
                }
            }
        };
        GetCartableDocument(Count);

        return Service.START_STICKY;
    }

    private void GetCartableDocument(int count) {
        getCartableDocumentFromServerPresenter.GetCartableDocumentList(count, cartableDocumentListListener);
    }

    private void SaveData(final ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        final StructureInboxDocumentRES structureInboxDocumentRES = inboxCartableDocumentList.get(0);
        farzinCartableQuery.saveCartableDocument(structureInboxDocumentRES, Type.RECEIVED, status, new CartableDocumentQuerySaveListener() {

            @Override
            public void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB) {

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
            }

            @Override
            public void onExisting() {
                ShowToast("Duplicate ducument");
                //CallMulltiMessageNotification();
                existCont++;
                inboxCartableDocumentList.remove(0);
                if (inboxCartableDocumentList.size() == 0) {
                    if (existCont == dataSize) {
                        if(BaseActivity.dialogMataDataSync!=null){
                            BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncCartableDocument);
                        }
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
            public void onFailed(String message) {
                ShowToast("save message onFailed");
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    //ShowToast("WatingForNetwork");
                    onFailed("");
                } else {
                    reGetData(Count);
                }

            }

            @Override
            public void onCancel() {
                ShowToast("save message onCancel");
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    //ShowToast("WatingForNetwork");
                    onCancel();
                } else {
                    reGetData(Count);
                }
            }
        });

    }


    private void CallMulltiMessageNotification() {
        ShowToast("new CartableDocument");
        long cartableDocumentCount = new FarzinCartableQuery().getCartableDocumentCount();
        String title = Resorse.getString(context, R.string.Cartable);
        String message = cartableDocumentCount + " " + Resorse.getString(context, R.string.NewCartableDocument);
        callNotification(title, "" + message, GetNotificationPendingIntent(GetMultiCartableDocumentIntent()));
    }


    private void reGetData(int count) {
        ShowToast("re Get cartable document");
        Count = count;
       /* String CurentDateTime = CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.D                  ateTime_yyyy_MM_dd_hh_mm_ss.getValue());
        getFarzinPrefrences().putCartableDocumentDataSyncDate(CurentDateTime);*/
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetCartableDocument(Count);
            }
        }, DELAY);
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
            notifyID = 2;
            String CHANNEL_ID = "Ican_Notification_CHID";// The id of the channel.
            CharSequence name = "Ican_Farzin";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;


            //CustomFunction.DismissNotification(context, notifyID);


            Notification notification =
                    new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setDeleteIntent(getDeleteIntent())
                            .setSmallIcon(R.drawable.ic_notification)
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
            if (App.fragmentCartable != null && !App.fragmentCartable.isHidden()) {
                new FarzinCartableQuery().UpdateAllNewCartableDocumentStatusToUnreadStatus();
            }

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

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
