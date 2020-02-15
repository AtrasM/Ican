package avida.ican.Farzin.Model.Interface.Chat.Room;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;

/**
 * Created by AtrasVida on 2019-12-23 at 12:51 PM
 */

public interface GetChatRoomListListener {
    void onSuccess(ArrayList<StructureChatRoomModelRES> structureChatRoomModelListRES);

    void onFailed(String message);

    void onCancel();

}
