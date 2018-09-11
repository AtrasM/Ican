package avida.ican.Farzin.Model.Structure.Response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.Date;
import java.util.List;

/**
 * Created by AtrasVida on 2018-07-02 at 4:16 PM
 */

@Root(name = "Message")
public class StructureMessageRES {

    @Element
    private int ID;
    @Element
    private boolean IsRead;
    @Element(required = false)
    private String Subject;
    @Element(required = false)
    private String Description;
    @Element(required = false)
    private String SentDate;
    @Element(required = false)
    private String ViewDate;
    @ElementList(required = false)
    private List<StructureReceiverRES> Receivers;
    @ElementList(required = false)
    private List<StructureMessageAttachRES> MessageFiles;
    @Element(required = false)
    private StructureSenderRES Sender;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSentDate() {
        return SentDate;
    }

    public void setSentDate(String sentDate) {
        SentDate = sentDate;
    }

    public List<StructureReceiverRES> getReceivers() {
        return Receivers;
    }

    public void setReceivers(List<StructureReceiverRES> receivers) {
        Receivers = receivers;
    }

    public List<StructureMessageAttachRES> getMessageFiles() {
        return MessageFiles;
    }

    public void setMessageFiles(List<StructureMessageAttachRES> messageFiles) {
        MessageFiles = messageFiles;
    }

    public StructureSenderRES getSender() {
        return Sender;
    }

    public void setSender(StructureSenderRES sender) {
        Sender = sender;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    public boolean isRead() {
        return IsRead;
    }

    public String getViewDate() {
        return ViewDate;
    }

    public void setViewDate(String viewDate) {
        ViewDate = viewDate;
    }
}
