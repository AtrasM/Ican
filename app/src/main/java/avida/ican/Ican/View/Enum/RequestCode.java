package avida.ican.Ican.View.Enum;

/**
 * Created by AtrasVida on 2018-04-10 at 4:32 PM
 */

public enum RequestCode {
    AudioRecordRequestCode(0),
    FilePickerRequestCode(112),
    MediaPickerRequestCode(222);
    private int intValue;

        RequestCode(int value) {
        intValue = value;
    }
    public int getValue() {
        return intValue;
    }
}
