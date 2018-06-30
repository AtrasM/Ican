package avida.ican.Farzin.Model.Structure.OutPut;

/**
 * Created by AtrasVida on 2018-03-17 at 3:33 PM
 */

public class StructureSendMessageOP {
    private int SendMessageResult;
    private String strErrorMsg;

    public int getSendMessageResult() {
        return SendMessageResult;
    }

    public void setSendMessageResult(int sendMessageResult) {
        SendMessageResult = sendMessageResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
