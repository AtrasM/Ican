package avida.ican.Farzin.Model.Interface.Cartable;


import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocIndicatorRES;

/**
 * Created by AtrasVida on 2019-07-30 at 11:20 AM
 */

public interface CreateDocumentListener {
    void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES);

    void onSuccessAddToQueue();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
