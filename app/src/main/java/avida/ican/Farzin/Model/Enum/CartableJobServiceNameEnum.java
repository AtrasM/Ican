package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2019-11-13 at 12:28 PM
 */

public enum CartableJobServiceNameEnum {
    DocumentOprator("DocumentOprator"),
    GetConfirmation("GetConfirmation"),
    ImportDocument("ImportDocument"),
    AttachFile("AttachFile"),
    GetCartable("GetCartable"),
    Finish("Finish"),
    Failed("Failed");
    private String strValue;
    private int intValue;


    CartableJobServiceNameEnum(int value) {
        intValue = value;
    }

    CartableJobServiceNameEnum(String value) {
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
