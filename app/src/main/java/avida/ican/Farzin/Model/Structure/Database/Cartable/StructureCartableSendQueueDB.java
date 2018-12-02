package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2018-11-27 at 5:21 PM
 */
@DatabaseTable(tableName = "QueueCartableSend")
public class StructureCartableSendQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private String strStructureAppendREQ;

    public StructureCartableSendQueueDB() {
    }

    public StructureCartableSendQueueDB(int ETC, int EC, String strStructureAppendREQ) {
        this.ETC = ETC;
        this.EC = EC;
        this.strStructureAppendREQ = strStructureAppendREQ;
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

    public String getStrStructureAppendREQ() {
        return strStructureAppendREQ;
    }

    public void setStrStructureAppendREQ(String strStructureAppendREQ) {
        this.strStructureAppendREQ = strStructureAppendREQ;
    }
}
