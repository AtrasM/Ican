package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureConfirmRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-10-24 at 10:06
 */


public class CartableDocumentConfirmPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MethodName = "Response";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CartableDocumentConfirmPresenter";
    private FarzinPrefrences farzinPrefrences;

    public CartableDocumentConfirmPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void ConfirmDocument(int receiverCode, DocumentOperatorQueuesListener listener) {
        CallRequest(getSoapObject(receiverCode), listener);
    }

    private void CallRequest(SoapObject soapObject, final DocumentOperatorQueuesListener listener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
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
        StructureConfirmRES structureConfirmRES = xmlToObject.DeserializationGsonXml(xml, StructureConfirmRES.class);
        if (structureConfirmRES.getStrErrorMsg() == null || structureConfirmRES.getStrErrorMsg().isEmpty()) {

            if (structureConfirmRES.isResponseResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + structureConfirmRES.getStrErrorMsg());
            }

        } else {
            listener.onFailed("" + structureConfirmRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(int receiverCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("receiverCode", receiverCode);
        soapObject.addProperty("description", Resorse.getString(R.string.document_confirm_description));
        soapObject.addProperty("responseType", "approve");
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

