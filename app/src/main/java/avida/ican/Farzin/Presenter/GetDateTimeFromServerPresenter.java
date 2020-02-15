package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.GetDateTimeListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureGetDateTimeREQ;
import avida.ican.Farzin.Model.Structure.Response.StructureGetDateTimeRES;
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
 * Created by AtrasVida on 2019-08-18 at 10:30 AM
 */


public class GetDateTimeFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "Authentication";
    private String MethodName = "GetDateTime";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetDateTimeFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetDateTimeFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void getDateTime(StructureGetDateTimeREQ structureGetDateTimeREQ, final GetDateTimeListener dateTimeListener) {

        CallApi(MethodName, EndPoint, getSoapObject(structureGetDateTimeREQ), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, dateTimeListener);
            }

            @Override
            public void onFailed() {
                dateTimeListener.onFailed("");
            }

            @Override
            public void onCancel() {
                dateTimeListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, GetDateTimeListener listener) {
        StructureGetDateTimeRES structureGetDateTimeRES = xmlToObject.DeserializationGsonXml(xml, StructureGetDateTimeRES.class);
        if (structureGetDateTimeRES.getStrErrorMsg() == null || structureGetDateTimeRES.getStrErrorMsg().isEmpty()) {

            if (structureGetDateTimeRES.getGetDateTimeResult() != null && !structureGetDateTimeRES.getGetDateTimeResult().isEmpty()) {
                listener.onSuccess(structureGetDateTimeRES.getGetDateTimeResult());
            } else {
                listener.onFailed("" + structureGetDateTimeRES.getGetDateTimeResult());
            }

        } else {
            listener.onFailed(structureGetDateTimeRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureGetDateTimeREQ structureGetDateTimeREQ) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("year", structureGetDateTimeREQ.getYear());
        soapObject.addProperty("month", structureGetDateTimeREQ.getMonth());
        soapObject.addProperty("day", structureGetDateTimeREQ.getDay());
        soapObject.addProperty("hour", structureGetDateTimeREQ.getHour());
        soapObject.addProperty("minute", structureGetDateTimeREQ.getMinute());
        soapObject.addProperty("second", structureGetDateTimeREQ.getSecond());
        return soapObject;
    }

    private void CallApi(String MethodName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
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
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
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

