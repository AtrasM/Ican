package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2019-05-26 at 1:48 PM
 */
@Root(name = "GetSignatureListResponse")
public class StructureSignaturesRES {
    @Element()
    StructureSignatureListResultRES GetSignatureListResult=new StructureSignatureListResultRES();
    @Element(required = false)
    private String strErrorMsg;

    public StructureSignatureListResultRES getGetSignatureListResult() {
        return GetSignatureListResult;
    }

    public void setGetSignatureListResult(StructureSignatureListResultRES getSignatureListResult) {
        GetSignatureListResult = getSignatureListResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }


}
