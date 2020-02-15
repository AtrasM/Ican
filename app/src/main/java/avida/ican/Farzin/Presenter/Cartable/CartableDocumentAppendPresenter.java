package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructurePersonREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureSenderREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSendRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-11-18 at 3:14 PM
 */


public class CartableDocumentAppendPresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MethodName = "Append";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CartableDocumentAppendPresenter";
    private FarzinPrefrences farzinPrefrences;
    private int ETC;
    private int EC;

    public CartableDocumentAppendPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void AppendDocument(int ETC, int EC, StructureAppendREQ structureAppendREQ, DocumentOperatorQueuesListener listener) {
        this.ETC = ETC;
        this.EC = EC;
        CallRequest(getSoapObject(structureAppendREQ), listener);
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
        StructureSendRES structureSendRES = xmlToObject.DeserializationGsonXml(xml, StructureSendRES.class);
        if (structureSendRES.getStrErrorMsg() == null || structureSendRES.getStrErrorMsg().isEmpty()) {
            if (structureSendRES.getAppendResult() > 0) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + structureSendRES.getStrErrorMsg());
            }
        } else {
            listener.onFailed("" + structureSendRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureAppendREQ structureAppendREQ) {
   /*     String strObj ="{\"EC\":1517,\"ETC\":979,\"structurePersonsREQ\":[{\"PriorityID_Send\":1,\"action\":5,\"description\":\"t_sh 1\",\"hameshContent\":\"d_e 1\",\"hameshTitle\":\"كاربر 25 [ مدير استقرار 25 ] \",\"responseUntilDate\":\"\",\"roleId\":936},{\"PriorityID_Send\":1,\"action\":9,\"description\":\"t_sh 2\",\"hameshContent\":\"d_e 2\",\"hameshTitle\":\"حميـــدرضا بلورچيان [ مدير توسعه فرزين ] \",\"responseUntilDate\":\"\",\"roleId\":425}],\"structureSenderREQ\":{\"description\":\"\",\"isLocked\":false,\"roleId\":425,\"sendParentCode\":-1,\"viewInOutbox\":1}}";
        structureAppendREQ=new CustomFunction().ConvertStringToObject(strObj,StructureAppendREQ.class);*/
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("EntityTypeCode", ETC);
        soapObject.addProperty("EntityCode", EC);
        String DocumentTag = "<Document><Workflow><Sender roleId=\"" + structureAppendREQ.getStructureSenderREQ().getRoleId() + "\" sendParentCode=\"" + structureAppendREQ.getStructureSenderREQ().getSendParentCode() + "\" description=\"\" isLocked=\"0\" viewInOutbox=\"1\" /><Receivers>";
        for (int i = 0; i < structureAppendREQ.getStructurePersonsREQ().size(); i++) {
            StructurePersonREQ structurePersonREQ = structureAppendREQ.getStructurePersonsREQ().get(i);
            DocumentTag = DocumentTag + "<Person RoleID=\"" + structurePersonREQ.getRoleId() + "\" action=\"" + structurePersonREQ.getAction() + "\" description=\"" + structurePersonREQ.getDescription() + "\" hameshTitle=\"" + structurePersonREQ.getHameshTitle() + "\" hameshContent=\"" + structurePersonREQ.getHameshContent() + "\"/>";

        }
        DocumentTag = DocumentTag + "</Receivers></Workflow></Document>";
        soapObject.addProperty("flowStructure", DocumentTag);
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

