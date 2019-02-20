package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-07-16 at 3:35 AM
 */

@Root(name = "GetRecieveMessageListResponse")
public class StructureRecieveMessageListRES {

    @ElementList(required = false)
    private List<StructureMessageRES> GetRecieveMessageListResult=new ArrayList<>();

    @Element(required = false)
    private String strErrorMsg;

    public List<StructureMessageRES> getGetRecieveMessageListResult() {
        return GetRecieveMessageListResult;
    }

    public void setGetRecieveMessageListResult(List<StructureMessageRES> getRecieveMessageListResult) {
        GetRecieveMessageListResult = getRecieveMessageListResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
