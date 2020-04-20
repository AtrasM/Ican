package avida.ican.Farzin.View.Enum.Chat;

/**
 * Created by AtrasVida on 2020-04-13 at 11:23 AM
 */

public enum ChatPutExtraEnum {
    RoomReceiver("ChatRoomReceiver"),
    QueueResponseReceiver("ChatQueueResponseReceiver"),
    MessageReceiver("ChatMessageReceiver"),
    ConnectionReceiver("ChatConnectionReceiver"),
    RoomMessageIDString("RoomMessageIDString"),
    MessageBND("ChatMessageBND"),
    MessageTempID("ChatMessageTempID"),
    UpdateRoom("UpdateChatRoom"),
    ConnectionStatus("ChatConnectionStatus"),
    QueueServiceResponse("QueueServiceResponse");


    private final String name;

    private ChatPutExtraEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String getValue() {
        return this.name;
    }
}
