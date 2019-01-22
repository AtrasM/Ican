package avida.ican.Farzin.Presenter.Message;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.MessageSaxHandler;
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

    private void CheckSentMessageListStructure(String xml, MessageListListener messageListListener) {
        StructureSentMessageListRES structureSentMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureSentMessageListRES.class);
        if (structureSentMessageListRES.getStrErrorMsg() == null || structureSentMessageListRES.getStrErrorMsg().isEmpty()) {
            if (structureSentMessageListRES.getGetSentMessageListResult().size() <= 0) {
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
            } else {
                List<StructureMessageRES> messageListResult = structureSentMessageListRES.getGetSentMessageListResult();
                // changeXml.CharDecoder(structureMessageList.get())
                messageListListener.onSuccess(new ArrayList<>(messageListResult));
            }
            getFarzinPrefrences().putMessageSentLastCheckDate(CustomFunction.getCurentDateTime().toString());
        } else {
            messageListListener.onFailed("" + structureSentMessageListRES.getStrErrorMsg());
        }
    }

    private void CheckReciverMessageListStructure(String xml, MessageListListener messageListListener) {
        StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();
        if (xml.contains(App.RESPONSEPATH)) {
            //structureRecieveMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureRecieveMessageListRES.class);

            MessageSaxHandler contentHandler = xmlToObject.parseXmlWithSax(xml, new MessageSaxHandler());
            structureRecieveMessageListRES = contentHandler.getObject();
            Log.i("MessageData", "xml= " + xml);
        } else {
            structureRecieveMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureRecieveMessageListRES.class);
        }
        if (structureRecieveMessageListRES.getStrErrorMsg() != null || structureRecieveMessageListRES.getStrErrorMsg().isEmpty()) {
            if (structureRecieveMessageListRES.getGetRecieveMessageListResult().size() <= 0) {
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
            } else {
                List<StructureMessageRES> messageListResult = structureRecieveMessageListRES.getGetRecieveMessageListResult();
                // changeXml.CharDecoder(structureMessageList.get())
                messageListListener.onSuccess(new ArrayList<>(messageListResult));
            }
            getFarzinPrefrences().putMessageRecieveLastCheckDate(CustomFunction.getCurentDateTime().toString());


        } else {
            messageListListener.onFailed("" + structureRecieveMessageListRES.getStrErrorMsg());
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
            //String Xml = new CustomFunction().readXmlResponseFromStorage();
            try {
                if (Xml.contains(MetodName)) {
                    Xml = changeXml.CropAsResponseTag(Xml, MetodName);
                }
               /* XmlFile = changeXml.GetContentTag(Xml, "<MessageFiles>", "</MessageFiles>");
                Xml = changeXml.RemoveContentWithTag(Xml, "<MessageFiles>", "</MessageFiles>");
                Log.i("MessageData", "MessageWithout data Xml= " + Xml);
                Log.i("MessageData", "XmlFile= " + XmlFile);
                new CustomFunction().saveXmlResponseToStorage(XmlFile);
                List<StructureMessageAttachRES> MessageFiles = new ArrayList<>();
                StructureMessageRES structureMessageRES = xmlToObject.DeserializationGsonXml(XmlFile, StructureMessageRES.class);
                Log.i("MessageData", "MessageFiles= " + structureMessageRES.getMessageFiles().size());
                MessageFiles = structureMessageRES.getMessageFiles();*/

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

    private StructureRecieveMessageListRES parseXmlWithSax(String xmlFilePath) {
        StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();
        List<StructureMessageRES> Messages = new ArrayList<>();
        File file = new File(xmlFilePath);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SaxHandler
            MessageSaxHandler saxHandler = new MessageSaxHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(fileInputStream));
            // get the `Employee list`
            structureRecieveMessageListRES = saxHandler.getObject();
        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser error =" + ex.toString());
            Log.d("XML", "SAXXMLParser: parse() failed");
        }

        return structureRecieveMessageListRES;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}

