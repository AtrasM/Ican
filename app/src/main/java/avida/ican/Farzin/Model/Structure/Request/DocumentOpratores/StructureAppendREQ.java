package avida.ican.Farzin.Model.Structure.Request.DocumentOpratores;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Request.StructurePersonREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureSenderREQ;

/**
 * Created by AtrasVida on 2018-11-18 at 3:05 PM
 */

public class StructureAppendREQ  {
    private StructureSenderREQ structureSenderREQ = new StructureSenderREQ();
    private  ArrayList<StructurePersonREQ> structurePersonsREQ = new ArrayList<>();

    public StructureAppendREQ() {
    }

    public StructureAppendREQ(StructureSenderREQ structureSenderREQ, ArrayList<StructurePersonREQ> structurePersonsREQ) {

        this.structureSenderREQ = structureSenderREQ;
        this.structurePersonsREQ = structurePersonsREQ;
    }


    public StructureSenderREQ getStructureSenderREQ() {
        return structureSenderREQ;
    }

    public void setStructureSenderREQ(StructureSenderREQ structureSenderREQ) {
        this.structureSenderREQ = structureSenderREQ;
    }

    public ArrayList<StructurePersonREQ> getStructurePersonsREQ() {
        return structurePersonsREQ;
    }

    public void setStructurePersonsREQ(ArrayList<StructurePersonREQ> structurePersonsREQ) {
        this.structurePersonsREQ = structurePersonsREQ;
    }
}
