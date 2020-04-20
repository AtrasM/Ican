package avida.ican.Farzin.Chat;

import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import org.acra.ACRA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Chat.ChatMessageTypeEnum;
import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.Chat.Room.ChatRoomModelQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Chat.RoomMessage.ChatRoomMessageModelQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.ToastEnum;

public class FarzinChatQuery {
    private ChatRoomModelQuerySaveListener chatRoomModelQuerySaveListener;
    private ChatRoomMessageModelQuerySaveListener chatRoomMessageModelQuerySaveListener;

    //_______________________***Dao***______________________________

    private Dao<StructureChatRoomDB, Integer> chatRoomDao = null;
    private Dao<StructureChatRoomMessageDB, Integer> chatRoomMessageDao = null;

    public FarzinChatQuery() {
        initDao();
    }

    private void initDao() {
        try {
            chatRoomDao = App.getFarzinDatabaseHelper().getChatRoomListDBDao();
            chatRoomMessageDao = App.getFarzinDatabaseHelper().getChatRoomMessageListDBDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void saveChatRoom(StructureChatRoomModelRES structureChatRoomModelRES, final ChatRoomModelQuerySaveListener chatRoomModelQuerySaveListener) {
        this.chatRoomModelQuerySaveListener = chatRoomModelQuerySaveListener;
        new saveChatRoom().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureChatRoomModelRES);
    }

    public void saveChatRoomMessage(StructureChatRoomMessageModelRES structureChatRoomMessageModelRES, final ChatRoomMessageModelQuerySaveListener chatRoomMessageModelQuerySaveListener) {
        this.chatRoomMessageModelQuerySaveListener = chatRoomMessageModelQuerySaveListener;
        new saveChatRoomMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureChatRoomMessageModelRES);
    }


    private class saveChatRoom extends AsyncTask<StructureChatRoomModelRES, Void, Void> {
        @Override
        protected Void doInBackground(StructureChatRoomModelRES... structureChatRoomModelRES) {
            Date creationDate = null;
            Date lastMessageCreationDate = null;
            ChatRoomTypeEnum chatRoomTypeEnum;
            ChatMessageTypeEnum chatMessageTypeEnum;
            int roomType = structureChatRoomModelRES[0].getRoomType();
            int lastMessageRoomType = structureChatRoomModelRES[0].getLastMessageType();

            //-------------------------------------------------------------------

            if (roomType == ChatRoomTypeEnum.Private.getIntValue()) {
                chatRoomTypeEnum = ChatRoomTypeEnum.Private;
            } else if (roomType == ChatRoomTypeEnum.Room.getIntValue()) {
                chatRoomTypeEnum = ChatRoomTypeEnum.Room;
            } else if (roomType == ChatRoomTypeEnum.Channele.getIntValue()) {
                chatRoomTypeEnum = ChatRoomTypeEnum.Channele;
            } else if (roomType == ChatRoomTypeEnum.ChatWithYoureSelf.getIntValue()) {
                chatRoomTypeEnum = ChatRoomTypeEnum.ChatWithYoureSelf;
            } else {
                chatRoomTypeEnum = ChatRoomTypeEnum.Private;
            }

            //_______________________________________________________________________

            if (lastMessageRoomType == ChatMessageTypeEnum.Text.getIntValue()) {
                chatMessageTypeEnum = ChatMessageTypeEnum.Text;
            } else {
                chatMessageTypeEnum = ChatMessageTypeEnum.Text;
            }

            //-------------------------------------------------------------------

            creationDate = new Date(CustomFunction.StandardizeTheDateFormat(structureChatRoomModelRES[0].getCreationDate()));
            lastMessageCreationDate = new Date(CustomFunction.StandardizeTheDateFormat(structureChatRoomModelRES[0].getLastMessageCreationDate()));
            StructureChatRoomDB structureChatRoomDB = new StructureChatRoomDB(structureChatRoomModelRES[0].getChatRoomID(), structureChatRoomModelRES[0].getChatRoomActorID(), structureChatRoomModelRES[0].getChatRoomIDString(), structureChatRoomModelRES[0].getChatRoomActorIDString(), structureChatRoomModelRES[0].getUserID(), structureChatRoomModelRES[0].isChatRoomActorIsActive(), structureChatRoomModelRES[0].getName(), structureChatRoomModelRES[0].isActive(), structureChatRoomModelRES[0].isAddUserForAll(), structureChatRoomModelRES[0].isMuteNotification(), structureChatRoomModelRES[0].getUnseenCount(), structureChatRoomModelRES[0].getMessageLength(), structureChatRoomModelRES[0].getFileSize(), structureChatRoomModelRES[0].isUploadFile(), chatRoomTypeEnum, structureChatRoomModelRES[0].getLastMessageContent(), creationDate, lastMessageCreationDate, structureChatRoomModelRES[0].getLastMessageCreationTime(), chatMessageTypeEnum, structureChatRoomModelRES[0].isHasPicture());

            try {
                chatRoomDao.create(structureChatRoomDB);
                chatRoomModelQuerySaveListener.onSuccess(structureChatRoomDB);
            } catch (SQLException e) {
                e.printStackTrace();
                chatRoomModelQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }
            return null;
        }
    }

