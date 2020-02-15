package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-12-23 at 12:10 PM
 */

public class DialogImageViewer {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialogImageViewer;
    private int width = 0;
    private int height = 0;

    public class Binding {

        @BindView(R.id.btn_dialog_image_viewer_close)
        Button btnDialogImageViewerClose;
        @BindView(R.id.image_viewer)
        ImageView imageViewer;
        @BindView(R.id.ln_image_view)
        LinearLayout lnImageView;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogImageViewer(Activity context) {
        this.context = context;
    }


    public void dismiss() {
        dialogImageViewer.dismiss();
        App.canBack = true;
    }

    public void show(byte[] byteArray) {
        Creat();
        if (dialogImageViewer != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            setImage(bmp);
            dialogImageViewer.show();
        }
    }

    public void show(Bitmap bmp) {
        Creat();
        if (dialogImageViewer != null) {
            setImage(bmp);
            dialogImageViewer.show();
        }
    }

    private void setImage(Bitmap bmp) {
        viewHolder.imageViewer.setImageBitmap(bmp);
    }

    public DialogImageViewer Creat() {
        width = new CustomFunction(context).getWidthOrHeightColums(true);
        height = new CustomFunction(context).getWidthOrHeightColums(false);
        width = (int) (new CustomFunction().dpToPx(width) / 1.2);
        height = (int) (new CustomFunction().dpToPx(height) / 1.2);
        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogImageViewer = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_image_viewer))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialogImageViewer.getHolderView());


                viewHolder.btnDialogImageViewerClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();

                    }
                });


            }

        });
        return this;
    }

}
