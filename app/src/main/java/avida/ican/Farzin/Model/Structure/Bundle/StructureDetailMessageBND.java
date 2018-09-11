package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.MessageType;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageFileDB;

/**
 * Created by AtrasVida on 2018-08-26 at 3:18 PM
 */

public class StructureDetailMessageBND implements Serializable {
    private int main_id;
    private int sender_user_id;
    private int sender_role_id;
    private String SenderFullName;
    private String SenderRoleName;
    private String subject;
    private String content;
    private String sent_date;
    private String sent_time;
    private MessageType messageType;
    private ArrayList<StructureMessageFileDB> message_files=new ArrayList<>();

    public StructureDetailMessageBND() {
    }

    public StructureDetailMessageBND(int main_id, int sender_user_id, int sender_role_id, String senderFullName, String senderRoleName, String subject, String content, String sent_date, String sent_time, ArrayList<StructureMessageFileDB> message_files,MessageType messageType) {
        this.main_id = main_id;
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.SenderFullName = senderFullName;
        this.SenderRoleName = senderRoleName;
        this.subject = subject;
        this.content = content;
        this.sent_date = sent_date;
        this.sent_time = sent_time;
        this.message_files = message_files;
        this.messageType = messageType;
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

    public String getSenderFullName() {
        return SenderFullName;
    }

    public void setSenderFullName(String senderFullName) {
        SenderFullName = senderFullName;
    }

    public String getSenderRoleName() {
        return SenderRoleName;
    }

    public void setSenderRoleName(String senderRoleName) {
        SenderRoleName = senderRoleName;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
