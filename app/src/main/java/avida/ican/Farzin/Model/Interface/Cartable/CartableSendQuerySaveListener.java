package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-11-28 at 12:01 AM
 */

public interface CartableSendQuerySaveListener {
    void onSuccess();
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
