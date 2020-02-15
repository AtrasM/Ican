package avida.ican.Farzin.Model.Structure.Response;


/**
 * Created by AtrasVida on 2019-07-02 at 12:25 PM
 */
public class StructureChangeActiveRoleRES {
    private boolean ChangeActiveRoleResult;
    private String strErrorMsg;

    public boolean isChangeActiveRoleResult() {
        return ChangeActiveRoleResult;
    }

    public void setChangeActiveRoleResult(boolean changeActiveRoleResult) {
        ChangeActiveRoleResult = changeActiveRoleResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
