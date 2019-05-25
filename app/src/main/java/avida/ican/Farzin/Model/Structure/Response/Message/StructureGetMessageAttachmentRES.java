package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida in 2019-05-11 at 17:17 PM
 */

public class StructureGetMessageAttachmentRES {
    @ElementList(required = false)
    private List<StructureMessageAttachRES> GetMessageAttachmentResult = new ArrayList<>();

    @Element(required = false)
    private String strErrorMsg;

    public List<StructureMessageAttachRES> getGetMessageAttachmentResult() {
        return GetMessageAttachmentResult;
    }

    public void setGetMessageAttachmentResult(List<StructureMessageAttachRES> getMessageAttachmentResult) {
        GetMessageAttachmentResult = getMessageAttachmentResult;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}



