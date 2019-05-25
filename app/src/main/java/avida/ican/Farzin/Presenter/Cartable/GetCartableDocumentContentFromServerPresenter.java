package avida.ican.Farzin.Presenter.Cartable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentContentRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.saxHandler.DocumentContentSaxHandler;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-12-04 at 1:38 PM
 */


public class GetCartableDocumentContentFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "GetContentFormAs";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetCartableDocumentContentFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private AsyncTask<Void, Void, Void> task;

    public GetCartableDocumentContentFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetContent(int ETC, int EC, CartableDocumentContentListener documentContentListener) {
        CallRequest(getSoapObject(ETC, EC), documentContentListener);
    }

    private void CallRequest(SoapObject soapObject, final CartableDocumentContentListener documentContentListener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
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

    @SuppressLint("StaticFieldLeak")
    private void initStructure(final String xml, final CartableDocumentContentListener documentContentListener) {
        final StructureCartableDocumentContentRES[] cartableDocumentContentRES = new StructureCartableDocumentContentRES[1];
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (xml.contains(App.RESPONSEPATH)) {
                    DocumentContentSaxHandler documentContentSaxHandler = xmlToObject.parseXmlWithSax(xml, new DocumentContentSaxHandler());
                    cartableDocumentContentRES[0] = documentContentSaxHandler.getObject();
                } else {
                    cartableDocumentContentRES[0] = xmlToObject.DeserializationGsonXml(xml, StructureCartableDocumentContentRES.class);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (cartableDocumentContentRES[0].getStrErrorMsg() == null || cartableDocumentContentRES[0].getStrErrorMsg().isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (cartableDocumentContentRES[0].getGetContentFormAsStringBuilder() != null && cartableDocumentContentRES[0].getGetContentFormAsStringBuilder().length() > 0) {
                        stringBuilder = cartableDocumentContentRES[0].getGetContentFormAsStringBuilder();
                    } else {
                        stringBuilder.append(cartableDocumentContentRES[0].getGetContentFormAsResult());
                    }
                    documentContentListener.onSuccess(stringBuilder);
                } else {
                    documentContentListener.onFailed("" + cartableDocumentContentRES[0].getStrErrorMsg());
                }
            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);
        soapObject.addProperty("LayoutID", -1);
        soapObject.addProperty("exportType", "pdf");
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
                if (Xml.contains(MethodName)) {
                    Xml = changeXml.CropAsResponseTag(Xml, MethodName);
                }
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

