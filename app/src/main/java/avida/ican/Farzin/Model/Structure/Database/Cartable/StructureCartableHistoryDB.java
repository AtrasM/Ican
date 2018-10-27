package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;

/**
 * Created by AtrasVida on 2018-10-06 at 3:40 PM
 */
@DatabaseTable(tableName = "cartable_history")
public class StructureCartableHistoryDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    String DataXml;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    int EC;

    public StructureCartableHistoryDB() {
    }

    public StructureCartableHistoryDB(String dataXml, int ETC, int EC) {
        DataXml = dataXml;
        this.ETC = ETC;
        this.EC = EC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDataXml() {
        return DataXml;
    }

    public void setDataXml(String dataXml) {
        DataXml = dataXml;
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
