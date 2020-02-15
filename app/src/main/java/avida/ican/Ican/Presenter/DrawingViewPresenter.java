package avida.ican.Ican.Presenter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Base64;
import android.view.View;

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.enums.DrawingTool;
import com.byox.drawview.views.DrawView;

import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.GravityEnum;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-07-28 at 12:15 PM
 */

public class DrawingViewPresenter {
    private Activity context;
    private DrawView mDrawView;
    private View selectView;
    private GravityEnum selectViewGravity;
    private GravityEnum selectColorViewGravity;
    private View selectColorView;

    public DrawingViewPresenter(Activity context, DrawView mDrawView) {
        this.context = context;
        this.mDrawView = mDrawView;
    }

    public void Undo() {
        if (mDrawView.canUndo())
            mDrawView.undo();
    }

    public void Redo() {
        if (mDrawView.canRedo())
            mDrawView.redo();

    }

    public void clearAll() {
        mDrawView.restartDrawing();
    }

    public void ChangeModeToERASER() {
        SetDrawWidth(15);
        mDrawView.setDrawingMode(DrawingMode.ERASER);
        mDrawView.setDrawingTool(DrawingTool.CIRCLE);
    }

    public void ChangeModeToDraw() {
        SetDrawWidth(5);
        mDrawView.setDrawingMode(DrawingMode.DRAW);
        mDrawView.setDrawingTool(DrawingTool.PEN);
    }

    public void ChangeDrawingColor(int color) {
        mDrawView.setDrawColor(Resorse.getColor(color));
    }

    public void SetDrawWidth(int size) {
        mDrawView.setDrawWidth(size);
    }

    public String getFileAsBase64(byte[] file) {
        String s = Base64.encodeToString(file, Base64.DEFAULT);
        return s;
    }

    public byte[] getDrawingAsByteArray() {
        byte[] b = null;
        try {
            mDrawView.createCapture(DrawingCapture.BYTES);
            Bitmap bitmap = mDrawView.getDrawingCache();
            b = CustomFunction.BitmapToByteArray(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public void setToolSelected(GravityEnum gravityEnum, View curentView) {
        if (selectView != null) {
            switch (selectViewGravity) {
                case Left: {
                    selectView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_left_draw_tool));
                    break;
                }
                case Right: {
                    selectView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_right_draw_tool));
                    break;
                }
                default: {
                    selectView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_draw_tool));
                    break;
                }
            }

        }
        curentView.setBackground(Resorse.getDrawable(R.drawable.border_select_draw_tool));
        selectView = curentView;
        selectViewGravity = gravityEnum;
    }

    public void setColorSelected(GravityEnum gravityEnum, View curentView) {
        if (selectColorView != null) {
            switch (selectColorViewGravity) {
                case Left: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_left_draw_tool));
                    break;
                }
                case Right: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_right_draw_tool));

                    break;
                }
                default: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_draw_tool));
                    break;
                }
            }
        }
        curentView.setBackground(Resorse.getDrawable(R.drawable.border_select_draw_tool));
        selectColorView = curentView;
        selectColorViewGravity = gravityEnum;
    }

    public void setUnToolSelected(View curentView) {

        curentView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_draw_tool));
        selectView = null;
    }

    public void setColorUnSelected() {
        if (selectColorView != null) {
            switch (selectColorViewGravity) {
                case Left: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_left_draw_tool));
                    break;
                }
                case Right: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_right_draw_tool));

                    break;
                }
                default: {
                    selectColorView.setBackground(Resorse.getDrawable(R.drawable.border_unselect_draw_tool));
                    break;
                }
            }
        }
        selectColorView = null;
    }

}
