package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by AtrasVida on 2019-06-26 at 4:26 PM
 */
@Root(name = "GetConfirmationListResponse")
public class StructureConfirmationListResultRES {
    @ElementList(required = false)
    private List<StructureConfirmationItemRES> GetConfirmationListResult=new ArrayList<>();

    @Element(required = false)
    private String strErrorMsg;

    public List<StructureConfirmationItemRES> getGetConfirmationListResult() {
        return GetConfirmationListResult;
    }

    public void setGetConfirmationListResult(List<StructureConfirmationItemRES> getConfirmationListResult) {
        GetConfirmationListResult = getConfirmationListResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
