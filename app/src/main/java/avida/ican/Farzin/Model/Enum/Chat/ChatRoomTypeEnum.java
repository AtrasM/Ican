package avida.ican.Farzin.Model.Enum.Chat;

/**
 * Created by AtrasVida on 2019-12-22 at 3:56 PM
 */

public enum ChatRoomTypeEnum {
    NoType(-1),
    All(0),
    Private(1),
    Room(2),
    Channele(3),
    ChatWithYoureSelf(4);
    private int intValue;

    ChatRoomTypeEnum(int value) {
        intValue = value;
    }

    public int getIntValue() {
        return intValue;
    }
}
