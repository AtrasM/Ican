package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2018-08-06 at 3:30 PM
 */

public enum Type {
    SENDED("send"),
    ALL(""),
    RECEIVED("receive");
    private String strValue;

    Type(String value) {
        strValue = value;
    }

    public String getValue() {
        return this.strValue;
    }
}
