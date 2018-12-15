package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-06-19 at 16:04 PM
 */

public class FarzinMessageQuery {
    private String Tag = "FarzinMessageQuery";
    private MessageQuerySaveListener messageQuerySaveListener;
    private int sender_user_id;
    private int sender_role_id;
    private String subject;
    private String content;
    private String Date;
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private List<StructureUserAndRoleDB> structureUserAndRole = new ArrayList<>();
    private ChangeXml changeXml;
    private Status status = Status.WAITING;
    private int MessageID = -1;
    private boolean SendFromApp = false;
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

    public void SaveSendLocalMessage(int sender_user_id, int sender_role_id, String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureReceiver, MessageQuerySaveListener messageQuerySaveListener) {
        this.structureUserAndRole.clear();
        this.structureAttaches.clear();
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.subject = subject;
        this.content = content;
        this.structureAttaches = structureAttaches;
        this.structureUserAndRole = structureReceiver;
        this.messageQuerySaveListener = messageQuerySaveListener;
        this.status = Status.WAITING;
        this.Date = CustomFunction.getCurentDateTimeAsDateFormat(SimpleDateFormatEnum.DateTime_yyyy_MM_dd_hh_mm_ss.getValue()).toString();
        this.SendFromApp = true;
        new SaveMessage().execute(Type.SENDED);
    }

    public void SaveMessage(StructureMessageRES structureMessageRES, Type type, Status status, MessageQuerySaveListener messageQuerySaveListener) {
        int ID = structureMessageRES.getID();
        this.structureUserAndRole.clear();
        this.structureAttaches.clear();
        if (IsMessageExist(ID)) {
            UpdateMessageView(structureMessageRES, type);
            messageQuerySaveListener.onExisting();
        } else {
            this.MessageID = ID;
            this.sender_user_id = structureMessageRES.getSender().getUserID();
            this.sender_role_id = structureMessageRES.getSender().getRoleID();
            this.subject = structureMessageRES.getSubject();
            this.Date = structureMessageRES.getSentDate();
            if (structureMessageRES.getDescription() == null) {
                content = "";
            } else {
                this.content = changeXml.CharDecoder(structureMessageRES.getDescription());
            }

            this.status = status;

            for (int i = 0; i < structureMessageRES.getMessageFiles().size(); i++) {
                StructureMessageAttachRES MessageAttachRES = structureMessageRES.getMessageFiles().get(i);
                this.structureAttaches.add(new StructureAttach(MessageAttachRES.getFileBinary(), MessageAttachRES.getFileName(), MessageAttachRES.getFileExtension(), MessageAttachRES.getDescription()));
            }

            for (int i = 0; i < structureMessageRES.getReceivers().size(); i++) {
                StructureReceiverRES receiverRES = structureMessageRES.getReceivers().get(i);
                this.structureUserAndRole.add(new StructureUserAndRoleDB(receiverRES.getUserName(), receiverRES.getUserID(), receiverRES.getRoleID()));
            }
            this.structureReceiverRES = structureMessageRES.getReceivers();
            this.messageQuerySaveListener = messageQuerySaveListener;

            new SaveMessage().execute(type);
        }

    }


    @SuppressLint("StaticFieldLeak")
    private class SaveMessage extends AsyncTask<Type, Void, Void> {

