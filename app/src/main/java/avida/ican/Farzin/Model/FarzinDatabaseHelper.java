package avida.ican.Farzin.Model;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureCreatDocumentQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Model.Structure.Database.StructureLogDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Ican.App;

/**
 * Created by AtrasVida on 2018-04-18 at 5:36 PM
 */

public class FarzinDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Ican_Farzin.db";
    private static final int DATABASE_VERSION = 4;
    private Dao<StructureUserAndRoleDB, Integer> mGetUserAndRoleListDao = null;
    private Dao<StructureMessageDB, Integer> mMessageDao = null;
    private Dao<StructureMessageFileDB, Integer> mMessageFileDao = null;
    private Dao<StructureReceiverDB, Integer> mReceiverDao = null;
    private Dao<StructureMessageQueueDB, Integer> mMessageQueueDao = null;
    private Dao<StructureInboxDocumentDB, Integer> mCartableDocumentDao = null;
    private Dao<StructureHameshDB, Integer> mHameshDao = null;
    private Dao<StructureCartableHistoryDB, Integer> mHistoryDao = null;
    private Dao<StructureZanjireMadrakFileDB, Integer> mZanjireMadrakFileDao = null;
    private Dao<StructureCartableDocumentActionsDB, Integer> mCartableDocumentActionsDao = null;
    private Dao<StructureCartableDocumentContentDB, Integer> mDocumentContentDao = null;
    private Dao<StructureCartableDocumentSignatureDB, Integer> mCartableDocumentSignatureDao = null;
    private Dao<StructureUserRoleDB, Integer> mStructureUserRoleDBDao = null;
    private Dao<StructureDocumentOperatorsQueueDB, Integer> mStructureDocumentOperatorsQueueDBDao = null;
    private Dao<StructureDocumentAttachFileQueueDB, Integer> mStructureDocumentAttachFileQueueDBDao = null;
    private Dao<StructureCreatDocumentQueueDB, Integer> mStructureCreatDocumentQueueDBDao = null;
    private Dao<StructureZanjireMadrakEntityDB, Integer> mStructureZanjireMadrakEntityDBDao = null;
    private Dao<StructureLogDB, Integer> mStructureLogDBDao = null;
    private Dao<StructureChatRoomListDB, Integer> mChatRoomListDBDao = null;
    private Dao<StructureChatRoomMessageDB, Integer> mChatRoomMessageListDBDao = null;

    private static FarzinDatabaseHelper mInstance = null;

    public static FarzinDatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new FarzinDatabaseHelper();
        }
        return mInstance;
    }

    private FarzinDatabaseHelper() {
        super(App.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            //db.delete("GetUserAndRoleLists",null, null);
            TableUtils.createTable(connectionSource, StructureUserAndRoleDB.class);
            TableUtils.createTable(connectionSource, StructureMessageDB.class);
            TableUtils.createTable(connectionSource, StructureMessageFileDB.class);
            TableUtils.createTable(connectionSource, StructureReceiverDB.class);
            TableUtils.createTable(connectionSource, StructureMessageQueueDB.class);
            TableUtils.createTable(connectionSource, StructureInboxDocumentDB.class);
            TableUtils.createTable(connectionSource, StructureHameshDB.class);
            TableUtils.createTable(connectionSource, StructureCartableHistoryDB.class);
            TableUtils.createTable(connectionSource, StructureZanjireMadrakFileDB.class);
            TableUtils.createTable(connectionSource, StructureCartableDocumentActionsDB.class);
            TableUtils.createTable(connectionSource, StructureCartableDocumentSignatureDB.class);
            TableUtils.createTable(connectionSource, StructureCartableDocumentContentDB.class);
            TableUtils.createTable(connectionSource, StructureUserRoleDB.class);
            TableUtils.createTable(connectionSource, StructureDocumentOperatorsQueueDB.class);
            TableUtils.createTable(connectionSource, StructureDocumentAttachFileQueueDB.class);
            TableUtils.createTable(connectionSource, StructureCreatDocumentQueueDB.class);
            TableUtils.createTable(connectionSource, StructureZanjireMadrakEntityDB.class);
            TableUtils.createTable(connectionSource, StructureLogDB.class);
            TableUtils.createTable(connectionSource, StructureChatRoomListDB.class);
            TableUtils.createTable(connectionSource, StructureChatRoomMessageDB.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            if (oldVersion < 3) {
                Dao messageDao = getMessageDao();
                Dao inboxDocumentDao = getCartableDocumentDao();
                messageDao.executeRaw("ALTER TABLE 'message' ADD COLUMN isNew Boolean");
                inboxDocumentDao.executeRaw("ALTER TABLE 'cartable_inbox_document' ADD COLUMN isNew Boolean");
            }
    /*      TableUtils.dropTable(connectionSource, StructureUserAndRoleDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageFileDB.class, true);
            TableUtils.dropTable(connectionSource, StructureReceiverDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureInboxDocumentDB.class, true);
            TableUtils.dropTable(connectionSource, StructureHameshDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableHistoryDB.class, true);
            TableUtils.dropTable(connectionSource, StructureZanjireMadrakFileDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentActionsDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentSignatureDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentContentDB.class, true);
            TableUtils.dropTable(connectionSource, StructureUserRoleDB.class, true);
            TableUtils.dropTable(connectionSource, StructureDocumentOperatorsQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureDocumentAttachFileQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCreatDocumentQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureZanjireMadrakEntityDB.class, true);
            onCreate(db, connectionSource);*/
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<StructureUserAndRoleDB, Integer> getUserAndRoleListDao() throws SQLException {
        if (mGetUserAndRoleListDao == null) {
            mGetUserAndRoleListDao = getDao(StructureUserAndRoleDB.class);
        }

        return mGetUserAndRoleListDao;
    }

    public Dao<StructureInboxDocumentDB, Integer> getCartableDocumentDao() throws SQLException {
        if (mCartableDocumentDao == null) {
            mCartableDocumentDao = getDao(StructureInboxDocumentDB.class);
        }

        return mCartableDocumentDao;
    }

    public Dao<StructureMessageDB, Integer> getMessageDao() throws SQLException {
        if (mMessageDao == null) {
            mMessageDao = getDao(StructureMessageDB.class);
        }

        return mMessageDao;
    }

    public Dao<StructureMessageFileDB, Integer> getMessageFileDao() throws SQLException {
        if (mMessageFileDao == null) {
            mMessageFileDao = getDao(StructureMessageFileDB.class);
        }

        return mMessageFileDao;
    }

    public Dao<StructureReceiverDB, Integer> getReceiverDao() throws SQLException {
        if (mReceiverDao == null) {
            mReceiverDao = getDao(StructureReceiverDB.class);
        }

        return mReceiverDao;
    }

    public Dao<StructureMessageQueueDB, Integer> getMessageQueueDao() throws SQLException {
        if (mMessageQueueDao == null) {
            mMessageQueueDao = getDao(StructureMessageQueueDB.class);
        }

        return mMessageQueueDao;
    }

    public Dao<StructureHameshDB, Integer> getHameshDao() throws SQLException {
        if (mHameshDao == null) {
            mHameshDao = getDao(StructureHameshDB.class);
        }

        return mHameshDao;
    }

    public Dao<StructureCartableHistoryDB, Integer> getHistoryDao() throws SQLException {
        if (mHistoryDao == null) {
            mHistoryDao = getDao(StructureCartableHistoryDB.class);
        }

        return mHistoryDao;
    }

    public Dao<StructureZanjireMadrakFileDB, Integer> getZanjireMadrakDao() throws SQLException {
        if (mZanjireMadrakFileDao == null) {
            mZanjireMadrakFileDao = getDao(StructureZanjireMadrakFileDB.class);
        }

        return mZanjireMadrakFileDao;
    }

    public Dao<StructureCartableDocumentActionsDB, Integer> getDocumentActionsDao() throws SQLException {
        if (mCartableDocumentActionsDao == null) {
            mCartableDocumentActionsDao = getDao(StructureCartableDocumentActionsDB.class);
        }
        return mCartableDocumentActionsDao;

    }

    public Dao<StructureCartableDocumentSignatureDB, Integer> getCartableDocumentSignatureDao() throws SQLException {
        if (mCartableDocumentSignatureDao == null) {
            mCartableDocumentSignatureDao = getDao(StructureCartableDocumentSignatureDB.class);
        }
        return mCartableDocumentSignatureDao;
    }

    public Dao<StructureCartableDocumentContentDB, Integer> getDocumentContentDao() throws SQLException {
        if (mDocumentContentDao == null) {
            mDocumentContentDao = getDao(StructureCartableDocumentContentDB.class);
        }
        return mDocumentContentDao;
    }

    public Dao<StructureUserRoleDB, Integer> getUserRolesDBDao() throws SQLException {
        if (mStructureUserRoleDBDao == null) {
            mStructureUserRoleDBDao = getDao(StructureUserRoleDB.class);
        }
        return mStructureUserRoleDBDao;
    }

    public Dao<StructureDocumentOperatorsQueueDB, Integer> getDocumentOperatorsQueueDBDao() throws SQLException {
        if (mStructureDocumentOperatorsQueueDBDao == null) {
            mStructureDocumentOperatorsQueueDBDao = getDao(StructureDocumentOperatorsQueueDB.class);
        }
        return mStructureDocumentOperatorsQueueDBDao;
    }

    public Dao<StructureDocumentAttachFileQueueDB, Integer> getDocumentAttachFileDao() throws SQLException {
        if (mStructureDocumentAttachFileQueueDBDao == null) {
            mStructureDocumentAttachFileQueueDBDao = getDao(StructureDocumentAttachFileQueueDB.class);
        }
        return mStructureDocumentAttachFileQueueDBDao;
    }

    public Dao<StructureCreatDocumentQueueDB, Integer> getCreatDocumentDao() throws SQLException {
        if (mStructureCreatDocumentQueueDBDao == null) {
            mStructureCreatDocumentQueueDBDao = getDao(StructureCreatDocumentQueueDB.class);
        }
        return mStructureCreatDocumentQueueDBDao;
    }

    public Dao<StructureZanjireMadrakEntityDB, Integer> getZanjireMadrakEntityDao() throws SQLException {
        if (mStructureZanjireMadrakEntityDBDao == null) {
            mStructureZanjireMadrakEntityDBDao = getDao(StructureZanjireMadrakEntityDB.class);
        }
        return mStructureZanjireMadrakEntityDBDao;
    }

    public Dao<StructureLogDB, Integer> getStructureLogDBDao() throws SQLException {
        if (mStructureLogDBDao == null) {
            mStructureLogDBDao = getDao(StructureLogDB.class);
        }
        return mStructureLogDBDao;
    }

    public Dao<StructureChatRoomListDB, Integer> getChatRoomListDBDao() throws SQLException {
        if (mChatRoomListDBDao == null) {
            mChatRoomListDBDao = getDao(StructureChatRoomListDB.class);
        }
        return mChatRoomListDBDao;
    }

    public Dao<StructureChatRoomMessageDB, Integer> getChatRoomMessageListDBDao() throws SQLException {
        if (mChatRoomMessageListDBDao == null) {
            mChatRoomMessageListDBDao = getDao(StructureChatRoomMessageDB.class);
        }
        return mChatRoomMessageListDBDao;
    }

    @Override
    public void close() {
        mGetUserAndRoleListDao = null;
        mMessageDao = null;
        mMessageFileDao = null;
        mReceiverDao = null;
        mMessageQueueDao = null;
        mCartableDocumentDao = null;
        mHameshDao = null;
        mHistoryDao = null;
        mZanjireMadrakFileDao = null;
        mCartableDocumentActionsDao = null;
        mCartableDocumentSignatureDao = null;
        mDocumentContentDao = null;
        mStructureUserRoleDBDao = null;
        mStructureDocumentOperatorsQueueDBDao = null;
        mStructureDocumentAttachFileQueueDBDao = null;
        mStructureCreatDocumentQueueDBDao = null;
        mStructureZanjireMadrakEntityDBDao = null;
        mStructureLogDBDao = null;
        mChatRoomListDBDao = null;
        mChatRoomMessageListDBDao = null;
        super.close();
    }

    public void clearUserAndRole() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureUserAndRoleDB.class);

    }

    public void clearMessage() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageDB.class);

    }

    public void clearMessageFile() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageFileDB.class);

    }

    public void clearReceiver() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureReceiverDB.class);

    }

    public void clearMessageQueue() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageQueueDB.class);

    }

    public void clearCartableDocument() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureInboxDocumentDB.class);

    }

    public void clearHamesh() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureHameshDB.class);
    }

    public void clearCartableHistory() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableHistoryDB.class);
    }

    public void clearZanjireMadrak() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureZanjireMadrakFileDB.class);
    }


    public void clearDocumentActions() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentActionsDB.class);
    }


    public void clearDocumentContent() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentContentDB.class);
    }

    public void clearCartableDocumentActions() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentActionsDB.class);
    }

    public void clearCartableDocumentSignature() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentSignatureDB.class);
    }

    public void clearUserRoles() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureUserRoleDB.class);
    }

    public void clearDocumentOperatorsQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureDocumentOperatorsQueueDB.class);
    }

    public void clearDocumentAttachFileQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureDocumentAttachFileQueueDB.class);
    }

    public void clearCreatDocumentQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCreatDocumentQueueDB.class);
    }

    public void clearZanjireMadrakEntity() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureZanjireMadrakEntityDB.class);
    }

    public void clearStructureLogDB() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureLogDB.class);
    }

    public void clearChatRoomListDB() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureChatRoomListDB.class);
    }

    public void clearChatRoomMessageListDB() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureChatRoomMessageDB.class);
    }

    public void clearAllTable() throws SQLException {
        clearDocumentActions();
        clearDocumentContent();
        clearMessageQueue();
        clearCartableDocument();
        clearHamesh();
        clearCartableHistory();
        clearZanjireMadrak();
        clearUserAndRole();
        clearMessage();
        clearMessageFile();
        clearReceiver();
        clearCartableDocumentActions();
        clearCartableDocumentSignature();
        clearUserRoles();
        clearDocumentOperatorsQueue();
        clearDocumentAttachFileQueue();
        clearCreatDocumentQueue();
        clearZanjireMadrakEntity();
        clearStructureLogDB();
        clearChatRoomListDB();
        clearChatRoomMessageListDB();
    }
}
