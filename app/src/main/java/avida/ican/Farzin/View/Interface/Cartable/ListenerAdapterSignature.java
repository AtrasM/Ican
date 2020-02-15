package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;

/**
 * Created by AtrasVida on 2019-05-29 at 12:56 PM
 */

public interface ListenerAdapterSignature {
    void onSelect(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB);
    void unSelect(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB);
    void showLoading();
    void hideLoading();
}
