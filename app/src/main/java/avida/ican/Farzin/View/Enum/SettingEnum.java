package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-10-27 at 1:17 PM
 */

public enum SettingEnum {
    SYNC(100),
    MANUALLY(101),
    AUTOMATIC(102),
    PRIVACY(103);

    private int value;

    private SettingEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
