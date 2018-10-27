package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-10-14 at 11:16 AM
 */


public class GetZanjireMadrakFromServerPresenter {

    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MetodName = "GetFileDependency";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetZanjireMadrakFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetZanjireMadrakFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetZanjireMadrakList(int EntityTypeCode, int EntityCode, ZanjireMadrakListener zanjireMadrakListener) {
        GetData(getSoapObject(EntityTypeCode, EntityCode), zanjireMadrakListener);
    }

    private void GetData(SoapObject soapObject, final ZanjireMadrakListener zanjireMadrakListener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, zanjireMadrakListener);
            }

            @Override
            public void onFailed() {
                zanjireMadrakListener.onFailed("");
            }

            @Override
            public void onCancel() {
                zanjireMadrakListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, ZanjireMadrakListener zanjireMadrakListener) {
        xml=xml.replaceAll("xsi:type=\"FarzinFile\"","");
        StructureZanjireMadrakListRES structureZanjireMadrakListRES = xmlToObject.DeserializationSimpleXml(xml, StructureZanjireMadrakListRES.class);
        if (structureZanjireMadrakListRES.getStrErrorMsg() == null || structureZanjireMadrakListRES.getStrErrorMsg().isEmpty()) {
            if (structureZanjireMadrakListRES.getGetFileDependencyResult() == null) {
                zanjireMadrakListener.onSuccess(new StructureZanjireMadrakRES());
            } else {
                StructureZanjireMadrakRES structureZanjireMadrakRES = structureZanjireMadrakListRES.getGetFileDependencyResult();
                // changeXml.CharDecoder(structureMessageList.get())
                zanjireMadrakListener.onSuccess(structureZanjireMadrakRES);
            }
        } else {
            zanjireMadrakListener.onFailed("" + structureZanjireMadrakListRES.getStrErrorMsg());
        }
    }


    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);

        return soapObject;

    }


    private void CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        String SessionId = farzinPrefrences.getCookie();
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
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
                //Xml = changeXml.CharDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MetodName);
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

