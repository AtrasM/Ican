package avida.ican.Farzin.Model.Interface.Message;


/**
 * Created by AtrasVida on 2018-006-09 at 3:03 PM
 */

public interface SendMessageListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

}
