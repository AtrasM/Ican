package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AtrasVida on 2019-12-22 at 12:46 PM
 */
@Root(name = "GetRoomListResult")
public class StructureRoomListResultRES {
    @ElementList(required = false, entry = "ChatRoomModel", inline = true)
    List<StructureChatRoomModelRES> ChatRoomModel = new ArrayList<>();

    public List<StructureChatRoomModelRES> getChatRoomModel() {
        return ChatRoomModel;
    }

    public void setChatRoomModel(List<StructureChatRoomModelRES> chatRoomModel) {
        ChatRoomModel = chatRoomModel;
    }
}
