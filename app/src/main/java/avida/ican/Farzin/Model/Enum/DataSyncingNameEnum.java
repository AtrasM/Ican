package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2018-04-24 at 1:39 PM
 */

public enum DataSyncingNameEnum {
    SyncUserAndRole("SyncUserAndRole"),
    SyncDocumentActions("SyncDocumentActions"),
    SyncCartableDocument("SyncCartableDocument"),
    SyncSendMessage("SyncSendMessage"),
    SyncReceiveMessage("SyncReceiveMessage"),
    SyncFailed("SyncFailed");
    private String strValue;
    private int intValue;


    DataSyncingNameEnum(int value) {
        intValue = value;
    }

    DataSyncingNameEnum(String value) {
        strValue = value;
    }


    public int getIntValue() {
        return intValue;
    }

    public static int getMetaDataCount() {
        return 2;
    }

    public static int getDataSyncingCount() {
        return (getAllCount() - getMetaDataCount());
    }

    private static int getAllCount() {
        return 5;
    }

    public String getStringValue() {
        return strValue;
    }
}
