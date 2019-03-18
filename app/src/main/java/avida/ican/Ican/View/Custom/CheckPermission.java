package avida.ican.Ican.View.Custom;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * Created by AtrasVida on 2018-04-10 at 12:35 PM
 */

public class CheckPermission {
    /**
     * Example: CheckPermission.requestPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
     */

    public boolean recordAudio(int requestCode, Activity activity) {
        return requestPermission(requestCode, activity, Manifest.permission.RECORD_AUDIO);
    }

    public boolean writeExternalStorage(int requestCode, Activity activity) {
        return requestPermission(requestCode, activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private static boolean requestPermission(int requestCode, Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

}
