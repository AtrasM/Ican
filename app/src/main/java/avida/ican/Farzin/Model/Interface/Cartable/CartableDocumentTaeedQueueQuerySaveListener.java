package avida.ican.Farzin.Model.Interface.Cartable;

/**
 * Created by AtrasVida on 2018-10-23 at 4:13 PM
 */

public interface CartableDocumentTaeedQueueQuerySaveListener {
    void onSuccess(int receiveCode);

    void onExisting();

    void onFailed(String message);

    void onCancel();

}
