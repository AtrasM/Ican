package avida.ican.Ican.View.Custom.Enum;

/**
 * Created by AtrasVida on 2018-12-24 at 4:05 PM
 */

public enum CompareTimeEnum {
    isAfter("time + delay >= curent system time"),
    isEqual("time + delay > curent system time"),
    isBefore("time + delay < curent system time"),
    Error("");

    private final String name;

    private CompareTimeEnum(String s) {
        name = s;
    }
    public String getValue() {
        return this.name;
    }
    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

}
