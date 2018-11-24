package avida.ican.Farzin.Model.Structure.Request;

import java.util.ArrayList;

/**
 * Created by AtrasVida on 2018-11-18 at 3:05 PM
 */

public class StructureAppendREQ {
    int ETC;
    int EC;
    StructureSenderREQ structureSenderREQ = new StructureSenderREQ();
    ArrayList<StructurePersonREQ> structurePersonsREQ = new ArrayList<>();

    public StructureAppendREQ() {
    }

    public StructureAppendREQ(int ETC, int EC, StructureSenderREQ structureSenderREQ, ArrayList<StructurePersonREQ> structurePersonsREQ) {
        this.ETC = ETC;
        this.EC = EC;
        this.structureSenderREQ = structureSenderREQ;
        this.structurePersonsREQ = structurePersonsREQ;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
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
