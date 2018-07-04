package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2018-006-09 at 15:03 PM
 */

public interface MessageListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

}
