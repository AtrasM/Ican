package avida.ican.Farzin.Model.Interface.Chat.RoomMessage;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;

/**
 * Created by AtrasVida on 2020-01-08 at 1:19 PM
 */

public interface GetChatRoomMessageListListener {
    void onSuccess(ArrayList<StructureChatRoomMessageModelRES> structureChatRoomMessagesModelRES);

    void onFailed(String message);

    void onCancel();

}
