package avida.ican.Farzin.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import avida.ican.Farzin.Model.Structure.Database.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;

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
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public Dao<StructureUserAndRoleDB, Integer> getGetUserAndRoleListDao() throws SQLException {
        if (mGetUserAndRoleListDao == null) {
            mGetUserAndRoleListDao = getDao(StructureUserAndRoleDB.class);
        }

        return mGetUserAndRoleListDao;
    }

    public Dao<StructureMessageDB, Integer> getMessageDao() throws SQLException {
        if (mMessageDao == null) {
            mMessageDao = getDao(StructureMessageDB.class);
        }

        return mMessageDao;
    }

    public Dao<StructureMessageFileDB, Integer> getGetMessageFileDao() throws SQLException {
        if (mMessageFileDao == null) {
            mMessageFileDao = getDao(StructureMessageFileDB.class);
        }

        return mMessageFileDao;
    }

    public Dao<StructureReceiverDB, Integer> getGetReceiverDao() throws SQLException {
        if (mReceiverDao == null) {
            mReceiverDao = getDao(StructureReceiverDB.class);
        }

        return mReceiverDao;
    }
    public Dao<StructureMessageQueueDB, Integer> getGetMessageQueueDao() throws SQLException {
        if (mMessageQueueDao == null) {
            mMessageQueueDao = getDao(StructureMessageQueueDB.class);
        }

        return mMessageQueueDao;
    }


    @Override
    public void close() {
        mGetUserAndRoleListDao = null;
        mMessageDao = null;
        mMessageFileDao = null;
        mReceiverDao = null;
        mMessageQueueDao = null;
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
}
