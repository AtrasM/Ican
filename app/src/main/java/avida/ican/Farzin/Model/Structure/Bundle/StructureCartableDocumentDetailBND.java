package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import avida.ican.Ican.Model.Structure.StructureAttach;

/**
 * Created by AtrasVida on 2018-10-22 at 1:32 PM
 */

public class StructureCartableDocumentDetailBND implements Serializable {
    private int ETC;
    private int EC;
    private int ReceiverCode;
    private Date ReceiveDate;
    private String Title;
    private String SenderName;
    private String SenderRoleName;
    private String EntityNumber;

    public StructureCartableDocumentDetailBND() {
    }

    public StructureCartableDocumentDetailBND(int ETC, int EC, int receiverCode, Date receiveDate, String title, String senderName, String senderRoleName, String entityNumber) {
        this.ETC = ETC;
        this.EC = EC;
        ReceiverCode = receiverCode;
        ReceiveDate = receiveDate;
        Title = title;
        SenderName = senderName;
        SenderRoleName = senderRoleName;
        EntityNumber = entityNumber;
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
}
