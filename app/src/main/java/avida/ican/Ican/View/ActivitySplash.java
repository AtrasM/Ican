package avida.ican.Ican.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CheckPermission;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogReCheckPermision;
import avida.ican.Ican.View.Enum.RequestCode;
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

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        avLoadingIndicatorView.setIndicatorColor(Resorse.getColor(R.color.color_White));
        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        boolean b = new CheckPermission().writeExternalStorage(1, App.CurentActivity);

        if (b) {
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
      /*  String filepath = new CustomFunction().saveFileToStorage("mahdiatras", "atrasTestFile");
        String data = new CustomFunction().getFileFromStorageAsByte(filepath);
        int index = filepath.lastIndexOf(".");
        if (index > 0) {
            filepath = filepath.substring(0, index);
        }*/
        //isRemember = false;
        isRemember = farzinPrefrences.isRemember();
        if (!isRemember) {
            goToActivity(FarzinActivityLogin.class);
            avLoadingIndicatorView.setVisibility(View.GONE);
            Finish(App.CurentActivity);
        } else {
            loginPresenter.AutoAuthentiocation(new LoginViewListener() {
                @Override
                public void onSuccess() {
                    //goToActivity(ActivityMain.class);
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
