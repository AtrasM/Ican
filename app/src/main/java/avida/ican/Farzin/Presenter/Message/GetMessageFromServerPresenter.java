package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageListRES;
import avida.ican.Farzin.Model.saxHandler.MessageSaxHandler;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-07-03 at 5:27 PM
 */

public class GetMessageFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "MessageSystemManagment";
    private String MethodName = "GetMessageList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetMessageFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private String XmlFile = "";
    private AsyncTask<Void, Void, StructureMessageListRES> task;

    public GetMessageFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetReceiveMessageList(int count, MessageListListener messageListListener) {
        String startDateTime = getFarzinPrefrences().getReceiveMessageDataSyncDate();
        GetMessage(startDateTime, "", count, true, messageListListener);
    }

    public void GetSentMessageList(int count, MessageListListener messageListListener) {
        String startDateTime = getFarzinPrefrences().getSendMessageDataSyncDate();
        GetMessage(startDateTime, "", count, false, messageListListener);
    }

    public void GetReceiveMessageList(String startDateTime, String finishDateTime, int count, MessageListListener messageListListener) {
        GetMessage(startDateTime, finishDateTime, count, true, messageListListener);
    }

    public void GetSentMessageList(String startDateTime, String finishDateTime, int count, MessageListListener messageListListener) {
        GetMessage(startDateTime, finishDateTime, count, false, messageListListener);
    }

    private void GetMessage(String startDateTime, String finishDateTime, int count, final boolean isReceiveMessage, final MessageListListener messageListListener) {
        CallApi(MethodName, EndPoint, getSoapObject(startDateTime, finishDateTime, count, isReceiveMessage), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                CheckMessageListStructure(Xml, messageListListener);
            }

            @Override
            public void onFailed() {
                messageListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                messageListListener.onCancel();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private void CheckMessageListStructure(final String xml, final MessageListListener messageListListener) {
        //final StructureMessageListRES[] structureSentMessageListRES = new StructureMessageListRES[1];

        task = new AsyncTask<Void, Void, StructureMessageListRES>() {
            @Override
            protected StructureMessageListRES doInBackground(Void... voids) {
                Log.i("xml", "doInBackground SentMessage xmlfile= " + xml);
                StructureMessageListRES structureMessageListRES = new StructureMessageListRES();

                if (xml.contains(App.RESPONSEPATH)) {
                    MessageSaxHandler contentHandler = xmlToObject.parseXmlWithSax(xml, new MessageSaxHandler());
                    structureMessageListRES = contentHandler.getObject();
                    sleep();
                } else {
                    structureMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureMessageListRES.class);
                }

                return structureMessageListRES;
            }

            @Override
            protected void onPostExecute(StructureMessageListRES structureMessageListRES) {
                //Log.i("xml", "onPostExecute SentMessage xmlfile= " + xml);
                if (structureMessageListRES.getStrErrorMsg() == null || structureMessageListRES.getStrErrorMsg().isEmpty()) {
                    if (structureMessageListRES.getGetMessageListResult().size() <= 0) {
                        messageListListener.onSuccess(new ArrayList<>());
                    } else {
                        List<StructureMessageRES> messageListResult = structureMessageListRES.getGetMessageListResult();
                        messageListListener.onSuccess(new ArrayList<>(messageListResult));
                    }
                    getFarzinPrefrences().putMessageSentLastCheckDate(CustomFunction.getCurentDateTime().toString());
                } else {
                    messageListListener.onFailed("" + structureMessageListRES.getStrErrorMsg());
                }
            }
        };
        //task.execute();
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void sleep() {
        try {
            Thread.sleep(TimeValue.SecondsInMilli() * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SoapObject getSoapObject(String startDateTime, String finishDateTime, int count, boolean isReceiveMessage) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        SoapObject Filter = new SoapObject(NameSpace, "filter");

        /*--------Optional----------*/
        Filter.addProperty("CountOfRecord", count);
        Filter.addProperty("SortType", "ASC");
        Filter.addProperty("MsgSubject", "");
        Filter.addProperty("MsgText", "");
        /*--------Optional----------*/
        if (isReceiveMessage) {
            Filter.addProperty("MsgType", "receive");
        } else {
            Filter.addProperty("MsgType", "send");
        }

        if (!startDateTime.isEmpty()) {
            startDateTime = CustomFunction.arabicToDecimal(startDateTime);
            Filter.addProperty("StartDateTime", startDateTime);
        }
        if (!finishDateTime.isEmpty()) {
            finishDateTime = CustomFunction.arabicToDecimal(finishDateTime);
            Filter.addProperty("FinishDateTime", finishDateTime);
        }

        soapObject.addSoapObject(Filter);
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

