package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.saxHandler.MessageSaxHandler;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureRecieveMessageListRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureSentMessageListRES;
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
    private String MetodName = "";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetMessageFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private String XmlFile = "";
    private AsyncTask<Void, Void, StructureSentMessageListRES> SentMessageTask;
    private AsyncTask<Void, Void, StructureRecieveMessageListRES> RecieveMessageTask;

    public GetMessageFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetRecieveMessageList(int page, int count, MessageListListener messageListListener) {
        this.MetodName = "GetRecieveMessageList";
        GetMessage(page, count, true, messageListListener);

    }

    public void GetSentMessageList(int page, int count, MessageListListener messageListListener) {
        this.MetodName = "GetSentMessageList";
        GetMessage(page, count, false, messageListListener);
    }

    private void GetMessage(int page, int count, final boolean RecieveMessage, final MessageListListener messageListListener) {
        CallApi(MetodName, EndPoint, getSoapObject(page, count), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                if (RecieveMessage) {
                    CheckReciverMessageListStructure(Xml, messageListListener);
                } else {
                    CheckSentMessageListStructure(Xml, messageListListener);
                }


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
    private void CheckSentMessageListStructure(final String xml, final MessageListListener messageListListener) {
        //final StructureSentMessageListRES[] structureSentMessageListRES = new StructureSentMessageListRES[1];


        SentMessageTask = new AsyncTask<Void, Void, StructureSentMessageListRES>() {
            @Override
            protected StructureSentMessageListRES doInBackground(Void... voids) {
                Log.i("xml", "doInBackground SentMessage xmlfile= " + xml);
                StructureSentMessageListRES structureSentMessageListRES = new StructureSentMessageListRES();

                if (xml.contains(App.RESPONSEPATH)) {
                    MessageSaxHandler contentHandler = xmlToObject.parseXmlWithSax(xml, new MessageSaxHandler());
                    structureSentMessageListRES = contentHandler.getObject(false);
                    sleep();
                } else {
                    structureSentMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureSentMessageListRES.class);
                }

                return structureSentMessageListRES;
            }

            @Override
            protected void onPostExecute(StructureSentMessageListRES structureSentMessageListRES) {
                Log.i("xml", "onPostExecute SentMessage xmlfile= " + xml);
                if (structureSentMessageListRES.getStrErrorMsg() == null || structureSentMessageListRES.getStrErrorMsg().isEmpty()) {
                    if (structureSentMessageListRES.getGetSentMessageListResult().size() <= 0) {
                        messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
                    } else {
                        List<StructureMessageRES> messageListResult = structureSentMessageListRES.getGetSentMessageListResult();
                        messageListListener.onSuccess(new ArrayList<>(messageListResult));
                    }
                    getFarzinPrefrences().putMessageSentLastCheckDate(CustomFunction.getCurentDateTime().toString());
                } else {
                    messageListListener.onFailed("" + structureSentMessageListRES.getStrErrorMsg());
                }
            }
        };
        //SentMessageTask.execute();
        SentMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @SuppressLint("StaticFieldLeak")
    private void CheckReciverMessageListStructure(final String xml, final MessageListListener messageListListener) {
        //final StructureRecieveMessageListRES[] structureRecieveMessageListRES = new StructureRecieveMessageListRES[1];
        RecieveMessageTask = new AsyncTask<Void, Void, StructureRecieveMessageListRES>() {
            @Override
            protected StructureRecieveMessageListRES doInBackground(Void... voids) {
                Log.i("xml", "doInBackground ReciverMessage xmlfile= " + xml);
                StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();

                if (xml.contains(App.RESPONSEPATH)) {
                    MessageSaxHandler contentHandler = xmlToObject.parseXmlWithSax(xml, new MessageSaxHandler());
                    structureRecieveMessageListRES = contentHandler.getObject(true);
                    sleep();
                } else {
                    structureRecieveMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureRecieveMessageListRES.class);
                }
                return structureRecieveMessageListRES;
            }

            @Override
            protected void onPostExecute(StructureRecieveMessageListRES structureRecieveMessageListRES) {
                Log.i("xml", "onPostExecute ReciverMessage xmlfile= " + xml);
                if (structureRecieveMessageListRES.getStrErrorMsg() == null || structureRecieveMessageListRES.getStrErrorMsg().isEmpty()) {
                    if (structureRecieveMessageListRES.getGetRecieveMessageListResult().size() <= 0) {
                        messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
                    } else {
                        List<StructureMessageRES> messageListResult = structureRecieveMessageListRES.getGetRecieveMessageListResult();
                        // changeXml.charDecoder(structureMessageList.get())
                        messageListListener.onSuccess(new ArrayList<>(messageListResult));
                    }
                    getFarzinPrefrences().putMessageRecieveLastCheckDate(CustomFunction.getCurentDateTime().toString());
                } else {
                    messageListListener.onFailed("" + structureRecieveMessageListRES.getStrErrorMsg());
                }
            }
        };
        //RecieveMessageTask.execute();
        RecieveMessageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    private void sleep() {
        try {
            Thread.sleep(TimeValue.SecondsInMilli() * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private SoapObject getSoapObject(int page, int count) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("iPage", page);
        soapObject.addProperty("iRecordPerPage", count);
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
                if (Xml.contains(MetodName)) {
                    Xml = changeXml.CropAsResponseTag(Xml, MetodName);
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

