package avida.ican.Ican.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.aflak.libraries.callback.FingerprintCallback;
import me.aflak.libraries.callback.FingerprintDialogCallback;

import me.aflak.libraries.view.Fingerprint;

/**
 * Created by AtrasVida on 2019-08-19 at 11:40 AM
 */

public class DialogFingerPrint {
    private final Activity context;
    private FingerprintDialogCallback listener;
    private Binding viewHolder;
    private DialogPlus dialogFingerPrint;
    private String mTitle = "";

    public class Binding {
        @BindView(R.id.btn_close)
        Button btnClose;
        @BindView(R.id.txt_error)
        TextView txtError;
        @BindView(R.id.fingerprint)
        Fingerprint fingerprint;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogFingerPrint(Activity context) {
        this.context = context;
    }


    public DialogFingerPrint setOnListener(FingerprintDialogCallback listener) {
        this.listener = listener;
        return this;
    }

    public void dismiss() {
        viewHolder.fingerprint.cancel();
        dialogFingerPrint.dismiss();
        App.canBack = true;
    }

    @SuppressLint("SetTextI18n")
    public void Show() {
        BaseActivity.closeKeyboard();
        App.canBack = false;

        context.runOnUiThread(() -> {
            dialogFingerPrint = DialogPlus.newDialog(context)
                    .setHeader(R.layout.item_header_dialog_finger_print)
                    .setContentHolder(new ViewHolder(R.layout.dialog_finger_print))
                    .setGravity(Gravity.CENTER)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setCancelable(false)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            viewHolder = new Binding(dialogFingerPrint.getHolderView());
            View viewHolderHeader = dialogFingerPrint.getHeaderView();
            TextView txtCodeVersion = viewHolderHeader.findViewById(R.id.txt_code_version);
            txtCodeVersion.setText(Resorse.getString(R.string.version) + " " + App.getAppVersionName());

            viewHolder.btnClose.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onAuthenticationCancel();
                }
                dismiss();
            });

            viewHolder.fingerprint.callback(new FingerprintCallback() {
                @Override
                public void onAuthenticationSucceeded() {
                    listener.onAuthenticationSucceeded();
                }

                @Override
                public void onAuthenticationFailed() {
                    //listener.onAuthenticationCancel();
                }

                @Override
                public void onAuthenticationError(int errorCode, String error) {
                    //listener.onAuthenticationCancel();
                    if (errorCode == 9) {
                        viewHolder.txtError.setText(Resorse.getString(R.string.error_finger_print_sensor_disabled));
                    } else if (errorCode == 7) {
                        viewHolder.txtError.setText(Resorse.getString(R.string.error_max_try_finger_print));
                    } else if (errorCode == 5) {
                        viewHolder.txtError.setText(Resorse.getString(R.string.error_finger_print_operation_canceled));
                    } else {
                        viewHolder.txtError.setText(error);
                    }
                    viewHolder.txtError.setVisibility(View.VISIBLE);

                  /*  errorCode = 9
                    "Too many attempts. Fingerprint sensor disabled."*/
                  /*  errorCode = 5
                    "Fingerprint operation canceled."*/
                  /*  errorCode = 7
                    "Too many attempts. Try again later."*/
                }
            });
            viewHolder.fingerprint
                    .fingerprintErrorColor(R.color.color_White)
                    .circleErrorColor(R.color.color_Red_dark)
                    .circleSuccessColor(R.color.color_Success)
                    .circleScanningColor(R.color.color_Info)
                    .authenticate();
            dialogFingerPrint.show();

        });


    }

}

