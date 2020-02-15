package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2019-06-03 at 12:47 PM
 */

public interface SignDocumentListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();
}
