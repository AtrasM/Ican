package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2018-006-09 at 15:03 PM
 */

public interface MessageQuerySaveListener {
    void onSuccess();
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
