package avida.ican.Farzin.Model.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;

/**
 * Created by AtrasVida on 2018-12-05 at 11:38 PM
 */

public interface CartableDocumentContentQuerySaveListener {
    void onSuccess(StructureCartableDocumentContentDB structureCartableDocumentContentDB );

    void onExisting();

    void onFailed(String message);

    void onCancel();

}
