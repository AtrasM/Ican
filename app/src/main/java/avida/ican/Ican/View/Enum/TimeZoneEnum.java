package avida.ican.Ican.View.Enum;

/**
 * Created by AtrasVida on 2019-07-20 at 12:56 PM
 */

public enum TimeZoneEnum {
    IRAN_TEHRAN("GMT+3:30");



    private final String name;

    private TimeZoneEnum(String s) {
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
