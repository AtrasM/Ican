package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureDocumentAttachFileREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureAttachFileRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2019-07-23 at 5:00 PM
 */


public class AttachFileToServerPresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "AttachFile";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "AttachFileToServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public AttachFileToServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void AttachFile(StructureDocumentAttachFileREQ documentAttachFileREQ, DocumentAttachFileListener listener) {
        CallRequest(getSoapObject(documentAttachFileREQ), listener);
    }

    private void CallRequest(SoapObject soapObject, final DocumentAttachFileListener listener) {

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

    private void initStructure(String xml, DocumentAttachFileListener listener) {
        StructureAttachFileRES structureAttachFileRES = xmlToObject.DeserializationGsonXml(xml, StructureAttachFileRES.class);
        if (structureAttachFileRES.getStrErrorMsg() == null || structureAttachFileRES.getStrErrorMsg().isEmpty()) {
            if (structureAttachFileRES.isAttachFileResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + structureAttachFileRES.getStrErrorMsg());
            }
        } else {
            listener.onFailed("" + structureAttachFileRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureDocumentAttachFileREQ documentAttachFileREQ) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("MainETC", documentAttachFileREQ.getETC());
        soapObject.addProperty("MainEC", documentAttachFileREQ.getEC());
        soapObject.addProperty("bfile", documentAttachFileREQ.getFileBinary());
        soapObject.addProperty("fileName", documentAttachFileREQ.getFileName());
        soapObject.addProperty("fileExtension", documentAttachFileREQ.getFileExtension());
        soapObject.addProperty("DependencyType", documentAttachFileREQ.getDependencyType());
        soapObject.addProperty("Description", documentAttachFileREQ.getDescription());
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

