package avida.ican.Farzin.Presenter;

import android.util.Log;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.StructureLoginRES;
import avida.ican.Farzin.View.Interface.LoginViewListener;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerInternet;

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

    public LoginPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void AutoAuthentiocation(final LoginViewListener loginViewListener) {
        this.loginViewListener = loginViewListener;
        if (!farzinPrefrences.getUserName().equals("-1")) {

            new CheckNetworkAvailability().isInternetAvailable(new ListenerInternet() {
                @Override
                public void onConnected() {
                    //App.networkStatus = NetworkStatus.Connected;
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

    public void Authentiocation(String UserName, String Password, String ServerUrl, LoginViewListener loginViewListener) {
        this.loginViewListener = loginViewListener;
        if (!ServerUrl.contains("http")) {
            ServerUrl = "http://" + ServerUrl;
        }
        if (!ServerUrl.substring(ServerUrl.length()).equals("/")) {
            ServerUrl = ServerUrl + "/";
        }
        farzinPrefrences.putServerUrl(ServerUrl);
        CallApi(farzinPrefrences.getBaseUrl(), ServerUrl, UserName, Password, false);
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
                        loginViewListener.onFailed();
                    }
                }).execute();

    }

    private void processData(WebServiceResponse webServiceResponse) {
        if (!webServiceResponse.isResponse()) {
            loginViewListener.onFailed();
            return;
        }
        String Xml = webServiceResponse.getHttpTransportSE().responseDump;
        try {
            Xml = changeXml.CropAsResponseTag(Xml, MetodeName);
            Log.i(Tag, "XmlCropAsResponseTag= " + Xml);
            StructureLoginRES structureLoginRES = xmlToObject.DeserializationGsonXml(Xml, StructureLoginRES.class);
            if (structureLoginRES.isLoginResult()) {
                if (!IsPasswordEncript) {
                    Password = WebService.EncriptToSHA1(Password);
                }
                farzinPrefrences.putUserAuthenticationInfo(UserName, Password, webServiceResponse.getHeaderList());
                loginViewListener.onSuccess();
            } else {
                loginViewListener.onAccessDenied();
            }
        } catch (Exception e) {
            loginViewListener.onFailed();
            e.printStackTrace();
        }
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
