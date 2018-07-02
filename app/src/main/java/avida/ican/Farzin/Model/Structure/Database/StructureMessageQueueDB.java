package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.MessageQueueStatus;

/**
 * Created by AtrasVida in 2018-06-19 at 15:25 PM
 */

@DatabaseTable(tableName = "message_queue")
public class StructureMessageQueueDB implements Serializable {
    private static final String Message_ID_FIELD_NAME = "message_id";
    @DatabaseField( canBeNull = false,generatedId = true)
    private int id;
    @DatabaseField()
    private String sender_user_id;
    @DatabaseField()
    private String sender_role_id;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private MessageQueueStatus status;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Message_ID_FIELD_NAME)
    private StructureMessageDB message;

    public StructureMessageQueueDB() {
    }


    public StructureMessageQueueDB(String sender_user_id, String sender_role_id, MessageQueueStatus status, StructureMessageDB message) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.status = status;
        this.message = message;
    }

    public StructureMessageDB getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
}

