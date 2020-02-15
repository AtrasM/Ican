package avida.ican.Farzin.Presenter;

import android.util.Log;

import org.acra.ACRA;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.ChangeActiveRoleListener;
import avida.ican.Farzin.Model.Interface.UserRoleListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureUserRoleREQ;
import avida.ican.Farzin.Model.Structure.Response.StructureLoginDataRES;
import avida.ican.Farzin.Model.Structure.Response.StructureLoginRES;
import avida.ican.Farzin.Model.Structure.Response.StructureUserRoleRES;
import avida.ican.Farzin.Presenter.Cartable.ChangeActiveRolePresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinCartableDocumentPublicQueuePresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Farzin.View.Dialog.DialogUserRoleList;
import avida.ican.Farzin.View.Interface.CompareCurrentUserNameWithLastUserNameListener;
import avida.ican.Farzin.View.Interface.ListenerDialogUserRole;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Farzin.View.Interface.SetLicenseKeyListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;


/**
 * Created by AtrasVida on 2018-03-17 at 2:21 PM
 */

public class LoginPresenter {
    private LoginViewListener loginViewListener;
    private String ServerUrl = "";
    private String BaseUrl = "";
    private String UserName = "";
    private String Password = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "Authentication";
    private String MethodName = "Login3";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "Farzin";
    private FarzinPrefrences farzinPrefrences;
    private boolean IsPasswordEncript = false;
    private boolean isRemember = false;
    private Loading loadingDilaog;
    private boolean isAutomatic = false;
    private FarzinMetaDataQuery farzinMetaDataQuery;
    private List HeaderList = new ArrayList();

    public LoginPresenter() {
        farzinPrefrences = getFarzinPrefrences();
        try {
            farzinMetaDataQuery = new FarzinMetaDataQuery(App.CurentActivity);
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }

    }

    public LoginPresenter(Loading loadingDilaog) {
        farzinPrefrences = getFarzinPrefrences();
        try {
            farzinMetaDataQuery = new FarzinMetaDataQuery(App.CurentActivity);
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
        this.loadingDilaog = loadingDilaog;
    }

    public void AutoAuthentiocation(final LoginViewListener loginViewListener) {
        this.loginViewListener = loginViewListener;
        this.isRemember = farzinPrefrences.isRemember();
        isAutomatic = true;
        // Log.i("LOG","AutoAuthentiocation userName= "+farzinPrefrences.getUserName());
        if (!farzinPrefrences.getUserName().isEmpty() || !farzinPrefrences.getUserName().equals("-1")) {

            CheckNetwork(new ListenerNetwork() {
                @Override
                public void onConnected() {
                    CallApi(farzinPrefrences.getBaseUrl(), farzinPrefrences.getServerUrl(), farzinPrefrences.getUserName(), farzinPrefrences.getPassword(), true);
                }

                @Override
                public void disConnected() {
                    App.getHandlerMainThread().post(() -> {
                        App.networkStatus = NetworkStatus.WatingForNetwork;
                        if (App.activityStacks == null) {
                            loginViewListener.onAccessDenied();
                        } else {
                            if (!getFarzinPrefrences().isMetaDataForFirstTimeSync() || !getFarzinPrefrences().isDataForFirstTimeSync()) {
                                loginViewListener.onAccessDenied();
                            } else {
                                loginViewListener.onSuccess();
                            }
                        }

                    });
                }

                @Override
                public void onFailed() {

                }
            });


        } else {
            //loginViewListener.onAccessDenied();
            App.getHandlerMainThread().post(() -> {
                loginViewListener.invalidLogin("UserName= "+farzinPrefrences.getUserName());
            });
        }
    }

    public void Authentiocation(final String UserName, final String Password, String ServerUrl, boolean isRemember, final LoginViewListener loginViewListener) {
        this.isRemember = isRemember;
        isAutomatic = false;
        this.loginViewListener = loginViewListener;
        if (!ServerUrl.contains("http")) {
            ServerUrl = "http://" + ServerUrl;
        }
        if (!ServerUrl.substring(ServerUrl.length() - 1).equals("/")) {
            ServerUrl = ServerUrl + "/";
        }
        this.ServerUrl = ServerUrl;
        final String finalServerUrl = ServerUrl;
        farzinPrefrences.putServerUrl(ServerUrl);
        CheckNetwork(new ListenerNetwork() {
            @Override
            public void onConnected() {
                CallApi(farzinPrefrences.getBaseUrl(), finalServerUrl, UserName, Password, false);
            }

            @Override
            public void disConnected() {
                loginViewListener.onAccessDenied();
            }

            @Override
            public void onFailed() {
                loginViewListener.onAccessDenied();
            }
        });

    }

    private void CallApi(String BaseUrl, String ServerUrl, String UserName, String Password, boolean IsPasswordEncript) {
        this.UserName = UserName;
        this.Password = Password;
        this.ServerUrl = ServerUrl;
        this.BaseUrl = BaseUrl;
        this.IsPasswordEncript = IsPasswordEncript;
        new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
                .Authentication(UserName, Password, IsPasswordEncript)
                .setOnListener(new WebserviceResponseListener() {

                    @Override
                    public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                        processData(webServiceResponse);
                    }

                    @Override
                    public void NetworkAccessDenied() {
                        loginViewListener.onAccessDenied();
                    }
                }).execute();

    }

