package avida.ican.Ican.Model;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Ican.App;
import avida.ican.Ican.IcanHttpTransportSE;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-03-13 at 1:14 PM
 */

public class WebService {
    private WebServiceResponse webServiceResponse = new WebServiceResponse();
    private int EnvelopVersion = SoapEnvelope.VER11;
    private String SERVER_URL = "";
    private String BASE_URL = "";
    private String EndPoint = "";
    private String URL = "";
    private String NAME_SPACE = "";
    private String METODE_NAME = "";
    private String SOAP_ACTION = "";
    private String SESSION_ID = "";
    private SoapObject soapObject;
    private String MappingNameSpace = "";
    private ArrayList<String> MappingName = new ArrayList<>();
    private String UserName = "";
    private String Password = "";
    private ArrayList<Class> MappingClass = new ArrayList<>();
    private List headerList;
    private ArrayList headerArrayList = null;
    private WebserviceResponseListener webserviceResponseListener;
    private int TimeOut = (int) TimeValue.SecondsInMilli() * 20;
    private String Tag = "WebService";
    private boolean IsPasswordEncript;
    private boolean isNetworkCheking;

    public WebService(String NameSpace, String MetodeName, String ServerUrl, String BaseUrl, String endPoint) {
        this.NAME_SPACE = NameSpace;
        this.METODE_NAME = MetodeName;
        this.SOAP_ACTION = NameSpace + MetodeName;
        this.SERVER_URL = ServerUrl;
        this.BASE_URL = BaseUrl;
        this.EndPoint = endPoint + ".asmx?wsdl";
        this.URL = SERVER_URL + BASE_URL + EndPoint;
        Log.i(Tag, "Request is: " + this.URL);
    }

    public WebService(String NameSpace, String MetodeName, String ServerUrl, String BaseUrl) {
        this.NAME_SPACE = NameSpace;
        this.METODE_NAME = MetodeName;
        this.SOAP_ACTION = NameSpace + MetodeName;
        this.SERVER_URL = ServerUrl;
        this.BASE_URL = BaseUrl;
        this.URL = SERVER_URL + BASE_URL;
        Log.i(Tag, "Request is: " + this.URL);
    }

    public WebService SoapSerializationEnvelopeVersion(int version) {
        this.EnvelopVersion = version;
        return this;

    }

    public WebService setServerUrl(String ServerUrl) {
        this.SERVER_URL = ServerUrl;
        return this;
    }

    public WebService setBaseUrl(String BaseUrl) {
        this.BASE_URL = BaseUrl;
        return this;
    }

    public WebService setNetworkCheking(boolean isNetworkCheking) {
        this.isNetworkCheking = isNetworkCheking;
        return this;
    }

    public WebService setTimeOut(int timeOut) {
        this.TimeOut = timeOut;
        return this;
    }

    public WebService setEndPoint(String EndPoint) {
        EndPoint = EndPoint + ".asmx?wsdl";
        if (TextUtils.isEmpty(this.EndPoint)) {
            this.EndPoint = EndPoint;
            this.URL = URL + EndPoint;
        } else {
            this.EndPoint = EndPoint;
            this.URL = SERVER_URL + BASE_URL + EndPoint;
        }
        Log.i(Tag, "Request is: " + this.URL);
        return this;
    }

    public WebService setNameSpace(String NameSpace) {
        this.NAME_SPACE = NameSpace;
        return this;
    }

    public WebService setMetodeName(String MetodeName) {
        this.METODE_NAME = MetodeName;
        return this;
    }

    public WebService setSessionId(String SessionId) {
        this.SESSION_ID = SessionId;
        return this;
    }

    public WebService setSoapObject(SoapObject soapObject) {
        this.soapObject = soapObject;
        return this;
    }

    public WebService setOnListener(WebserviceResponseListener webserviceResponseListener) {
        this.webserviceResponseListener = webserviceResponseListener;
        return this;
    }

    public WebService Authentication(String UserName, String Password, boolean IsPasswordEncript) {
        this.UserName = UserName;
        this.Password = Password;
        this.IsPasswordEncript = IsPasswordEncript;
        return this;
    }

    public WebService addMapping(String MappingNameSpace, String MappingName, Class MappingClass) {
        this.MappingNameSpace = MappingNameSpace;
        this.MappingName.add(MappingName);
        this.MappingClass.add(MappingClass);
        return this;
    }

    public WebService addMapping(String MappingName, Class MappingClass) {
        this.MappingName.add(MappingName);
        this.MappingClass.add(MappingClass);
        return this;
    }

    public void execute() {
        new SoapRequest().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
       /* boolean Connected = new CheckNetworkAvailability().execuet();
        if (Connected) {
            new SoapRequest().execute();
        } else {
            new NoNetworkAccess().ShowDialog();
        }*/

    }

    @SuppressLint("StaticFieldLeak")
    private class SoapRequest extends AsyncTask<Void, Void, WebServiceResponse> {

        @Override
        protected WebServiceResponse doInBackground(Void... voids) {

            try {
                return Request();
            } catch (NoSuchAlgorithmException e) {
                Log.i(Tag, "NoSuchAlgorithmException = " + e.toString());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.i(Tag, "XmlPullParserException = " + e.toString());
                e.printStackTrace();
            } catch (SocketTimeoutException e) {
                Log.i(Tag, "SocketTimeoutException  = " + e.toString());
                e.printStackTrace();
            } catch (IOException e) {
                Log.i(Tag, "IOException = " + e.toString());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.i(Tag, "TimeOutExeption = " + e.toString());
                e.printStackTrace();
            }
            return WebServiceResponse();
        }

