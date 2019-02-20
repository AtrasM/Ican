package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-09-26 at 11:50 AM
 */

@Root(name = "HameshImage")
public class StructureHameshImageRES {
    @Element(required = false)
    String FileBinary="";
    private StringBuilder FileAsStringBuilder=new StringBuilder();
    @Element(required = false)
    String FileExtension;
    @Element(required = false)
    String FileName;

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
        return new ChangeXml().viewCharDecoder(FileName);
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
}
