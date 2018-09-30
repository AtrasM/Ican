package avida.ican.Farzin.Model.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;

/**
 * Created by AtrasVida on 2018-09-26 at 3:29 PM
 */

public interface HameshQuerySaveListener {
    void onSuccess(StructureHameshDB structureHameshDB);

    void onExisting();

    void onFailed(String message);

    void onCancel();

}
