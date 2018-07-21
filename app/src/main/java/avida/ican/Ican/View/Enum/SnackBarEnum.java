package avida.ican.Ican.View.Enum;

/**
 * Created by AtrasVida on 2018-04-23 at 11:15 AM
 */

public enum SnackBarEnum {
    SNACKBAR_LONG_TIME(0),
    SNACKBAR_SHORT_TIME(1),
    SNACKBAR_INDEFINITE(2);
    private int intValue;

     SnackBarEnum(int value) {
        intValue = value;
    }

}
