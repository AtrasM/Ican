package avida.ican.Farzin.Model.Structure.Request;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-10-31 at 11:09 AM
 */

public class StructureOpticalPenREQ {

    private int ETC;
    private int EC;
    private String bfile;
    private String strExtention;
    private String Hameshtitle;
    private boolean hiddenHamesh;

    public StructureOpticalPenREQ() {
    }

    public StructureOpticalPenREQ(int ETC, int EC, String bfile, String strExtention, String hameshtitle, boolean hiddenHamesh) {
        this.ETC = ETC;
        this.EC = EC;
        this.bfile = bfile;
        this.strExtention = strExtention;
        Hameshtitle = hameshtitle;
        this.hiddenHamesh = hiddenHamesh;
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

    public String getBfile() {
        return bfile;
    }

    public void setBfile(String bfile) {
        this.bfile = bfile;
    }

    public String getStrExtention() {
        return strExtention;
    }

    public void setStrExtention(String strExtention) {
        this.strExtention = strExtention;
    }

    public String getHameshtitle() {
        return Hameshtitle;
    }

    public void setHameshtitle(String hameshtitle) {
        Hameshtitle = hameshtitle;
    }

    public boolean isHiddenHamesh() {
        return hiddenHamesh;
    }

    public void setHiddenHamesh(boolean hiddenHamesh) {
        this.hiddenHamesh = hiddenHamesh;
    }
}



