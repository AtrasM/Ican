package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2019-11-13 at 3:31 PM
 */

public enum JobServiceIDEnum {
    CHECK_SERVER_AVIABLE_JOBID("CheckServerAviableJobID"),
    CARTABLE_PARENT_SERVICE_JOBID("CartableParentJobServiceID"),
    MESSAGE_PARENT_SERVICE_JOBID("MessageParentJobService"),
    DOCUMENT_OPRERATORS_SERVICE_JOBID("DocumentOpreratorsJobServiceID"),
    GET_CONFIRMATION_SERVICE_JOBID("GetConfirmationListJobServiceID"),
    IMPORT_DOCUMENT_SERVICE_JOBID("ImportDocumentJobServiceID"),
    DOCUMENT_ATTACHFILE_SERVICE_JOBID("DocumentAttachFileJobServiceID"),
    GET_CARTABLE_DOCUMENT_SERVICE_JOBID("GetCartableDocumentJobServiceID"),
    GET_RECIEVE_MESSAGE_SERVICE_JOBID("GetRecieveMessageJobServiceID"),
    GET_SENT_MESSAGE_SERVICE_JOBID("GetSentMessageJob"),
    SEND_MESSAGE_SERVICE_JOBID("SendMessageJobService");
    private String strValue;
    private int intValue;

    JobServiceIDEnum(int value) {
        intValue = value;
    }

    JobServiceIDEnum(String value) {
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
