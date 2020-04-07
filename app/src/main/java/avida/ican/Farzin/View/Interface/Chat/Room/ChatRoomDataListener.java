package avida.ican.Farzin.View.Interface.Chat.Room;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;

/**
 * Created by AtrasVida on 2019-12-25 at 11:45 PM
 */

public interface ChatRoomDataListener {
    void newData(StructureChatRoomDB structureChatRoomDB);
    void noData();
}
