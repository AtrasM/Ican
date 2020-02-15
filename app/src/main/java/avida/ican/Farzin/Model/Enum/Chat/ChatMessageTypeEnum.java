package avida.ican.Farzin.Model.Enum.Chat;

/**
 * Created by AtrasVida on 2019-12-29 at 12:19 PM
 */

public enum ChatMessageTypeEnum {
    Text(1);
    private int intValue;

    ChatMessageTypeEnum(int value) {
        intValue = value;
    }

    public int getIntValue() {
        return intValue;
    }
}
