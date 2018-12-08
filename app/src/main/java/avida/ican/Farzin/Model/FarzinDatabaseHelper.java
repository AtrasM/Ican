package avida.ican.Farzin.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableSendQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentTaeedQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;

/**
 * Created by AtrasVida on 2018-04-18 at 5:36 PM
 */

public class FarzinDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "Ican_Farzin.db";
    private static final int DATABASE_VERSION = 1;

    private Dao<StructureUserAndRoleDB, Integer> mGetUserAndRoleListDao = null;
    private Dao<StructureMessageDB, Integer> mMessageDao = null;
    private Dao<StructureMessageFileDB, Integer> mMessageFileDao = null;
    private Dao<StructureReceiverDB, Integer> mReceiverDao = null;
    private Dao<StructureMessageQueueDB, Integer> mMessageQueueDao = null;
    private Dao<StructureInboxDocumentDB, Integer> mCartableDocumentDao = null;
    private Dao<StructureHameshDB, Integer> mHameshDao = null;
    private Dao<StructureCartableHistoryDB, Integer> mHistoryDao = null;
    private Dao<StructureZanjireMadrakFileDB, Integer> mZanjireMadrakFileDao = null;
    private Dao<StructureCartableDocumentTaeedQueueDB, Integer> mCartableDocumentTaeedQueueDao = null;
    private Dao<StructureOpticalPenQueueDB, Integer> mOpticalPenQueueDBDao = null;
    private Dao<StructureCartableDocumentActionsDB, Integer> mCartableDocumentActionsDao = null;
    private Dao<StructureCartableSendQueueDB, Integer> mCartableSendQueueDao = null;
    private Dao<StructureCartableDocumentContentDB, Integer> mDocumentContentDao = null;

    public FarzinDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
            TableUtils.createTable(connectionSource, StructureCartableDocumentTaeedQueueDB.class);
            TableUtils.createTable(connectionSource, StructureOpticalPenQueueDB.class);
            TableUtils.createTable(connectionSource, StructureCartableDocumentActionsDB.class);
            TableUtils.createTable(connectionSource, StructureCartableSendQueueDB.class);
            TableUtils.createTable(connectionSource, StructureCartableDocumentContentDB.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, StructureUserAndRoleDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageFileDB.class, true);
            TableUtils.dropTable(connectionSource, StructureReceiverDB.class, true);
            TableUtils.dropTable(connectionSource, StructureMessageQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureInboxDocumentDB.class, true);
            TableUtils.dropTable(connectionSource, StructureHameshDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableHistoryDB.class, true);
            TableUtils.dropTable(connectionSource, StructureZanjireMadrakFileDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentTaeedQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureOpticalPenQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentActionsDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableSendQueueDB.class, true);
            TableUtils.dropTable(connectionSource, StructureCartableDocumentContentDB.class, true);
            onCreate(db, connectionSource);
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

    public Dao<StructureCartableDocumentTaeedQueueDB, Integer> getCartableDocumentTaeedDao() throws SQLException {
        if (mCartableDocumentTaeedQueueDao == null) {
            mCartableDocumentTaeedQueueDao = getDao(StructureCartableDocumentTaeedQueueDB.class);
        }
        return mCartableDocumentTaeedQueueDao;
    }

    public Dao<StructureOpticalPenQueueDB, Integer> getOpticalPenDao() throws SQLException {
        if (mOpticalPenQueueDBDao == null) {
            mOpticalPenQueueDBDao = getDao(StructureOpticalPenQueueDB.class);
        }
        return mOpticalPenQueueDBDao;
    }

    public Dao<StructureCartableDocumentActionsDB, Integer> getDocumentActionsDao() throws SQLException {
        if (mCartableDocumentActionsDao == null) {
            mCartableDocumentActionsDao = getDao(StructureCartableDocumentActionsDB.class);
        }
        return mCartableDocumentActionsDao;
    }

    public Dao<StructureCartableSendQueueDB, Integer> getCartableSendQueueDao() throws SQLException {
        if (mCartableSendQueueDao == null) {
            mCartableSendQueueDao = getDao(StructureCartableSendQueueDB.class);
        }
        return mCartableSendQueueDao;
    }

    public Dao<StructureCartableDocumentContentDB, Integer> getDocumentContentDao() throws SQLException {
        if (mDocumentContentDao == null) {
            mDocumentContentDao = getDao(StructureCartableDocumentContentDB.class);
        }

        return mDocumentContentDao;
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
        mCartableDocumentTaeedQueueDao = null;
        mOpticalPenQueueDBDao = null;
        mCartableDocumentActionsDao = null;
        mCartableSendQueueDao = null;
        mDocumentContentDao = null;
        super.close();
    }

    public void ClearUserAndRole() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureUserAndRoleDB.class);

    }

    public void ClearMessage() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageDB.class);

    }

    public void ClearMessageFile() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageFileDB.class);

    }

    public void ClearReceiver() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureReceiverDB.class);

    }

    public void ClearMessageQueue() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureMessageQueueDB.class);

    }

    public void ClearCartableDocument() throws SQLException {

        TableUtils.clearTable(getConnectionSource(), StructureInboxDocumentDB.class);

    }

    public void ClearHamesh() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureHameshDB.class);
    }

    public void ClearCartableHistory() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableHistoryDB.class);
    }

    public void ClearZanjireMadrak() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureZanjireMadrakFileDB.class);
    }

    public void ClearCartableDocumentTaeedQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentTaeedQueueDB.class);
    }

    public void ClearOpticalPenQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureOpticalPenQueueDB.class);
    }

    public void ClearDocumentActions() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentActionsDB.class);
    }

    public void ClearCartableSendQueue() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableSendQueueDB.class);
    }

    public void ClearDocumentContent() throws SQLException {
        TableUtils.clearTable(getConnectionSource(), StructureCartableDocumentContentDB.class);
    }
}
