package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2019-07-30 at 12:10 PM
 */
@Root(name = "ImportDocResponse")
public class StructureImportDocumentRES {
    @Element()
    boolean ImportDocResult;
    @Element(required = false)
    StructureImportDocumentResultRES result = new StructureImportDocumentResultRES();
    @Element(required = false)
    private String strErrorMsg;

    public boolean isImportDocResult() {
        return ImportDocResult;
    }

    public void setImportDocResult(boolean importDocResult) {
        ImportDocResult = importDocResult;
    }

    public StructureImportDocumentResultRES getResult() {
        return result;
    }

    public void setResult(StructureImportDocumentResultRES result) {
        this.result = result;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }

}
