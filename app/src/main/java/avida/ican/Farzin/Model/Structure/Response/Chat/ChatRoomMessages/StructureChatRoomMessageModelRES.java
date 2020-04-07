package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages;

import org.simpleframework.xml.Element;

/**
 * Created by AtrasVida on 2019-12-22 at 12:55 PM
 */

public class StructureChatRoomMessageModelRES {
    @Element(required = false)
    private int MessageID;
    @Element(required = false)
    private String MessageIDString;
    @Element(required = false)
    private int ChatRoomID;
    @Element(required = false)
    private String ChatRoomIDString;
    @Element(required = false)
    private String ActorIDString;
    @Element(required = false)
    private int OriginalMessageID;
    @Element(required = false)
    private String OriginalMessageIDString;
    @Element(required = false)
    private boolean Seen;
    @Element(required = false)
    private String MessageContent;
    @Element(required = false)
    private String UserFullName;
    @Element(required = false)
    private int MessageType;
    @Element(required = false)
    private int ReplyToMessageID;
    @Element(required = false)
    private String ReplyToMessageIDString;
    @Element(required = false)
    private String ReplyToMessageContent;
    @Element(required = false)
    private String ReplyToMessageUser;
    @Element(required = false)
    private int ReplyToMessageType;
    @Element(required = false)
    private String ReplyToMessageTableExtension;
    @Element(required = false)
    private int SeenCount;
    @Element(required = false)
    private String FaSeenCount;
    @Element(required = false)
    private boolean Deleted;
    @Element(required = false)
    private String Extension;
    @Element(required = false)
    private String TableExtension;
    @Element(required = false)
    private boolean CurrentUserIsWriter;
    @Element(required = false)
    private String CreationDay;
    @Element(required = false)
    private String PersianCreationTime;
    @Element(required = false)
    private String PersianCreationTimeWithoutSecond;
    @Element(required = false)
    private String PersianCreationDate;
    @Element(required = false)
    private String PersianCreationDay;
    @Element(required = false)
    private String CreationDate;

    public int getMessageID() {
        return MessageID;
    }

    public void setMessageID(int messageID) {
        MessageID = messageID;
    }

    public String getMessageIDString() {
        return MessageIDString;
    }

    public void setMessageIDString(String messageIDString) {
        MessageIDString = messageIDString;
    }

    public int getChatRoomID() {
        return ChatRoomID;
    }

    public void setChatRoomID(int chatRoomID) {
        ChatRoomID = chatRoomID;
    }

    public String getChatRoomIDString() {
        return ChatRoomIDString;
    }

    public void setChatRoomIDString(String chatRoomIDString) {
        ChatRoomIDString = chatRoomIDString;
    }

    public String getActorIDString() {
        return ActorIDString;
    }

    public void setActorIDString(String actorIDString) {
        ActorIDString = actorIDString;
    }

    public int getOriginalMessageID() {
        return OriginalMessageID;
    }

    public void setOriginalMessageID(int originalMessageID) {
        OriginalMessageID = originalMessageID;
    }

    public String getOriginalMessageIDString() {
        return OriginalMessageIDString;
    }

    public void setOriginalMessageIDString(String originalMessageIDString) {
        OriginalMessageIDString = originalMessageIDString;
    }

    public boolean isSeen() {
        return Seen;
    }

    public void setSeen(boolean seen) {
        Seen = seen;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public int getMessageType() {
        return MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public int getReplyToMessageID() {
        return ReplyToMessageID;
    }

    public void setReplyToMessageID(int replyToMessageID) {
        ReplyToMessageID = replyToMessageID;
    }

    public String getReplyToMessageIDString() {
        return ReplyToMessageIDString;
    }

    public void setReplyToMessageIDString(String replyToMessageIDString) {
        ReplyToMessageIDString = replyToMessageIDString;
    }

    public String getReplyToMessageContent() {
        return ReplyToMessageContent;
    }

    public void setReplyToMessageContent(String replyToMessageContent) {
        ReplyToMessageContent = replyToMessageContent;
    }

    public String getReplyToMessageUser() {
        return ReplyToMessageUser;
    }

    public void setReplyToMessageUser(String replyToMessageUser) {
        ReplyToMessageUser = replyToMessageUser;
    }

    public int getReplyToMessageType() {
        return ReplyToMessageType;
    }

    public void setReplyToMessageType(int replyToMessageType) {
        ReplyToMessageType = replyToMessageType;
    }

    public String getReplyToMessageTableExtension() {
        return ReplyToMessageTableExtension;
    }

    public void setReplyToMessageTableExtension(String replyToMessageTableExtension) {
        ReplyToMessageTableExtension = replyToMessageTableExtension;
    }

    public int getSeenCount() {
        return SeenCount;
    }

    public void setSeenCount(int seenCount) {
        SeenCount = seenCount;
    }

    public String getFaSeenCount() {
        return FaSeenCount;
    }

    public void setFaSeenCount(String faSeenCount) {
        FaSeenCount = faSeenCount;
    }

    public boolean isDeleted() {
        return Deleted;
    }

    public void setDeleted(boolean deleted) {
        Deleted = deleted;
    }

    public String getExtension() {
        return Extension;
    }

    public void setExtension(String extension) {
        Extension = extension;
    }

    public String getTableExtension() {
        return TableExtension;
    }

    public void setTableExtension(String tableExtension) {
        TableExtension = tableExtension;
    }

    public boolean isCurrentUserIsWriter() {
        return CurrentUserIsWriter;
    }

    public void setCurrentUserIsWriter(boolean currentUserIsWriter) {
        CurrentUserIsWriter = currentUserIsWriter;
    }

    public String getCreationDay() {
        return CreationDay;
    }

    public void setCreationDay(String creationDay) {
        CreationDay = creationDay;
    }

    public String getPersianCreationTime() {
        return PersianCreationTime;
    }

    public void setPersianCreationTime(String persianCreationTime) {
        PersianCreationTime = persianCreationTime;
    }

    public String getPersianCreationTimeWithoutSecond() {
        return PersianCreationTimeWithoutSecond;
    }

    public void setPersianCreationTimeWithoutSecond(String persianCreationTimeWithoutSecond) {
        PersianCreationTimeWithoutSecond = persianCreationTimeWithoutSecond;
    }

    public String getPersianCreationDate() {
        return PersianCreationDate;
    }

    public void setPersianCreationDate(String persianCreationDate) {
        PersianCreationDate = persianCreationDate;
    }

    public String getPersianCreationDay() {
        return PersianCreationDay;
    }

    public void setPersianCreationDay(String persianCreationDay) {
        PersianCreationDay = persianCreationDay;
    }
}
