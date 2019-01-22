package avida.ican.Ican.View.Interface;

/**
 * Created by AtrasVida on 2019-01-16 at 1:36 PM
 */

public interface ListenerStorageFile {
    void onSuccess(String filePath);

    void onCancel(String error);
}
