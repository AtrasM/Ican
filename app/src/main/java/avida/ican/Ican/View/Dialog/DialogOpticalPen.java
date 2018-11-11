package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.Structure.StructureOpticalPen;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.OpticalPenDialogListener;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-11-04 at 10:39 PM
 */

public class DialogOpticalPen {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialog;
    private OpticalPenDialogListener opticalPenDialogListener;
    private View selectView;
    private View selectColorView;

    public class Binding {
        @BindView(R.id.draw_view)
        DrawView mDrawView;
        @BindView(R.id.img_trash)
        ImageView imgTrash;
        @BindView(R.id.img_eraser)
        ImageView imgEraser;
        @BindView(R.id.img_redo)
        ImageView imgRedo;
        @BindView(R.id.img_undo)
        ImageView imgUndo;
        @BindView(R.id.img_pen)
        ImageView imgPen;
        @BindView(R.id.ln_color_danger)
        LinearLayout lnColorDanger;
        @BindView(R.id.ln_color_black)
        LinearLayout lnColorBlack;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.btn_send)
        Button btnSend;
        @BindView(R.id.edt_title)
        EditText edtTitle;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogOpticalPen(Activity context) {
        this.context = context;
    }


    public DialogOpticalPen setOnListener(OpticalPenDialogListener opticalPenDialogListener) {
        this.opticalPenDialogListener = opticalPenDialogListener;
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
                        .setContentHolder(new ViewHolder(R.layout.dialog_optical_pen))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setHeader(R.layout.item_dialog_header)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialog.getHolderView());
                View viewhelder = dialog.getHeaderView();
                TextView txtHeader = viewhelder.findViewById(R.id.txt_dialog_title);
                txtHeader.setText(Resorse.getString(R.string.title_dialog_optical_pen));
                initDrawingView();
                dialog.show();

            }

        });
    }

    private void initDrawingView() {
        ChangeModeToDraw();
        ChangeDrawingColor(R.color.color_Black);

        setSelected(viewHolder.imgPen);
        setColorSelected(viewHolder.lnColorBlack);
        viewHolder.imgUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Undo();
            }
        });
        viewHolder.imgRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Redo();
            }
        });
        viewHolder.imgTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });
        new CustomFunction(context).ChengeDrawableColorAndSetToImageView(viewHolder.imgTrash, R.drawable.ic_trash, R.color.color_Black);
        viewHolder.lnColorDanger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorSelected(viewHolder.lnColorDanger);
                ChangeDrawingColor(R.color.color_Danger);
            }
        });
        viewHolder.lnColorBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setColorSelected(viewHolder.lnColorBlack);
                ChangeDrawingColor(R.color.color_Black);
            }
        });
        viewHolder.imgEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(viewHolder.imgEraser);
                ChangeModeToERASER();
            }
        });
        viewHolder.imgPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(viewHolder.imgPen);
                ChangeModeToDraw();
            }
        });

        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opticalPenDialogListener.onCancel();
                dismiss();
            }
        });
        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = "" + viewHolder.edtTitle.getText();
                byte[] bytes = getDrawingAsByteArray();
                if (bytes != null) {
                    StructureOpticalPen structureOpticalPen = new StructureOpticalPen(getFileAsBase64(bytes), Title, ".png");
                    opticalPenDialogListener.onSuccess(structureOpticalPen);
                    dismiss();
                } else {
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.optical_pen_send_error), SnackBarEnum.SNACKBAR_LONG_TIME);
                }
            }
        });

    }

    private void Undo() {
        if (viewHolder.mDrawView.canUndo())
            viewHolder.mDrawView.undo();
    }

    private void Redo() {
        if (viewHolder.mDrawView.canRedo())
            viewHolder.mDrawView.redo();

    }

    private void clearAll() {
        viewHolder.mDrawView.restartDrawing();
    }

    private void ChangeModeToERASER() {
        SetDrawWidth(15);
        viewHolder.mDrawView.setDrawingMode(DrawingMode.ERASER);
        viewHolder.mDrawView.setDrawingTool(DrawingTool.CIRCLE);
    }

    private void ChangeModeToDraw() {
        SetDrawWidth(5);
        viewHolder.mDrawView.setDrawingMode(DrawingMode.DRAW);
        viewHolder.mDrawView.setDrawingTool(DrawingTool.PEN);
    }

    private void ChangeDrawingColor(int color) {
        viewHolder.mDrawView.setDrawColor(Resorse.getColor(color));
    }

    private void SetDrawWidth(int size) {
        viewHolder.mDrawView.setDrawWidth(size);
    }

    private String getFileAsBase64(byte[] file) {
        String s = Base64.encodeToString(file, Base64.DEFAULT);
        return s;
    }

    private byte[] getDrawingAsByteArray() {
        byte[] b = null;
        try {
            viewHolder.mDrawView.createCapture(DrawingCapture.BYTES);
            Bitmap bitmap = viewHolder.mDrawView.getDrawingCache();
            b = CustomFunction.BitmapToByteArray(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void setSelected(View curentView) {
        if (selectView != null) {

            selectView.setBackground(null);
        }
        curentView.setBackground(Resorse.getDrawable(R.drawable.border_selected));
        selectView = curentView;
    }

    private void setColorSelected(View curentView) {
        if (selectColorView != null) {

            selectColorView.setBackground(null);
        }
        curentView.setBackground(Resorse.getDrawable(R.drawable.border_cadr));
        selectColorView = curentView;
    }
}
