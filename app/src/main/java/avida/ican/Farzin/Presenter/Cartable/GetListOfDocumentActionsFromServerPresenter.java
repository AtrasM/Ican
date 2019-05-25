package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionsListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionsRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-11-19 at 2:26 PM
 */


public class GetListOfDocumentActionsFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "GetListOfActions";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetListOfDocumentActionsFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetListOfDocumentActionsFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void CallRequest(int ETC, final CartableDocumentActionsListListener listener) {

        CallApi(MethodName, EndPoint, getSoapObject(ETC), new DataProcessListener() {
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

    private void initStructure(String xml, CartableDocumentActionsListListener listener) {
        StructureCartableDocumentActionsRES structureCartableDocumentActionsRES = xmlToObject.DeserializationGsonXml(xml, StructureCartableDocumentActionsRES.class);
        if (structureCartableDocumentActionsRES.getStrErrorMsg() == null || structureCartableDocumentActionsRES.getStrErrorMsg().isEmpty()) {
            ArrayList<StructureCartableDocumentActionRES> structureCartableDocumentActionRES = new ArrayList<>(structureCartableDocumentActionsRES.getGetListOfActionsResult().getRows().getAction());
            if (structureCartableDocumentActionRES.size() > 0) {
                listener.onSuccess(structureCartableDocumentActionRES);
            } else {
                listener.onFailed("" + structureCartableDocumentActionsRES.getStrErrorMsg());
            }

        } else {
            listener.onFailed("" + structureCartableDocumentActionsRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(int ETC) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("ETC", ETC);
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
                Xml = changeXml.charDecoder(Xml);
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

