package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;

/**
 * Created by AtrasVida on 2018-08-26 at 3:18 PM
 */

public class StructureDetailMessageBND implements Serializable {
    private ArrayList<StructureReceiverDB> structureReceiverDBS = new ArrayList<>();
    private int id;
    private int main_id;
    private int sender_user_id;
    private int sender_role_id;
    private String ReceiverFullName;
    private String ReceiverRoleName;
    private String subject;
    private String content;
    private String sent_date;
    private String sent_time;
    private Type messageType;
    private boolean isFilesDownloaded;
    private int AttachmentCount;
    private ArrayList<StructureMessageFileDB> message_files = new ArrayList<>();

    public StructureDetailMessageBND() {
    }

    public StructureDetailMessageBND(int id, int main_id, int sender_user_id, int sender_role_id, String receiverFullName, String receiverRoleName, String subject, String content, String sent_date, String sent_time, ArrayList<StructureMessageFileDB> message_files,int AttachmentCount,boolean isFilesDownloaded, ArrayList<StructureReceiverDB> structureReceiverDBS, Type type) {
        this.id = id;
        this.main_id = main_id;
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.ReceiverFullName = receiverFullName;
        this.ReceiverRoleName = receiverRoleName;
        this.subject = subject;
        this.content = content;
        this.sent_date = sent_date;
        this.sent_time = sent_time;
        this.message_files = message_files;
        this.AttachmentCount = AttachmentCount;
        this.isFilesDownloaded = isFilesDownloaded;
        this.structureReceiverDBS = structureReceiverDBS;
        this.messageType = type;
    }

    public int getMain_id() {
        return main_id;
    }

    public void setMain_id(int main_id) {
        this.main_id = main_id;
    }

    public int getSender_user_id() {
        return sender_user_id;
    }

    public void setSender_user_id(int sender_user_id) {
        this.sender_user_id = sender_user_id;
    }

    public int getSender_role_id() {
        return sender_role_id;
    }

    public void setSender_role_id(int sender_role_id) {
        this.sender_role_id = sender_role_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSent_date() {
        return sent_date;
    }

    public void setSent_date(String sent_date) {
        this.sent_date = sent_date;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }

    public ArrayList<StructureMessageFileDB> getMessage_files() {
        return message_files;
    }

    public void setMessage_files(ArrayList<StructureMessageFileDB> message_files) {
        this.message_files = message_files;
    }

    public boolean isFilesDownloaded() {
        return isFilesDownloaded;
    }

    public void setFilesDownloaded(boolean filesDownloaded) {
        isFilesDownloaded = filesDownloaded;
    }

    public int getAttachmentCount() {
        return AttachmentCount;
    }

    public void setAttachmentCount(int attachmentCount) {
        AttachmentCount = attachmentCount;
    }

    public String getReceiverFullName() {
        return ReceiverFullName;
    }

    public void setReceiverFullName(String receiverFullName) {
        ReceiverFullName = receiverFullName;
    }

    public String getReceiverRoleName() {
        return ReceiverRoleName;
    }

    public void setReceiverRoleName(String receiverRoleName) {
        ReceiverRoleName = receiverRoleName;
    }

    public Type getMessageType() {
        return messageType;
    }

    public void setMessageType(Type type) {
        this.messageType = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<StructureReceiverDB> getStructureReceiverDBS() {
        return structureReceiverDBS;
    }

    public void setStructureReceiverDBS(ArrayList<StructureReceiverDB> structureReceiverDBS) {
        this.structureReceiverDBS = structureReceiverDBS;
    }
}
