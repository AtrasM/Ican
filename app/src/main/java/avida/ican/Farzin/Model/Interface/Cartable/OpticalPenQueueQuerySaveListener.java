package avida.ican.Farzin.Model.Interface.Cartable;

/**
 * Created by AtrasVida on 2018-10-31 at 4:47 PM
 */

public interface OpticalPenQueueQuerySaveListener {
    void onSuccess();

    void onExisting();

    void onFailed(String message);

    void onCancel();

}
