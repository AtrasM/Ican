package avida.ican.Farzin.Model.Structure.Bundle.Queue;
import java.io.Serializable;

/**
 * Created by AtrasVida on 2019-07-30 at 10:55 AM
 */
public class StructureCreateDocumentQueueBND implements Serializable {
    private int id;
    private int ETC;
    private int EC;
    private String subject;
    private String senderUserName;
    private String senderFullName;
    private String receiverFullName;
    private String importOriginDate;


    public StructureCreateDocumentQueueBND() {
    }

    public StructureCreateDocumentQueueBND(int id,int ETC, int EC, String subject, String senderUserName, String senderFullName, String receiverFullName, String importOriginDate) {
        this.id = id;
        this.ETC = ETC;
        this.EC = EC;
        this.subject = subject;
        this.senderUserName = senderUserName;
        this.senderFullName = senderFullName;
        this.receiverFullName = receiverFullName;
        this.importOriginDate = importOriginDate;
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


}
