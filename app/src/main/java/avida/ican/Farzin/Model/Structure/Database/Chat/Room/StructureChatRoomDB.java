package avida.ican.Farzin.Model.Structure.Database.Chat.Room;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.Chat.ChatMessageTypeEnum;
import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-06-19 at 1:24 PM
 */
@DatabaseTable(tableName = "chat_room")
public class StructureChatRoomDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    private int ChatRoomID;
    @DatabaseField(canBeNull = false)
    private String ChatRoomIDString;
    @DatabaseField(canBeNull = false)
    private String ChatRoomActorIDString;
    @DatabaseField(canBeNull = false)
    private int ChatRoomActorID;
    @DatabaseField()
    private int UserID;
    @DatabaseField(canBeNull = false)
    private boolean ChatRoomActorIsActive;
    @DatabaseField()
    private String Name;
    @DatabaseField()
    private boolean Active;
    @DatabaseField()
    private boolean AddUserForAll;
    @DatabaseField()
    private boolean MuteNotification;
    @DatabaseField()
    private int UnseenCount;
    @DatabaseField()
    private long MessageLength;
    @DatabaseField()
    private long FileSize;
    @DatabaseField()
    private boolean UploadFile;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private ChatRoomTypeEnum ChatRoomTypeEnum;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private ChatMessageTypeEnum LastMessageType;
    @DatabaseField()
    private String LastMessageContent;
    @DatabaseField()
    private long CreationDate;
    @DatabaseField()
    private long LastMessageCreationDate;
    @DatabaseField()
    private String LastMessageCreationTime;
    @DatabaseField()
    private boolean HasPicture;

    public StructureChatRoomDB() {
    }


    public StructureChatRoomDB(int chatRoomID, int chatRoomActorID, String chatRoomIDString, String chatRoomActorIDString, int userID, boolean chatRoomActorIsActive, String name, boolean active, boolean addUserForAll, boolean muteNotification, int unseenCount, long messageLength, long fileSize, boolean uploadFile, avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum chatRoomTypeEnum, String lastMessageContent, Date creationDate, Date lastMessageCreationDate, String lastMessageCreationTime, ChatMessageTypeEnum lastMessageType, boolean hasPicture) {
        ChatRoomID = chatRoomID;
        ChatRoomActorID = chatRoomActorID;
        if (userID <= 0) {
            UserID = -1;
        } else {
            UserID = userID;
        }
        ChatRoomIDString = chatRoomIDString;
        ChatRoomActorIDString = chatRoomActorIDString;
        ChatRoomActorIsActive = chatRoomActorIsActive;
        Name = name;
        Active = active;
        AddUserForAll = addUserForAll;
        MuteNotification = muteNotification;
        UnseenCount = unseenCount;
        MessageLength = messageLength;
        FileSize = fileSize;
        UploadFile = uploadFile;
        ChatRoomTypeEnum = chatRoomTypeEnum;
        LastMessageContent = lastMessageContent;
        CreationDate = creationDate.getTime();
        LastMessageCreationDate = lastMessageCreationDate.getTime();
        LastMessageCreationTime = lastMessageCreationTime;
        LastMessageType = lastMessageType;
        HasPicture = hasPicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatRoomID() {
        return ChatRoomID;
    }

    public void setChatRoomID(int chatRoomID) {
        ChatRoomID = chatRoomID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getChatRoomActorID() {
        return ChatRoomActorID;
    }

    public void setChatRoomActorID(int chatRoomActorID) {
        ChatRoomActorID = chatRoomActorID;
    }

    public boolean isChatRoomActorIsActive() {
        return ChatRoomActorIsActive;
    }

    public void setChatRoomActorIsActive(boolean chatRoomActorIsActive) {
        ChatRoomActorIsActive = chatRoomActorIsActive;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public boolean isAddUserForAll() {
        return AddUserForAll;
    }

    public void setAddUserForAll(boolean addUserForAll) {
        AddUserForAll = addUserForAll;
    }

    public boolean isMuteNotification() {
        return MuteNotification;
    }

    public void setMuteNotification(boolean muteNotification) {
        MuteNotification = muteNotification;
    }

    public int getUnseenCount() {
        return UnseenCount;
    }

    public void setUnseenCount(int unseenCount) {
        UnseenCount = unseenCount;
    }

    public long getMessageLength() {
        return MessageLength;
    }

    public void setMessageLength(long messageLength) {
        MessageLength = messageLength;
    }

    public long getFileSize() {
        return FileSize;
    }

    public void setFileSize(long fileSize) {
        FileSize = fileSize;
    }

    public boolean isUploadFile() {
        return UploadFile;
    }

    public void setUploadFile(boolean uploadFile) {
        UploadFile = uploadFile;
    }

    public avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum getChatRoomTypeEnum() {
        return ChatRoomTypeEnum;
    }

    public void setChatRoomTypeEnum(avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum chatRoomTypeEnum) {
        ChatRoomTypeEnum = chatRoomTypeEnum;
    }

    public String getLastMessageContent() {
        return LastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        LastMessageContent = lastMessageContent;
    }

    public Date getCreationDate() {
        return CustomFunction.convertLongTimeToDateStandartFormat(CreationDate);
    }

    public void setCreationDate(long creationDate) {
        CreationDate = creationDate;
    }

    public Date getLastMessageCreationDate() {
        return CustomFunction.convertLongTimeToDateStandartFormat(LastMessageCreationDate);
    }

    public void setLastMessageCreationDate(long lastMessageCreationDate) {
        LastMessageCreationDate = lastMessageCreationDate;
    }

    public ChatMessageTypeEnum getLastMessageType() {
        return LastMessageType;
    }

    public void setLastMessageType(ChatMessageTypeEnum lastMessageType) {
        LastMessageType = lastMessageType;
    }

    public boolean isHasPicture() {
        return HasPicture;
    }

    public void setHasPicture(boolean hasPicture) {
        HasPicture = hasPicture;
    }

    public String getChatRoomIDString() {
        return ChatRoomIDString;
    }

    public void setChatRoomIDString(String chatRoomIDString) {
        ChatRoomIDString = chatRoomIDString;
    }

    public String getChatRoomActorIDString() {
        return ChatRoomActorIDString;
    }

    public void setChatRoomActorIDString(String chatRoomActorIDString) {
        ChatRoomActorIDString = chatRoomActorIDString;
    }

    public String getLastMessageCreationTime() {
        return LastMessageCreationTime;
    }

    public void setLastMessageCreationTime(String lastMessageCreationTime) {
        LastMessageCreationTime = lastMessageCreationTime;
    }
}
