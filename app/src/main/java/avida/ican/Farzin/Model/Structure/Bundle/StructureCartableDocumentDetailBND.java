package avida.ican.Farzin.Model.Structure.Bundle;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by AtrasVida on 2018-10-22 at 1:32 PM
 */

public class StructureCartableDocumentDetailBND implements Serializable {
    private int ETC;
    private int EC;
    private int SendCode;
    private int ReceiverCode;
    private Date ReceiveDate;
    private String Title;
    private String SenderName;
    private String SenderRoleName;
    private String EntityNumber;
    private String ImportEntityNumber;
    private boolean isFromEntityDependency;
    private boolean bInWorkFlow;

    public StructureCartableDocumentDetailBND() {

    }

    public StructureCartableDocumentDetailBND(int ETC, int EC, String title, String entityNumber, String importEntityNumber) {
        this.ETC = ETC;
        this.EC = EC;
        Title = title;
        EntityNumber = entityNumber;
        ImportEntityNumber = importEntityNumber;
        this.isFromEntityDependency = true;
        this.bInWorkFlow = false;
    }

    public StructureCartableDocumentDetailBND(int ETC, int EC, int SendCode, int receiverCode, Date receiveDate, String title, String senderName, String senderRoleName, String entityNumber, String importEntityNumber, boolean bInWorkFlow) {
        this.ETC = ETC;
        this.EC = EC;
        this.SendCode = SendCode;
        ReceiverCode = receiverCode;
        ReceiveDate = receiveDate;
        Title = title;
        SenderName = senderName;
        SenderRoleName = senderRoleName;
        EntityNumber = entityNumber;
        ImportEntityNumber = importEntityNumber;
        this.bInWorkFlow = bInWorkFlow;
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

    public Date getReceiveDate() {
        return ReceiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        ReceiveDate = receiveDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
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

    public String getImportEntityNumber() {
        return ImportEntityNumber;
    }

    public void setImportEntityNumber(String importEntityNumber) {
        ImportEntityNumber = importEntityNumber;
    }

    public boolean isFromEntityDependency() {
        return isFromEntityDependency;
    }

    public void setFromEntityDependency(boolean fromEntityDependency) {
        isFromEntityDependency = fromEntityDependency;
    }

    public boolean isbInWorkFlow() {
        return bInWorkFlow;
    }

    public void setbInWorkFlow(boolean bInWorkFlow) {
        this.bInWorkFlow = bInWorkFlow;
    }
}
