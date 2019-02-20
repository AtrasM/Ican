package avida.ican.Ican.Model.Structure;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-06-02 at 11:35 AM
 */

public class StructureAttach {
    private String filePath;
    private StringBuilder fileAsStringBuilder;
    private String Name;
    private String FileExtension;
    private String Description;
    private int icon;

    public String getFilePath() {
        return new ChangeXml().viewCharDecoder(filePath);
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return new ChangeXml().viewCharDecoder(Name);
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public StructureAttach(String name, String fileExtension, int icon) {
        this.Name = name;
        this.FileExtension = fileExtension;
        this.icon = icon;
    }

    public StructureAttach(String filePath, String name, String fileExtension) {
        this.filePath = filePath;
        this.Name = name;
        this.FileExtension = fileExtension;
    }

    public StructureAttach(StringBuilder fileAsStringBuilder, String name, String fileExtension, int icon) {
        this.fileAsStringBuilder = fileAsStringBuilder;
        this.Name = name;
        this.FileExtension = fileExtension;
        this.icon = icon;
    }

    public StructureAttach(String name, String fileExtension) {
        this.Name = name;
        this.FileExtension = fileExtension;
    }

    public StructureAttach(StringBuilder fileAsStringBuilder, String name, String fileExtension) {
        this.fileAsStringBuilder = fileAsStringBuilder;
        this.Name = name;
        this.FileExtension = fileExtension;
    }


    public StructureAttach(StringBuilder fileAsStringBuilder, String name, String fileExtension, String description) {
        this.fileAsStringBuilder = fileAsStringBuilder;
        this.Name = name;
        this.FileExtension = fileExtension;
        this.Description = description;
    }

    public StringBuilder getFileAsStringBuilder() {
        return fileAsStringBuilder;
    }

    public void setFileAsStringBuilder(StringBuilder fileAsStringBuilder) {
        this.fileAsStringBuilder = fileAsStringBuilder;
    }

    public String getDescription() {
        return new ChangeXml().viewCharDecoder(Description);
    }

    public void setDescription(String description) {
        Description = description;
    }
}
