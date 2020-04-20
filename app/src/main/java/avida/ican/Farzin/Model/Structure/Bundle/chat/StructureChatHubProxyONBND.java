package avida.ican.Farzin.Model.Structure.Bundle.chat;

import java.io.Serializable;

import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Farzin.View.Enum.Chat.ChatMessageActionsEnum;

public class StructureChatHubProxyONBND implements Serializable {
    private StructureChatRoomMessageModelRES chatRoomMessageModelRES;
    private StructureChatRoomModelRES chatRoomModelRES;
    private ChatMessageActionsEnum messageActionsEnum;
    private String tempMessageID;

    public StructureChatHubProxyONBND() {
    }

    public StructureChatHubProxyONBND(StructureChatRoomMessageModelRES chatRoomMessageModelRES, StructureChatRoomModelRES chatRoomModelRES, ChatMessageActionsEnum messageActionsEnum) {
        this.chatRoomMessageModelRES = chatRoomMessageModelRES;
        this.chatRoomModelRES = chatRoomModelRES;
        this.messageActionsEnum = messageActionsEnum;
    }

    public StructureChatHubProxyONBND(StructureChatRoomMessageModelRES chatRoomMessageModelRES, StructureChatRoomModelRES chatRoomModelRES, String tempMessageID, ChatMessageActionsEnum messageActionsEnum) {
        this.chatRoomMessageModelRES = chatRoomMessageModelRES;
        this.chatRoomModelRES = chatRoomModelRES;
        this.tempMessageID = tempMessageID;
        this.messageActionsEnum = messageActionsEnum;
    }

    public StructureChatRoomMessageModelRES getChatRoomMessageModelRES() {
        return chatRoomMessageModelRES;
    }

    public void setChatRoomMessageModelRES(StructureChatRoomMessageModelRES chatRoomMessageModelRES) {
        this.chatRoomMessageModelRES = chatRoomMessageModelRES;
    }

    public StructureChatRoomModelRES getChatRoomModelRES() {
        return chatRoomModelRES;
    }

    public void setChatRoomModelRES(StructureChatRoomModelRES chatRoomModelRES) {
        this.chatRoomModelRES = chatRoomModelRES;
    }

    public String getTempMessageID() {
        return tempMessageID;
    }

    public void setTempMessageID(String tempMessageID) {
        this.tempMessageID = tempMessageID;
    }

    public ChatMessageActionsEnum getMessageActionsEnum() {
        return messageActionsEnum;
    }

    public void setMessageActionsEnum(ChatMessageActionsEnum messageActionsEnum) {
        this.messageActionsEnum = messageActionsEnum;
    }
}
