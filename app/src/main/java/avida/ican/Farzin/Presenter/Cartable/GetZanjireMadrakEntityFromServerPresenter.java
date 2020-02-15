package avida.ican.Farzin.Presenter.Cartable;

import android.annotation.SuppressLint;
import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakEntityListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireEntityListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireEntityRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2019-08-03 at 11:22 AM
 */


public class GetZanjireMadrakEntityFromServerPresenter {

    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "GetEntityDependency";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetZanjireMadrakEntityFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetZanjireMadrakEntityFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetZanjireEntityList(int EntityTypeCode, int EntityCode, ZanjireMadrakEntityListener zanjireMadrakEntityListener) {
        CallRequest(getSoapObject(EntityTypeCode, EntityCode), zanjireMadrakEntityListener);
    }

    private void CallRequest(SoapObject soapObject, final ZanjireMadrakEntityListener zanjireMadrakEntityListener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, zanjireMadrakEntityListener);
            }

            @Override
            public void onFailed() {
                zanjireMadrakEntityListener.onFailed("");
            }

            @Override
            public void onCancel() {
                zanjireMadrakEntityListener.onCancel();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void initStructure(String xml, final ZanjireMadrakEntityListener zanjireMadrakEntityListener) {

         xml = xml.replaceAll("xsi:type=\"EntityDependencyItem\"", "");
        StructureZanjireEntityListRES structureZanjireEntityListRES = xmlToObject.DeserializationSimpleXml(xml, StructureZanjireEntityListRES.class);
        try {
            if (structureZanjireEntityListRES.getGetEntityDependencyResult() == null) {
                if (structureZanjireEntityListRES.getStrErrorMsg() == null || structureZanjireEntityListRES.getStrErrorMsg().isEmpty()) {
                    zanjireMadrakEntityListener.onSuccess(new StructureZanjireEntityRES());
                } else {
                    zanjireMadrakEntityListener.onFailed("");
                }
            } else {
                StructureZanjireEntityRES structureZanjireEntityRES = structureZanjireEntityListRES.getGetEntityDependencyResult();
                zanjireMadrakEntityListener.onSuccess(structureZanjireEntityRES);
            }

        } catch (Exception e) {
            zanjireMadrakEntityListener.onFailed("");
            e.printStackTrace();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(TimeValue.SecondsInMilli() * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);

        return soapObject;
    }

    private void CallApi(String MethodName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        String SessionId = farzinPrefrences.getCookie();
        new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setSessionId(SessionId)
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
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
                if (!Xml.isEmpty()) {
                    Xml = changeXml.charDecoder(Xml);
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

