package avida.ican.Farzin.Model.Interface.Chat;


/**
 * Created by AtrasVida on 2020-04-11 at 12:57 PM
 */

public interface ChatProcessListener {
    void onSuccess();
    void onFailed(String error);
    void onCancel();
    void onFinish();
}
