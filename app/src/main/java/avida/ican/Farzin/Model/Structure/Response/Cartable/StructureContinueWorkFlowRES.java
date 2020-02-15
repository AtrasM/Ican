package avida.ican.Farzin.Model.Structure.Response.Cartable;

/**
 * Created by AtrasVida on 2020-02-02 at 12:15 PM
 */

public class StructureContinueWorkFlowRES {
    private boolean ContinueFlowResult;
    private String strErrorMSG;

    public boolean isContinueFlowResult() {
        return ContinueFlowResult;
    }

    public void setContinueFlowResult(boolean continueFlowResult) {
        ContinueFlowResult = continueFlowResult;
    }

    public String getStrErrorMSG() {
        return strErrorMSG;
    }

    public void setStrErrorMSG(String strErrorMSG) {
        this.strErrorMSG = strErrorMSG;
    }
}
