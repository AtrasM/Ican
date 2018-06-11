package avida.ican.Ican.View.Interface;

/**
 * Created by AtrasVida on 2018-04-10 at 9:31 AM
 */

public interface AudioRecorderListener {
    void onSuccess(String base64,String fileName);
    void onFaild();
    void onCancel();
}
