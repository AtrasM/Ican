package avida.ican.Farzin.Model.Structure.Database.Queue;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;

/**
 * Created by AtrasVida in 2018-06-19 at 3:25 PM
 */

@DatabaseTable(tableName = "message_queue")
public class StructureMessageQueueDB implements Serializable {
    private static final String Message_ID_FIELD_NAME = "message_id";
    @DatabaseField( canBeNull = false,generatedId = true)
    private int id;
    @DatabaseField()
    private int sender_user_id;
    @DatabaseField()
    private int sender_role_id;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private QueueStatus queueStatus;
    @DatabaseField()
    private String strError;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Message_ID_FIELD_NAME)
    private StructureMessageDB message;

    public StructureMessageQueueDB() {
    }


    public StructureMessageQueueDB(int sender_user_id, int sender_role_id, QueueStatus queueStatus, StructureMessageDB message) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.queueStatus = queueStatus;
        this.message = message;
    }

    public StructureMessageDB getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public QueueStatus getQueueStatus() {
        return queueStatus;
    }

    public void setQueueStatus(QueueStatus queueStatus) {
        this.queueStatus = queueStatus;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }
}

