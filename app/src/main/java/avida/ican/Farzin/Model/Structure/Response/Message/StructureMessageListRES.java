package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-07-03 at 9:36 AM
 * Edit by AtrasVida on 2018-07-16 at 3:25 AM
 */

@Root(name = "GetMessageListResponse")
public class StructureMessageListRES {

    @ElementList(required = false)
    private List<StructureMessageRES> GetMessageListResult =new ArrayList<>();

    @Element(required = false)
    private String strErrorMsg;

    public List<StructureMessageRES> getGetMessageListResult() {
        return GetMessageListResult;
    }

    public void setGetMessageListResult(List<StructureMessageRES> getMessageListResult) {
        GetMessageListResult = getMessageListResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
