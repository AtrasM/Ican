package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2019-07-22 at 2:20 PM
 */

public enum QueueEnum {
    CreatDocument(1),
    DocumentOperator(2),
    DocumentAttachFile(3),
    Message(4);

    private int value;

    private QueueEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
