package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;

/**
 * Created by AtrasVida on 2018-10-06 at 11:45 AM
 */

public interface CartableHistoryListListener {
    void onSuccess(ArrayList<StructureGraphRES> structureGraphRES,String xml);

    void onFailed(String message);

    void onCancel();

}
