package avida.ican.Ican.View.Enum;

/**
 * Created by AtrasVida on 2018-03-13 at 12:15 PM
 */

public enum ToastEnum {

    TOAST_LONG_TIME(1),
    TOAST_SHORT_TIME(0);
    private int intValue;

    private   ToastEnum(int value) {
        intValue = value;
    }

}
