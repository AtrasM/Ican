package avida.ican.Farzin.View.Interface.Chat.Room;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;

/**
 * Created by AtrasVida on 2019-12-28 at 1:38 PM
 */

public interface ListenerAdapterChatRoom {
    void onDelet(StructureChatRoomListDB structureChatRoomListDB);
    void onItemClick(StructureChatRoomListDB structureChatRoomListDB, int position);
}
