package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Enum.AttachEnum;
import avida.ican.Ican.View.Interface.ListenerAttach;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-05-30 at 12:47 PM
 */

public class DialogAttach {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialog;
    private ListenerAttach listenerAttach;

    public class Binding {

        @BindView(R.id.btn_camera_picker)
        Button btnCameraPicker;
        @BindView(R.id.btn_photo_picker)
        Button btnPhotoPicker;
        @BindView(R.id.btn_audio_recorder_picker)
        Button btnAudioRecorderPicker;
        @BindView(R.id.btn_video_picker)
        Button btnVideoPicker;
        @BindView(R.id.btn_file_picker)
        Button btnFilePicker;
        @BindView(R.id.img_cancel)
        ImageView imgCancel;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogAttach(Activity context) {
        this.context = context;
    }


    public DialogAttach setOnListener(ListenerAttach listenerAttach) {
        this.listenerAttach = listenerAttach;
        return this;
    }


    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

    public void Show() {

        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {

            dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_attach))
                    .setGravity(Gravity.BOTTOM)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setCancelable(false)
                    .setContentBackgroundResource(android.R.color.transparent)
                    .create();
            viewHolder = new Binding(dialog.getHolderView());
            viewHolder.btnCameraPicker.setOnClickListener(view -> {
                listenerAttach.onSuccess(AttachEnum.CameraPicker);
                dismiss();

            });
            viewHolder.btnPhotoPicker.setOnClickListener(view -> {
                listenerAttach.onSuccess(AttachEnum.GallaryPicker);
                dismiss();

            });
            viewHolder.btnVideoPicker.setOnClickListener(view -> {
                listenerAttach.onSuccess(AttachEnum.VideoPicker);
                dismiss();

            });
            viewHolder.btnAudioRecorderPicker.setOnClickListener(view -> {
                listenerAttach.onSuccess(AttachEnum.AudioRecorde);
                dismiss();

            });
            viewHolder.btnFilePicker.setOnClickListener(view -> {
                listenerAttach.onSuccess(AttachEnum.FilePicker);
                dismiss();

            });
            viewHolder.imgCancel.setOnClickListener(view -> {
                listenerAttach.onCancel();
                dismiss();
            });
            dialog.show();

        });


    }

}
