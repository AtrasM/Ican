package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2018-04-24 at 1:39 PM
 */

public enum MetaDataNameEnum {
    SyncUserAndRoleList(0);
    private int intValue;

       MetaDataNameEnum(int value) {
        intValue = value;
    }
    public int getValue() {
        return intValue;
    }
}
