package avida.ican.Farzin.Model.Structure.Request.DocumentOpratores;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-10-31 at 11:09 AM
 */

public class StructureOpticalPenREQ {

    private String bfile;
    private String strExtention;
    private String Hameshtitle;
    private boolean hiddenHamesh;

    public StructureOpticalPenREQ() {
    }

    public StructureOpticalPenREQ( String bfile, String strExtention, String hameshtitle, boolean hiddenHamesh) {

        this.bfile = bfile;
        this.strExtention = strExtention;
        Hameshtitle = hameshtitle;
        this.hiddenHamesh = hiddenHamesh;
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



