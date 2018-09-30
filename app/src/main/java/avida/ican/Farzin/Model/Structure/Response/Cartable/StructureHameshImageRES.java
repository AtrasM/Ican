package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2018-09-26 at 11:50 AM
 */

@Root(name = "HameshImage")
public class StructureHameshImageRES {
    @Element(required = false)
    String FileBinary;
    @Element(required = false)
    String FileExtens;
    @Element(required = false)
    String FileName;

    public String getFileBinary() {
        return FileBinary;
    }

    public void setFileBinary(String fileBinary) {
        FileBinary = fileBinary;
    }

    public String getFileExtens() {
        return FileExtens;
    }

    public void setFileExtens(String fileExtens) {
        FileExtens = fileExtens;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }
}
