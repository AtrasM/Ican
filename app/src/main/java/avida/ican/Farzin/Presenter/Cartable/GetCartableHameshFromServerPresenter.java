package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-09-12 at 11:08
 */


public class GetCartableHameshFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MetodName = "GetHameshList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetCartableHameshFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetCartableHameshFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetHameshList(int EntityTypeCode, int EntityCode, CartableHameshListListener cartableHameshListListener) {
        GetData(getSoapObject(EntityTypeCode, EntityCode), cartableHameshListListener);
    }

    private void GetData(SoapObject soapObject, final CartableHameshListListener cartableHameshListListener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, cartableHameshListListener);
            }

            @Override
            public void onFailed() {
                cartableHameshListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                cartableHameshListListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, CartableHameshListListener cartableHameshListListener) {
        xml=xml.replaceAll("xsi:type=\"Hamesh\"","");
        StructureHameshListRES structureHameshListRES = xmlToObject.DeserializationSimpleXml(xml, StructureHameshListRES.class);
        if (structureHameshListRES.getStrErrorMsg() != null) {
            cartableHameshListListener.onFailed("" + structureHameshListRES.getStrErrorMsg());
        } else {
            if (structureHameshListRES.getGetHameshListResult().size() <= 0) {
                cartableHameshListListener.onSuccess(new ArrayList<StructureHameshRES>());
            } else {
                ArrayList<StructureHameshRES> structureHameshRES = new ArrayList<>(structureHameshListRES.getGetHameshListResult());
                // changeXml.CharDecoder(structureMessageList.get())
                cartableHameshListListener.onSuccess(structureHameshRES);
            }
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

