package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida in 2019-11-30 at 4:19 PM
 */

@DatabaseTable(tableName = "logger")
public class StructureLogDB implements Serializable {
    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;
    @DatabaseField()
    private String date;
    @DatabaseField()
    private String log;

    public StructureLogDB() {
    }

    public StructureLogDB(String date, String log) {
        this.date = date;
        this.log = log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}

