package avida.ican.Ican.View.Custom.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum SimpleDateFormatEnum {
    DateTime_yyyy_MM_dd_hh_mm_ss("yyyy-MM-dd hh:mm:ss"),
    DateTime_yyyy_MM_dd_hh_mm_ss_2("yyyy MM dd hh:mm:ss"),
    DateTime_Y_m_d_H_i_s("Y/m/d H:i:s"),
    DateTime_as_iso_8601("yyyy-MM-dd'T'HH:mm:ss"),
    EE_MMM_dd_HH_mm_ss_z_yyyy("EE MMM dd HH:mm:ss z yyyy"),
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