        @Override
        protected void onPostExecute(final WebServiceResponse webServiceResponse) {
            if (webServiceResponse.getHttpTransportSE() == null) {
                if (isNetworkCheking) {
                    webserviceResponseListener.WebserviceResponseListener(webServiceResponse);
                } else {
                    CheckNetwork(false, webServiceResponse);
                }
            } else {
                if (webserviceResponseListener != null) {
                    boolean invalidLogin = false;
                    if (webServiceResponse.isResponse()) {
                        String xml = webServiceResponse.getHttpTransportSE().responseDump;
                        if (xml != null) {
                            invalidLogin = xml.contains("Invalid Login");
                        }

                    }

                    if (isNetworkCheking) {
                        isNetworkCheking = false;
                        webserviceResponseListener.WebserviceResponseListener(webServiceResponse);
                    } else {
                        if (App.networkStatus == NetworkStatus.NoAction) {
                            CheckNetwork(invalidLogin, webServiceResponse);
                        } else {
                            final boolean finalInvalidLogin = invalidLogin;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                                        App.networkStatus = NetworkStatus.WatingForNetwork;
                                        if (App.netWorkStatusListener != null) {
                                            App.netWorkStatusListener.WatingForNetwork();
                                        }
                                        webserviceResponseListener.NetworkAccessDenied();
                                    } else {
                                        if (finalInvalidLogin && App.CurentActivity != null) {
                                            gotoActivityLogin();
                                        } else {
                                            webserviceResponseListener.WebserviceResponseListener(webServiceResponse);
                                        }
                                    }
                                }
                            });


                        }
                    }

                }
            }


            super.onPostExecute(webServiceResponse);
        }

    }

    private void CheckNetwork(final boolean invalidLogin, final WebServiceResponse webServiceResponse) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.networkStatus = NetworkStatus.Connected;
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.Connected();
                        }
                        if (invalidLogin && App.CurentActivity != null) {
                            gotoActivityLogin();
                        } else {
                            webserviceResponseListener.WebserviceResponseListener(webServiceResponse);
                        }
                    }
                });

            }

            @Override
            public void disConnected() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        App.networkStatus = NetworkStatus.WatingForNetwork;
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.WatingForNetwork();
                        }
                        webserviceResponseListener.NetworkAccessDenied();
                    }
                });

            }

            @Override
            public void onFailed() {

            }
        });
    }


    private void runOnUiThread(Runnable r) {
        App.getHandlerMainThread().post(r);
    }

    private void gotoActivityLogin() {
        Class<?> cls;
        switch (App.getCurentProject()) {
            case Farzin: {
                cls = FarzinActivityLogin.class;
                break;
            }
            default: {
                cls = FarzinActivityLogin.class;
            }
        }
        Intent intent = new Intent(App.CurentActivity, cls);
        intent.putExtra("LogOut", true);
        App.CurentActivity.startActivity(intent);
        App.isLoading = false;
        App.canBack = true;
        App.CurentActivity.finish();
    }

    private WebServiceResponse Request() throws NoSuchAlgorithmException, XmlPullParserException, IOException, InterruptedException {
        if (!TextUtils.isEmpty(UserName)) return Authentication();
        return CallApi();

    }

    private WebServiceResponse Authentication() throws IOException, NoSuchAlgorithmException, XmlPullParserException, InterruptedException {


        soapObject = new SoapObject(NAME_SPACE, METODE_NAME);
        if (!IsPasswordEncript) {
            Password = EncriptToSHA1(Password);
        }
        soapObject.addProperty("userName", UserName);
        soapObject.addProperty("password", Password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(EnvelopVersion);
        envelope.setOutputSoapObject(soapObject);
        envelope.dotNet = true;
        IcanHttpTransportSE androidHttpTransport = new IcanHttpTransportSE(URL, TimeOut);
        androidHttpTransport.debug = true;
        webServiceResponse.setHeaderList(androidHttpTransport.call(SOAP_ACTION, envelope, null));
        return WebServiceResponse( androidHttpTransport, webServiceResponse.getHeaderList());
    }

    private WebServiceResponse CallApi() throws IOException, XmlPullParserException {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(EnvelopVersion);
        envelope.setOutputSoapObject(soapObject);
        if (MappingName != null && MappingName.size() > 0) {
            for (int i = 0; i < MappingName.size(); i++) {
                envelope.addMapping(MappingNameSpace, MappingName.get(i), MappingClass.get(i));
            }
        }
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        IcanHttpTransportSE androidHttpTransport = new IcanHttpTransportSE(URL, TimeOut);
        androidHttpTransport.debug = true;
        if (!TextUtils.isEmpty(SESSION_ID)) {
            headerArrayList = new ArrayList();
            final HeaderProperty headerPropertyObj = new HeaderProperty("cookie", SESSION_ID);
            headerArrayList.add(headerPropertyObj);
        }
        try {
            webServiceResponse.setHeaderList(androidHttpTransport.call(SOAP_ACTION, envelope, headerArrayList));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return WebServiceResponse( androidHttpTransport, webServiceResponse.getHeaderList());
    }

    private WebServiceResponse WebServiceResponse( IcanHttpTransportSE androidHttpTransport, List headerList) {
        //webServiceResponse.setEnvelope(envelope);
        webServiceResponse.setHttpTransportSE(androidHttpTransport);
        webServiceResponse.setHeaderList(headerList);
        webServiceResponse.setResponse(true);
        return webServiceResponse;
    }

    private WebServiceResponse WebServiceResponse() {
        webServiceResponse.setResponse(false);
        return webServiceResponse;
    }


    @NonNull
    public static String EncriptToSHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    @NonNull
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
