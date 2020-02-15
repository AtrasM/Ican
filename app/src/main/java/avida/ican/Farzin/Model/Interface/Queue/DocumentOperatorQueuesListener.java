package avida.ican.Farzin.Model.Interface.Queue;


/**
 * Created by AtrasVida on 2019-07-06 at 4:06 PM
 */

public interface DocumentOperatorQueuesListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
