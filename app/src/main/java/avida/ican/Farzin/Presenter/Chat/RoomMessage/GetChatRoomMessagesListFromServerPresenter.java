package avida.ican.Farzin.Presenter.Chat.RoomMessage;

import androidx.annotation.Nullable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Chat.Room.GetChatRoomListListener;
import avida.ican.Farzin.Model.Interface.Chat.RoomMessage.GetChatRoomMessageListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessagesResponseRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2019-05-26 at 12:47 PM
 */


public class GetChatRoomMessagesListFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "ChatWebService";
    private String MethodName = "GetRoomMessages";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetChatRoomMessagesListFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetChatRoomMessagesListFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void getMessageList(String chatRoomId, GetChatRoomMessageListListener listListener) {
        CallRequest(getSoapObject(chatRoomId, null, null, null, null), listListener);
    }

    public void getMessageList(String chatRoomId, String tableExtension, String lastMessageID, GetChatRoomMessageListListener listListener) {
        CallRequest(getSoapObject(chatRoomId, tableExtension, lastMessageID, null, null), listListener);
    }

    public void getMessageList(String chatRoomId, String tableExtension, String lastMessageID, String toMessageID, String toTableExtension, GetChatRoomMessageListListener listListener) {
        CallRequest(getSoapObject(chatRoomId, tableExtension, lastMessageID, toMessageID, toTableExtension), listListener);
    }

    private void CallRequest(SoapObject soapObject, final GetChatRoomMessageListListener listener) {

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

    private void initStructure(String xml, GetChatRoomMessageListListener listener) {
        StructureChatRoomMessagesResponseRES structureChatRoomMessagesResponseRES = xmlToObject.DeserializationSimpleXml(xml, StructureChatRoomMessagesResponseRES.class);
        if (structureChatRoomMessagesResponseRES.getStrErrorMsg() == null || structureChatRoomMessagesResponseRES.getStrErrorMsg().isEmpty()) {
            ArrayList<StructureChatRoomMessageModelRES> structureChatRoomsRES = new ArrayList<>(structureChatRoomMessagesResponseRES.getGetRoomMessagesResult().getChatRoomMessageModel());
            if (structureChatRoomsRES.size() <= 0) {
                listener.onSuccess(new ArrayList<>());
            } else {
                listener.onSuccess(structureChatRoomsRES);
            }
        } else {
            listener.onFailed("" + structureChatRoomMessagesResponseRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(String chatRoomId, @Nullable String tableExtension, @Nullable String lastMessageID, @Nullable String toMessageID, @Nullable String toTableExtension) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("chatRoomId", chatRoomId);
        if (tableExtension != null && !tableExtension.isEmpty()) {
            soapObject.addProperty("tableExtension", tableExtension);
        }
        if (lastMessageID != null && !lastMessageID.isEmpty()) {
            soapObject.addProperty("lastMessageID", lastMessageID);
        }
        if (toMessageID != null && !toMessageID.isEmpty()) {
            soapObject.addProperty("toMessageID", toMessageID);
        }
        if (toTableExtension != null && !toTableExtension.isEmpty()) {
            soapObject.addProperty("toTableExtension", toTableExtension);
        }
        return soapObject;
    }

    private void CallApi(String MethodName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseChatUrl();
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
                Xml = changeXml.charDecoder(Xml);
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

