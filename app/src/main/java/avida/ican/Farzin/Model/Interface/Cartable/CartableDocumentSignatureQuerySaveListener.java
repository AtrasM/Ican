package avida.ican.Farzin.Model.Interface.Cartable;


import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;

/**
 * Created by AtrasVida on 2019-05-29 at 10:37 AM
 */

public interface CartableDocumentSignatureQuerySaveListener {
    void onSuccess(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB);
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
