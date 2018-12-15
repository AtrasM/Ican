package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2018-04-24 at 1:39 PM
 */

public enum MetaDataNameEnum {
    SyncUserAndRole("SyncUserAndRole"),
    SyncCartableDocument("SyncCartableDocument"),
    SyncSendMessage("SyncSendMessage"),
    SyncReceiveMessage("SyncReceiveMessage");
    private String strValue;
    private int intValue;


    MetaDataNameEnum(int value) {
        intValue = value;
    }
    MetaDataNameEnum(String value) {
        strValue = value;
    }


    public int getIntValue() {
        return intValue;
    }
    public String getStringValue() {
        return strValue;
    }
}
