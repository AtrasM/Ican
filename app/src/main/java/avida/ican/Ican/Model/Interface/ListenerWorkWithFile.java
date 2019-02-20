package avida.ican.Ican.Model.Interface;


/**
 * Created by AtrasVida on 2019-02-05 at 1:05 PM
 */

public interface ListenerWorkWithFile {
    void onSuccess(String filePath);

    void onFailed(String error);
}
