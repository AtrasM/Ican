package avida.ican.Ican.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySplash extends BaseActivity {

    @BindView(R.id.av_loading_indicator_view)
    AVLoadingIndicatorView avLoadingIndicatorView;
    private LoginPresenter loginPresenter = new LoginPresenter();

    private boolean isRemember = false;
    private FarzinPrefrences farzinPrefrences = getFarzinPrefrences();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avLoadingIndicatorView.setIndicatorColor(Resorse.getColor(R.color.color_White));
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        isRemember = farzinPrefrences.isRemember();
        if (!isRemember) {
            goToActivity(FarzinActivityLogin.class);
            avLoadingIndicatorView.setVisibility(View.GONE);
            Finish(App.CurentActivity);
        } else {
            loginPresenter.AutoAuthentiocation(new LoginViewListener() {
                @Override
                public void onSuccess() {
                    goToActivity(FarzinActivityMain.class);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Finish(App.CurentActivity);
                }

                @Override
                public void onAccessDenied() {
                    goToActivity(FarzinActivityLogin.class);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Finish(App.CurentActivity);
                }

                @Override
                public void onFailed(String error) {
                    goToActivity(FarzinActivityLogin.class);
                    avLoadingIndicatorView.setVisibility(View.GONE);
                    Finish(App.CurentActivity);
                }


            });
        }


    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
