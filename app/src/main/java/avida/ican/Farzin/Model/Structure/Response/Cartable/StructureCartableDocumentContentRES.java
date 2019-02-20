package avida.ican.Farzin.Model.Structure.Response.Cartable;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-12-04 at 3:04 PM
 */

public class StructureCartableDocumentContentRES {
    private String GetContentFormAsResult="";
    private StringBuilder GetContentFormAsStringBuilder=new StringBuilder();
    private String strErrorMsg;

    public String getGetContentFormAsResult() {
        return GetContentFormAsResult;
    }

    public void setGetContentFormAsResult(String getContentFormAsResult) {
        GetContentFormAsResult = getContentFormAsResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

    public StringBuilder getGetContentFormAsStringBuilder() {
        return GetContentFormAsStringBuilder;
    }

    public void setGetContentFormAsStringBuilder(StringBuilder getContentFormAsStringBuilder) {
        GetContentFormAsStringBuilder = getContentFormAsStringBuilder;
    }
}
