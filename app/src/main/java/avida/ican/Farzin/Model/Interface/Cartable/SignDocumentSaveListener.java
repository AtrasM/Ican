package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2019-06-08 at 12:00 PM
 */

public interface SignDocumentSaveListener {
    void onSuccess();

    void onExisting();

    void onFailed(String message);

    void onCancel();
}
