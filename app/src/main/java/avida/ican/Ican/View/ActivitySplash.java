package avida.ican.Ican.View;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.View;

import com.j256.ormlite.dao.Dao;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.SQLException;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CheckPermission;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogReCheckPermision;
import avida.ican.Ican.View.Enum.RequestCode;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivitySplash extends BaseActivity {

    @BindView(R.id.av_loading_indicator_view)
    AVLoadingIndicatorView avLoadingIndicatorView;

    private LoginPresenter loginPresenter = new LoginPresenter();
    private boolean isRemember = false;
    private FarzinPrefrences farzinPrefrences = getFarzinPrefrences();
    private Dao<StructureUserAndRoleDB, Integer> userAndRoleListDao;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avLoadingIndicatorView.setIndicatorColor(Resorse.getColor(R.color.color_White));
        boolean b = new CheckPermission().writeExternalStorage(1, App.CurentActivity);

        if (b) {
            isRemember = farzinPrefrences.isRemember();
            continueProcess();
        }
    }

    private void showDialog() {
        new DialogReCheckPermision(App.CurentActivity)
                .setTitle(Resorse.getString(R.string.shouldAllowexternalStoragePermision))
                .setOnListener(new ListenerQuestion() {
                    @Override
                    public void onSuccess() {
                        reCheck();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).Show();
    }

    private void reCheck() {
        boolean b = new CheckPermission().writeExternalStorage(1, App.CurentActivity);
    }


    private void continueProcess() {
        long userAndRoleRowsCount = 0;
        try {
            userAndRoleListDao = App.getFarzinDatabaseHelper().getUserAndRoleListDao();
            userAndRoleRowsCount = userAndRoleListDao.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        if (userAndRoleRowsCount <= 0 && farzinPrefrences.isMetaDataForFirstTimeSync()) {
            App.getHandlerMainThread().postDelayed(() -> CustomFunction.resetApp(App.CurentActivity, new ListenerQuestion() {
                @Override
                public void onSuccess() {
                    goToActivityLogin();
                }

                @Override
                public void onCancel() {
                    finish();
                }
            }), TimeValue.SecondsInMilli());
        } else {
            if (farzinPrefrences.isAnableAppLock() || !isRemember) {
                App.getHandlerMainThread().postDelayed(() -> {
                    goToActivityLogin();
                }, TimeValue.SecondsInMilli());
            } else {
                String userName = farzinPrefrences.getUserName();
                if (userName.isEmpty() || userName.equals("-1")) {
                    App.getHandlerMainThread().postDelayed(() -> {
                        goToActivityLogin();
                    }, TimeValue.SecondsInMilli());
                } else {
                    loginPresenter.AutoAuthentiocation(new LoginViewListener() {
                        @Override
                        public void onSuccess() {
                            goToActivityMain();
                        }

                        @Override
                        public void onAccessDenied() {
                            goToActivityMain();
                        }

                        @Override
                        public void onFailed(String error) {
                            goToActivityLogin();
                        }

                        @Override
                        public void invalidLogin(String error) {
                            App.ShowMessage().ShowToast(error, ToastEnum.TOAST_LONG_TIME);
                            goToActivityLogin();
                        }

                        @Override
                        public void invalidLicense(String error) {
                            App.ShowMessage().ShowToast(error, ToastEnum.TOAST_LONG_TIME);
                            goToActivityLogin();
                        }

                    });
                }

            }
        }

    }

    private void goToActivityLogin() {
        goToActivity(FarzinActivityLogin.class);
        //avLoadingIndicatorView.setVisibility(View.GONE);
        Finish(App.CurentActivity);
    }

    private void goToActivityMain() {
        goToActivity(FarzinActivityMain.class);
        //avLoadingIndicatorView.setVisibility(View.GONE);
        Finish(App.CurentActivity);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.WRITEEXTERNALSTORAGE.getValue()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                continueProcess();
            } else {
                showDialog();
            }
        }
    }

}
