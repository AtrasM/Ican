package avida.ican.Ican.View.Custom.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum SimpleDateFormatEnum {
    DateTime_yyyy_MM_dd_hh_mm_ss("yyyy-MM-dd hh:mm:ss"),
    Date_yyyy_MM_dd("yyyy-MM-dd"),
    Time_hh_mm_ss("hh:mm:ss"),
    Time_hh_mm("hh:mm");


    private final String name;

    private SimpleDateFormatEnum(String s) {
        name = s;
    }
    public String getValue() {
        return this.name;
    }
    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

}
