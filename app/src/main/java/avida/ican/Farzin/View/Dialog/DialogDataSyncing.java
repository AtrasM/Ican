package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataParentSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-04-28 at 11:00 AM
 */

public class DialogDataSyncing {
    private final Activity context;
    private int ServiceCount;
    private Binding viewHolder;
    private int serviceCounter;
    private boolean isManual = false;
    private boolean isMetaData;
    //private DialogPlus dialogSync;
    private FarzinPrefrences farzinPrefrences;
    private ArrayList<String> dataFinish = new ArrayList<>();
    private MetaDataParentSyncListener metaDataSyncListener;
    private long startTime;
    private long endTime;


    public class Binding {
        @BindView(R.id.ln_main)
        LinearLayout lnMain;


        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }


    public DialogDataSyncing(Activity context, int ServiceCount, boolean isMetaData, MetaDataParentSyncListener metaDataSyncListener) {
        this.isMetaData = isMetaData;
        this.context = context;
        this.ServiceCount = ServiceCount;
        this.serviceCounter = 0;
        this.metaDataSyncListener = metaDataSyncListener;
        farzinPrefrences = new FarzinPrefrences().init();
        startTime = System.nanoTime();
        Log.i("SyncTime", "StartSyncTime= " + startTime);
    }

    public DialogDataSyncing(Activity context, int ServiceCount, MetaDataParentSyncListener metaDataSyncListener) {
        this.isMetaData = false;
        this.isManual = true;
        this.context = context;
        this.ServiceCount = ServiceCount;
        this.serviceCounter = 0;
        this.metaDataSyncListener = metaDataSyncListener;
        farzinPrefrences = new FarzinPrefrences().init();
        startTime = System.nanoTime();
        Log.i("SyncTime", "StartSyncTime= " + startTime);
    }

    public void serviceGetDataFinish(DataSyncingNameEnum dataSyncingNameEnum) {
        if (!isManual) {
            checkServiceGetDataFinish(dataSyncingNameEnum);
        }
    }

    public void serviceGetDataFinish() {
        serviceCounter++;
        try {
            if (serviceCounter >= ServiceCount) {
                endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;
                final double seconds = (double) elapsedTime / 1_000_000_000.0;
                Log.i("SyncTime", "endSyncTime= " + elapsedTime);
                Log.i("SyncTime", "elapsedTime as Seconds= " + seconds);

                // App.getHandlerMainThread().post(() -> new Message().ShowSnackBar("elapsedTime as Seconds= " + seconds, SnackBarEnum.SNACKBAR_LONG_TIME));
                try {
                    App.canBack = true;
                    context.runOnUiThread(() -> dismiss());
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkServiceGetDataFinish(DataSyncingNameEnum dataSyncingNameEnum) {
        Log.i("DataSyncingNameEnum", "" + dataSyncingNameEnum.getStringValue());
        if (!dataFinish.contains(dataSyncingNameEnum.getStringValue())) {
            serviceCounter++;
            dataFinish.add(dataSyncingNameEnum.getStringValue());
        }

        try {
            if (serviceCounter >= ServiceCount) {
                endTime = System.nanoTime();
                long elapsedTime = endTime - startTime;
                final double seconds = (double) elapsedTime / 1_000_000_000.0;
                Log.i("SyncTime", "endSyncTime= " + elapsedTime);
                Log.i("SyncTime", "elapsedTime as Seconds= " + seconds);

                if (isMetaData) {
                    farzinPrefrences.putMetaDataForFirstTimeSync(true);
                } else {
                    //App.getHandlerMainThread().post(() -> new Message().ShowSnackBar("elapsedTime as Seconds= " + seconds, SnackBarEnum.SNACKBAR_LONG_TIME));
                    farzinPrefrences.putDataForFirstTimeSync(true);
                }

                try {
                    App.canBack = true;
                    context.runOnUiThread(() -> dismiss());

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        Log.i("count", "dismiss");
        App.canBack = true;
        Log.i("count", "dismiss serviceCounter= " + serviceCounter);
        Log.i("count", "dismiss serviceCount= " + ServiceCount);
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
        int screanHeightPX = new CustomFunction(context).getWidthOrHeightColums(false);
        screanHeightPX = new CustomFunction(context).dpToPx((screanHeightPX));
        int lnMainHeight = screanHeightPX;
        BaseActivity.dialog = null;
        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {

            BaseActivity.dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_data_sync))
                    .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                    .setGravity(Gravity.CENTER)
                    .setCancelable(false)
                    .setContentBackgroundResource(R.drawable.border_dialog_sync)
                    .create();
            viewHolder = new Binding(BaseActivity.dialog.getHolderView());
            viewHolder.lnMain.getLayoutParams().height = lnMainHeight;
            viewHolder.lnMain.requestLayout();
            BaseActivity.dialog.show();
        });
        App.networkStatus = NetworkStatus.Syncing;
        if (App.netWorkStatusListener != null) {
            App.netWorkStatusListener.Syncing();
        }
    }
}
