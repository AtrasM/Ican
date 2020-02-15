package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2019-07-09 at 3:54 PM
 */

public interface DocumentProcessListener {
    void onSuccess();
    void onFailed(String error);
    void onCancel();
    void onFinish();
}
