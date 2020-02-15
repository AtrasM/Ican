package avida.ican.Ican.View.Dialog;

import android.annotation.SuppressLint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida on 2018-03-19 at 11:06 AM
 */

public class NoNetworkAccess {

    private DialogPlus dialog;

    @SuppressLint("ResourceAsColor")
    static class Binding {
        @BindView(R.id.btn_exit)
        Button btnExit;
        @BindView(R.id.txt_desc)
        TextView txtDesc;
        @BindView(R.id.img_network_state)
        ImageView imgNetworkState;
        @BindView(R.id.img_refresh)
        ImageView imgRefresh;
        @BindView(R.id.avi_network_state)
        AVLoadingIndicatorView aviNetWorkState;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public void ShowDialog() {
        BaseActivity.closeKeyboard();
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.canBack = false;
                dialog = DialogPlus.newDialog(App.CurentActivity)
                        .setContentHolder(new ViewHolder(R.layout.dialog_network_state))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .create();
                final Binding viewHolder = new Binding(dialog.getHolderView());
                BaseActivity.dialog = dialog;
                //View view = dialog.getHolderView();
                viewHolder.aviNetWorkState.hide();
                viewHolder.imgRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.aviNetWorkState.setVisibility(View.VISIBLE);
                        viewHolder.imgRefresh.setVisibility(View.GONE);
                        viewHolder.txtDesc.setVisibility(View.GONE);
                        boolean NetworkAccess = new CheckNetworkAvailability().execuet();
                        if (NetworkAccess) {
                            dialog.dismiss();
                            App.dialogIsShow = false;
                            App.canBack = true;
                            App.CurentActivity.recreate();
                        } else {
                            viewHolder.aviNetWorkState.setVisibility(View.GONE);
                            viewHolder.imgRefresh.setVisibility(View.VISIBLE);
                            viewHolder.txtDesc.setVisibility(View.VISIBLE);
                        }

                    }
                });

                viewHolder.btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        App.canBack = true;
                        try {
                            BaseActivity.dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        App.CurentActivity.finish();
                        System.exit(0);
                    }
                });

                dialog.show();
            }

        });
    }
}
