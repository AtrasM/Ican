package avida.ican.Farzin.Model.Structure.Bundle;

/**
 * Created by AtrasVida on 2018-10-07 at 10:14 AM
 */

public class StructureCartableHistoryBND {
    String xml;
    int ETC;
    int EC;

    public StructureCartableHistoryBND(String xml, int ETC, int EC) {
        this.xml = xml;
        this.ETC = ETC;
        this.EC = EC;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
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
