package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2018-10-17 at 12:18 PM
 */

@Root(name = "anyType")
public class StructureFileRES {
    @Element(required = false)
    private String FileBinary;
    @Element
    private String FileExtension;
    @Element
    private String FileName;

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

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
