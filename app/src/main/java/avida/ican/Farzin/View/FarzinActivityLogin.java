package avida.ican.Farzin.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Farzin.View.Validation.Validation;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityLogin extends BaseToolbarActivity {
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.edt_user_name)
    EditText edtUserName;
    @BindView(R.id.edt_paasword)
    EditText edtPaasword;
    @BindView(R.id.edt_server_url)
    EditText edtServerUrl;
    @BindView(R.id.ln_main)
    LinearLayout lnMain;

    @BindString(R.string.TitleFarzin)
    String Title;

    LoginPresenter loginPresenter = new LoginPresenter();
    private String UserName;
    private String Password;
    private String ServerUrl;
    private FarzinPrefrences farzinPrefrences;
    Loading loading;

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new Loading(this).Creat();
        loading.Show();
        initTollBar(Title);
        App.setCurentProject(CurentProject.Ican);
        loginPresenter.AutoAuthentiocation(new LoginViewListener() {
            @Override
            public void onSuccess() {
                goToActivity(FarzinActivityMain.class);
                loading.Hide();
                Finish();
            }

            @Override
            public void onAccessDenied() {
                lnMain.setVisibility(View.VISIBLE);
                loading.Hide();
            }

            @Override
            public void onFailed() {
                loading.Hide();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                closeKeboard();
                if (isValid()) {
                    loading.Show();
                    UserName = edtUserName.getText().toString();
                    Password = edtPaasword.getText().toString();
                    ServerUrl = edtServerUrl.getText().toString();
                    Login();
                }
            }
        });
    }

    private void Login() {
        loginPresenter.Authentiocation(UserName, Password, ServerUrl, new LoginViewListener() {
            @Override
            public void onSuccess() {
                goToActivity(FarzinActivityMain.class);
                loading.Hide();
                Finish();
            }

            @Override
            public void onAccessDenied() {
                App.ShowMessage().ShowToast("onAccessDenied", ToastEnum.TOAST_LONG_TIME);
                loading.Hide();
            }

            @Override
            public void onFailed() {
                App.ShowMessage().ShowToast("onFailed", ToastEnum.TOAST_LONG_TIME);
                loading.Hide();
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


}
