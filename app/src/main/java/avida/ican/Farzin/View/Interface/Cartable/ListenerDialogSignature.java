package avida.ican.Farzin.View.Interface.Cartable;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;

/**
 * Created by AtrasVida on 2019-05-29 at 2:36 PM
 */

public interface ListenerDialogSignature {
    void onSuccess(List<StructureCartableDocumentSignatureDB> structureCartableDocumentSignaturesDB);

    void onCancel();

}
