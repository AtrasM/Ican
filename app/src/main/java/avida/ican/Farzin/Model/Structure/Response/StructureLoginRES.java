package avida.ican.Farzin.Model.Structure.Response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2018-03-17 at 3:33 PM
 * Update by AtrasVida on 2019-07-01 at 2:54 PM
 */
@Root(name = "Login3Response")
public class StructureLoginRES {
    @Element(required = false)
    private boolean Login3Result;
    @Element(required = false)
    private StructureLoginDataRES Data = new StructureLoginDataRES();
    @Element(required = false)
    private String strErrorMsg;


    public boolean isLogin3Result() {
        return Login3Result;
    }

    public void setLogin3Result(boolean login3Result) {
        Login3Result = login3Result;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

    public StructureLoginDataRES getData() {
        return Data;
    }

    public void setData(StructureLoginDataRES data) {
        Data = data;
    }


}
