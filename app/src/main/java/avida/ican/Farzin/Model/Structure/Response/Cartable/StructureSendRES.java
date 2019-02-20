package avida.ican.Farzin.Model.Structure.Response.Cartable;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-11-27 at 11:29 AM
 */

public class StructureSendRES {
    private int AppendResult;
    private String strErrorMsg;

    public int getAppendResult() {
        return AppendResult;
    }

    public void setAppendResult(int appendResult) {
        AppendResult = appendResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
