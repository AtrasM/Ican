package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2019-07-24 at 11:46 AM
 */

public interface DocumentAttachFileListener {
    void onSuccess();
    void onSuccessAddToQueue();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
