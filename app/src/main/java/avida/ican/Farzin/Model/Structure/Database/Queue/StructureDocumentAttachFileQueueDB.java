package avida.ican.Farzin.Model.Structure.Database.Queue;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.DependencyTypeEnum;
import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;

/**
 * Created by AtrasVida on 2019-07-24 at 10:54 AM
 */
@DatabaseTable(tableName = "document-attach_file_queue")
public class StructureDocumentAttachFileQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private boolean isLock;
    @DatabaseField()
    private String file_name;
    @DatabaseField()
    private String file_path;
    @DatabaseField()
    private String file_extension;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private DependencyTypeEnum DependencyType;
    @DatabaseField()
    private String Description;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private QueueStatus queueStatus;
    @DatabaseField()
    private String strError;


    public StructureDocumentAttachFileQueueDB() {
    }

    public StructureDocumentAttachFileQueueDB(int ETC, int EC,boolean isLock, String file_name, String file_path, String file_extension, DependencyTypeEnum dependencyType, String description) {
        this.ETC = ETC;
        this.EC = EC;
        this.isLock = isLock;
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_extension = file_extension;
        DependencyType = dependencyType;
        Description = description;
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

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public DependencyTypeEnum getDependencyType() {
        return DependencyType;
    }

    public void setDependencyType(DependencyTypeEnum dependencyType) {
        DependencyType = dependencyType;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
