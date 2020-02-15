package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2019-06-26 at 4:33 PM
 */
@Root(name = "ConfirmationItem")
public class StructureConfirmationItemRES {
    @Element(required = false)
    int ReceiverCode;
    @Element(required = false)
    String ResponseDate;

    public int getReceiverCode() {
        return ReceiverCode;
    }

    public void setReceiverCode(int receiverCode) {
        ReceiverCode = receiverCode;
    }

    public String getResponseDate() {
        return ResponseDate;
    }

    public void setResponseDate(String responseDate) {
        ResponseDate = responseDate;
    }
}
