package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-09-26 at 2:42 PM
 */

public interface OpticalPenListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
