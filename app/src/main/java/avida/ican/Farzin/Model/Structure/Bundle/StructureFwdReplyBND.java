package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import avida.ican.Ican.Model.Structure.StructureAttach;

/**
 * Created by AtrasVida on 2018-09-05 at 5:30 PM
 */

public class StructureFwdReplyBND implements Serializable {
    private int sender_user_id;
    private int sender_role_id;
    private String SenderFullName;
    private String SenderRoleName;
    private String subject;
    private String content;
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private boolean Reply;

    public StructureFwdReplyBND() {
    }

    public StructureFwdReplyBND(int sender_user_id, int sender_role_id, String senderFullName, String senderRoleName, String subject, String content, boolean reply) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        SenderFullName = senderFullName;
        SenderRoleName = senderRoleName;
        this.subject = subject;
        this.content = content;
        Reply = reply;
    }

    public StructureFwdReplyBND(int sender_user_id, int sender_role_id, String senderFullName, String senderRoleName, String subject, String content, ArrayList<StructureAttach> structureAttaches, boolean reply) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        SenderFullName = senderFullName;
        SenderRoleName = senderRoleName;
        this.subject = subject;
        this.content = content;
        this.structureAttaches = structureAttaches;
        Reply = reply;
    }

    public boolean isReply() {
        return Reply;
    }

    public void setReply(boolean reply) {
        Reply = reply;
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

    public ArrayList<StructureAttach> getStructureAttaches() {
        return structureAttaches;
    }

    public void setStructureAttaches(ArrayList<StructureAttach> structureAttaches) {
        this.structureAttaches = structureAttaches;
    }
}
