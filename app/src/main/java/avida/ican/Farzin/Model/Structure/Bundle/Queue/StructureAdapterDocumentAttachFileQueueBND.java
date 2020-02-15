package avida.ican.Farzin.Model.Structure.Bundle.Queue;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;

/**
 * Created by AtrasVida on 2019-07-24 at 4:04 PM
 */

public class StructureAdapterDocumentAttachFileQueueBND {

    private ArrayList<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();

    public StructureAdapterDocumentAttachFileQueueBND() {
    }

    public StructureAdapterDocumentAttachFileQueueBND(ArrayList<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS) {
        this.structureDocumentAttachFileQueueDBS = structureDocumentAttachFileQueueDBS;
    }

    public ArrayList<StructureDocumentAttachFileQueueDB> getStructureDocumentAttachFileQueueDBS() {
        return structureDocumentAttachFileQueueDBS;
    }

    public void setStructureDocumentAttachFileQueueDBS(ArrayList<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS) {
        this.structureDocumentAttachFileQueueDBS = structureDocumentAttachFileQueueDBS;
    }
}
