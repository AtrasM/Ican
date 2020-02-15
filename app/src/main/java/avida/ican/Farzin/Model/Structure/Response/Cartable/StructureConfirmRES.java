package avida.ican.Farzin.Model.Structure.Response.Cartable;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-10-24 at 10:27 AM
 */

public class StructureConfirmRES {
    private boolean ResponseResult;
    private String strErrorMsg;

    public boolean isResponseResult() {
        return ResponseResult;
    }

    public void setResponseResult(boolean responseResult) {
        ResponseResult = responseResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
