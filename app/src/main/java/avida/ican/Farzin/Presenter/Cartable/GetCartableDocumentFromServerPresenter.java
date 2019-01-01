package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-09-12 at 11:08
 */


public class GetCartableDocumentFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MetodName = "GetCartableDocument";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetCartableDocumentFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;


    public GetCartableDocumentFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetCartableDocumentList(int count, CartableDocumentListListener cartableDocumentListListener) {

        String LastDate = getFarzinPrefrences().getCartableDocumentDataSyncDate();
        GetCartableDocument(getSoapObject(LastDate, count), cartableDocumentListListener);
    }

    private void GetCartableDocument(SoapObject soapObject, final CartableDocumentListListener cartableDocumentListListener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                CheckCartableDocumentListStructure(Xml, cartableDocumentListListener);
            }

            @Override
            public void onFailed() {
                cartableDocumentListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                cartableDocumentListListener.onCancel();
            }
        });

    }

    private void CheckCartableDocumentListStructure(String xml, CartableDocumentListListener cartableDocumentListListener) {
        StructureCartableDocumentListRES structureCartableDocumentListRES = xmlToObject.DeserializationSimpleXml(xml, StructureCartableDocumentListRES.class);
        if (structureCartableDocumentListRES.getStrErrorMsg() == null || structureCartableDocumentListRES.getStrErrorMsg().isEmpty()) {
            if (structureCartableDocumentListRES.getGetCartableDocumentResult().size() <= 0) {
                cartableDocumentListListener.onSuccess(new ArrayList<StructureInboxDocumentRES>());
            } else {
                ArrayList<StructureInboxDocumentRES> structureInboxDocumentRES = new ArrayList<>(structureCartableDocumentListRES.getGetCartableDocumentResult());
                // changeXml.CharDecoder(structureMessageList.get())
                cartableDocumentListListener.onSuccess(structureInboxDocumentRES);
            }
        } else {
            cartableDocumentListListener.onFailed("" + structureCartableDocumentListRES.getStrErrorMsg());
        }
        getFarzinPrefrences().putCartableLastCheckDate(CustomFunction.getCurentDateTime().toString());
    }


    private SoapObject getSoapObject(String LastDate, int count) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        SoapObject Filter = new SoapObject(NameSpace, "filter");
        Filter.addProperty("ETC", -1);
        Filter.addProperty("ActionCode", -1);
        if (!LastDate.isEmpty()) {
            LastDate = CustomFunction.arabicToDecimal(LastDate);
            Filter.addProperty("StartDateTime", LastDate);
        }

        Filter.addProperty("CountOfRecord", count);
        Filter.addProperty("SortType", "ASC");
        soapObject.addSoapObject(Filter);
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

