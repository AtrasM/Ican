package avida.ican.Farzin.Model.Structure.Bundle.Queue;

import com.j256.ormlite.field.DatabaseField;

import avida.ican.Farzin.Model.Enum.DependencyTypeEnum;


/**
 * Created by AtrasVida on 2019-07-24 at 11:42 AM
 */

public class StructureDocumentAttachFileBND {

    private int ETC;
    private int EC;
    @DatabaseField()
    private boolean isLock;
    private String file_name;
    private String file_path;
    private StringBuilder fileAsStringBuilder;
    private String file_extension;
    private DependencyTypeEnum DependencyType;
    private String Description;


    public StructureDocumentAttachFileBND() {
    }



    public StructureDocumentAttachFileBND(int ETC, int EC,boolean isLock, String file_name, StringBuilder fileAsStringBuilder, String file_extension, DependencyTypeEnum dependencyType, String description) {
        this.ETC = ETC;
        this.EC = EC;
        this.isLock = isLock;
        this.file_name = file_name;
        this.fileAsStringBuilder = fileAsStringBuilder;
        this.file_path = "";
        this.file_extension = file_extension;
        DependencyType = dependencyType;
        Description = description;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public StringBuilder getFileAsStringBuilder() {
        return fileAsStringBuilder;
    }

    public void setFileAsStringBuilder(StringBuilder fileAsStringBuilder) {
        this.fileAsStringBuilder = fileAsStringBuilder;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public DependencyTypeEnum getDependencyType() {
        return DependencyType;
    }

    public void setDependencyType(DependencyTypeEnum dependencyType) {
        DependencyType = dependencyType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
