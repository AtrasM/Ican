package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-09-26 at 11:27 AM
 */

@Root(name = "GetHameshListResponse" )
public class StructureHameshListRES {

    @ElementList(required = false)
    private List<StructureHameshRES>  GetHameshListResult;

    @Element(required = false)
    private String strErrorMsg;

    public List<StructureHameshRES> getGetHameshListResult() {
        return GetHameshListResult;
    }

    public void setGetHameshListResult(List<StructureHameshRES> getHameshListResult) {
        GetHameshListResult = getHameshListResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }


}
