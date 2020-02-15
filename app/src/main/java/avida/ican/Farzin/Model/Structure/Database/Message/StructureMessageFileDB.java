package avida.ican.Farzin.Model.Structure.Database.Message;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida in 2018-06-19 at 3:25 PM
 */

@DatabaseTable(tableName = "message_file")
public class StructureMessageFileDB implements Serializable {
    private static final String Message_ID_FIELD_NAME = "message_id";
    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Message_ID_FIELD_NAME)
    private StructureMessageDB message;
    @DatabaseField()
    private String file_name;
    @DatabaseField()
    private String file_path;
    @DatabaseField()
    private String file_extension;


    public StructureMessageFileDB() {
    }

    public StructureMessageFileDB(StructureMessageDB message, String file_name, String file_path, String file_extension) {
        this.message = message;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_extension = file_extension;
    }

    public StructureMessageDB getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getFile_extension() {
        return file_extension;
    }
}

