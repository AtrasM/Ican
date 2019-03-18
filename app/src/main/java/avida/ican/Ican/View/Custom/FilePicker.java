package avida.ican.Ican.View.Custom;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;

import avida.ican.Ican.Model.Structure.StructureFileStringTypeList;
import avida.ican.Ican.View.Interface.FilePickerListener;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-09 at 2:38 PM
 */

public class FilePicker extends AppCompatActivity {

    /**
     * ِgradle dependency is: implementation 'com.github.angads25:filepicker:1.1.1'
     */
    private final Activity context;
    private static FilePickerListener filePickerListener;
    private int SelectionMode = DialogConfigs.MULTI_MODE;
    private int SelectionType = DialogConfigs.FILE_SELECT;
    private FilePickerDialog dialog;

    public FilePicker(Activity context) {
        this.context = context;
    }

    public void setOnListener(FilePickerListener filePickerListener) {
        this.filePickerListener = filePickerListener;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Show();
            }
        });

    }

    public FilePicker setSelectionMode(boolean isSelectMultiple) {
        if (isSelectMultiple) {

            this.SelectionMode = DialogConfigs.MULTI_MODE;
        } else {

            this.SelectionMode = DialogConfigs.SINGLE_MODE;
        }
        return this;
    }


    public FilePicker setSelectionType(int FileOrDir) {
        /**
         * example DialogConfigs.FILE_SELECT
         */
        this.SelectionType = FileOrDir;
        return this;
    }

    private void Show() {
        DialogProperties properties = new DialogProperties();
        properties.selection_mode = SelectionMode;
        properties.selection_type = SelectionType;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        dialog = new FilePickerDialog(context, properties, R.style.AppTheme);
        dialog.setTitle("انتخاب فایل");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                StructureFileStringTypeList structureFileStringTypeList= new Base64EncodeDecodeFile().EncodeFilesPathToFileAndBase64(files);
                if (structureFileStringTypeList.getEncodeBase64ArrayList().size() > 0) {
                    filePickerListener.onSuccess(structureFileStringTypeList.getFileArrayList(),structureFileStringTypeList.getEncodeBase64ArrayList(),structureFileStringTypeList.getFileNames());
                } else {
                    filePickerListener.onFailed();
                }
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                filePickerListener.onCancel();
            }
        });

        dialog.show();


    }


    //Add this method to show Dialog when the required permission has been granted to the app.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == 1) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a || grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.READ_EXTERNAL_STORAGE: {
                        dialog.show();
                        break;
                    }

                }
            }
        }
    }

}
