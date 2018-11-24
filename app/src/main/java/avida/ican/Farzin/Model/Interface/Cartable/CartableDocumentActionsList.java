package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;

/**
 * Created by AtrasVida on 2018-11-19 at 4:42 PM
 */

public interface CartableDocumentActionsList {
    void onSuccess(ArrayList<StructureCartableDocumentActionRES> cartableDocumentActionsRES);

    void onFailed(String message);

    void onCancel();

}
