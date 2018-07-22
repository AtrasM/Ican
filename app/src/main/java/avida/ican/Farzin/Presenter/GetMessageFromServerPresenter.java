package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.MessageListListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.StructureRecieveMessageListRES;
import avida.ican.Farzin.Model.Structure.Response.StructureSentMessageListRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

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
    private String Tag = "SendMessageToServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private final int COUNT = 1;


    public GetMessageFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetRecieveMessageList(int page, MessageListListener messageListListener) {
        this.MetodName = "GetRecieveMessageList";
        GetMessage(page, true, messageListListener);
    }


    public void GetSentMessageList(int page, MessageListListener messageListListener) {
        this.MetodName = "GetSentMessageList";
        GetMessage(page, false, messageListListener);
    }

    private void GetMessage(int page, final boolean RecieveMessage, final MessageListListener messageListListener) {

        CallApi(MetodName, EndPoint, getSoapObject(page, COUNT), new DataProcessListener() {
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
        if (structureSentMessageListRES.getStrErrorMsg() != null) {
            messageListListener.onFailed("" + structureSentMessageListRES.getStrErrorMsg());
        } else {
            if (structureSentMessageListRES.getGetSentMessageListResult().size() <= 0) {
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
            } else {
                List<StructureMessageRES> messageListResult = structureSentMessageListRES.getGetSentMessageListResult();
                // changeXml.CharDecoder(structureMessageList.get())
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>(messageListResult));
            }
        }
    }

    private void CheckReciverMessageListStructure(String xml, MessageListListener messageListListener) {
        StructureRecieveMessageListRES structureRecieveMessageListRES = xmlToObject.DeserializationSimpleXml(xml, StructureRecieveMessageListRES.class);
        if (structureRecieveMessageListRES.getStrErrorMsg() != null) {
            messageListListener.onFailed("" + structureRecieveMessageListRES.getStrErrorMsg());
        } else {
            if (structureRecieveMessageListRES.getGetRecieveMessageListResult().size() <= 0) {
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>());
            } else {
                List<StructureMessageRES> messageListResult = structureRecieveMessageListRES.getGetRecieveMessageListResult();
                // changeXml.CharDecoder(structureMessageList.get())
                messageListListener.onSuccess(new ArrayList<StructureMessageRES>(messageListResult));
            }
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

