package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-28 at 11:00 AM
 */

public class DialogFirstMetaDataSync {
    private final Activity context;


    public DialogFirstMetaDataSync(Activity context) {
        this.context = context;
    }


    public void Creat() {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BaseActivity.dialogMataDataSync = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_first_meta_data_sync))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        //.setContentBackgroundResource(android.R.color.transparent)
                        .create();
                BaseActivity.dialogMataDataSync.show();

            }

        });
    }

}
