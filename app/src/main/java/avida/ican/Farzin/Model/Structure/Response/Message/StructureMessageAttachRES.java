package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida in 2018-07-08 at 15:15 PM
 */

@Root(name = "MessageFile")
public class StructureMessageAttachRES {
    @Element
    private String FileName;
    @Element
    private String FileBinary;
    @Element(required = false)
    private String FileExtension;
    @Element(required = false)
    private String Description;

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

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}



