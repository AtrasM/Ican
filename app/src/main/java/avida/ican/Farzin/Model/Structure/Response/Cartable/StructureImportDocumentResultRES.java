package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


/**
 * Created by AtrasVida on 2019-07-30 at 11:33 AM
 */
public class StructureImportDocumentResultRES {
    @Element(required = false)
    StructureImportDocIndicatorRES Indicator = new StructureImportDocIndicatorRES();

    public StructureImportDocIndicatorRES getIndicator() {
        return Indicator;
    }

    public void setIndicator(StructureImportDocIndicatorRES indicator) {
        Indicator = indicator;
    }
}
