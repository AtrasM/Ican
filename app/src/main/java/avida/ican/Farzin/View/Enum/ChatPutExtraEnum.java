package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum ChatPutExtraEnum {
    RoomMessageIDString("RoomMessageIDString");



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
