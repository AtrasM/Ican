package avida.ican.Farzin.Model.Structure.Bundle.Queue;


import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;

/**
 * Created by AtrasVida on 2019-07-08 at 12:32 PM
 */

public class StructureDocumentOperatorsQueueBND implements Serializable {
    private int id;
    private int ETC;
    private int EC;
    private boolean isLock;
    private DocumentOperatoresTypeEnum documentOpratoresTypeEnum;
    private QueueStatus queueStatus;
    private String strError;
    private String strDataREQ;

    public StructureDocumentOperatorsQueueBND() {
    }

    public StructureDocumentOperatorsQueueBND(int ETC, int EC,boolean isLock, DocumentOperatoresTypeEnum documentOpratoresTypeEnum, String strDataREQ) {
        this.ETC = ETC;
        this.EC = EC;
        this.isLock = isLock;
        this.documentOpratoresTypeEnum = documentOpratoresTypeEnum;
        this.queueStatus = QueueStatus.WAITING;
        this.strError = "";
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
}
