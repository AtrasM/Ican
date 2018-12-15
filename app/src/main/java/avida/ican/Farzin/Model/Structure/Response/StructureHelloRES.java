package avida.ican.Farzin.Model.Structure.Response;

/**
 * Created by AtrasVida on 2018-12-12 at 3:57 PM
 */

public class StructureHelloRES {
    private boolean HelloResult;
    private String strErrorMsg;

    public boolean isHelloResult() {
        return HelloResult;
    }

    public void setHelloResult(boolean helloResult) {
        HelloResult = helloResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
