package avida.ican.Farzin.Model.Structure.Database.Queue;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;

/**
 * Created by AtrasVida on 2019-07-06 at 3:55 PM
 */
@DatabaseTable(tableName = "document_operator_queue")
public class StructureDocumentOperatorsQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private boolean isLock;
    @DatabaseField(dataType =  DataType.ENUM_INTEGER)
    private DocumentOperatoresTypeEnum documentOpratoresTypeEnum;
    @DatabaseField()
    private int priority;
    @DatabaseField(dataType =  DataType.ENUM_INTEGER)
    private QueueStatus queueStatus;
    @DatabaseField()
    private String strError;
    @DatabaseField()
    private String strDataREQ;

    int count=0;
    public StructureDocumentOperatorsQueueDB() {
    }

    public StructureDocumentOperatorsQueueDB(int ETC, int EC,boolean isLock, DocumentOperatoresTypeEnum documentOpratoresTypeEnum, int priority, QueueStatus queueStatus, String strError, String strDataREQ) {
        this.ETC = ETC;
        this.EC = EC;
        this.isLock = isLock;
        this.documentOpratoresTypeEnum = documentOpratoresTypeEnum;
        this.priority = priority;
        this.queueStatus = queueStatus;
        this.strError = strError;
        this.strDataREQ = strDataREQ;
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

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public DocumentOperatoresTypeEnum getDocumentOpratoresTypeEnum() {
        return documentOpratoresTypeEnum;
    }

    public void setDocumentOpratoresTypeEnum(DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        this.documentOpratoresTypeEnum = documentOpratoresTypeEnum;
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

    public String getStrDataREQ() {
        return strDataREQ;
    }

    public void setStrDataREQ(String strDataREQ) {
        this.strDataREQ = strDataREQ;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
