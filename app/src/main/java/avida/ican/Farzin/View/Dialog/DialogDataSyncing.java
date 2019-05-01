package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-28 at 11:00 AM
 */

public class DialogDataSyncing {
    private final Activity context;
    private int ServiceCount = -1;
    private int serviceCounter = 0;
    private boolean isMetaData = false;
    private FarzinPrefrences farzinPrefrences;
    private ArrayList<String> dataFinish = new ArrayList<>();
    private MetaDataSyncListener metaDataSyncListener;
    private long startTime;
    private long endTime;

    public DialogDataSyncing(Activity context, int ServiceCount, boolean isMetaData, MetaDataSyncListener metaDataSyncListener) {
        this.isMetaData = isMetaData;
        this.context = context;
        this.ServiceCount = ServiceCount;
        this.metaDataSyncListener = metaDataSyncListener;
        farzinPrefrences = new FarzinPrefrences().init();
        startTime = System.nanoTime();
        Log.i("SyncTime", "StartSyncTime= " + startTime);
    }

    public void serviceGetDataFinish(DataSyncingNameEnum dataSyncingNameEnum) {
        Log.i("DataSyncingNameEnum", "" + dataSyncingNameEnum.getStringValue());
        if (!dataFinish.contains(dataSyncingNameEnum.getStringValue())) {
            serviceCounter++;
            dataFinish.add(dataSyncingNameEnum.getStringValue());
        }

        try {
            if (serviceCounter >= ServiceCount) {
                Log.i("count", "serviceCounter= " + serviceCounter);
                Log.i("count", "ServiceCount= " + ServiceCount);
                endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;
                final double seconds = (double) elapsedTime / 1_000_000_000.0;
                Log.i("SyncTime", "endSyncTime= " + elapsedTime);
                Log.i("SyncTime", "elapsedTime as Seconds= " + seconds);

                if (isMetaData) {
                    farzinPrefrences.putMetaDataForFirstTimeSync(true);
                } else {
                    App.getHandlerMainThread().post(() -> new Message().ShowSnackBar("elapsedTime as Seconds= " + seconds, SnackBarEnum.SNACKBAR_LONG_TIME));
                    farzinPrefrences.putDataForFirstTimeSync(true);
                }

                try {
                    App.canBack = true;
                    context.runOnUiThread(() -> dismiss());

                } catch (Exception e) {
                    e.printStackTrace();
                }
              /*  if (BaseActivity.dialogDataSyncing != null) {
                    dismiss();
                    App.canBack = true;
                } */


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        Log.i("count", "dismiss" );
        App.canBack = true;
        Log.i("count", "dismiss serviceCounter= " + serviceCounter);
        Log.i("count", "dismiss ServiceCount= " + ServiceCount);
        ServiceCount = 0;
        serviceCounter = 0;
        if (metaDataSyncListener != null) {
            metaDataSyncListener.onFinish();
        }
        App.networkStatus = NetworkStatus.Connected;
        if (App.netWorkStatusListener != null) {
            App.netWorkStatusListener.Connected();
        }
        BaseActivity.dialog.dismiss();
        BaseActivity.dialogDataSyncing = null;
        BaseActivity.dialog = null;
    }

    public void Creat() {
      /*  if (farzinPrefrences.isDataForFirstTimeSync()) {
            return;
        }*/
        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(() -> {
            BaseActivity.dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_first_meta_data_sync))
                    .setGravity(Gravity.CENTER)
                    .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                    .setCancelable(false)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            Log.i("count", "BaseActivity.dialog.isShowing();= " + BaseActivity.dialog.isShowing());

            BaseActivity.dialog.show();
        });
        App.networkStatus = NetworkStatus.Syncing;
        if (App.netWorkStatusListener != null) {
            App.netWorkStatusListener.Syncing();
        }
    }
}
