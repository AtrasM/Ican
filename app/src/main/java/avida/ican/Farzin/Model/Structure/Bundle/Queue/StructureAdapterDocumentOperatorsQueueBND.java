package avida.ican.Farzin.Model.Structure.Bundle.Queue;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;

/**
 * Created by AtrasVida on 2019-07-13 at 12:12 PM
 */

public class StructureAdapterDocumentOperatorsQueueBND {

    private ArrayList<StructureDocumentOperatorsQueueDB> documentOperatorsQueueDBS = new ArrayList<>();

    public StructureAdapterDocumentOperatorsQueueBND() {
    }

    public StructureAdapterDocumentOperatorsQueueBND(ArrayList<StructureDocumentOperatorsQueueDB> documentOperatorsQueueDBS) {
        this.documentOperatorsQueueDBS = documentOperatorsQueueDBS;
    }

    public ArrayList<StructureDocumentOperatorsQueueDB> getDocumentOperatorsQueueDBS() {
        return documentOperatorsQueueDBS;
    }

    public void setDocumentOperatorsQueueDBS(ArrayList<StructureDocumentOperatorsQueueDB> documentOperatorsQueueDBS) {
        this.documentOperatorsQueueDBS = documentOperatorsQueueDBS;
    }
}