    private class saveChatRoomMessage extends AsyncTask<StructureChatRoomMessageModelRES, Void, Void> {
        @Override
        protected Void doInBackground(StructureChatRoomMessageModelRES... structureChatRoomMessageModelRES) {
            ChatMessageTypeEnum messageTypeEnum;
            ChatMessageTypeEnum replyToMessageTypeEnum;
            int messageType = structureChatRoomMessageModelRES[0].getMessageType();
            int replyToMessageType = structureChatRoomMessageModelRES[0].getReplyToMessageType();
            //--------------------------------------------------------------------------------

            if (messageType == ChatMessageTypeEnum.Text.getIntValue()) {
                messageTypeEnum = ChatMessageTypeEnum.Text;
            } else {
                messageTypeEnum = ChatMessageTypeEnum.Text;
            }

            //_____________________________________________________________________

            if (replyToMessageType == ChatMessageTypeEnum.Text.getIntValue()) {
                replyToMessageTypeEnum = ChatMessageTypeEnum.Text;
            } else {
                replyToMessageTypeEnum = ChatMessageTypeEnum.Text;
            }

            //--------------------------------------------------------------------------------

            StructureChatRoomMessageDB structureChatRoomMessageDB = new StructureChatRoomMessageDB(structureChatRoomMessageModelRES[0].getMessageID(), structureChatRoomMessageModelRES[0].getMessageIDString(), structureChatRoomMessageModelRES[0].getChatRoomID(), structureChatRoomMessageModelRES[0].getChatRoomIDString(), structureChatRoomMessageModelRES[0].getActorIDString(), structureChatRoomMessageModelRES[0].getOriginalMessageID(), structureChatRoomMessageModelRES[0].getOriginalMessageIDString(), structureChatRoomMessageModelRES[0].isSeen(), structureChatRoomMessageModelRES[0].getMessageContent(), structureChatRoomMessageModelRES[0].getUserFullName(), messageTypeEnum, structureChatRoomMessageModelRES[0].getReplyToMessageID(), structureChatRoomMessageModelRES[0].getReplyToMessageIDString(), structureChatRoomMessageModelRES[0].getReplyToMessageContent(), structureChatRoomMessageModelRES[0].getReplyToMessageUser(), replyToMessageTypeEnum, structureChatRoomMessageModelRES[0].getReplyToMessageTableExtension(), structureChatRoomMessageModelRES[0].getSeenCount(), structureChatRoomMessageModelRES[0].getFaSeenCount(), structureChatRoomMessageModelRES[0].isDeleted(), structureChatRoomMessageModelRES[0].getExtension(), structureChatRoomMessageModelRES[0].getTableExtension(), structureChatRoomMessageModelRES[0].isCurrentUserIsWriter(), structureChatRoomMessageModelRES[0].getCreationDay(), structureChatRoomMessageModelRES[0].getPersianCreationTime(), structureChatRoomMessageModelRES[0].getPersianCreationTimeWithoutSecond(), structureChatRoomMessageModelRES[0].getPersianCreationDate(), structureChatRoomMessageModelRES[0].getPersianCreationDay());
            try {
                chatRoomMessageDao.create(structureChatRoomMessageDB);
                chatRoomMessageModelQuerySaveListener.onSuccess(structureChatRoomMessageDB);
            } catch (SQLException e) {
                e.printStackTrace();
                chatRoomMessageModelQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }
            return null;
        }
    }