    private void processData(final WebServiceResponse webServiceResponse) {
        if (!webServiceResponse.isResponse()) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_get_data_faild));
            return;
        }
        String Xml = webServiceResponse.getHttpTransportSE().responseDump;
        //String Xml = new CustomFunction().readXmlResponseFromStorage();
        try {
            Xml = changeXml.charDecoder(Xml);
            Xml = changeXml.CropAsResponseTag(Xml, MethodName);
            if (Xml.contains("<Data>")) {
                Xml = Xml.replaceFirst("<Data>", "");
                Xml = Xml.replaceFirst("</Data>", "");
            }
            Log.i(Tag, "XmlCropAsResponseTag= " + Xml);
            StructureLoginRES structureLoginRES = xmlToObject.DeserializationSimpleXml(Xml, StructureLoginRES.class);
            if (structureLoginRES.isLogin3Result()) {
                this.HeaderList = webServiceResponse.getHeaderList();
                farzinPrefrences.putRoleIDToken(structureLoginRES.getData().getRoleIDToken());
                farzinPrefrences.putActorIDToken(structureLoginRES.getData().getMain_ActorToken());
                farzinPrefrences.putUserIDToken(structureLoginRES.getData().getMain_UserCodeToken());
                if (App.CurentActivity == null && App.activityStacks == null) {
                    continueeProcessData();
                } else {
                    farzinMetaDataQuery.saveUserRole(structureLoginRES.getData(), new UserRoleListener() {
                        @Override
                        public void onSuccess(StructureLoginDataRES structureLoginDataRES) {
                            if (structureLoginDataRES.getActiveRoles().size() == 1) {
                                StructureUserRoleRES structureUserRoleRES = structureLoginDataRES.getActiveRoles().get(0);
                                farzinPrefrences.putUserBaseInfo(getFarzinPrefrences().getUserName(), structureUserRoleRES.getRoleID(), structureUserRoleRES.getActorIDToken());
                                CheckLastUserName(new CompareCurrentUserNameWithLastUserNameListener() {
                                    @Override
                                    public void onEquals() {
                                        continueeProcessData();
                                    }

                                    @Override
                                    public void onCancel() {
                                    }

                                    @Override
                                    public void AppDataSuccessReset() {
                                        continueeProcessData();
                                    }
                                });

                            } else {
                                if (!isAutomatic) {
                                    CheckLastUserName(new CompareCurrentUserNameWithLastUserNameListener() {
                                        @Override
                                        public void onEquals() {
                                            showDialogUserRoles(structureLoginDataRES.getRoleIDToken(), structureLoginDataRES.getMain_UserCodeToken());
                                        }

                                        @Override
                                        public void onCancel() {
                                        }

                                        @Override
                                        public void AppDataSuccessReset() {
                                            showDialogUserRoles(structureLoginDataRES.getRoleIDToken(), structureLoginDataRES.getMain_UserCodeToken());
                                        }
                                    });

                                } else {
                                    continueeProcessData();
                                }
                            }

                        }

                        @Override
                        public void onFailed(String error) {
                            loginViewListener.onFailed(error);
                        }
                    });

                }

            } else {
                loginViewListener.invalidLogin(structureLoginRES.getStrErrorMsg());
            }
        } catch (Exception e) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_get_data_faild));
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
    }


    private void showDialogUserRoles(String curentRoleIDToken, String mainUserCodeToken) {
        try {
            if (loadingDilaog != null) {
                loadingDilaog.Hide();
            }
            if (BaseActivity.dialog != null) {
                BaseActivity.dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DialogUserRoleList dialogUserRoleList = new DialogUserRoleList(App.CurentActivity, farzinMetaDataQuery.getUserRole(), false);
        dialogUserRoleList.setOnListener(new ListenerDialogUserRole() {
            @Override
            public void onSuccess(StructureUserRoleDB userRoleDB) {
                StructureUserRoleREQ structureUserRoleREQ = new StructureUserRoleREQ(UserName, curentRoleIDToken, userRoleDB.getRoleIDToken(), userRoleDB.getRoleID(), userRoleDB.getActorIDToken(), mainUserCodeToken);
                if (userRoleDB.isDef()) {
                    farzinPrefrences.putUserBaseInfo(structureUserRoleREQ.getUserName(), structureUserRoleREQ.getRoleID(), userRoleDB.getActorIDToken());
                    continueeProcessData();
                } else {
                    ChangeActiveRole(structureUserRoleREQ);
                }

            }

            @Override
            public void onCancel() {
                loginViewListener.onAccessDenied();
            }
        });
        App.getHandlerMainThread().postDelayed(() -> dialogUserRoleList.Show(), 500);
    }

    private void ChangeActiveRole(StructureUserRoleREQ structureUserRoleREQ) {

        String lastUserName = getFarzinPrefrences().getUserName();
        if (!lastUserName.isEmpty() && !lastUserName.equals("-1") && lastUserName.equals(UserName)) {
            if (new FarzinCartableDocumentPublicQueuePresenter().userHasQueue()) {
                App.getHandlerMainThread().post(() -> App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.invalid_user_has_queue), SnackBarEnum.SNACKBAR_LONG_TIME));
            } else {
                continueChangeActiveRole(structureUserRoleREQ);
            }
        } else {
            continueChangeActiveRole(structureUserRoleREQ);
        }

    }

    private void continueChangeActiveRole(StructureUserRoleREQ structureUserRoleREQ) {
        App.getHandlerMainThread().postDelayed(() -> {
            if (loadingDilaog != null) {
                App.canBack = false;
                loadingDilaog.Show();
            }
        }, 300);

        farzinPrefrences.putUserAuthenticationInfo("", "", HeaderList);
        ChangeActiveRolePresenter changeActiveRolePresenter = new ChangeActiveRolePresenter();
        changeActiveRolePresenter.CallRequest(structureUserRoleREQ, new ChangeActiveRoleListener() {
            @Override
            public void doProcess() {

            }

            @Override
            public void onSuccess() {
                farzinPrefrences.putRoleIDToken(structureUserRoleREQ.getNewRoleID());
                farzinPrefrences.putUserIDToken(structureUserRoleREQ.getMainUserCodeToken());
                farzinPrefrences.putUserBaseInfo(structureUserRoleREQ.getUserName(), structureUserRoleREQ.getRoleID(), structureUserRoleREQ.getActorIDToken());
                continueeProcessData();
            }

            @Override
            public void onFailed(String error) {
                loginViewListener.onFailed(error);
            }

        });
    }

    private void CheckLastUserName(CompareCurrentUserNameWithLastUserNameListener listener) {

        String lastUserName = getFarzinPrefrences().getUserName();
        if (!lastUserName.isEmpty() && !lastUserName.equals("-1") && !lastUserName.equals(UserName)) {
            App.getHandlerMainThread().post(() -> {
                try {
                    if (loadingDilaog != null) {
                        loadingDilaog.Hide();
                    }
                    if (BaseActivity.dialog != null) {
                        BaseActivity.dialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            App.getHandlerMainThread().postDelayed(() -> CustomFunction.resetApp(App.CurentActivity, new ListenerQuestion() {
                @Override
                public void onSuccess() {
                    listener.AppDataSuccessReset();
                }

                @Override
                public void onCancel() {
                    listener.onCancel();
                }
            }), TimeValue.SecondsInMilli());
        } else {
            listener.onEquals();
        }
    }

    private void continueeProcessData() {
 /*       if (loadingDilaog != null) {
            App.canBack = false;
            loadingDilaog.Show();
        }*/
        try {
            if (!IsPasswordEncript) {
                Password = WebService.EncriptToSHA1(Password);
            }
            farzinPrefrences.putUserAuthenticationInfo(UserName, Password, HeaderList);
            farzinPrefrences.putIsRemember(isRemember);
            farzinPrefrences.putServerUrl(ServerUrl);
            checkLicenseKey();
        } catch (NoSuchAlgorithmException e) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_encript_to_sha1));
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        } catch (UnsupportedEncodingException e) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_encript_to_sha1));
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }

    }

    private void checkLicenseKey() {
        setLicenseKey(new SetLicenseKeyListener() {
            @Override
            public void onSuccess() {
                loginViewListener.onSuccess();
            }

            @Override

            public void onFailed(String errorCode) {
                ACRA.getErrorReporter().handleSilentException(new Exception("setLicenseKey Error code= " + errorCode));
                if (errorCode.equals("2")) {
                    checkLicenseKey();
                } else if (errorCode.equals("3")) {

                } else if (errorCode.equals("4")) {

                } else if (errorCode.equals("5")) {
                    checkLicenseKey();
                }
                //loginViewListener.onAccessDenied();
            }
        });
    }

    public void setLicenseKey(final SetLicenseKeyListener setLicenseKeyListener) {
        //setLicenseKeyListener.onSuccess();
        SetLicenseKeyPresenter setLicenseKeyPresenter = new SetLicenseKeyPresenter();
        setLicenseKeyPresenter.CallRequest(setLicenseKeyListener);

    }

    public void CheckNetwork(final ListenerNetwork listenerNetwork) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {

                App.getHandlerMainThread().post(() -> {
                    App.networkStatus = NetworkStatus.Connected;
                    listenerNetwork.onConnected();
                });
            }

            @Override
            public void disConnected() {
                App.getHandlerMainThread().post(() -> {
                    App.networkStatus = NetworkStatus.WatingForNetwork;
                    listenerNetwork.disConnected();
                });

            }

            @Override
            public void onFailed() {
                listenerNetwork.onFailed();
            }
        });
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
