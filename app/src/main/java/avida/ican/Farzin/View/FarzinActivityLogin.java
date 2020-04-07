package avida.ican.Farzin.View;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Farzin.View.Validation.Validation;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogFingerPrint;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerCloseActivitys;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;
import me.aflak.libraries.callback.FingerprintDialogCallback;

public class FarzinActivityLogin extends BaseActivity {
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.edt_user_name)
    AutoCompleteTextView edtUserName;
    @BindView(R.id.edt_paasword)
    EditText edtPaasword;
    @BindView(R.id.edt_server_url)
    AutoCompleteTextView edtServerUrl;
    @BindView(R.id.ln_main)
    LinearLayout lnMain;
    @BindView(R.id.chk_remember)
    CheckBox chkRemember;
    @BindView(R.id.img_finger_print)
    ImageView imgFingerPrint;


    private LoginPresenter loginPresenter = new LoginPresenter();
    private String UserName;
    private String Password;
    private String ServerUrl;
    private FarzinPrefrences farzinPrefrences;
    private Loading loading;
    private boolean isLogOut = false;
    private boolean isRemember = false;
    private StructureUserAndRoleDB structureUserAndRoleDB;
    private boolean canUseFingerPrint = true;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //--------------------------
        loading = new Loading(this).setIndicatorColor(Resorse.getColor(R.color.color_White)).Creat();
        //--------------------------
        loginPresenter = new LoginPresenter(loading);
        App.setCurentProject(CurentProject.Ican);
        farzinPrefrences = getFarzinPrefrences();
        isLogOut = getIntent().getBooleanExtra("LogOut", false);
        isRemember = farzinPrefrences.isRemember();
        chkRemember.setChecked(isRemember);
        UserName = farzinPrefrences.getUserName();

        if (isLogOut) {
            farzinPrefrences.putIsRemember(false);
        }
        if (UserName.equals("-1")) {
            edtUserName.setText("");
        } else {
            edtUserName.setText(UserName);
        }
        edtServerUrl.setText(farzinPrefrences.getServerUrl());

        try {
            App.getFarzinBroadCastReceiver().stopServices();
        } catch (Exception e) {
            e.printStackTrace();
        }


        CloseActivitys(false, new ListenerCloseActivitys() {
            @Override
            public void onFinish() {
                lnMain.setVisibility(View.VISIBLE);
                loading.Hide();
            }

            @Override
            public void onCancel() {
                lnMain.setVisibility(View.VISIBLE);
                loading.Hide();
            }
        });


        btnSignIn.setOnClickListener(view -> {
            closeKeyboard();
            if (isValid()) {
                loading.Show();
                UserName = CustomFunction.arabicToDecimal(edtUserName.getText().toString());
                Password = CustomFunction.arabicToDecimal(edtPaasword.getText().toString());
                ServerUrl = CustomFunction.arabicToDecimal(edtServerUrl.getText().toString());
                Login();

            }
        });

        imgFingerPrint.setOnClickListener(view -> {
            showDialogFingerPrint();
        });
        //aoutoLogin();
        initFingerPrintDialog();

    }

    private void initFingerPrintDialog() {
        if (!farzinPrefrences.isMetaDataForFirstTimeSync() || !farzinPrefrences.isDataForFirstTimeSync()) {
            App.networkStatus = NetworkStatus.NoAction;
            canUseFingerPrint = false;
        } else {
            String userName = farzinPrefrences.getUserName();
            if (userName.isEmpty() || userName.equals("-1")) {
                App.networkStatus = NetworkStatus.NoAction;
                farzinPrefrences.putIsAnableFingerPrint(false);
                canUseFingerPrint = false;
            } else {
                if (farzinPrefrences.isAnableAppLock() && farzinPrefrences.isSupportFingerPrint() && farzinPrefrences.isAnableFingerPrint()) {
                    canUseFingerPrint = true;
                } else {
                    canUseFingerPrint = false;
                }
            }
        }

        if (canUseFingerPrint) {
            if (!isLogOut) {
                if (isRemember) {
                    showDialogFingerPrint();
                }
            }
        } else {
            imgFingerPrint.setVisibility(View.GONE);
        }

    }

    private void showDialogFingerPrint() {
        loading.Hide();
        DialogFingerPrint dialogFingerPrint = new DialogFingerPrint(this);
        dialogFingerPrint.setOnListener(new FingerprintDialogCallback() {
            @Override
            public void onAuthenticationSucceeded() {

                App.getHandlerMainThread().postDelayed(() -> dialogFingerPrint.dismiss(), 200);
                App.getHandlerMainThread().postDelayed(() -> aoutoLogin(), TimeValue.SecondsInMilli());

            }

            @Override
            public void onAuthenticationCancel() {

            }
        }).Show();

    }


    private void initImgProfile() {
        int userId = farzinPrefrences.getUserID();
        int roleId = farzinPrefrences.getRoleID();
        if (userId > 0) {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(userId, roleId);
        }
     /*   if (structureUserAndRoleDB != null) {
            if (!structureUserAndRoleDB.getLastName().isEmpty() && structureUserAndRoleDB.getLastName().length() >= 1) {
                String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
                Char = Char + "" + structureUserAndRoleDB.getLastName().substring(0, 1);
                imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            } else {
                String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
                imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            }
        }*/
    }


    private void Login() {
        boolean isRemember = chkRemember.isChecked();
        loginPresenter.Authentiocation(UserName, Password, ServerUrl, isRemember, new LoginViewListener() {
            @Override
            public void onSuccess() {
                goToActivityMain();
            }

            @Override
            public void onAccessDenied() {
                String lastUserName = farzinPrefrences.getUserName();
                String lastPassword = farzinPrefrences.getPassword();
                if (!lastUserName.isEmpty() && !lastUserName.equals("-1") && lastUserName.equals(UserName)) {
                    String encriptPassword = "";
                    try {
                        encriptPassword = WebService.EncriptToSHA1(Password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if (!lastPassword.isEmpty() && !lastPassword.equals("-1") && lastPassword.equals(encriptPassword)) {
                        goToActivityMain();
                    } else {
                        loading.Hide();
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.server_acces_denied), SnackBarEnum.SNACKBAR_LONG_TIME);
                    }
                } else {
                    loading.Hide();
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.server_acces_denied), SnackBarEnum.SNACKBAR_LONG_TIME);
                }
            }

            @Override
            public void onFailed(String error) {
                loading.Hide();
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_get_data_faild) + " : " + error, SnackBarEnum.SNACKBAR_INDEFINITE);
            }

            @Override
            public void invalidLogin(String error) {
                loading.Hide();
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_access_denied), SnackBarEnum.SNACKBAR_LONG_TIME);
            }

            @Override
            public void invalidLicense(String error) {
                farzinPrefrences.putIsRemember(false);
                loading.Hide();
                App.ShowMessage().ShowSnackBar(error, SnackBarEnum.SNACKBAR_INDEFINITE);

            }

        });
    }


    private boolean isValid() {
        Validation validation = new Validation();
        boolean isValid = true;
        if (validation.UserNameLen(edtUserName.getText().toString())) {
            edtUserName.setError(getString(R.string.error_invalid_user_name));
            isValid = false;
        }
        if (!validation.PassWordLen(edtPaasword.getText().toString())) {
            edtPaasword.setError(getString(R.string.error_invalid_password));
            isValid = false;
        }
        if (validation.PassWordContent(edtPaasword.getText().toString())) {
            edtPaasword.setError(getString(R.string.error_incorrect_password));
            isValid = false;
        }
        if (!validation.Url(edtServerUrl.getText().toString())) {
            edtServerUrl.setError(getString(R.string.error_incorrect_Url));
            isValid = false;
        }
        return isValid;
    }

    private void aoutoLogin() {
        loading.Show();
        farzinPrefrences.putIsRemember(true);
        loginPresenter.CheckNetwork(new ListenerNetwork() {
            @Override
            public void onConnected() {
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
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_get_data_faild) + " : " + error, SnackBarEnum.SNACKBAR_INDEFINITE);
                        loading.Hide();
                    }

                    @Override
                    public void invalidLogin(String error) {
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_access_denied), SnackBarEnum.SNACKBAR_LONG_TIME);
                        loading.Hide();
                    }

                    @Override
                    public void invalidLicense(String error) {

                        farzinPrefrences.putIsRemember(false);
                        loading.Hide();
                        App.ShowMessage().ShowSnackBar(error, SnackBarEnum.SNACKBAR_INDEFINITE);
                    }


                });
            }

            @Override
            public void disConnected() {
                goToActivityMain();
            }

            @Override
            public void onFailed() {

            }
        });

    }

    private void goToActivityMain() {
        loading.Hide();
        farzinPrefrences.putIsRemember(chkRemember.isChecked());
        goToActivity(FarzinActivityMain.class);
        App.canBack = true;
        App.isLoading = false;
        Finish(App.CurentActivity);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (doubleBackToExitPressedOnce) {
                Finish(App.CurentActivity);
                return true;
            } else {
                App.ShowMessage().ShowToast(Resorse.getString(R.string.msg_double_back), ToastEnum.TOAST_LONG_TIME);
            }
            doubleBackToExitPressedOnce = true;
            App.getHandlerMainThread().postDelayed(() -> {
                doubleBackToExitPressedOnce = false;
            }, TimeValue.SecondsInMilli() * 2);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
