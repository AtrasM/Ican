package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.acra.ACRA;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Message.MessageAttachmentListListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureGetMessageAttachmentRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.saxHandler.MessageAttachmentSaxHandler;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-07-03 at 5:27 PM
 */

public class GetMessageAttachmentFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "MessageSystemManagment";
    private String MethodName = "GetMessageAttachment";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetMessageFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private String XmlFile = "";
    private AsyncTask<Void, Void, Void> task;

    public GetMessageAttachmentFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetMessageAttachment(int messageID, MessageAttachmentListListener messageAttachmentListListener) {
        CallRequest(getSoapObject(messageID), messageAttachmentListListener);
    }

    private void CallRequest(SoapObject soapObject, final MessageAttachmentListListener messageAttachmentListListener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, messageAttachmentListListener);
            }

            @Override
            public void onFailed() {
                messageAttachmentListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                messageAttachmentListListener.onCancel();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void initStructure(final String data, final MessageAttachmentListListener messageAttachmentListListener) {
        final StructureGetMessageAttachmentRES[] structureGetMessageAttachmentRES = {new StructureGetMessageAttachmentRES()};

        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (data.contains(App.getResponsePath())) {
                        MessageAttachmentSaxHandler messageAttachmentSaxHandler = xmlToObject.parseXmlWithSax(data, new MessageAttachmentSaxHandler());
                        structureGetMessageAttachmentRES[0] = messageAttachmentSaxHandler.getObject();
                        sleep();
                    } else {
                        String xml = data.replaceAll("xsi:type=\"FarzinFile\"", "");
                        structureGetMessageAttachmentRES[0] = xmlToObject.DeserializationSimpleXml(xml, StructureGetMessageAttachmentRES.class);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ACRA.getErrorReporter().handleSilentException(e);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    if (structureGetMessageAttachmentRES[0].getStrErrorMsg() == null || structureGetMessageAttachmentRES[0].getStrErrorMsg().isEmpty()) {
                        if (structureGetMessageAttachmentRES[0].getGetMessageAttachmentResult().size() <= 0) {
                            messageAttachmentListListener.onSuccess(new ArrayList<>());
                        } else {
                            List<StructureMessageAttachRES> structureMessageAttachRES = structureGetMessageAttachmentRES[0].getGetMessageAttachmentResult();
                            messageAttachmentListListener.onSuccess(new ArrayList<>(structureMessageAttachRES));
                        }
                    } else {
                        messageAttachmentListListener.onFailed("" + structureGetMessageAttachmentRES[0].getStrErrorMsg());
                    }
                } catch (Exception e) {
                    messageAttachmentListListener.onFailed("");

                    e.printStackTrace();
                }
            }
        };

        task.execute();
    }

    private void sleep() {
        try {
            Thread.sleep(TimeValue.SecondsInMilli() * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SoapObject getSoapObject(int messageID) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("msgID", messageID);

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
                        // new GetZanjireMadrakFromServerPresenter.processData(webServiceResponse, dataProcessListener);
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

