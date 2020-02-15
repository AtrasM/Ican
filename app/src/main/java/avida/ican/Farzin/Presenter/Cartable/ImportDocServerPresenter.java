package avida.ican.Farzin.Presenter.Cartable;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.CreateDocumentListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureCreateDocumentQueueBND;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocIndicatorRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocumentRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-07-30 at 11:08 AM
 */


public class ImportDocServerPresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "IndicatorManagment";
    private String MethodName = "ImportDoc";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "ImportDocServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public ImportDocServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void ImportDoc(StructureCreateDocumentQueueBND creatDocumentQueueBND, CreateDocumentListener listener) {
        CallRequest(getSoapObject(creatDocumentQueueBND), listener);
    }

    private void CallRequest(SoapObject soapObject, final CreateDocumentListener listener) {

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

    private void initStructure(String xml, CreateDocumentListener listener) {
        StructureImportDocumentRES importDocumentRES = xmlToObject.DeserializationSimpleXml(xml, StructureImportDocumentRES.class);
        if (importDocumentRES.isImportDocResult()) {
            StructureImportDocIndicatorRES importDocIndicatorRES = importDocumentRES.getResult().getIndicator();
            if (importDocIndicatorRES.getFollowID() > 0) {
                listener.onSuccess(importDocIndicatorRES);
            } else {
                listener.onFailed("FollowID= " + importDocIndicatorRES.getFollowID());
            }
        } else {
            listener.onFailed("" + importDocumentRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureCreateDocumentQueueBND creatDocumentQueueBND) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        String description = Resorse.getString(R.string.part1_import_doc_description) + " " + creatDocumentQueueBND.getSenderFullName() + " " + Resorse.getString(R.string.part2_import_doc_description);
        String XmlTag = "<Indicators><Indicator ETC=\"-1\" IndicatorID=\"-1\"> <Information ImportOriginNO= \"ICANAPP\" ImportOriginDate= \"" + CustomFunction.arabicToDecimal(creatDocumentQueueBND.getImportOriginDate()) + "\" FromCode= \"" + creatDocumentQueueBND.getSenderUserName() + "\" From=\"" + creatDocumentQueueBND.getSenderFullName() + "\" MainInnerReceiverCode=\"\" MainInnerReceiver=\"" + creatDocumentQueueBND.getReceiverFullName() + "\" DocSubjectCode=\"\" DocSubject=\"" + creatDocumentQueueBND.getSubject() + "\" DocType=\"اصل\" SecurityLevelCode=\"1\" ImportMethod=\"ICANAPP\" ImportPriorityID=\"1\" DocKeywords=\"\" NOPgAttached=\"ندارد\" ImportDesc=\"" + description + "\" ></Information>";
        XmlTag = XmlTag + "</Indicator></Indicators>";

        Log.i("LOG", "ImportDoc SoapObject= " + XmlTag);
        soapObject.addProperty("structurexml", XmlTag);
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
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
                if (!Xml.isEmpty()) {
                    Xml = changeXml.charDecoder(Xml);
                    if (Xml.contains("<Result>")) {
                        Xml = Xml.replaceFirst("<Result>", "");
                        Xml = Xml.replaceFirst("</Result>", "");
                    }
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

