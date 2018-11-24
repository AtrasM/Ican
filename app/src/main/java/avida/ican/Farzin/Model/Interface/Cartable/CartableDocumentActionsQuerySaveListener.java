package avida.ican.Farzin.Model.Interface.Cartable;


import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;

/**
 * Created by AtrasVida on 2018-11-20 at 12:00 PM
 */

public interface CartableDocumentActionsQuerySaveListener {
    void onSuccess(StructureCartableDocumentActionsDB structureCartableDocumentActionsDB);
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
