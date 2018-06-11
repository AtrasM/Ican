package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Interface.ListenerDelet;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-05-26 at 4:11 PM
 */

public class DialogDelet {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialog;
    private ListenerDelet listenerDelet;

    public class Binding {

        @BindView(R.id.btn_ok)
        Button mbtnOk;
        @BindView(R.id.btn_cancel)
        Button mbtnCancel;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogDelet(Activity context) {
        this.context = context;
    }


    public DialogDelet setOnListener(ListenerDelet listenerDelet) {
        this.listenerDelet = listenerDelet;
        return this;
    }


    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

    public void Show() {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_delet))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialog.getHolderView());
                viewHolder.mbtnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listenerDelet != null) listenerDelet.onSuccess();
                        dismiss();

                    }
                });
                viewHolder.mbtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listenerDelet != null) listenerDelet.onCancel();
                        dismiss();
                    }
                });
                dialog.show();

            }

        });


    }

}