        @Override
        protected Void doInBackground(Type... types) {
            StructureMessageDB structureMessageDB;
            content = CustomFunction.AddXmlCData(content);
            java.util.Date date = new Date(CustomFunction.StandardizeTheDateFormat(Date));
            structureMessageDB = new StructureMessageDB(MessageID, sender_user_id, sender_role_id, subject, content, date, status);
            try {
                messageDao.create(structureMessageDB);

                for (int i = 0; i < structureAttaches.size(); i++) {
                    StructureAttach Attach = structureAttaches.get(i);
                    StructureMessageFileDB structureMessageFileDB = new StructureMessageFileDB(structureMessageDB, Attach.getName(), Attach.getBase64File(), Attach.getFileExtension());

                    messageFileDao.create(structureMessageFileDB);
                    threadSleep();
                }
                if (SendFromApp) {
                    for (int i = 0; i < structureUserAndRole.size(); i++) {
                        StructureReceiverDB structureReceiverDB = new StructureReceiverDB(structureMessageDB, structureUserAndRole.get(i).getUser_ID(), structureUserAndRole.get(i).getRole_ID(), structureUserAndRole.get(i).getUserName(), "", true, "");
                        receiverDao.create(structureReceiverDB);
                        threadSleep();
                    }
                    StructureMessageQueueDB structureMessageQueueDB = new StructureMessageQueueDB(sender_user_id, sender_role_id, status, structureMessageDB);
                    messageQueueDao.create(structureMessageQueueDB);
                } else {
                    for (int i = 0; i < structureUserAndRole.size(); i++) {
                        StructureReceiverDB structureReceiverDB = new StructureReceiverDB(structureMessageDB, structureReceiverRES.get(i), structureUserAndRole.get(i));
                        receiverDao.create(structureReceiverDB);
                        threadSleep();
                    }
                }
               /* if (types != null) {
                    if (types[0] == Type.RECEIVED) {
                        if (App.fragmentMessageList != null) {
                            final ArrayList<StructureMessageDB> structureMessagesDB = new ArrayList<>();
                            structureMessagesDB.add(GetMessage(structureMessageDB.getMain_id()));
                            if (App.CurentActivity != null) {
                                App.CurentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        App.fragmentMessageList.AddReceiveNewMessage(structureMessagesDB);
                                        // UpdateAllNewMessageStatusToUnreadStatus();
                                    }
                                });

                            }

                        }
                    }
                }*/
                messageQuerySaveListener.onSuccess(GetMessage(structureMessageDB.getMain_id()));

            } catch (SQLException e) {
                e.printStackTrace();
                messageQuerySaveListener.onFailed(e.toString());
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

    public List<StructureMessageQueueDB> getMessageQueue(int user_id, int role_id, Status status) {

        QueryBuilder<StructureMessageQueueDB, Integer> queryBuilder = messageQueueDao.queryBuilder();
        List<StructureMessageQueueDB> structureMessageQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("sender_user_id", user_id).and().eq("sender_role_id", role_id).and().eq("status", status);
            structureMessageQueueDBS = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageQueueDBS;
    }

    public StructureMessageDB GetMessage(int ID) {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        StructureMessageDB structureMessageDB = new StructureMessageDB();
        try {
            queryBuilder.where().eq("main_id", ID).and().ne("status", Status.STOPED);
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
                messageQB.where().eq("status", status);
            }
            //structureMessageDBS = messageQB.query();
            if (count > 0) {
                messageQB.offset(start).limit(count);
            }

            structureMessageDBS = messageQB.orderBy("sent_date", false).query();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageDBS;
    }

    public List<StructureMessageDB> GetSendMessages(int user_id, Status status, long start, long count) {
        QueryBuilder<StructureMessageDB, Integer> messageQB = messageDao.queryBuilder();
        QueryBuilder<StructureReceiverDB, Integer> receiverQB = receiverDao.queryBuilder();
        List<StructureMessageDB> structureMessageDBS = new ArrayList<>();
        try {
            if (status != null) {
                messageQB.where().eq("status", status).and().eq("sender_user_id", user_id).and().ne("status", Status.STOPED);
                ;
            } else {
                messageQB.where().eq("sender_user_id", user_id);
            }

            if (count > 0) {
                messageQB.offset(start).limit(count);
            }
            structureMessageDBS = messageQB.orderBy("sent_date", false).query();
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

    public void UpdateMessageQueueStatus(int id, Status status) {
        try {
            UpdateBuilder<StructureMessageQueueDB, Integer> updateBuilder = messageQueueDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("status", status);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void UpdateMessageView(StructureMessageRES MessageRES, Type type) {
        try {
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
                StructureMessageDB MessageDB = GetMessage(MessageRES.getID());
                DeletMessageReceiver(MessageDB);
                for (StructureReceiverRES receiverRES : MessageRES.getReceivers()) {
                    String ViewDate = "";
                    if (receiverRES.getViewDate() != null) {
                        ViewDate = "" + receiverRES.getViewDate();
                    }
                    StructureReceiverDB structureReceiverDB = new StructureReceiverDB(MessageDB, receiverRES.getUserID(), receiverRES.getRoleID(), receiverRES.getUserName(), "", receiverRES.isRead(), ViewDate);
                    receiverDao.createOrUpdate(structureReceiverDB);
                    // receiverDBS.add(structureReceiverDB);
                }
            }

            StructureMessageDB MessageDB2 = GetMessage(MessageRES.getID());
            new FarzinPrefrences().init().putMessageViewSyncDate(CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.DateTime_yyyy_MM_dd_hh_mm_ss.getValue()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean IsMessageExist(int ID) {
        boolean existing = false;
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("main_id", ID));
            queryBuilder.setCountOf(true);
            long count = messageDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

}






