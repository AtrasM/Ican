package avida.ican.Farzin.Model.Structure.Response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by AtrasVida on 2018-07-03 at 9:36 AM
 * Edit by AtrasVida on 2018-07-16 at 3:25 AM
 */

@Root(name = "GetSentMessageListResponse")
public class StructureSentMessageListRES {

    @ElementList(required = false)
    private List<StructureMessageRES> GetSentMessageListResult;

    @Element(required = false)
    private String StrErrorMsg;

    public List<StructureMessageRES> getGetSentMessageListResult() {
        return GetSentMessageListResult;
    }

    public void setGetSentMessageListResult(List<StructureMessageRES> getSentMessageListResult) {
        GetSentMessageListResult = getSentMessageListResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }
}
