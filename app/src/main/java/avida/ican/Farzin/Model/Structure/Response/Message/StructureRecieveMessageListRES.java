package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by AtrasVida on 2018-07-16 at 3:35 AM
 */

@Root(name = "GetRecieveMessageListResponse")
public class StructureRecieveMessageListRES {

    @ElementList(required = false)
    private List<StructureMessageRES> GetRecieveMessageListResult;

    @Element(required = false)
    private String StrErrorMsg;

    public List<StructureMessageRES> getGetRecieveMessageListResult() {
        return GetRecieveMessageListResult;
    }

    public void setGetRecieveMessageListResult(List<StructureMessageRES> getRecieveMessageListResult) {
        GetRecieveMessageListResult = getRecieveMessageListResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }
}
