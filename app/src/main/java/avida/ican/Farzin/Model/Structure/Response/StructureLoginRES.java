package avida.ican.Farzin.Model.Structure.Response;

/**
 * Created by AtrasVida on 2018-03-17 at 3:33 PM
 */

public class StructureLoginRES {
    private boolean LoginResult;
    private String strErrorMsg;

    public boolean isLoginResult() {
        return LoginResult;
    }

    public void setLoginResult(boolean loginResult) {
        LoginResult = loginResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
