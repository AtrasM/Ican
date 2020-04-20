package avida.ican.Farzin.Chat.Room;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Chat.Room.GetChatRoomListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomListRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2019-05-26 at 12:47 PM
 */


public class GetChatRoomListFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "ChatWebService";
    private String MethodName = "GetRoomList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetChatRoomListFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetChatRoomListFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void getRoomList(GetChatRoomListListener listListener) {
        CallRequest(getSoapObject(), listListener);
    }

    private void CallRequest(SoapObject soapObject, final GetChatRoomListListener listener) {

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

    private void initStructure(String xml, GetChatRoomListListener listener) {
        StructureChatRoomListRES structureChatRoomListRES = xmlToObject.DeserializationSimpleXml(xml, StructureChatRoomListRES.class);
        if (structureChatRoomListRES.getStrErrorMsg() == null || structureChatRoomListRES.getStrErrorMsg().isEmpty()) {
            ArrayList<StructureChatRoomModelRES> structureChatRoomsRES = new ArrayList<>(structureChatRoomListRES.getGetRoomListResult().getChatRoomModel());
            if (structureChatRoomsRES.size() <= 0) {
                listener.onSuccess(new ArrayList<>());
            } else {
                listener.onSuccess(structureChatRoomsRES);
            }

        } else {
            listener.onFailed("" + structureChatRoomListRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject() {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
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

