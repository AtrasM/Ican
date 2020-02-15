package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AtrasVida on 2019-12-22 at 12:46 PM
 */
@Root(name = "GetRoomMessagesResult")
public class StructureChatRoomMessagesResultRES {
    @ElementList(required = false, entry = "ChatRoomMessageModel", inline = true)
    List<StructureChatRoomMessageModelRES> ChatRoomMessageModel = new ArrayList<>();

    public List<StructureChatRoomMessageModelRES> getChatRoomMessageModel() {
        return ChatRoomMessageModel;
    }

    public void setChatRoomMessageModel(List<StructureChatRoomMessageModelRES> chatRoomMessageModel) {
        ChatRoomMessageModel = chatRoomMessageModel;
    }
}
