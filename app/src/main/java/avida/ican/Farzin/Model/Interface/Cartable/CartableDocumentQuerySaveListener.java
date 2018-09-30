package avida.ican.Farzin.Model.Interface.Cartable;


import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;

/**
 * Created by AtrasVida on 2018-09-16 at 5:20 PM
 */

public interface CartableDocumentQuerySaveListener {
    void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB);
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
