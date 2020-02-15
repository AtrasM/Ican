package avida.ican.Farzin.Model.Structure.Bundle;


import avida.ican.Farzin.Model.Enum.DocumentContentFileTypeEnum;

/**
 * Created by AtrasVida on 2018-12-05 at 12:06 PM
 */

public class StructureCartableDocumentContentBND {
    private StringBuilder FileAsStringBuilder = new StringBuilder();
    private String FileExtension;
    private DocumentContentFileTypeEnum  fileTypeEnum;
    private int ETC;
    private int EC;

    public StructureCartableDocumentContentBND() {
    }


    public StructureCartableDocumentContentBND(StringBuilder fileAsStringBuilder, String fileExtension, DocumentContentFileTypeEnum fileTypeEnum, int ETC, int EC) {
        FileAsStringBuilder = fileAsStringBuilder;
        FileExtension = fileExtension;
        this.fileTypeEnum = fileTypeEnum;
        this.ETC = ETC;
        this.EC = EC;
    }

    public StringBuilder getFileAsStringBuilder() {
        return FileAsStringBuilder;
    }

    public void setFileAsStringBuilder(StringBuilder fileAsStringBuilder) {
        FileAsStringBuilder = fileAsStringBuilder;
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

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public DocumentContentFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(DocumentContentFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }
}
