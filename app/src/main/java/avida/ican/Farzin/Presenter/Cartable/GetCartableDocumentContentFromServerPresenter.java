package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentContentRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-12-04 at 1:38 PM
 */


public class GetCartableDocumentContentFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MetodName = "GetContentFormAs";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetCartableDocumentContentFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetCartableDocumentContentFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetContent(int ETC, int EC, CartableDocumentContentListener documentContentListener) {
        CallRequest(getSoapObject(ETC, EC), documentContentListener);
    }

    private void CallRequest(SoapObject soapObject, final CartableDocumentContentListener documentContentListener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, documentContentListener);
            }

            @Override
            public void onFailed() {
                documentContentListener.onFailed("");
            }

            @Override
            public void onCancel() {
                documentContentListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, CartableDocumentContentListener documentContentListener) {
        StructureCartableDocumentContentRES cartableDocumentContentRES = xmlToObject.DeserializationGsonXml(xml, StructureCartableDocumentContentRES.class);
        if (cartableDocumentContentRES.getStrErrorMsg() == null || cartableDocumentContentRES.getStrErrorMsg().isEmpty()) {
            documentContentListener.onSuccess(cartableDocumentContentRES.getGetContentFormAsResult());
        } else {
            documentContentListener.onFailed("" + cartableDocumentContentRES.getStrErrorMsg());
        }
    }


    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);
        soapObject.addProperty("LayoutID", -1);
        soapObject.addProperty("exportType", "pdf");
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

