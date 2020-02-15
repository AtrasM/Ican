package avida.ican.Farzin.Model.Structure.Response.Cartable;

/**
 * Created by AtrasVida on 2019-06-03 at 12:35 PM
 */

public class StructureSignDocumentRES {
    private boolean SignDocumentResult;
    private String strErrorMsg;

    public boolean isSignDocumentResult() {
        return SignDocumentResult;
    }

    public void setSignDocumentResult(boolean signDocumentResult) {
        SignDocumentResult = signDocumentResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
