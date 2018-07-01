package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-06-19 at 16:04 PM
 */

public class FarzinMessageQuery {
    private String Tag = "FarzinMessageQuery";
    private MessageQuerySaveListener messageQuerySaveListener;
    private String sender_user_id;
    private String sender_role_id;
    private String subject;
    private String content;
    private ArrayList<StructureAttach> structureAttaches;
    private List<StructureUserAndRoleDB> structureReceiver;
    //_______________________***Dao***______________________________

    private Dao<StructureMessageDB, Integer> messageDao = null;
    private Dao<StructureMessageFileDB, Integer> messageFileDao = null;
    private Dao<StructureReceiverDB, Integer> receiverDao = null;
    private Dao<StructureMessageQueueDB, Integer> messageQueueDao = null;

    //_______________________***Dao***______________________________

    public FarzinMessageQuery() {
        initDao();
    }

    private void initDao() {
        try {
            messageDao = App.getFarzinDatabaseHelper().getMessageDao();
            messageFileDao = App.getFarzinDatabaseHelper().getGetMessageFileDao();
            receiverDao = App.getFarzinDatabaseHelper().getGetReceiverDao();
            messageQueueDao = App.getFarzinDatabaseHelper().getGetMessageQueueDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void SaveMessage(String sender_user_id, String sender_role_id, String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureReceiver, MessageQuerySaveListener messageQuerySaveListener) {
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.subject = subject;
        this.content = content;
        this.structureAttaches = structureAttaches;
        this.structureReceiver = structureReceiver;
        this.messageQuerySaveListener = messageQuerySaveListener;
        new SaveMessage().execute();
    }


    @SuppressLint("StaticFieldLeak")
    private class SaveMessage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            StructureMessageDB structureMessageDB;
            content = CustomFunction.AddXmlCData(content);
            structureMessageDB = new StructureMessageDB(sender_user_id, sender_role_id, subject, content);
            try {
                messageDao.create(structureMessageDB);

                for (int i = 0; i < structureAttaches.size(); i++) {
                    StructureAttach Attach = structureAttaches.get(i);
                    StructureMessageFileDB structureMessageFileDB = new StructureMessageFileDB(structureMessageDB, Attach.getName(), Attach.getBase64File(), Attach.getFileExtension());

                    messageFileDao.create(structureMessageFileDB);
                    threadSleep();
                }
                for (int i = 0; i < structureReceiver.size(); i++) {
                    StructureUserAndRoleDB UserAndRole = structureReceiver.get(i);
                    StructureReceiverDB structureReceiverDB = new StructureReceiverDB(structureMessageDB, UserAndRole.getUser_ID(), Integer.parseInt(UserAndRole.getRole_ID()), UserAndRole.getUserName(), UserAndRole.getNativeID());
                    receiverDao.create(structureReceiverDB);
                    threadSleep();
                }

                StructureMessageQueueDB structureMessageQueueDB = new StructureMessageQueueDB(sender_user_id, sender_role_id, structureMessageDB);
                messageQueueDao.create(structureMessageQueueDB);
                messageQuerySaveListener.onSuccess();

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

    List<StructureMessageQueueDB> getMessageQueue(int user_id, int role_id) {
        QueryBuilder<StructureMessageQueueDB, Integer> queryBuilder = messageQueueDao.queryBuilder();
        List<StructureMessageQueueDB> structureMessageQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("sender_user_id", user_id).and().eq("sender_role_id", role_id);
            structureMessageQueueDBS = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureMessageQueueDBS;
    }

    boolean DeletMessageQueueRowWithId(int id) {
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

    public List<StructureMessageDB> getMessage(int user_id, int role_id) {
        QueryBuilder<StructureMessageDB, Integer> queryBuilder = messageDao.queryBuilder();

        List<StructureMessageDB> structureMessageDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("sender_user_id", user_id).and().eq("sender_role_id", role_id);
            structureMessageDBS = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
       /* try {
            structureMessageDBS = messageDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        return structureMessageDBS;
    }
}






