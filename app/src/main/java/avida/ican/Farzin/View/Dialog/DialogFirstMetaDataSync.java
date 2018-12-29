package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-28 at 11:00 AM
 */

public class DialogFirstMetaDataSync {
    private final Activity context;
    private static int SERVICECOUNT = 4;
    private static int serviceCounter = 0;
    private FarzinPrefrences farzinPrefrences;
    private static ArrayList<String> dataFinish = new ArrayList<>();
    private static MetaDataSyncListener metaDataSyncListener;

    public DialogFirstMetaDataSync(Activity context, MetaDataSyncListener metaDataSyncListener) {
        this.context = context;
        this.metaDataSyncListener = metaDataSyncListener;
        farzinPrefrences = new FarzinPrefrences().init();
    }

    public void serviceGetDataFinish(MetaDataNameEnum metaDataNameEnum) {
        Log.i("Log", "" + metaDataNameEnum.getStringValue());
        if (!dataFinish.contains(metaDataNameEnum.getStringValue())) {
            serviceCounter++;
            dataFinish.add(metaDataNameEnum.getStringValue());
        }


        try {
            if (serviceCounter >= SERVICECOUNT) {
                farzinPrefrences.putDataForFirstTimeSync(true);
                if (BaseActivity.dialogMataDataSync != null) {
                    dismiss();
                    App.canBack = true;
                }

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
