package avida.ican.Farzin.Model.Structure.Bundle;


/**
 * Created by AtrasVida on 2018-12-05 at 12:06 PM
 */

public class StructureCartableDocumentContentBND {
    private StringBuilder FileAsStringBuilder =new StringBuilder();
    private int ETC;
    private int EC;

    public StructureCartableDocumentContentBND() {
    }

    public StructureCartableDocumentContentBND(StringBuilder fileAsStringBuilder, int ETC, int EC) {
        FileAsStringBuilder = fileAsStringBuilder;
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

}
