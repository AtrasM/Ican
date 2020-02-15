package avida.ican.Farzin.Model.Structure.Database.Queue;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.QueueStatus;

/**
 * Created by AtrasVida on 2019-07-30 at 9:59 AM
 */
@DatabaseTable(tableName = "queue_create_document")
public class StructureCreatDocumentQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private String subject;
    @DatabaseField()
    private String senderUserName;
    @DatabaseField()
    private String senderFullName;
    @DatabaseField()
    private String receiverFullName;
    @DatabaseField()
    private String importOriginDate;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private QueueStatus queueStatus;
    @DatabaseField()
    private String strError;


    public StructureCreatDocumentQueueDB() {
    }

    public StructureCreatDocumentQueueDB(int ETC, int EC, String subject, String senderUserName, String senderFullName, String receiverFullName, String importOriginDate) {
        this.ETC = ETC;
        this.EC = EC;
        this.subject = subject;
        this.senderUserName = senderUserName;
        this.senderFullName = senderFullName;
        this.receiverFullName = receiverFullName;
        this.importOriginDate = importOriginDate;
        this.queueStatus = QueueStatus.WAITING;
        this.strError = "";
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getSenderFullName() {
        return senderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    public String getReceiverFullName() {
        return receiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        this.receiverFullName = receiverFullName;
    }

    public String getImportOriginDate() {
        return importOriginDate;
    }

    public void setImportOriginDate(String importOriginDate) {
        this.importOriginDate = importOriginDate;
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

    public String getFIELD_NAME_ID() {
        return FIELD_NAME_ID;
    }

}
