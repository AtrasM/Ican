package avida.ican.Farzin.Model.Structure.Response.Message;


import android.util.Log;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Ican.Model.ChangeXml;

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
    private List<StructureReceiverRES> Receivers = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureMessageAttachRES> MessageFiles = new ArrayList<>();
    @Element(required = false)
    private StructureSenderRES Sender;

    public String getSubject() {
        return new ChangeXml().viewCharDecoder(Subject);
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
        return new ChangeXml().viewCharDecoder(Description);
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSentDate() {
        Log.i("SentDate", "befor getSentDate= " + SentDate);
        Log.i("SentDate", "after getSentDate= " + new ChangeXml().viewCharDecoder(SentDate));

        return new ChangeXml().viewCharDecoder(SentDate);
    }

    public void setSentDate(String sentDate) {
        Log.i("SentDate", "setSentDate= " + sentDate);
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
        return new ChangeXml().viewCharDecoder(ViewDate);
    }

    public void setViewDate(String viewDate) {
        ViewDate = viewDate;
    }
}
