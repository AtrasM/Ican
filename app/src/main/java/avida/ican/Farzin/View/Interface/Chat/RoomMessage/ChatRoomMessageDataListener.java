package avida.ican.Farzin.View.Interface.Chat.RoomMessage;

import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;

/**
 * Created by AtrasVida on 2020-01-12 at 11:54 AM
 */

public interface ChatRoomMessageDataListener {
    void downloadedReplyData(StructureChatRoomMessageDB structureChatRoomMessageDB);
    void newData(StructureChatRoomMessageDB structureChatRoomMessageDB);
    void noData();
}
