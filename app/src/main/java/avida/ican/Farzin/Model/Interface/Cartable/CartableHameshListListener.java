package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;

/**
 * Created by AtrasVida on 2018-09-26 at 2:42 PM
 */

public interface CartableHameshListListener {
    void onSuccess(ArrayList<StructureHameshRES> structureHameshRES);

    void onFailed(String message);

    void onCancel();

}
