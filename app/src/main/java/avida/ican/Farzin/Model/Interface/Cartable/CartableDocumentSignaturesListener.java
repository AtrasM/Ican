package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2019-05-26 at 5:16 PM
 */

public interface CartableDocumentSignaturesListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

}
