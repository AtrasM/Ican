package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-11-27 at 11:26 PM
 */

public interface SendListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
