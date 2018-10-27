package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-10-27 at 1:17 PM
 */

public enum CartableDocumentDetailActionsEnum {
    TAEED(100),
    ERJA(101),
    TAEED_ERJA(102);

    private int value;

    private CartableDocumentDetailActionsEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
