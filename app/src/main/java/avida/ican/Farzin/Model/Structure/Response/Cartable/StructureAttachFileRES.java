package avida.ican.Farzin.Model.Structure.Response.Cartable;

/**
 * Created by AtrasVida on 2019-07-23 at 5:38 PM
 */

public class StructureAttachFileRES {
    private boolean AttachFileResult;
    private String strErrorMsg;

    public boolean isAttachFileResult() {
        return AttachFileResult;
    }

    public void setAttachFileResult(boolean attachFileResult) {
        AttachFileResult = attachFileResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
