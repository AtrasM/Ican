package avida.ican.Farzin.View.Interface.Chat.Room;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;

/**
 * Created by AtrasVida on 2019-12-28 at 1:38 PM
 */

public interface ListenerAdapterChatRoom {
    void onDelet(StructureChatRoomDB structureChatRoomDB);
    void onItemClick(StructureChatRoomDB structureChatRoomDB, int position);
}
