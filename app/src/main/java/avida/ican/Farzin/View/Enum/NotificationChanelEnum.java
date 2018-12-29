package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-12-24 at 11:46 AM
 */

public enum NotificationChanelEnum {
    Message(1),
    Cartable(2);
    private int value;

    private NotificationChanelEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
