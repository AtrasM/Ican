package avida.ican.Ican.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;
import butterknife.BindView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySplash extends BaseActivity {

    @BindView(R.id.av_loading_indicator_view)
    AVLoadingIndicatorView avLoadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToActivity(ActivityMain.class);
                Finish(App.CurentActivity);
            }
        }, 1000);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
