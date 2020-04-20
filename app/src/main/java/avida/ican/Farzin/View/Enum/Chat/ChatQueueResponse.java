package avida.ican.Farzin.View.Enum.Chat;

/**
 * Created by AtrasVida on 2020-04-14 at 13:50 PM
 */

public enum ChatQueueResponse {
    ShowSendMessage("showSendMessage"),
    Failed("Failed");
    //ReceiveMessage("receiveMessage");

    private final String name;

    private ChatQueueResponse(String s) {
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
