package avida.ican.Farzin.Model.Structure.Request;

/**
 * Created by AtrasVida in 2019-07-23 at 4:28 PM
 */

public class StructureDocumentAttachFileREQ {
    private int ETC;
    private int EC;
    private  String FileBinary;
    private String fileName;
    private String fileExtension;
    private int DependencyType;
    private String Description;


    public StructureDocumentAttachFileREQ() {

    }

    public StructureDocumentAttachFileREQ(int ETC, int EC, String fileBinary, String fileName, String fileExtension, int dependencyType, String description) {
        this.ETC = ETC;
        this.EC = EC;
        FileBinary = fileBinary;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
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

    public String getFileBinary() {
        return FileBinary;
    }

    public void setFileBinary(String fileBinary) {
        FileBinary = fileBinary;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public int getDependencyType() {
        return DependencyType;
    }

    public void setDependencyType(int dependencyType) {
        DependencyType = dependencyType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}



