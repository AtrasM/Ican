package avida.ican.Farzin.Model.Interface.Chat.RoomMessage;

import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;

/**
 * Created by AtrasVida on 2020-01-12 at 2:40 AM
 */

public interface ChatRoomMessageModelQuerySaveListener {
    void onSuccess(StructureChatRoomMessageDB structureChatRoomMessageDB);

    void onExisting();

    void onFailed(String errorMessage);

    void onCancel();

}
