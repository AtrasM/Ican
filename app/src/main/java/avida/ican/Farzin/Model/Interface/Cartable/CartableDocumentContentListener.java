package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;

/**
 * Created by AtrasVida on 2018-12-04 at 3:34 PM
 */

public interface CartableDocumentContentListener {
    void onSuccess(String FileBinary);

    void onFailed(String message);

    void onCancel();

}
