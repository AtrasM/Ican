package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import org.acra.ACRA;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-06-19 at 16:04 PM
 */

public class FarzinMessageQuery {
    private String Tag = "FarzinMessageQuery";
    private MessageQuerySaveListener messageQuerySaveListener;
    private int sender_user_id;
    private int sender_role_id;
    private String sender_role_name;
    private String sender_first_name;
    private String sender_last_name;
    private String subject;
    private String content;
    private String tempstrDate;
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private List<StructureUserAndRoleDB> structureUserAndRole = new ArrayList<>();
    private ChangeXml changeXml;
    private Status status = Status.WAITING;
    private int MessageID = -1;
    private boolean SendFromApp = false;
    private int AttachmentCount = 0;
    private List<StructureReceiverRES> structureReceiverRES;

    //_______________________***Dao***______________________________

    private Dao<StructureMessageDB, Integer> messageDao = null;
    private Dao<StructureMessageFileDB, Integer> messageFileDao = null;
    private Dao<StructureReceiverDB, Integer> receiverDao = null;
    private Dao<StructureMessageQueueDB, Integer> messageQueueDao = null;


    //_______________________***Dao***______________________________

    public FarzinMessageQuery() {
        changeXml = new ChangeXml();
        initDao();
    }

    private void initDao() {
        try {
            messageDao = App.getFarzinDatabaseHelper().getMessageDao();
            messageFileDao = App.getFarzinDatabaseHelper().getMessageFileDao();
            receiverDao = App.getFarzinDatabaseHelper().getReceiverDao();
            messageQueueDao = App.getFarzinDatabaseHelper().getMessageQueueDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SaveSendLocalMessage(int sender_user_id, int sender_role_id, String sender_role_name, String sender_first_name, String sender_last_name, String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureReceiver, MessageQuerySaveListener messageQuerySaveListener) {
        this.structureUserAndRole.clear();
        this.structureAttaches.clear();
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.sender_role_name = sender_role_name;
        this.sender_first_name = sender_first_name;
        this.sender_last_name = sender_last_name;
        this.subject = subject;
        this.content = content;
        this.structureAttaches = structureAttaches;
        this.structureUserAndRole = structureReceiver;
        this.messageQuerySaveListener = messageQuerySaveListener;
        this.status = Status.WAITING;
        this.tempstrDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
        this.SendFromApp = true;
        this.AttachmentCount = structureAttaches.size();
        new SaveMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Type.SENDED);
    }

    public void SaveMessage(StructureMessageRES structureMessageRES, Type type, Status status, MessageQuerySaveListener messageQuerySaveListener) {
        int ID = structureMessageRES.getID();
        this.structureUserAndRole.clear();
        this.structureAttaches.clear();
        if (IsMessageExist(ID, type)) {
            for (StructureMessageAttachRES structureMessageAttachRES : structureMessageRES.getMessageFiles()) {
                if (structureMessageAttachRES.getFileAsStringBuilder().length() < 256) {
                    File file = new File(structureMessageAttachRES.getFileAsStringBuilder().toString());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            UpdateMessageView(structureMessageRES, type);
            if (type == Type.RECEIVED) {
                getFarzinPrefrences().putReceiveMessageDataSyncDate(structureMessageRES.getSentDate());
                if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {

                    messageQuerySaveListener.onSuccess(new StructureMessageDB());
                } else {
                    messageQuerySaveListener.onExisting();
                }
            } else if (type == Type.SENDED) {
                getFarzinPrefrences().putSendMessageDataSyncDate(structureMessageRES.getSentDate());
                if (!getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                    messageQuerySaveListener.onSuccess(new StructureMessageDB());
                } else {
                    messageQuerySaveListener.onExisting();
                }
            }
        } else {
            this.MessageID = ID;
            this.sender_user_id = structureMessageRES.getSender().getUserID();
            this.sender_role_id = structureMessageRES.getSender().getRoleID();
            this.sender_role_name = structureMessageRES.getSender().getRoleName();
            this.sender_first_name = structureMessageRES.getSender().getFirstName();
            this.sender_last_name = structureMessageRES.getSender().getLastName();
            this.subject = structureMessageRES.getSubject();
            this.tempstrDate = structureMessageRES.getSentDate();
            if (structureMessageRES.getDescription() == null) {
                content = "";
            } else {
                this.content = structureMessageRES.getDescription();
            }
            this.status = status;

            this.AttachmentCount = structureMessageRES.getAttachmentCount();
            for (int i = 0; i < structureMessageRES.getReceivers().size(); i++) {
                StructureReceiverRES receiverRES = structureMessageRES.getReceivers().get(i);
                this.structureUserAndRole.add(new StructureUserAndRoleDB(receiverRES.getUserName(), receiverRES.getUserID(), receiverRES.getRoleID(), receiverRES.getRoleName(), receiverRES.getFirstName(), receiverRES.getLastName()));
            }
            this.structureReceiverRES = structureMessageRES.getReceivers();
            this.messageQuerySaveListener = messageQuerySaveListener;

            new SaveMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, type);
        }

    }

    public void SaveMessageAttachment(int id, ArrayList<StructureMessageAttachRES> structureMessageAttachRES, MessageQuerySaveListener messageQuerySaveListener) {
        this.messageQuerySaveListener = messageQuerySaveListener;

        StructureMessageDB structureMessageDB = GetMessageWithId(id);
        final ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>(structureMessageDB.getMessage_files());
        if (structureMessageFileDBS.size() > 0) {
            DeletMessageFile(structureMessageDB);
        }

        for (int i = 0; i < structureMessageAttachRES.size(); i++) {
            StructureMessageAttachRES MessageAttachRES = structureMessageAttachRES.get(i);
            if (MessageAttachRES.getFileAsStringBuilder() != null && MessageAttachRES.getFileAsStringBuilder().length() > 0) {
                this.structureAttaches.add(new StructureAttach(MessageAttachRES.getFileAsStringBuilder(), MessageAttachRES.getFileName(), MessageAttachRES.getFileExtension(), MessageAttachRES.getDescription()));

            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MessageAttachRES.getFileBinary());
                this.structureAttaches.add(new StructureAttach(stringBuilder, MessageAttachRES.getFileName(), MessageAttachRES.getFileExtension(), MessageAttachRES.getDescription()));

            }
        }
        new SaveMessageFile().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id);
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveMessage extends AsyncTask<Type, Void, Void> {

        @Override
        protected Void doInBackground(Type... types) {
            StructureMessageDB structureMessageDB;
            Date date = new Date();

            if (tempstrDate != null && !tempstrDate.isEmpty()) {
                date = new Date(CustomFunction.StandardizeTheDateFormat(tempstrDate));
            }

            structureMessageDB = new StructureMessageDB(MessageID, sender_user_id, sender_role_id, sender_role_name, sender_first_name, sender_last_name, subject, content, date, status, types[0], AttachmentCount, false);
            try {
                messageDao.create(structureMessageDB);

                if (SendFromApp) {
                    for (int i = 0; i < structureAttaches.size(); i++) {
                        StructureAttach Attach = structureAttaches.get(i);
                        String fileName = CustomFunction.deletExtentionAsFileName(Attach.getName());
                        String filePath = "";

                        if (Attach.getFileAsStringBuilder().length() < 256) {
                            filePath = Attach.getFileAsStringBuilder().toString();
                        } else {
                            filePath = new CustomFunction().saveFileToStorage(Attach.getFileAsStringBuilder(), fileName + CustomFunction.getRandomUUID());
                        }
                        StructureMessageFileDB structureMessageFileDB = new StructureMessageFileDB(structureMessageDB, fileName, filePath, Attach.getFileExtension());
                        messageFileDao.create(structureMessageFileDB);
                        threadSleep();
                    }

                    for (int i = 0; i < structureUserAndRole.size(); i++) {
                        StructureReceiverDB structureReceiverDB = new StructureReceiverDB(structureMessageDB, structureUserAndRole.get(i).getUser_ID(), structureUserAndRole.get(i).getRole_ID(), structureUserAndRole.get(i).getRoleName(), structureUserAndRole.get(i).getFirstName(), structureUserAndRole.get(i).getLastName(), structureUserAndRole.get(i).getUserName(), "", true, "");
                        receiverDao.create(structureReceiverDB);
                        threadSleep();
                    }
                    StructureMessageQueueDB structureMessageQueueDB = new StructureMessageQueueDB(sender_user_id, sender_role_id, QueueStatus.WAITING, structureMessageDB);
                    messageQueueDao.create(structureMessageQueueDB);
                } else {
                    if (tempstrDate != null && !tempstrDate.isEmpty()) {
                        if (types[0] == Type.SENDED) {
                            getFarzinPrefrences().putSendMessageDataSyncDate(tempstrDate);
                        } else if (types[0] == Type.RECEIVED) {
                            getFarzinPrefrences().putReceiveMessageDataSyncDate(tempstrDate);
                        }
                    }
                    for (int i = 0; i < structureUserAndRole.size(); i++) {
                        StructureReceiverDB structureReceiverDB = new StructureReceiverDB(structureMessageDB, structureReceiverRES.get(i), structureUserAndRole.get(i));
                        receiverDao.create(structureReceiverDB);
                        threadSleep();
                    }
                }
                App.needToReGetMessageList = true;
                messageQuerySaveListener.onSuccess(GetMessageWithMainID(structureMessageDB.getMain_id()));

            } catch (SQLException e) {
                e.printStackTrace();
                messageQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);

                return null;
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveMessageFile extends AsyncTask<Integer, Void, Void> {
        @Override

        protected Void doInBackground(Integer... id) {
            StructureMessageDB structureMessageDB = GetMessageWithId(id[0]);
            structureMessageDB.setFilesDownloaded(true);
            structureMessageDB.setAttachmentCount(structureAttaches.size());
            UpdateMessageFileds(structureMessageDB);
            try {
                for (int i = 0; i < structureAttaches.size(); i++) {
                    StructureAttach Attach = structureAttaches.get(i);
                    String fileName = CustomFunction.deletExtentionAsFileName(Attach.getName());
                    String filePath = "";
                    if (Attach.getFileAsStringBuilder().length() < 256) {
                        filePath = Attach.getFileAsStringBuilder().toString();
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(Attach.getFileAsStringBuilder(), fileName + CustomFunction.getRandomUUID());
                    }
                    StructureMessageFileDB structureMessageFileDB = new StructureMessageFileDB(structureMessageDB, fileName, filePath, Attach.getFileExtension());
                    messageFileDao.create(structureMessageFileDB);
                    threadSleep();
                }
                //UpdateMessageFileDownloaded(id[0], true);

                messageQuerySaveListener.onSuccess(GetMessageWithMainID(structureMessageDB.getMain_id()));
            } catch (SQLException e) {
                e.printStackTrace();
                messageQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }
            return null;
        }
    }

    private void threadSleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }

    public List<StructureMessageQueueDB> getMessageQueue(int user_id, int role_id, QueueStatus queueStatus) {

        QueryBuilder<StructureMessageQueueDB, Integer> queryBuilder = messageQueueDao.queryBuilder();
        List<StructureMessageQueueDB> structureMessageQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("sender_user_id", user_id).and().eq("sender_role_id", role_id).and().eq("queueStatus", queueStatus);
            structureMessageQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
        return structureMessageQueueDBS;
    }

    public List<StructureMessageQueueDB> getMessageQueue(int user_id, int role_id) {
        QueryBuilder<StructureMessageQueueDB, Integer> queryBuilder = messageQueueDao.queryBuilder();
        List<StructureMessageQueueDB> structureMessageQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("sender_user_id", user_id).and().eq("sender_role_id", role_id);
            structureMessageQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageQueueDBS;
    }


    public void setErrorToMessageQueue(int id, String strError) {
        UpdateBuilder<StructureMessageQueueDB, Integer> updateBuilder = messageQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("strError", strError);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMessageQueueStatus(int id, QueueStatus queueStatus) {
        UpdateBuilder<StructureMessageQueueDB, Integer> updateBuilder = messageQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", queueStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateMessageIsNewStatus(int id, boolean isNew) {
        UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("isNew", isNew);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllMessageIsNewStatusToFalse() {
        UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
        try {
            updateBuilder.updateColumnValue("isNew", false);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getNewMessageCount() {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("isNew", true).and().eq("status", Status.UnRead);
            count = messageDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getMessageQueueNotSendedCount() {

        QueryBuilder<StructureMessageQueueDB, Integer> queryBuilder = messageQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().ne("queueStatus", QueueStatus.SENDED);
            count = messageQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public StructureMessageDB getNewMessage() {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        StructureMessageDB structureMessageDB = new StructureMessageDB();
        try {
            queryBuilder.where().eq("isNew", true).and().eq("status", Status.UnRead);
            queryBuilder.orderBy("sent_date", false);
            structureMessageDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDB;
    }

    public StructureMessageDB GetMessageWithId(int ID) {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        StructureMessageDB structureMessageDB = new StructureMessageDB();
        try {
            queryBuilder.where().eq("id", ID);
            //queryBuilder.where().eq("id", id).and().ne("status", Status.STOPED);
            queryBuilder.orderBy("sent_date", false);
            structureMessageDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDB;
    }


    public List<StructureMessageDB> GetMessagesWithMainID(int mainId) {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        List<StructureMessageDB> structureMessagesDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("main_id", mainId).and().ne("status", Status.STOPED);
            queryBuilder.orderBy("sent_date", false);
            structureMessagesDB = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessagesDB;
    }

    public StructureMessageDB GetMessageWithMainID(int mainId) {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        StructureMessageDB structureMessageDB = new StructureMessageDB();
        try {
            queryBuilder.where().eq("main_id", mainId).and().ne("status", Status.STOPED);
            queryBuilder.orderBy("sent_date", false);
            structureMessageDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDB;
    }

    public List<StructureMessageDB> GetReceiveMessages(int user_id, Status status, long start, long count) {
        QueryBuilder<StructureMessageDB, Integer> messageQB = messageDao.queryBuilder();
        QueryBuilder<StructureReceiverDB, Integer> receiverQB = receiverDao.queryBuilder();
        List<StructureMessageDB> structureMessageDBS = new ArrayList<>();
        try {
            receiverQB.where().eq("user_id", user_id);
            messageQB.join(receiverQB);
            //structureMessageDBS = messageQB.query();
            if (status != null) {
                messageQB.where().eq("status", status).and().eq("type", Type.RECEIVED);
            } else {
                messageQB.where().ne("status", Status.WAITING).and().eq("type", Type.RECEIVED);
            }
            //structureMessageDBS = messageQB.query();
            if (count > 0) {
                messageQB.offset(start).limit(count);
            }
            structureMessageDBS = messageQB.distinct().orderBy("sent_date", false).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDBS;
    }


    public List<StructureMessageDB> GetSendMessages(int user_id, Status status, long start, long count) {
        QueryBuilder<StructureMessageDB, Integer> messageQB = messageDao.queryBuilder();
        QueryBuilder<StructureReceiverDB, Integer> receiverQB = receiverDao.queryBuilder();
        Where<StructureMessageDB, Integer> where = messageQB.where();
        List<StructureMessageDB> structureMessageDBS = new ArrayList<>();
        try {

            if (status != null) {
                if (status == Status.READ) {
                    receiverQB.where().eq("is_read", true);
                } else {
                    receiverQB.where().eq("is_read", false);
                }
                messageQB.join(receiverQB);
            }
            //where.and(where.eq("sender_user_id", user_id), where.or(where.ne("status", Status.STOPED), where.ne("status", Status.WAITING)));
            where.eq("sender_user_id", user_id).and().eq("type", Type.SENDED).and().ne("status", Status.STOPED).and().ne("status", Status.WAITING);

            messageQB.setWhere(where);
            if (count > 0) {
                messageQB.offset(start).limit(count);
            }
            structureMessageDBS = messageQB.orderBy("sent_date", false).query();
            //structureMessageDBS = messageQB.orderBy("sent_date", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDBS;
    }

    public long GetMessageCount(int user_id, Type type, Status status) {
        long count = 0;
        if (type == Type.RECEIVED) {
            count = GetReceiveMessages(user_id, status, 0, -1).size();
        } else {
            count = GetSendMessages(user_id, status, 0, -1).size();
        }
        return count;
    }


    public boolean DeletMessageQueue(int messageQueueId, StructureMessageDB MessageDB) {
        DeletMessageQueueRowWithId(messageQueueId);
        //StructureMessageDB messageDB =GetMessageWithId(MessageDB.getId());
        DeletMessageFile(MessageDB);
        DeletMessageRowWithId(MessageDB.getId());
        return true;
    }

    public boolean DeletMessageQueueRowWithId(int id) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureMessageQueueDB, Integer> deleteBuilder = messageQueueDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean DeletMessageReceiver(StructureMessageDB MessageDB) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureReceiverDB, Integer> deleteBuilder = receiverDao.deleteBuilder();
            deleteBuilder.where().eq("message_id", MessageDB);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean DeletMessageFile(StructureMessageDB message) {
        boolean isDelet = false;
        ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>(message.getMessage_files());
        for (StructureMessageFileDB structureMessageFileDB : structureMessageFileDBS) {
            String fileName = structureMessageFileDB.getFile_name();
            String filePath = structureMessageFileDB.getFile_path();
            File file = new File(filePath);
            file.delete();
        }
        try {
            DeleteBuilder<StructureMessageFileDB, Integer> deleteBuilder = messageFileDao.deleteBuilder();
            deleteBuilder.where().eq("message_id", message);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean DeletMessageRowWithId(int id) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureMessageDB, Integer> deleteBuilder = messageDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }


    public void UpdateMessageStatus(int id, Status status) {
        try {
            UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("status", status);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void UpdateAllNewMessageStatusToUnreadStatus() {
        try {
            UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
            updateBuilder.where().eq("status", Status.IsNew);
            updateBuilder.updateColumnValue("status", Status.UnRead);
            updateBuilder.update();
            Log.i("Notif", "Message Status change to UnRead");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void UpdateMessageID(int CurentID, int NewID) {
        try {
            UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
            updateBuilder.where().eq("id", CurentID);
            updateBuilder.updateColumnValue("main_id", NewID);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateMessageFileDownloaded(int id, boolean isFilesDownloaded) {
        try {
            UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("isFilesDownloaded", isFilesDownloaded);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateMessageFileds(StructureMessageDB structureMessageDB) {
        try {
            UpdateBuilder<StructureMessageDB, Integer> updateBuilder = messageDao.updateBuilder();
            updateBuilder.where().eq("id", structureMessageDB.getId());
            updateBuilder.updateColumnValue("isFilesDownloaded", structureMessageDB.isFilesDownloaded());
            updateBuilder.updateColumnValue("AttachmentCount", structureMessageDB.getAttachmentCount());
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateMessageView(StructureMessageRES MessageRES, Type type) {
        try {
            ArrayList<StructureAttach> tempStructureAttaches = new ArrayList<>();
            UpdateBuilder<StructureMessageDB, Integer> updateBuilderMessage = messageDao.updateBuilder();
            updateBuilderMessage.where().eq("main_id", MessageRES.getID());
            if (MessageRES.isRead()) {
                status = Status.READ;
            } else {
                status = Status.UnRead;
            }
            updateBuilderMessage.updateColumnValue("status", status);
            updateBuilderMessage.update();
            if (type != Type.RECEIVED) {
                StructureMessageDB MessageDB = GetMessageWithMainID(MessageRES.getID());
                boolean isDelet = DeletMessageReceiver(MessageDB);
                for (StructureReceiverRES receiverRES : MessageRES.getReceivers()) {
                    String ViewDate = "";
                    if (receiverRES.getViewDate() != null) {
                        ViewDate = "" + receiverRES.getViewDate();
                    }
                    StructureReceiverDB structureReceiverDB = new StructureReceiverDB(MessageDB, receiverRES.getUserID(), receiverRES.getRoleID(), receiverRES.getRoleName(), receiverRES.getFirstName(), receiverRES.getLastName(), receiverRES.getUserName(), "", receiverRES.isRead(), ViewDate);
                    receiverDao.createOrUpdate(structureReceiverDB);
                    // receiverDBS.add(structureReceiverDB);
                }
            }

            new FarzinPrefrences().init().putMessageViewSyncDate(CustomFunction.getCurentLocalDateTimeAsStringFormat());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean IsMessageExist(int MainId, Type type) {
        boolean existing = false;
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("main_id", MainId).and().eq("type", type));
            queryBuilder.setCountOf(true);
            long count = messageDao.countOf(queryBuilder.distinct().prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsMessageFileExist(StructureMessageDB message) {
        boolean existing = false;
        QueryBuilder<StructureMessageFileDB, Integer> queryBuilder = messageFileDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("message_id", message));
            queryBuilder.setCountOf(true);
            long count = messageFileDao.countOf(queryBuilder.distinct().prepare());
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}






