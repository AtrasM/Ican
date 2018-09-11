package avida.ican.Farzin.Model.Interface;


import avida.ican.Farzin.Model.Structure.Database.StructureMessageDB;

/**
 * Created by AtrasVida on 2018-006-09 at 15:03 PM
 */

public interface MessageQuerySaveListener {
    void onSuccess(StructureMessageDB structureMessageDB);
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
