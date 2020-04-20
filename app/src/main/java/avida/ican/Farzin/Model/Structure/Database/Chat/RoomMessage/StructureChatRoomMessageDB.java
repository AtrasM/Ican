package avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

import avida.ican.Farzin.Model.Enum.Chat.ChatMessageTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2020-01-12 at 11:12 AM
 */
@DatabaseTable(tableName = "chat_room_message")
public class StructureChatRoomMessageDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private int MessageID;
    @DatabaseField(canBeNull = false)
    private String MessageIDString;
    @DatabaseField(canBeNull = false)
    private int ChatRoomID;
    @DatabaseField(canBeNull = false)
    private String ChatRoomIDString;
    @DatabaseField(canBeNull = false)
    private String ActorIDString;
    @DatabaseField(canBeNull = false)
    private int OriginalMessageID;
    @DatabaseField(canBeNull = false)
    private String OriginalMessageIDString;
    @DatabaseField(canBeNull = false)
    private boolean Seen;
    @DatabaseField(canBeNull = false)
    private String MessageContent;
    @DatabaseField(canBeNull = false)
    private String UserFullName;
    @DatabaseField(dataType = DataType.ENUM_INTEGER, canBeNull = false)
    private ChatMessageTypeEnum MessageType;
    @DatabaseField()
    private int ReplyToMessageID;
    @DatabaseField()
    private String ReplyToMessageIDString;
    @DatabaseField()
    private String ReplyToMessageContent;
    @DatabaseField()
    private String ReplyToMessageUser;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private ChatMessageTypeEnum ReplyToMessageType;
    @DatabaseField()
    private String ReplyToMessageTableExtension;
    @DatabaseField(canBeNull = false)
    private int SeenCount;
    @DatabaseField(canBeNull = false)
    private String FaSeenCount;
    @DatabaseField(canBeNull = false)
    private boolean Deleted;
    @DatabaseField(canBeNull = false)
    private String Extension;
    @DatabaseField(canBeNull = false)
    private String TableExtension;
    @DatabaseField(canBeNull = false)
    private boolean CurrentUserIsWriter;
    @DatabaseField(canBeNull = false)
    private String CreationDay;
    @DatabaseField(canBeNull = false)
    private String PersianCreationTime;
    @DatabaseField(canBeNull = false)
    private String PersianCreationTimeWithoutSecond;
    @DatabaseField(canBeNull = false)
    private String PersianCreationDate;
    @DatabaseField(canBeNull = false)
    private String PersianCreationDay;
    @DatabaseField(canBeNull = false)
    private boolean isSendedFromApp;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private QueueStatus queueStatus;
    @DatabaseField()
    private String strError;
    @DatabaseField()
    private int errorCount;

    public StructureChatRoomMessageDB() {
    }

    public StructureChatRoomMessageDB(String chatRoomIDString, String messageIDString, int messageID, String messageContent, ChatMessageTypeEnum messageType, String creationDate) {
        ChatRoomIDString = chatRoomIDString;
        MessageIDString = messageIDString;
        MessageContent = CustomFunction.convertNullToEmpety(messageContent);
        MessageType = messageType;
        PersianCreationDay = creationDate;
        CurrentUserIsWriter = true;
        isSendedFromApp = true;
        MessageID = messageID;
        ChatRoomID = -1;
        ActorIDString = "";
        OriginalMessageID = -1;
        OriginalMessageIDString = "";
        Seen = false;

        UserFullName = "";
        SeenCount = -1;
        FaSeenCount = "-1";
        Deleted = false;
        Extension = "";
        TableExtension = "";
        CreationDay = creationDate;
        PersianCreationTime = "";
        PersianCreationTimeWithoutSecond = "";
        PersianCreationDate = creationDate;
    }

    public StructureChatRoomMessageDB(int messageID, String messageIDString, int chatRoomID, String chatRoomIDString, String actorIDString, int originalMessageID, String originalMessageIDString, boolean seen, String messageContent, String userFullName, ChatMessageTypeEnum messageType, int replyToMessageID, String replyToMessageIDString, String replyToMessageContent, String replyToMessageUser, ChatMessageTypeEnum replyToMessageType, String replyToMessageTableExtension, int seenCount, String faSeenCount, boolean deleted, String extension, String tableExtension, boolean currentUserIsWriter, String creationDay, String persianCreationTime, String persianCreationTimeWithoutSecond, String persianCreationDate, String persianCreationDay) {
        MessageID = messageID;
        MessageIDString = messageIDString;
        ChatRoomID = chatRoomID;
        ChatRoomIDString = chatRoomIDString;
        ActorIDString = CustomFunction.convertNullToEmpety(actorIDString);
        OriginalMessageID = originalMessageID;
        OriginalMessageIDString = CustomFunction.convertNullToEmpety(originalMessageIDString);
        Seen = seen;
        MessageContent = CustomFunction.convertNullToEmpety(messageContent);
        UserFullName = CustomFunction.convertNullToEmpety(userFullName);
        MessageType = messageType;
        ReplyToMessageID = replyToMessageID;
        ReplyToMessageIDString = CustomFunction.convertNullToEmpety(replyToMessageIDString);
        ReplyToMessageContent = CustomFunction.convertNullToEmpety(replyToMessageContent);
        ReplyToMessageUser = CustomFunction.convertNullToEmpety(replyToMessageUser);
        ReplyToMessageType = replyToMessageType;
        ReplyToMessageTableExtension = CustomFunction.convertNullToEmpety(replyToMessageTableExtension);
        SeenCount = seenCount;
        FaSeenCount = faSeenCount;
        Deleted = deleted;
        Extension = CustomFunction.convertNullToEmpety(extension);
        TableExtension = CustomFunction.convertNullToEmpety(tableExtension);
        CurrentUserIsWriter = currentUserIsWriter;
        CreationDay = creationDay;
        PersianCreationTime = persianCreationTime;
        PersianCreationTimeWithoutSecond = persianCreationTimeWithoutSecond;
        PersianCreationDate = persianCreationDate;
        PersianCreationDay = persianCreationDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public ChatMessageTypeEnum getMessageType() {
        return MessageType;
    }

    public void setMessageType(ChatMessageTypeEnum messageType) {
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

    public ChatMessageTypeEnum getReplyToMessageType() {
        return ReplyToMessageType;
    }

    public void setReplyToMessageType(ChatMessageTypeEnum replyToMessageType) {
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

    public boolean isSendedFromApp() {
        return isSendedFromApp;
    }

    public void setSendedFromApp(boolean sendedFromApp) {
        isSendedFromApp = sendedFromApp;
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

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }
}