    public void saveInputMessage(StructureChatRoomMessageDB structureChatRoomMessageDB, ChatRoomMessageModelQuerySaveListener chatRoomMessageModelQuerySaveListener) {
        try {
            chatRoomMessageDao.create(structureChatRoomMessageDB);
            chatRoomMessageModelQuerySaveListener.onSuccess(structureChatRoomMessageDB);
        } catch (SQLException e) {
            e.printStackTrace();
            chatRoomMessageModelQuerySaveListener.onFailed(e.toString());
            ACRA.getErrorReporter().handleSilentException(e);
            App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
        }
    }
    //--------------------------------*****ChatRoom*****-------------------------------------------------------

    public List<StructureChatRoomDB> getChatRoomList(long start, long count, ChatRoomTypeEnum chatRoomTypeEnum) {
        QueryBuilder<StructureChatRoomDB, Integer> queryBuilder = chatRoomDao.queryBuilder();
        List<StructureChatRoomDB> structureChatRoomDB = new ArrayList<>();
        try {
            if (chatRoomTypeEnum != ChatRoomTypeEnum.All) {
                queryBuilder.where().eq("ChatRoomTypeEnum", chatRoomTypeEnum);
            }

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureChatRoomDB = queryBuilder.orderBy("LastMessageCreationDate", false).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomDB;
    }

    public void replaceChatRoomAllColumn(int chatRoomID, StructureChatRoomModelRES chatRoomModelRES, ChatRoomModelQuerySaveListener chatRoomModelQuerySaveListener) {
        if (deletChatRoom(chatRoomID)) {
            saveChatRoom(chatRoomModelRES, chatRoomModelQuerySaveListener);
        }

    }

    public void updateChatRoomLastMessageContent(int chatRoomID, String lastMessageContent) {
        UpdateBuilder<StructureChatRoomDB, Integer> updateBuilder = chatRoomDao.updateBuilder();

        try {
            updateBuilder.where().eq("ChatRoomID", chatRoomID);
            updateBuilder.updateColumnValue("LastMessageContent", lastMessageContent);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletChatRoom(int chatRoomID) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureChatRoomDB, Integer> deleteBuilder = chatRoomDao.deleteBuilder();
            deleteBuilder.where().eq("ChatRoomID", chatRoomID);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletChatRoomList() {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureChatRoomDB, Integer> deleteBuilder = chatRoomDao.deleteBuilder();
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletChatRoomMessage(String tempMessageID) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureChatRoomMessageDB, Integer> deleteBuilder = chatRoomMessageDao.deleteBuilder();
            deleteBuilder.where().eq("MessageIDString", tempMessageID);

            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean clearChatRoomList() {
        boolean isClear = false;
        try {
            App.getFarzinDatabaseHelper().clearChatRoomListDB();
            isClear = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isClear;
    }

    public boolean isChatRoomExist(int chatRoomID) {
        boolean existing = false;
        QueryBuilder<StructureChatRoomDB, Integer> queryBuilder = chatRoomDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ChatRoomID", chatRoomID));
            queryBuilder.setCountOf(true);
            long count = chatRoomDao.countOf(queryBuilder.distinct().prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean hasChatRoomData(ChatRoomTypeEnum chatRoomTypeEnum) {
        boolean existing = false;
        QueryBuilder<StructureChatRoomDB, Integer> queryBuilder = chatRoomDao.queryBuilder();
        try {
            if (chatRoomTypeEnum != ChatRoomTypeEnum.All) {
                queryBuilder.where().eq("ChatRoomTypeEnum", chatRoomTypeEnum);
            }
            queryBuilder.setCountOf(true);
            long count = chatRoomDao.countOf(queryBuilder.distinct().prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }
    //--------------------------------*****ChatRoom*****-------------------------------------------------------


    //--------------------------------*****ChatRoomMessage*****-------------------------------------------------

    public List<StructureChatRoomMessageDB> getChatRoomMessageList(long start, long count, String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        List<StructureChatRoomMessageDB> structureChatRoomMessagesDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID);

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureChatRoomMessagesDB = queryBuilder.orderBy("isSendedFromApp", false).orderBy("MessageID", false).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }
    public StructureChatRoomMessageDB getChatMessage( String messageIDString) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB = new StructureChatRoomMessageDB();
        try {
            queryBuilder.where().eq("MessageIDString", messageIDString);

            structureChatRoomMessagesDB = queryBuilder.distinct().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public List<StructureChatRoomMessageDB> getChatRoomMessageQueueList() {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        List<StructureChatRoomMessageDB> structureChatRoomMessagesDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("isSendedFromApp", true);

            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public StructureChatRoomMessageDB getFirstChatRoomMessageQueue() {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB = new StructureChatRoomMessageDB();
        try {
            queryBuilder.where().eq("isSendedFromApp", true).and().lt("errorCount", 3);

            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }


    public List<StructureChatRoomMessageDB> getChatRoomMessageListBetween(int fromID, int toID, String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        List<StructureChatRoomMessageDB> structureChatRoomMessagesDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID).and().between("id", fromID, toID);
            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public void setErrorChatMessageQueue(int id, String strError) {
        UpdateBuilder<StructureChatRoomMessageDB, Integer> updateBuilder = chatRoomMessageDao.updateBuilder();
        try {
            strError = new ChangeXml().saxCharEncoder(strError);
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("strError", strError);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setErrorCountChatMessageQueue(String messageIDString, int errorCount) {
        UpdateBuilder<StructureChatRoomMessageDB, Integer> updateBuilder = chatRoomMessageDao.updateBuilder();
        try {
            updateBuilder.where().eq("MessageIDString", messageIDString);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("errorCount", errorCount);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setErrorToDocumentOperatorQueue(int id, String strError) {
        UpdateBuilder<StructureChatRoomMessageDB, Integer> updateBuilder = chatRoomMessageDao.updateBuilder();
        try {
            strError = new ChangeXml().saxCharEncoder(strError);
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("strError", strError);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int findReplyMessageID(int idCurrentMessage, int replyToMessageID, String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB;
        int id = -1;
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID).and().gt("id", idCurrentMessage).and().eq("MessageID", replyToMessageID);
            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().queryForFirst();
            if (structureChatRoomMessagesDB != null) {
                id = structureChatRoomMessagesDB.getId();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public StructureChatRoomMessageDB getLastMessageFromLocal(String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB = new StructureChatRoomMessageDB();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID);
            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", true).distinct().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public StructureChatRoomMessageDB getNewMessageFromLocal(String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB = new StructureChatRoomMessageDB();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID);
            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public StructureChatRoomMessageDB getLastMessageSend(String chatRoomID) {
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        StructureChatRoomMessageDB structureChatRoomMessagesDB = new StructureChatRoomMessageDB();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID).and().eq("isSendedFromApp", true);
            structureChatRoomMessagesDB = queryBuilder.orderBy("MessageID", false).distinct().queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureChatRoomMessagesDB;
    }

    public boolean hasChatRoomMessageData(String chatRoomID) {
        boolean existing = false;
        QueryBuilder<StructureChatRoomMessageDB, Integer> queryBuilder = chatRoomMessageDao.queryBuilder();
        try {
            queryBuilder.where().eq("ChatRoomIDString", chatRoomID);
            queryBuilder.setCountOf(true);

            long count = chatRoomMessageDao.countOf(queryBuilder.distinct().prepare());
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean deletChatRoomMessageList() {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureChatRoomMessageDB, Integer> deleteBuilder = chatRoomMessageDao.deleteBuilder();
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean clearChatRoomMessageList() {
        boolean isClear = false;
        try {
            App.getFarzinDatabaseHelper().clearChatRoomMessageListDB();
            isClear = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isClear;
    }


    //--------------------------------*****ChatRoomMessage*****-------------------------------------------------------

}
