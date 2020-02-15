package avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2019-12-22 at 12:55 PM
 */

public class StructureChatRoomModelRES {
    @Element(required = false)
    private int ChatRoomID;
    @Element(required = false)
    private String ChatRoomIDString;
    @Element(required = false)
    private String ChatRoomActorIDString;
    @Element(required = false)
    private int ChatRoomActorID;
    @Element(required = false)
    private boolean ChatRoomActorIsActive;
    @Element(required = false)
    private String Name;
    @Element(required = false)
    private int UserID;
    @Element(required = false)
    private boolean Active;
    @Element(required = false)
    private boolean AddUserForAll;
    @Element(required = false)
    private boolean MuteNotification;
    @Element(required = false)
    private int UnseenCount;
    @Element(required = false)
    private long MessageLength;
    @Element(required = false)
    private long FileSize;
    @Element(required = false)
    private boolean UploadFile;
    @Element(required = false)
    private int RoomType;
    @Element(required = false)
    private String CreationDate;
    @Element(required = false)
    private String LastMessageContent;
    @Element(required = false)
    private String LastMessageCreationDate;
    @Element(required = false)
    private String LastMessageCreationTime;
    @Element(required = false)
    private int LastMessageType;
    @Element(required = false)
    private boolean HasPicture;

    public int getChatRoomID() {
        return ChatRoomID;
    }

    public void setChatRoomID(int chatRoomID) {
        ChatRoomID = chatRoomID;
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

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
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

    public int getRoomType() {
        return RoomType;
    }

    public void setRoomType(int roomType) {
        RoomType = roomType;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public String getLastMessageContent() {
        return LastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        LastMessageContent = lastMessageContent;
    }

    public String getLastMessageCreationDate() {
        return LastMessageCreationDate;
    }

    public void setLastMessageCreationDate(String lastMessageCreationDate) {
        LastMessageCreationDate = lastMessageCreationDate;
    }

    public int getLastMessageType() {
        return LastMessageType;
    }

    public void setLastMessageType(int lastMessageType) {
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
