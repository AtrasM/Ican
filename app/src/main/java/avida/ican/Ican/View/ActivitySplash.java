package avida.ican.Ican.View;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
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
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogReCheckPermision;
import avida.ican.Ican.View.Enum.RequestCode;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;
import me.aflak.libraries.callback.FingerprintDialogCallback;
import me.aflak.libraries.dialog.DialogAnimation;
import me.aflak.libraries.dialog.FingerprintDialog;
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
            isRemember = farzinPrefrences.isRemember();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                continueProcess();
                //Fingerprint API only available on from Android 6.0 (M)
                FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
                if (!fingerprintManager.isHardwareDetected()) {
                    continueProcess();
                    // Device doesn't support fingerprint authentication
                } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                    continueProcess();
                    // User hasn't enrolled any fingerprints to authenticate with
                } else {

                    continueProcess();
                    //showDialogFingerPrint();
                    // Everything is ready for fingerprint authentication
                }
            }

        }


    }

    private void showDialogFingerPrint() {
        FingerprintDialog.initialize(this)
                .title(Resorse.getString(R.string.app_name))
                .message(Resorse.getString(R.string.fingerPrintMessage))
                .enterAnimation(DialogAnimation.Enter.BOTTOM)
                .exitAnimation(DialogAnimation.Exit.BOTTOM)
                .circleScanningColor(R.color.colorAccent)
                .callback(new FingerprintDialogCallback() {
                    @Override
                    public void onAuthenticationSucceeded() {
                        new Message().ShowSnackBar("valid:)", SnackBarEnum.SNACKBAR_SHORT_TIME);
                        continueProcess();
                    }

                    @Override
                    public void onAuthenticationCancel() {
                        new Message().ShowSnackBar("invalid(:", SnackBarEnum.SNACKBAR_SHORT_TIME);
                    }
                })
                .show();
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
