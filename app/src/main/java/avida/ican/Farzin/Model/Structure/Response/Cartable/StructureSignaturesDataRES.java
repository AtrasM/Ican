package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2019-06-02 at 3:20 PM
 */
public class StructureSignaturesDataRES {

    @ElementList(required = false, entry = "Field", inline = true)
    List<StructureSignatureRES> Field = new ArrayList<>();

    public List<StructureSignatureRES> getField() {
        return Field;
    }

    public void setField(List<StructureSignatureRES> field) {
        Field = field;
    }


}
