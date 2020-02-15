package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2019-11-16 at 11:03 AM
 */

public enum MessageJobServiceNameEnum {
    SendMessage("SendMessage"),
    GetRecieveMessage("GetRecieveMessage"),
    GetSentMessage("GetSentMessage"),
    Finish("Finish"),
    Failed("Failed");
    private String strValue;
    private int intValue;

    MessageJobServiceNameEnum(int value) {
        intValue = value;
    }

    MessageJobServiceNameEnum(String value) {
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
