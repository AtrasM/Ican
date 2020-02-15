package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * Created by AtrasVida on 2019-06-02 at 3:19 PM
 */
@Root(name = "GetSignatureListResult")
public class StructureSignatureListResultRES {
    @Element(required = false)
    StructureSignaturesDataRES Data = new StructureSignaturesDataRES();

    public StructureSignaturesDataRES getData() {
        return Data;
    }

    public void setData(StructureSignaturesDataRES data) {
        Data = data;
    }

}
