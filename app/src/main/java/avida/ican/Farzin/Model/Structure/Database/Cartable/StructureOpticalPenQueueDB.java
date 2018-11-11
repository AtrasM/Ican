package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2018-10-31 at 4:49 PM
 */
@DatabaseTable(tableName = "QueueHameshOpticalPen")
public class StructureOpticalPenQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private String bfile;
    @DatabaseField()
    private String strExtention;
    @DatabaseField()
    private String Hameshtitle;
    @DatabaseField()
    private boolean hiddenHamesh;

    public StructureOpticalPenQueueDB() {
    }

    public StructureOpticalPenQueueDB(int ETC, int EC, String bfile, String strExtention, String hameshtitle, boolean hiddenHamesh) {
        this.ETC = ETC;
        this.EC = EC;
        this.bfile = bfile;
        this.strExtention = strExtention;
        Hameshtitle = hameshtitle;
        this.hiddenHamesh = hiddenHamesh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFIELD_NAME_ID() {
        return FIELD_NAME_ID;
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
