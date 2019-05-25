package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;

/**
 * Created by AtrasVida on 2018-12-26 5:20 PM
 */

public interface CartableDocumentDataListener {
    void newData();

    void noData();

    void onFailed(String message);

}
