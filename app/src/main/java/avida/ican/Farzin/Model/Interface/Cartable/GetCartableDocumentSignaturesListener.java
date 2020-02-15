package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignatureRES;

/**
 * Created by AtrasVida on 2019-05-26 at 4:24 PM
 */

public interface GetCartableDocumentSignaturesListener {
    void onSuccess(ArrayList<StructureSignatureRES> structureSignatureListRES);

    void onFailed(String message);

    void onCancel();

}
