package avida.ican.Farzin.Model.Interface.Chat.Room;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;

/**
 * Created by AtrasVida on 2019-12-24 at 1:13 PM
 */

public interface ChatRoomModelQuerySaveListener {
    void onSuccess(StructureChatRoomListDB structureChatRoomListDB);

    void onExisting();

    void onFailed(String errorMessage);

    void onCancel();

}
