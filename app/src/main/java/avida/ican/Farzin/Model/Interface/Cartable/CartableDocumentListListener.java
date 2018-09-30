package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;

/**
 * Created by AtrasVida on 2018-09-16 at 11:23 AM
 */

public interface CartableDocumentListListener {
    void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList);

    void onFailed(String message);

    void onCancel();

}
