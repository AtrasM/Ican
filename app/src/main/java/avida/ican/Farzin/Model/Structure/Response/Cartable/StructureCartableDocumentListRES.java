package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by AtrasVida on 2018-09-12 at 15:44 PM
 */

@Root(name = "GetCartableDocumentResponse")
public class StructureCartableDocumentListRES {

    @ElementList(required = false)
    private List<StructureInboxDocumentRES>  GetCartableDocumentResult;

    @Element(required = false)
    private String StrErrorMsg;

    public List<StructureInboxDocumentRES> getGetCartableDocumentResult() {
        return GetCartableDocumentResult;
    }

    public void setGetCartableDocumentResult(List<StructureInboxDocumentRES> getCartableDocumentResult) {
        GetCartableDocumentResult = getCartableDocumentResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }


}
