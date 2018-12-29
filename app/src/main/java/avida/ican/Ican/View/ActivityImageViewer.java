package avida.ican.Ican.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

public class ActivityImageViewer extends BaseActivity {

    @BindView(R.id.image_viewer)
    ImageView imageViewer;
    @BindView(R.id.ln_image_view)
    LinearLayout lnImageView;

    public static byte[] byteArray;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_viewer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (byteArray == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_null_image), ToastEnum.TOAST_LONG_TIME);
            Finish();
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            setImage(bmp);
        }

    }

    private void setImage(Bitmap bmp) {
        byteArray = null;
        imageViewer.setImageBitmap(bmp);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    private void Finish() {
        byteArray = null;
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        App.activityStacks.values().remove(App.CurentActivity.getClass().getSimpleName());
    }
}
