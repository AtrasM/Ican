package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2019-07-23 at 10:36 PM
 */

public class StructureActivityDocumentAttachFileBND implements Serializable {
    private int ETC;
    private int EC;
    private String Title;


    public StructureActivityDocumentAttachFileBND() {
    }

    public StructureActivityDocumentAttachFileBND(int ETC, int EC, String title) {
        this.ETC = ETC;
        this.EC = EC;
        Title = title;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
