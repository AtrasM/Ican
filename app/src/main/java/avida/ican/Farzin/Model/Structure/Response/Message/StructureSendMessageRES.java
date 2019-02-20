package avida.ican.Farzin.Model.Structure.Response.Message;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-03-17 at 3:33 PM
 */

public class StructureSendMessageRES {
    private int SendMessageResult;
    private String strErrorMsg;

    public int getSendMessageResult() {
        return SendMessageResult;
    }

    public void setSendMessageResult(int sendMessageResult) {
        SendMessageResult = sendMessageResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
