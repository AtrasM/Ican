package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2018-10-14 at 10:33 AM
 */

@Root(name = "GetFileDependencyResponse")
public class StructureZanjireMadrakListRES {

    @Element
    private StructureZanjireMadrakRES GetFileDependencyResult;

    @Element(required = false)
    private String StrErrorMsg;


    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }

    public StructureZanjireMadrakRES getGetFileDependencyResult() {
        return GetFileDependencyResult;
    }

    public void setGetFileDependencyResult(StructureZanjireMadrakRES getFileDependencyResult) {
        GetFileDependencyResult = getFileDependencyResult;
    }
}
