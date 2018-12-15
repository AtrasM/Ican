package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureAddHameshOpticalPenRES;
import avida.ican.Farzin.Model.Structure.Response.StructureHelloRES;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-12-12 at 3:50 PM
 */


public class CheckServerAviablePresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "Authentication";
    private String MetodeName = "Hello";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CheckServerAviablePresenter";
    private FarzinPrefrences farzinPrefrences;

    public CheckServerAviablePresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void CallRequest(final ListenerNetwork listenerNetwork) {

        CallApi(MetodeName, EndPoint, getSoapObject(), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, listenerNetwork);
            }

            @Override
            public void onFailed() {
                listenerNetwork.onFailed();
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void initStructure(String xml, ListenerNetwork listenerNetwork) {
        StructureHelloRES structureHelloRES = xmlToObject.DeserializationGsonXml(xml, StructureHelloRES.class);
        if (structureHelloRES.getStrErrorMsg() == null || structureHelloRES.getStrErrorMsg().isEmpty()) {

            if (structureHelloRES.isHelloResult()) {
                App.networkStatus = NetworkStatus.Connected;
                listenerNetwork.onConnected();
            } else {
                App.networkStatus = NetworkStatus.WatingForNetwork;
                listenerNetwork.disConnected();
            }

        } else {
            App.networkStatus = NetworkStatus.WatingForNetwork;
            listenerNetwork.onFailed();
        }
    }

    private SoapObject getSoapObject() {
        SoapObject soapObject = new SoapObject(NameSpace, MetodeName);

        return soapObject;
    }


    private void CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setTimeOut((int) TimeValue.SecondsInMilli() * 2)
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
            try {
                //Xml = changeXml.CharDecoder(Xml);
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

