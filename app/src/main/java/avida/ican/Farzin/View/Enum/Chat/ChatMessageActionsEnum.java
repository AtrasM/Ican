package avida.ican.Farzin.View.Enum.Chat;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2020-04-13 at 11:22 AM
 */

public enum ChatMessageActionsEnum {
    ShowSendMessage("showSendMessage"),
    ReceiveMessage("receiveMessage");

    private final String name;

    private ChatMessageActionsEnum(String s) {
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
