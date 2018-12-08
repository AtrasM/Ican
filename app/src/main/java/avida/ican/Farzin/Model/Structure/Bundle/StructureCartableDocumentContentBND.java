package avida.ican.Farzin.Model.Structure.Bundle;


/**
 * Created by AtrasVida on 2018-12-05 at 12:06 PM
 */

public class StructureCartableDocumentContentBND {
    private String FileBinary;
    private int ETC;
    private int EC;

    public StructureCartableDocumentContentBND() {
    }

    public StructureCartableDocumentContentBND(String fileBinary, int ETC, int EC) {
        FileBinary = fileBinary;
        this.ETC = ETC;
        this.EC = EC;
    }

    public String getFileBinary() {
        return FileBinary;
    }

    public void setFileBinary(String fileBinary) {
        FileBinary = fileBinary;
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
