package avida.ican.Farzin.Model.Structure.Input;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-06-9 at 16:20 PM
 */

public class StructureMessageFileIP implements KvmSerializable {

    private  String FileName;
    private  String FileBinary;
    private  String FileExtension;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileBinary() {
        return FileBinary;
    }

    public void setFileBinary(String fileBinary) {
        FileBinary = fileBinary;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public StructureMessageFileIP() {
    }

    public StructureMessageFileIP(String fileName, String fileBinary, String fileExtension) {
        FileName = fileName;
        FileBinary = fileBinary;
        FileExtension = fileExtension;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return FileName;
            case 1:
                return FileBinary;

            case 2:
                return FileExtension;

        }
        return null;
    }

    public int getPropertyCount() {
        return 3;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo propertyInfo) {
        switch (index) {
            case 0:
                propertyInfo.name = "FileName";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 1:
                propertyInfo.name = "FileBinary";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            case 2:
                propertyInfo.name = "FileExtension";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;

            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0:
                this.FileName = value.toString();
                break;
            case 1:
                this.FileBinary = value.toString();
                break;
            case 2:
                this.FileExtension = value.toString();
                break;

            default:
                break;
        }
    }
}

