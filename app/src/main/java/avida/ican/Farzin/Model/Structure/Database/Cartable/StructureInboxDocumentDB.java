package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;

/**
 * Created by AtrasVida on 2018-09-16 at 4:40 PM
 */
@DatabaseTable(tableName = "cartable_inbox_document")
public class StructureInboxDocumentDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int ReceiverCode;
    @DatabaseField()
    private int EntityTypeCode;
    @DatabaseField()
    private String PriorityEntity_Name;
    @DatabaseField()
    private  int PrioritySend_ID;
    @DatabaseField()
    private int EntityCode;
    @DatabaseField()
    private int SendCode;
    @DatabaseField()
    private String Title;
    @DatabaseField()
    private  boolean HaveDependency;
    @DatabaseField()
    private int SecurityLevelCode;
    @DatabaseField()
    private String SecurityLevelName;
    @DatabaseField()
    private String EntityTypeName;
    @DatabaseField()
    private  int ActionCode;
    @DatabaseField()
    private  String ActionName;
    @DatabaseField()
    private  String SenderName;
    @DatabaseField()
    private String SenderFirstName;
    @DatabaseField()
    private  String SenderLastName;
    @DatabaseField()
    private String SenderRoleName;
    @DatabaseField()
    private String EntityNumber;
    @DatabaseField()
    private Date ImportDate;
    @DatabaseField()
    private  Date ExportDate;
    @DatabaseField()
    private Date ReceiveDate;
    @DatabaseField()
    private Date ExpireDate;
    @DatabaseField()
    private String UserDescription;
    @DatabaseField()
    private String PrivateHameshContent;
    @DatabaseField()
    private  String PrivateHameshTitle;
    @DatabaseField()
    private  boolean Pin;
    @DatabaseField()
    private boolean IsRead;
    @DatabaseField()
    private Date LastChangeViewStatesDate;
    @DatabaseField()
    private  boolean isTaeed;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private Status status;

    //temp filed for control adapter item view
    private boolean isLnMoreVisible;

    public StructureInboxDocumentDB() {
    }


    public StructureInboxDocumentDB(StructureInboxDocumentRES structureInboxDocumentRES, Date importDate, Date exportDate, Date receiveDate, Date expireDate, Date LastChangeViewStatesDate, Status status, boolean isPin) {
        HaveDependency = structureInboxDocumentRES.isHaveDependency();
        SecurityLevelCode = structureInboxDocumentRES.getSecurityLevelCode();
        SecurityLevelName = structureInboxDocumentRES.getSecurityLevelName();
        EntityTypeName = structureInboxDocumentRES.getEntityTypeName();
        ActionCode = structureInboxDocumentRES.getActionCode();
        ActionName = structureInboxDocumentRES.getActionName();
        SenderName = structureInboxDocumentRES.getSenderName();
        SenderFirstName = structureInboxDocumentRES.getSenderFirstName();
        SenderLastName = structureInboxDocumentRES.getSenderLastName();
        SenderRoleName = structureInboxDocumentRES.getSenderRoleName();
        EntityNumber = structureInboxDocumentRES.getEntityNumber();
        ReceiverCode = structureInboxDocumentRES.getReceiverCode();
        SendCode = structureInboxDocumentRES.getSendCode();
        EntityTypeCode = structureInboxDocumentRES.getEntityTypeCode();
        EntityCode = structureInboxDocumentRES.getEntityCode();
        Title = structureInboxDocumentRES.getTitle();
        PriorityEntity_Name = structureInboxDocumentRES.getPriorityEntity_Name();
        PrioritySend_ID = structureInboxDocumentRES.getPrioritySend_ID();
        IsRead = structureInboxDocumentRES.isRead();
        if (structureInboxDocumentRES.getPrivateHameshContent() != null) {
            this.PrivateHameshContent = structureInboxDocumentRES.getPrivateHameshContent();
        } else {
            this.PrivateHameshContent = "";
        }
        if (structureInboxDocumentRES.getPrivateHameshTitle() != null) {
            this.PrivateHameshTitle = structureInboxDocumentRES.getPrivateHameshTitle();
        } else {
            this.PrivateHameshTitle = "";
        }
        ImportDate = importDate;
        ExportDate = exportDate;
        ReceiveDate = receiveDate;
        ExpireDate = expireDate;
        this.LastChangeViewStatesDate = LastChangeViewStatesDate;
        UserDescription = structureInboxDocumentRES.getUserDescription();
        this.status = status;
        this.Pin = isPin;
        isTaeed = false;
    }

    public String getFIELD_NAME_ID() {
        return FIELD_NAME_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isHaveDependency() {
        return HaveDependency;
    }

    public void setHaveDependency(boolean haveDependency) {
        HaveDependency = haveDependency;
    }

    public int getSecurityLevelCode() {
        return SecurityLevelCode;
    }

    public void setSecurityLevelCode(int securityLevelCode) {
        SecurityLevelCode = securityLevelCode;
    }

    public String getSecurityLevelName() {
        return SecurityLevelName;
    }

    public void setSecurityLevelName(String securityLevelName) {
        SecurityLevelName = securityLevelName;
    }

    public String getEntityTypeName() {
        return EntityTypeName;
    }

    public void setEntityTypeName(String entityTypeName) {
        EntityTypeName = entityTypeName;
    }

    public int getActionCode() {
        return ActionCode;
    }

    public void setActionCode(int actionCode) {
        ActionCode = actionCode;
    }

    public String getActionName() {
        return ActionName;
    }

    public void setActionName(String actionName) {
        ActionName = actionName;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderRoleName() {
        return SenderRoleName;
    }

    public void setSenderRoleName(String senderRoleName) {
        SenderRoleName = senderRoleName;
    }

    public String getEntityNumber() {
        return EntityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        EntityNumber = entityNumber;
    }

    public Date getImportDate() {
        return ImportDate;
    }

    public void setImportDate(Date importDate) {
        ImportDate = importDate;
    }

    public Date getExportDate() {
        return ExportDate;
    }

    public void setExportDate(Date exportDate) {
        ExportDate = exportDate;
    }

    public Date getReceiveDate() {
        return ReceiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        ReceiveDate = receiveDate;
    }

    public Date getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(Date expireDate) {
        ExpireDate = expireDate;
    }

    public String getUserDescription() {
        return UserDescription;
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getReceiverCode() {
        return ReceiverCode;
    }

    public void setReceiverCode(int receiverCode) {
        ReceiverCode = receiverCode;
    }

    public int getSendCode() {
        return SendCode;
    }

    public void setSendCode(int sendCode) {
        SendCode = sendCode;
    }

    public boolean isPin() {
        return Pin;
    }

    public void setPin(boolean pin) {
        Pin = pin;
    }

    public String getPrivateHameshContent() {
        return PrivateHameshContent;
    }

    public void setPrivateHameshContent(String privateHameshContent) {
        PrivateHameshContent = privateHameshContent;
    }

    public String getPrivateHameshTitle() {
        return PrivateHameshTitle;
    }

    public void setPrivateHameshTitle(String privateHameshTitle) {
        PrivateHameshTitle = privateHameshTitle;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getEntityTypeCode() {
        return EntityTypeCode;
    }

    public void setEntityTypeCode(int entityTypeCode) {
        EntityTypeCode = entityTypeCode;
    }

    public int getEntityCode() {
        return EntityCode;
    }

    public void setEntityCode(int entityCode) {
        EntityCode = entityCode;
    }

    public String getPriorityEntity_Name() {
        return PriorityEntity_Name;
    }

    public void setPriorityEntity_Name(String priorityEntity_Name) {
        PriorityEntity_Name = priorityEntity_Name;
    }

    public int getPrioritySend_ID() {
        return PrioritySend_ID;
    }

    public void setPrioritySend_ID(int prioritySend_ID) {
        PrioritySend_ID = prioritySend_ID;
    }

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    public Date getLastChangeViewStatesDate() {
        return LastChangeViewStatesDate;
    }

    public void setLastChangeViewStatesDate(Date lastChangeViewStatesDate) {
        LastChangeViewStatesDate = lastChangeViewStatesDate;
    }

    public boolean isTaeed() {
        return isTaeed;
    }

    public void setTaeed(boolean taeed) {
        isTaeed = taeed;
    }

    public String getSenderFirstName() {
        return SenderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        SenderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return SenderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        SenderLastName = senderLastName;
    }

    public boolean isLnMoreVisible() {
        return isLnMoreVisible;
    }

    public void setLnMoreVisible(boolean lnMoreVisible) {
        isLnMoreVisible = lnMoreVisible;
    }
}
