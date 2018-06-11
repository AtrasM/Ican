package avida.ican.Ican.View.Interface;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AtrasVida on 2018-05-06 at 3:58 PM
 */

public interface MediaPickerListener {
    void onSuccess(ArrayList<File> files, ArrayList<String> base64s,ArrayList<String> fileNames);
    void onFailed();
    void onCancel();
}
