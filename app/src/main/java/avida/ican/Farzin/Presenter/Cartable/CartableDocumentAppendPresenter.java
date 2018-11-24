package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureMessageFileREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureReceiverREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureTaeedRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-11-18 at 3:14 PM
 */


public class CartableDocumentAppendPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MetodName = "Append";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CartableDocumentAppendPresenter";
    private FarzinPrefrences farzinPrefrences;

    public CartableDocumentAppendPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void AppendDocument(StructureAppendREQ structureAppendREQ, TaeedListener listener) {
        CallRequest(getSoapObject(structureAppendREQ), listener);
    }

    private void CallRequest(SoapObject soapObject, final TaeedListener listener) {

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

    private void initStructure(String xml, TaeedListener listener) {
        StructureTaeedRES structureTaeedRES = xmlToObject.DeserializationGsonXml(xml, StructureTaeedRES.class);
        if (structureTaeedRES.getStrErrorMsg() == null || structureTaeedRES.getStrErrorMsg().isEmpty()) {

            if (structureTaeedRES.isResponseResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + structureTaeedRES.getStrErrorMsg());
            }

        } else {
            listener.onFailed("" + structureTaeedRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureAppendREQ structureAppendREQ) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("EntityTypeCode", structureAppendREQ.getETC());
        soapObject.addProperty("EntityCode", structureAppendREQ.getEC());

        //*******____________________________  AttachList  ____________________________********
        SoapObject flowStructure = new SoapObject(NameSpace, "flowStructure");
        SoapObject Document = new SoapObject(NameSpace, "Document");
        SoapObject Workflow = new SoapObject(NameSpace, "Workflow");
        SoapObject Receivers = new SoapObject(NameSpace, "Receivers");
        Workflow.addProperty("Sender", structureAppendREQ.getStructureSenderREQ());

        for (int i = 0; i < structureAppendREQ.getStructurePersonsREQ().size(); i++) {
            Receivers.addProperty("Person", structureAppendREQ.getStructurePersonsREQ());
        }
        Workflow.addProperty("Sender", structureAppendREQ.getStructureSenderREQ());
        Workflow.addSoapObject(Receivers);
        Document.addSoapObject(Workflow);
        flowStructure.addSoapObject(Document);
        soapObject.addSoapObject(flowStructure);
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

