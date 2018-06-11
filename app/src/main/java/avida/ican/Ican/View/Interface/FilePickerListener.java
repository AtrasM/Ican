package avida.ican.Ican.View.Interface;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AtrasVida on 2018-04-09 at 2:40 PM
 */

public interface FilePickerListener {
    void onSuccess(ArrayList<File> files,ArrayList<String> base64s,ArrayList<String> fileNames);
    void onFailed();
    void onCancel();
}
