package avida.ican.Farzin.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.LoginPresenter;
import avida.ican.Farzin.View.Dialog.DialogFirstMetaDataSync;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Farzin.View.Validation.Validation;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.View.ActivityMain;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerCloseActivitys;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityLogin extends BaseActivity {
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
    @BindView(R.id.chk_remember)
    CheckBox chkRemember;
    @BindView(R.id.img_profile)
    ImageView imgProfile;

    @BindString(R.string.TitleFarzin)
    String Title;

    private LoginPresenter loginPresenter = new LoginPresenter();
    private String UserName;
    private String Password;
    private String ServerUrl;
    private FarzinPrefrences farzinPrefrences;
    private Loading loading;
    private boolean isLogOut = false;
    private boolean isRemember = false;
    private StructureUserAndRoleDB structureUserAndRoleDB;

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = new Loading(this).Creat();
        loading.Show();
        App.networkStatus = NetworkStatus.NoAction;
        App.setCurentProject(CurentProject.Ican);
        farzinPrefrences = getFarzinPrefrences();
        isLogOut = getIntent().getBooleanExtra("LogOut", false);
        farzinPrefrences.putIsRemember(false);
        //initImgProfile();
        //if (isLogOut) {
        if (App.getFarzinBroadCastReceiver() != null) {
            App.getFarzinBroadCastReceiver().stopServices();
        }
        CloseActivitys(new ListenerCloseActivitys() {
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

       /* } else {
            if (!isRemember) {
                lnMain.setVisibility(View.VISIBLE);
                loading.Hide();
            } else {
                loginPresenter.AutoAuthentiocation(new LoginViewListener() {
                    @Override
                    public void onSuccess() {
                        goToActivity(FarzinActivityMain.class);
                        loading.Hide();
                        Finish(App.CurentActivity);
                    }

                    @Override
                    public void onAccessDenied() {
                        lnMain.setVisibility(View.VISIBLE);
                        loading.Hide();
                    }

                    @Override
                    public void onFailed() {
                        lnMain.setVisibility(View.VISIBLE);
                        loading.Hide();
                    }
                });
            }

        }*/
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

    private void initImgProfile() {
        int userId = farzinPrefrences.getUserID();
        int roleId = farzinPrefrences.getRoleID();
        if (userId > 0) {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(userId, roleId);
        }
        if (structureUserAndRoleDB != null) {
            if (!structureUserAndRoleDB.getLastName().isEmpty() && structureUserAndRoleDB.getLastName().length() >= 1) {
                String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
                Char = Char + "" + structureUserAndRoleDB.getLastName().substring(0, 1);
                imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            } else {
                String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
                imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            }
        }
    }


    private void Login() {
        boolean isRemember = chkRemember.isChecked();
        loginPresenter.Authentiocation(UserName, Password, ServerUrl, isRemember, new LoginViewListener() {
            @Override
            public void onSuccess() {
                goToActivity(FarzinActivityMain.class);
                loading.Hide();
                Finish(App.CurentActivity);
            }

            @Override
            public void onAccessDenied() {
                App.ShowMessage().ShowToast(Resorse.getString(R.string.server_acces_denied), ToastEnum.TOAST_LONG_TIME);
                loading.Hide();
            }

            @Override
            public void onFailed(String error) {
                App.ShowMessage().ShowToast(error, ToastEnum.TOAST_LONG_TIME);
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


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
