package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2019-08-03 at 5:21 PM
 */

@Root(name = "GetEntityDependencyResponse")
public class StructureZanjireEntityListRES {

    @Element
    private StructureZanjireEntityRES GetEntityDependencyResult=new StructureZanjireEntityRES();

    @Element(required = false)
    private String strErrorMsg;

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

    public StructureZanjireEntityRES getGetEntityDependencyResult() {
        return GetEntityDependencyResult;
    }

    public void setGetEntityDependencyResult(StructureZanjireEntityRES getEntityDependencyResult) {
        GetEntityDependencyResult = getEntityDependencyResult;
    }
}
