package avida.ican.Farzin.View.Interface.Chat.RoomMessage;

import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;

/**
 * Created by AtrasVida on 2020-01-12 at 11:51 AM
 */

public interface ListenerAdapterChatRoomMessage {
    void onReplyClick(StructureChatRoomMessageDB structureChatRoomMessageDB,int maxSize,int currentPos);

    void onDelet(StructureChatRoomMessageDB structureChatRoomMessageDB);

    void onItemClick(StructureChatRoomMessageDB structureChatRoomMessageDB, int position);
}
