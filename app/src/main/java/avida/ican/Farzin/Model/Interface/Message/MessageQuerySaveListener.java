package avida.ican.Farzin.Model.Interface.Message;


import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;

/**
 * Created by AtrasVida on 2018-006-09 at 3:03 PM
 */

public interface MessageQuerySaveListener {
    void onSuccess(StructureMessageDB structureMessageDB);
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
