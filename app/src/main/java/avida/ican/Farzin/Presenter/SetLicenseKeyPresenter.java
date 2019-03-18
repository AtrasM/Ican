package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureSetLicenseKeyBND;
import avida.ican.Farzin.Model.Structure.Response.StructureHelloRES;
import avida.ican.Farzin.Model.Structure.Response.StructureSetLicenseKeyRES;
import avida.ican.Farzin.View.Interface.SetLicenseKeyListener;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2019-03-05 at 3:27 PM
 */


public class SetLicenseKeyPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "Authentication";
    private String MetodeName = "SetLicenseKey";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "SetLicenseKeyPresenter";
    private FarzinPrefrences farzinPrefrences;

    public SetLicenseKeyPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void CallRequest(final SetLicenseKeyListener setLicenseKeyListener) {

        CallApi(MetodeName, EndPoint, getSoapObject(), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, setLicenseKeyListener);
            }

            @Override
            public void onFailed() {
                setLicenseKeyListener.onFailed("");
            }

            @Override
            public void onCancel() {
                setLicenseKeyListener.onFailed("");
            }
        });

    }

    private void initStructure(String xml, SetLicenseKeyListener setLicenseKeyListener) {
        StructureSetLicenseKeyRES structureSetLicenseKeyRES = xmlToObject.DeserializationGsonXml(xml, StructureSetLicenseKeyRES.class);
        if (structureSetLicenseKeyRES.getStrErrorMsg() == null || structureSetLicenseKeyRES.getStrErrorMsg().isEmpty()) {
            if (structureSetLicenseKeyRES.isSetLicenseKeyResult()) {
                setLicenseKeyListener.onSuccess();
            } else {
                setLicenseKeyListener.onFailed("" + structureSetLicenseKeyRES.getiErrorCode());
            }

        } else {
            setLicenseKeyListener.onFailed("" + structureSetLicenseKeyRES.getiErrorCode());
        }
    }


    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String GetApplicationName();

    public native String GetRandom(String data);

    public  SoapObject getSoapObject() {
        SoapObject soapObject = new SoapObject(NameSpace, MetodeName);
        String uuid = CustomFunction.getRandomUUID();
        StructureSetLicenseKeyBND structureSetLicenseKeyBND = new StructureSetLicenseKeyBND(GetApplicationName(), uuid, GetRandom(uuid));
        soapObject.addProperty("ApplicationName", structureSetLicenseKeyBND.getApplicationName());
        soapObject.addProperty("Challenge", structureSetLicenseKeyBND.getChallenge());
        soapObject.addProperty("Random", structureSetLicenseKeyBND.getRandom());
        return soapObject;
    }


    private void CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setTimeOut((int) TimeValue.SecondsInMilli() * 3)
                .setNetworkCheking(true)
                .setOnListener(new WebserviceResponseListener() {

                    @Override
                    public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                        new processData(webServiceResponse, dataProcessListener);
                    }

                    @Override
                    public void NetworkAccessDenied() {
                        dataProcessListener.onFailed();
                    }
                }).execute();

    }


    private class processData {
        processData(WebServiceResponse webServiceResponse, DataProcessListener dataProcessListener) {

            if (!webServiceResponse.isResponse()) {
                dataProcessListener.onFailed();
                return;
            }

            String Xml = webServiceResponse.getHttpTransportSE().responseDump;
            //String Xml = new CustomFunction().readXmlResponseFromStorage();
            try {
                //Xml = changeXml.charDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MetodeName);
                if (!Xml.isEmpty()) {
                    dataProcessListener.onSuccess(Xml);
                } else {
                    dataProcessListener.onFailed();
                }
            } catch (Exception e) {
                dataProcessListener.onFailed();
                e.printStackTrace();
            }
        }

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}

