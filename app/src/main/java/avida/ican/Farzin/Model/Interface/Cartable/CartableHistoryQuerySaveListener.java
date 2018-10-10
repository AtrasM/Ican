package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-10-07 at 10:10 AM
 */

public interface CartableHistoryQuerySaveListener {
    void onSuccess();
    void onExisting();
    void onFailed(String message);
    void onCancel();

}
