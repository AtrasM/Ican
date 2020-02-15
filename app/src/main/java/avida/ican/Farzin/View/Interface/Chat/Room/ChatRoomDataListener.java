package avida.ican.Farzin.View.Interface.Chat.Room;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;

/**
 * Created by AtrasVida on 2019-12-25 at 11:45 PM
 */

public interface ChatRoomDataListener {
    void newData(StructureChatRoomListDB structureChatRoomListDB);
    void noData();
}
