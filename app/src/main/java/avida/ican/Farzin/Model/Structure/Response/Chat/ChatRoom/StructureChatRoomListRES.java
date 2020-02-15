package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2019-12-22 at 12:42 PM
 */
@Root(name = "GetRoomListResponse")

public class StructureChatRoomListRES {
    @Element()
    StructureRoomListResultRES GetRoomListResult = new StructureRoomListResultRES();
    @Element(required = false)
    private String strErrorMsg;

    public StructureRoomListResultRES getGetRoomListResult() {
        return GetRoomListResult;
    }

    public void setGetRoomListResult(StructureRoomListResultRES getRoomListResult) {
        GetRoomListResult = getRoomListResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
