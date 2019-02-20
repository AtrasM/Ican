package avida.ican.Farzin.Model.Structure.Response.Cartable;

import com.j256.ormlite.field.DatabaseField;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-09-12 at 3:53 PM
 */

@Root(name = "InboxDocument")
public class StructureInboxDocumentRES {
    @Element
    int ReceiverCode;
    @Element
    int SendCode;
    @Element
    int EntityTypeCode;
    @Element
    int EntityCode;
    @Element(required = false)
    boolean HaveDependency;
    @Element(required = false)
    int SecurityLevelCode;
    @Element(required = false)
    String SecurityLevelName;
    @Element(required = false)
    String EntityTypeName;
    @Element(required = false)
    int ActionCode;
    @Element(required = false)
    String ActionName;
    @Element(required = false)
    String SenderName;
    @Element(required = false)
    String SenderFirstName;
    @Element(required = false)
    String SenderLastName;
    @Element(required = false)
    String SenderRoleName;
    @Element(required = false)
    String EntityNumber;
    @Element(required = false)
    String ImportDate;
    @Element(required = false)
    String ExportDate;
    @Element(required = false)
    String ReceiveDate;
    @Element(required = false)
    String ExpireDate;
    @Element(required = false)
    String UserDescription;
    @Element(required = false)
    String Title;
    @Element(required = false)
    String PrivateHameshContent;
    @Element(required = false)
    String PrivateHameshTitle;
    @Element(required = false)
    String PriorityEntity_Name;
    @Element(required = false)
    int PrioritySend_ID;
    @Element(required = false)
    boolean IsRead;
    @Element(required = false)
    String LastChangeViewStatesDate;

    public StructureInboxDocumentRES() {
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
        return new ChangeXml().viewCharDecoder(SecurityLevelName);
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
        return new ChangeXml().viewCharDecoder(ActionName);
    }

    public void setActionName(String actionName) {
        ActionName = actionName;
    }

    public String getSenderName() {
        return new ChangeXml().viewCharDecoder(SenderName);
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderRoleName() {
        return new ChangeXml().viewCharDecoder(SenderRoleName);
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

    public String getImportDate() {
        return new ChangeXml().viewCharDecoder(ImportDate);
    }

    public void setImportDate(String importDate) {
        ImportDate = importDate;
    }

    public String getExportDate() {
        return new ChangeXml().viewCharDecoder(ExportDate);
    }

    public void setExportDate(String exportDate) {
        ExportDate = exportDate;
    }

    public String getReceiveDate() {
        return new ChangeXml().viewCharDecoder(ReceiveDate);
    }

    public void setReceiveDate(String receiveDate) {
        ReceiveDate = receiveDate;
    }

    public String getExpireDate() {
        return new ChangeXml().viewCharDecoder(ExpireDate);
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getUserDescription() {
        return new ChangeXml().viewCharDecoder(UserDescription);
    }

    public void setUserDescription(String userDescription) {
        UserDescription = userDescription;
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

    public String getPrivateHameshContent() {
        return new ChangeXml().viewCharDecoder(PrivateHameshContent);
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
        return new ChangeXml().viewCharDecoder(Title);
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
        return new ChangeXml().viewCharDecoder(PriorityEntity_Name);
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

    public String getLastChangeViewStatesDate() {
        return LastChangeViewStatesDate;
    }

    public void setLastChangeViewStatesDate(String lastChangeViewStatesDate) {
        LastChangeViewStatesDate = lastChangeViewStatesDate;
    }

    public String getSenderFirstName() {
        return new ChangeXml().viewCharDecoder(SenderFirstName);
    }

    public void setSenderFirstName(String senderFirstName) {
        SenderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return new ChangeXml().viewCharDecoder(SenderLastName);
    }

    public void setSenderLastName(String senderLastName) {
        SenderLastName = senderLastName;
    }
}
