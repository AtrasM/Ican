package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by AtrasVida on 2018-10-17 at 12:18 PM
 */

@Root(name = "anyType")
public class StructureFileRES {
    @Element(required = false)
    private String FileBinary;
    private StringBuilder FileAsStringBuilder = new StringBuilder();
    @Element(required = false)
    private String FileExtension;
    @Element(required = false)
    private String FileName;
    @Element(required = false)
    private String CreationFullName;
    @Element(required = false)
    private String CreationRoleName;
    @Element(required = false)
    private String CreationDate;

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

    public StringBuilder getFileAsStringBuilder() {
        return FileAsStringBuilder;
    }

    public void setFileAsStringBuilder(StringBuilder fileAsStringBuilder) {
        FileAsStringBuilder = fileAsStringBuilder;
    }

    public String getCreationFullName() {
        return CreationFullName;
    }

    public void setCreationFullName(String creationFullName) {
        CreationFullName = creationFullName;
    }

    public String getCreationRoleName() {
        return CreationRoleName;
    }

    public void setCreationRoleName(String creationRoleName) {
        CreationRoleName = creationRoleName;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }
}
