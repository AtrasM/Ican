package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-10-14 at 10:33 AM
 */

@Root(name = "GetFileDependencyResponse")
public class StructureZanjireMadrakListRES {

    @Element
    private StructureZanjireMadrakRES GetFileDependencyResult=new StructureZanjireMadrakRES();

    @Element(required = false)
    private String strErrorMsg;


    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

    public StructureZanjireMadrakRES getGetFileDependencyResult() {
        return GetFileDependencyResult;
    }

    public void setGetFileDependencyResult(StructureZanjireMadrakRES getFileDependencyResult) {
        GetFileDependencyResult = getFileDependencyResult;
    }
}
