package avida.ican.Farzin.Model.Structure.Database.Message;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-06-19 at 1:24 PM
 */
@DatabaseTable(tableName = "message")
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
    @DatabaseField
    private String sender_role_name;
    @DatabaseField
    private String sender_first_name;
    @DatabaseField
    private String sender_last_name;
    @DatabaseField()
    private String subject;
    @DatabaseField()
    private String content;
    @DatabaseField()
    private long sent_date;
    @DatabaseField()
    private boolean isFilesDownloaded;
    @DatabaseField()
    private int AttachmentCount;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<StructureMessageFileDB> message_files;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<StructureReceiverDB> receivers;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Status status;
    @DatabaseField()
    boolean isNew;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Type type;

    public StructureMessageDB() {
    }

    public StructureMessageDB(int main_id, int sender_user_id, int sender_role_id, String sender_role_name, String sender_first_name, String sender_last_name, String subject, String content, Date sent_date, Status status, Type type, int AttachmentCount, boolean isFilesDownloaded) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.sender_role_name = sender_role_name;
        this.sender_first_name = sender_first_name;
        this.sender_last_name = sender_last_name;
        this.subject = subject;
        this.content = content;
        this.sent_date = sent_date.getTime();
        this.status = status;
        this.type = type;
        this.isFilesDownloaded = isFilesDownloaded;
        this.AttachmentCount = AttachmentCount;
        this.main_id = main_id;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getSent_date() {
        return CustomFunction.convertLongTimeToDateStandartFormat(sent_date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isFilesDownloaded() {
        return isFilesDownloaded;
    }

    public void setFilesDownloaded(boolean filesDownloaded) {
        isFilesDownloaded = filesDownloaded;
    }

    public int getAttachmentCount() {
        return AttachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        AttachmentCount = attachmentCount;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getSender_role_name() {
        return sender_role_name;
    }

    public void setSender_role_name(String sender_role_name) {
        this.sender_role_name = sender_role_name;
    }

    public String getSender_first_name() {
        return sender_first_name;
    }

    public void setSender_first_name(String sender_first_name) {
        this.sender_first_name = sender_first_name;
    }

    public String getSender_last_name() {
        return sender_last_name;
    }

    public void setSender_last_name(String sender_last_name) {
        this.sender_last_name = sender_last_name;
    }
}
