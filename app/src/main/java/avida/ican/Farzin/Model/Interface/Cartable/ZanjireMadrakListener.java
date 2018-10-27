package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;


/**
 * Created by AtrasVida on 2018-10-14 at 11:22 AM
 */

public interface ZanjireMadrakListener {
    void onSuccess(StructureZanjireMadrakRES structureZanjireMadrakRES);

    void onFailed(String message);

    void onCancel();

}
