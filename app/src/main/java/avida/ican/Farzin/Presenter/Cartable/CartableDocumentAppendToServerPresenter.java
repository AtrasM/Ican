package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.SendListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
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


public class CartableDocumentAppendToServerPresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MetodName = "Append";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CartableDocumentAppendToServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public CartableDocumentAppendToServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void AppendDocument(StructureAppendREQ structureAppendREQ, SendListener listener) {
        CallRequest(getSoapObject(structureAppendREQ), listener);
    }

    private void CallRequest(SoapObject soapObject, final SendListener listener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
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

    private void initStructure(String xml, SendListener listener) {
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
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("EntityTypeCode", structureAppendREQ.getETC());
        soapObject.addProperty("EntityCode", structureAppendREQ.getEC());
        String DocumentTag = "<Document><Workflow><Sender roleId=\"" + structureAppendREQ.getStructureSenderREQ().getRoleId() + "\" sendParentCode=\"-1\" description=\"\" isLocked=\"0\" viewInOutbox=\"1\" /><Receivers>";
        for (int i = 0; i < structureAppendREQ.getStructurePersonsREQ().size(); i++) {
            StructurePersonREQ structurePersonREQ = structureAppendREQ.getStructurePersonsREQ().get(i);
            DocumentTag = DocumentTag + "<Person roleId=\"" + structurePersonREQ.getRoleId() + "\" action=\"" + structurePersonREQ.getAction() + "\" description=\"" + structurePersonREQ.getDescription() + "\" hameshTitle=\"" + structurePersonREQ.getHameshTitle() + "\" hameshContent=\"" + structurePersonREQ.getHameshContent() + "\"/>";

        }
        DocumentTag = DocumentTag + "</Receivers></Workflow></Document>";
        soapObject.addProperty("flowStructure", DocumentTag);
        return soapObject;
    }


    private void CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        String SessionId = farzinPrefrences.getCookie();
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setSessionId(SessionId)
                .addMapping(NameSpace, "Sender", new StructureSenderREQ().getClass())
                .addMapping(NameSpace, "Person", new StructurePersonREQ().getClass())
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

