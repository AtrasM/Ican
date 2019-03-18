package avida.ican.Farzin.Presenter;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.StructureLoginRES;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Farzin.View.Interface.SetLicenseKeyListener;
import avida.ican.Ican.App;
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
    private String MetodeName = "Login";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "Farzin";
    private FarzinPrefrences farzinPrefrences;
    private boolean IsPasswordEncript = false;
    private boolean isRemember = false;
    private Loading dialog;

    public LoginPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public LoginPresenter(Loading dialog) {
        farzinPrefrences = getFarzinPrefrences();
        this.dialog = dialog;
    }

    public void AutoAuthentiocation(final LoginViewListener loginViewListener) {
        this.loginViewListener = loginViewListener;
        this.isRemember = farzinPrefrences.isRemember();
        if (!farzinPrefrences.getUserName().equals("-1")) {

            CheckNetwork(new ListenerNetwork() {
                @Override
                public void onConnected() {
                    CallApi(farzinPrefrences.getBaseUrl(), farzinPrefrences.getServerUrl(), farzinPrefrences.getUserName(), farzinPrefrences.getPassword(), true);

                }

                @Override
                public void disConnected() {
                    App.CurentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            App.networkStatus = NetworkStatus.WatingForNetwork;
                            loginViewListener.onSuccess();
                        }
                    });

                }

                @Override
                public void onFailed() {

                }
            });


        } else {
            loginViewListener.onAccessDenied();
        }
    }

    public void Authentiocation(final String UserName, final String Password, String ServerUrl, boolean isRemember, final LoginViewListener loginViewListener) {
        this.isRemember = isRemember;
        this.loginViewListener = loginViewListener;
        if (!ServerUrl.contains("http")) {
            ServerUrl = "http://" + ServerUrl;
        }
        if (!ServerUrl.substring(ServerUrl.length()).equals("/")) {
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
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
                .Authentication(UserName, Password, IsPasswordEncript)
                .setOnListener(new WebserviceResponseListener() {

                    @Override
                    public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                        processData(webServiceResponse);
                    }

                    @Override
                    public void NetworkAccessDenied() {
                        loginViewListener.onFailed(Resorse.getString(R.string.server_acces_denied));
                    }
                }).execute();

    }

    private void processData(final WebServiceResponse webServiceResponse) {
        if (!webServiceResponse.isResponse()) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_faild));
            return;
        }
        String Xml = webServiceResponse.getHttpTransportSE().responseDump;
        //String Xml = new CustomFunction().readXmlResponseFromStorage();
        try {
            Xml = changeXml.CropAsResponseTag(Xml, MetodeName);
            Log.i(Tag, "XmlCropAsResponseTag= " + Xml);
            StructureLoginRES structureLoginRES = xmlToObject.DeserializationGsonXml(Xml, StructureLoginRES.class);
            if (structureLoginRES.isLoginResult()) {
                String lastUserName = getFarzinPrefrences().getUserName();
                if (!lastUserName.isEmpty() && !lastUserName.equals("-1") && !lastUserName.equals(UserName)) {
                    if (dialog != null) {
                        dialog.Hide();
                    }
                  App.getHandlerMainThread().postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          CustomFunction.resetApp(App.CurentActivity, new ListenerQuestion() {
                              @Override
                              public void onSuccess() {
                                  try {
                                      countinueProcessData(webServiceResponse);
                                  } catch (UnsupportedEncodingException e) {
                                      e.printStackTrace();
                                      loginViewListener.onFailed(Resorse.getString(R.string.error_faild));
                                  } catch (NoSuchAlgorithmException e) {
                                      e.printStackTrace();
                                      loginViewListener.onFailed(Resorse.getString(R.string.error_faild));
                                  }
                              }

                              @Override
                              public void onCancel() {

                              }
                          });
                      }
                  }, TimeValue.SecondsInMilli());
                } else {
                    countinueProcessData(webServiceResponse);
                }

            } else {
                loginViewListener.onFailed(structureLoginRES.getStrErrorMsg());
            }
        } catch (Exception e) {
            loginViewListener.onFailed(Resorse.getString(R.string.error_faild));
            e.printStackTrace();
        }
    }

    private void countinueProcessData(WebServiceResponse webServiceResponse) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (!IsPasswordEncript) {
            Password = WebService.EncriptToSHA1(Password);
        }
        farzinPrefrences.putUserAuthenticationInfo(UserName, Password, webServiceResponse.getHeaderList());
        farzinPrefrences.putIsRemember(isRemember);
        farzinPrefrences.putServerUrl(ServerUrl);
        setLicenseKey(new SetLicenseKeyListener() {
            @Override
            public void onSuccess() {
                loginViewListener.onSuccess();
            }

            @Override
            public void onFailed(String error) {
                loginViewListener.onAccessDenied();
            }
        });

    }
    public void setLicenseKey(final SetLicenseKeyListener setLicenseKeyListener) {
        setLicenseKeyListener.onSuccess();
     /*   SetLicenseKeyPresenter setLicenseKeyPresenter = new SetLicenseKeyPresenter();
        setLicenseKeyPresenter.CallRequest(setLicenseKeyListener);*/

    }
    private void CheckNetwork(final ListenerNetwork listenerNetwork) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {

                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        App.networkStatus = NetworkStatus.Connected;
                        listenerNetwork.onConnected();
                    }
                });
            }

            @Override
            public void disConnected() {
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        App.networkStatus = NetworkStatus.WatingForNetwork;
                        listenerNetwork.disConnected();
                    }
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
