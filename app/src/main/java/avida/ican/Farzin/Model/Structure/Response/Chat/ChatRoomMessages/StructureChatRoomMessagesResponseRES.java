package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2019-12-22 at 12:42 PM
 */
@Root(name = "GetRoomMessagesResponse")

public class StructureChatRoomMessagesResponseRES {
    @Element()
    StructureChatRoomMessagesResultRES GetRoomMessagesResult = new StructureChatRoomMessagesResultRES();
    @Element(required = false)
    private String strErrorMsg;

    public StructureChatRoomMessagesResultRES getGetRoomMessagesResult() {
        return GetRoomMessagesResult;
    }

    public void setGetRoomMessagesResult(StructureChatRoomMessagesResultRES getRoomMessagesResult) {
        GetRoomMessagesResult = getRoomMessagesResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
