package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-28 at 11:00 AM
 */

public class DialogFirstMetaDataSync {
    private final Activity context;
    private static int SERVICECOUNT = MetaDataNameEnum.getMetaDataCount();
    private static int serviceCounter = 0;
    private FarzinPrefrences farzinPrefrences;
    private static ArrayList<String> dataFinish = new ArrayList<>();
    private static MetaDataSyncListener metaDataSyncListener;
    private long startTime;
    private long endTime;

    public DialogFirstMetaDataSync(Activity context, MetaDataSyncListener metaDataSyncListener) {
        this.context = context;
        this.metaDataSyncListener = metaDataSyncListener;
        farzinPrefrences = new FarzinPrefrences().init();
        startTime = System.nanoTime();
        Log.i("SyncTime", "StartSyncTime= " + startTime);
    }

    public void serviceGetDataFinish(MetaDataNameEnum metaDataNameEnum) {
        Log.i("MetaDataNameEnum", "" + metaDataNameEnum.getStringValue());
        if (!dataFinish.contains(metaDataNameEnum.getStringValue())) {
            serviceCounter++;
            dataFinish.add(metaDataNameEnum.getStringValue());
        }

        try {
            if (serviceCounter >= SERVICECOUNT) {
                endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;
                final double seconds = (double) elapsedTime / 1_000_000_000.0;
                Log.i("SyncTime", "endSyncTime= " + elapsedTime);
                Log.i("SyncTime", "elapsedTime as Seconds= " + seconds);
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        new Message().ShowSnackBar("elapsedTime as Seconds= " + seconds, SnackBarEnum.SNACKBAR_LONG_TIME);
                    }
                });
                farzinPrefrences.putDataForFirstTimeSync(true);
                try {
                    App.canBack = true;
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
              /*  if (BaseActivity.dialogMataDataSync != null) {
                    dismiss();
                    App.canBack = true;
                } */


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        if (metaDataSyncListener != null) {
            metaDataSyncListener.onFinish();
        }
        App.networkStatus = NetworkStatus.Connected;
        if (App.netWorkStatusListener != null) {
            App.netWorkStatusListener.Connected();
        }
        BaseActivity.dialogMataDataSync = null;
        App.canBack = true;
        BaseActivity.dialog.dismiss();
        BaseActivity.dialog = null;
    }

    public void Creat() {
      /*  if (farzinPrefrences.isDataForFirstTimeSync()) {
            return;
        }*/
        BaseActivity.closeKeboard();
        App.canBack = false;
        App.getHandlerMainThread().post(new Runnable() {
            @Override
            public void run() {
                BaseActivity.dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_first_meta_data_sync))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                BaseActivity.dialog.show();
            }
        });
        App.networkStatus = NetworkStatus.Syncing;
        if (App.netWorkStatusListener != null) {
            App.netWorkStatusListener.Syncing();
        }
    }
}
