package avida.ican.Farzin.Model.Structure.Request.DocumentOpratores;

import java.util.ArrayList;

/**
 * Created by AtrasVida in 2019-06-008 at 10:49 AM
 */

public class StructureSignDocumentREQ {

    private ArrayList<String> ENs;

    public StructureSignDocumentREQ( ArrayList<String> ENs) {

        this.ENs = ENs;
    }


    public ArrayList<String> getENs() {
        return ENs;
    }

    public void setENs(ArrayList<String> ENs) {
        this.ENs = ENs;
    }
}



