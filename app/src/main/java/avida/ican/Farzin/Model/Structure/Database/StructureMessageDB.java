package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.MessageStatus;

/**
 * Created by AtrasVida on 2018-06-19 at 1:24 PM
 */
@DatabaseTable(tableName = "StructureMessageRES")
public class StructureMessageDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private int main_id;
    @DatabaseField()
    private int sender_user_id;
    @DatabaseField()
    private int sender_role_id;
    @DatabaseField()
    private String subject;
    @DatabaseField()
    private String content;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<StructureMessageFileDB> message_files;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<StructureReceiverDB> receivers;

    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private MessageStatus status;

    public StructureMessageDB() {
    }

    public StructureMessageDB(int sender_user_id, int sender_role_id, String subject, String content,MessageStatus status) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.subject = subject;
        this.content = content;
        this.status = status;
    }

    public int getMain_id() {
        return main_id;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public int getSender_role_id() {
        return sender_role_id;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public ForeignCollection<StructureMessageFileDB> getMessage_files() {
        return message_files;
    }

    public ForeignCollection<StructureReceiverDB> getReceivers() {
        return receivers;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }
}
