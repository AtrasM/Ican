package avida.ican.Ican.View.Custom.Enum;

/**
 * Created by AtrasVida on 2018-12-24 at 4:05 PM
 */

public enum CompareDateTimeEnum {
    isAfter("date2 is after date1"),
    isEqual("date2 is equal date1"),
    isBefore("date2 is before date1"),
    ErrorFormat("");

    private final String name;

    private CompareDateTimeEnum(String s) {
        name = s;
    }
    public String getValue() {
        return this.name;
    }
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

}
