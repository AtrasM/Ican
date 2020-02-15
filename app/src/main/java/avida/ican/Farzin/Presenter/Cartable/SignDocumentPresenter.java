package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignDocumentRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-06-09 at 14:59 PM
 */

public class SignDocumentPresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "SignDocument";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "SignDocumentPresenter";
    private FarzinPrefrences farzinPrefrences;

    public SignDocumentPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void SignDocument(int ETC, int EC, ArrayList<String> ENs, final DocumentOperatorQueuesListener listener) {

        CallApi(MethodName, EndPoint, getSoapObject(ETC, EC, ENs), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, listener);
            }

            @Override
            public void onFailed() {
                listener.onFailed("");
            }

            @Override
            public void onCancel() {
                listener.onCancel();
            }
        });
    }

    private void initStructure(String xml, DocumentOperatorQueuesListener listener) {
        StructureSignDocumentRES structureSignDocumentRES = xmlToObject.DeserializationGsonXml(xml, StructureSignDocumentRES.class);
        if (structureSignDocumentRES.getStrErrorMsg() == null || structureSignDocumentRES.getStrErrorMsg().isEmpty()) {
            if (structureSignDocumentRES.isSignDocumentResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("");
            }

        } else {
            listener.onFailed("" + structureSignDocumentRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(int ETC, int EC, ArrayList<String> ENs) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("EntityTypeCode", ETC);
        soapObject.addProperty("EntityCode", EC);

        String UserName = getFarzinPrefrences().getUserName();
        //*******____________________________  Sign  ____________________________********

        String SignaturesTag = "<Signatures>";

        for (int i = 0; i < ENs.size(); i++) {
            SignaturesTag = SignaturesTag + "<Sign userName=\"" + UserName + "\" fieldName=\"" + ENs.get(i) + "\" type=\"nvarchar\"" + " CheckFieldSecurity=\"1\""+ "></Sign>";

        }
        //*******___________________________________________________________________________********

        SignaturesTag = SignaturesTag + "</Signatures>";
        soapObject.addProperty("signStructure", SignaturesTag);
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
                Xml = changeXml.charDecoder(Xml);
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
