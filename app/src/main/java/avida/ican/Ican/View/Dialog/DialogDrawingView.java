package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byox.drawview.views.DrawView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.Structure.StructureOpticalPen;
import avida.ican.Ican.Presenter.DrawingViewPresenter;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.GravityEnum;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.OpticalPenDialogListener;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-11-04 at 10:39 PM
 */

public class DialogDrawingView {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialogOpticalPen;
    private OpticalPenDialogListener opticalPenDialogListener;
    private DrawingViewPresenter drawingViewPresenter;


    public class Binding {
        @BindView(R.id.draw_view)
        DrawView mDrawView;
        @BindView(R.id.ln_trash)
        LinearLayout lnTrash;
        @BindView(R.id.ln_eraser)
        LinearLayout lnEraser;
        @BindView(R.id.ln_redo)
        LinearLayout lnRedo;
        @BindView(R.id.ln_undo)
        LinearLayout lnUndo;
        @BindView(R.id.ln_pen)
        LinearLayout lnPen;
        @BindView(R.id.ln_color_blue)
        LinearLayout lnColorBlue;
        @BindView(R.id.ln_color_pink)
        LinearLayout lnColorPink;
        @BindView(R.id.ln_main_drawing_view)
        LinearLayout lnMainOpticalPen;
        @BindView(R.id.btn_dialog_drawing_view_cancel)
        Button btnDialogPpticalPenCancel;
        @BindView(R.id.btn_dialog_drawing_view_send)
        Button btnDialogDrawingViewOk;
        @BindView(R.id.edt_title)
        EditText edtTitle;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogDrawingView(Activity context) {
        this.context = context;
    }

    public DialogDrawingView setOnListener(OpticalPenDialogListener opticalPenDialogListener) {
        this.opticalPenDialogListener = opticalPenDialogListener;
        return this;
    }

    public void dismiss() {
        dialogOpticalPen.dismiss();
        App.canBack = true;
    }

    public void Show() {
        int screanHeightPX = new CustomFunction(context).getWidthOrHeightColums(false);
        screanHeightPX = new CustomFunction(context).dpToPx((screanHeightPX - 27) / 2);
        int drawViewHeight = screanHeightPX;

        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {
            dialogOpticalPen = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.layout_drawing_view))
                    .setGravity(Gravity.CENTER)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setCancelable(false)
                    .setHeader(R.layout.item_dialog_header)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            viewHolder = new Binding(dialogOpticalPen.getHolderView());
            View viewHolderHeader = dialogOpticalPen.getHeaderView();
            TextView txtHeader = viewHolderHeader.findViewById(R.id.txt_dialog_title);
            txtHeader.setText(Resorse.getString(R.string.title_dialog_optical_pen));
            initDrawingView();
            dialogOpticalPen.show();
            viewHolder.lnMainOpticalPen.setOnClickListener(view -> BaseActivity.closeKeyboard());
            viewHolder.mDrawView.getLayoutParams().height = drawViewHeight;
            viewHolder.mDrawView.requestLayout();
            viewHolder.mDrawView.setOnClickListener(view -> BaseActivity.closeKeyboard());
        });
    }


    private void initDrawingView() {
        initDrawingPresenter();
        drawingViewPresenter.ChangeModeToDraw();
        drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
        drawingViewPresenter.setToolSelected(GravityEnum.Center, viewHolder.lnPen);
        drawingViewPresenter.setColorSelected(GravityEnum.Right, viewHolder.lnColorBlue);

        viewHolder.lnUndo.setOnClickListener(view -> drawingViewPresenter.Undo());
        viewHolder.lnRedo.setOnClickListener(view -> drawingViewPresenter.Redo());
        viewHolder.lnTrash.setOnClickListener(view -> drawingViewPresenter.clearAll());

        viewHolder.lnColorPink.setOnClickListener(view -> {
            drawingViewPresenter.setColorSelected(GravityEnum.Center, viewHolder.lnColorPink);
            drawingViewPresenter.setToolSelected(GravityEnum.Center, viewHolder.lnPen);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_pink_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });

        viewHolder.lnColorBlue.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, viewHolder.lnPen);
            drawingViewPresenter.setColorSelected(GravityEnum.Right, viewHolder.lnColorBlue);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });

        viewHolder.lnEraser.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, viewHolder.lnEraser);
            drawingViewPresenter.setColorUnSelected();
            drawingViewPresenter.setColorUnSelected();
            drawingViewPresenter.ChangeModeToERASER();
        });

        viewHolder.lnPen.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, viewHolder.lnPen);
            drawingViewPresenter.setColorSelected(GravityEnum.Right, viewHolder.lnColorBlue);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });

        viewHolder.btnDialogPpticalPenCancel.setOnClickListener(view -> {
            BaseActivity.closeKeyboard();
            opticalPenDialogListener.onCancel();
            dismiss();
        });

        viewHolder.btnDialogDrawingViewOk.setOnClickListener(view -> {
            BaseActivity.closeKeyboard();
            String Title = "" + viewHolder.edtTitle.getText();
            byte[] bytes = drawingViewPresenter.getDrawingAsByteArray();
            if (bytes != null) {
                StructureOpticalPen structureOpticalPen = new StructureOpticalPen(drawingViewPresenter.getFileAsBase64(bytes), Title, ".png");
                opticalPenDialogListener.onSuccess(structureOpticalPen);
                dismiss();
            } else {
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.optical_pen_send_error), SnackBarEnum.SNACKBAR_LONG_TIME);
            }
        });
    }

    private void initDrawingPresenter() {
        drawingViewPresenter = new DrawingViewPresenter(App.CurentActivity, viewHolder.mDrawView);
    }


}
