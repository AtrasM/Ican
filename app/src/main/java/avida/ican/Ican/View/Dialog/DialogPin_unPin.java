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
import avida.ican.Ican.View.Enum.AttachEnum;
import avida.ican.Ican.View.Interface.ListenerAttach;
import avida.ican.Ican.View.Interface.ListenerPin_unPin;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-09-23 at 9:06 AM
 */

public class DialogPin_unPin {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialog;
    private ListenerPin_unPin listenerPin_unPin;

    public class Binding {

        @BindView(R.id.btn_pin)
        Button btnPin;
        @BindView(R.id.btn_unpin)
        Button btnUnpin;

        @BindView(R.id.btn_cancel)
        Button btnCancel;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogPin_unPin(Activity context) {
        this.context = context;
    }


    public DialogPin_unPin setOnListener(ListenerPin_unPin listenerPin_unPin) {
        this.listenerPin_unPin = listenerPin_unPin;
        return this;
    }


    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }
    public boolean isShowing() {
       return dialog.isShowing();
    }


    public void Show(final boolean isPin) {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_pin_unpin))
                        .setGravity(Gravity.BOTTOM)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        //.setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialog.getHolderView());
                viewHolder.btnPin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listenerPin_unPin.onPin();
                        dismiss();

                    }
                });
                viewHolder.btnUnpin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listenerPin_unPin.unPin();
                        dismiss();

                    }
                });
                viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listenerPin_unPin.onCancel();
                        dismiss();

                    }
                });
                if (isPin) {
                    viewHolder.btnPin.setVisibility(View.GONE);
                    viewHolder.btnUnpin.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnPin.setVisibility(View.VISIBLE);
                    viewHolder.btnUnpin.setVisibility(View.GONE);
                }
                dialog.show();

            }

        });


    }

}
