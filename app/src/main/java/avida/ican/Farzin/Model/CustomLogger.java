package avida.ican.Farzin.Model;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.acra.ACRA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureLogDB;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

public class CustomLogger {

    public CustomLogger() {
    }

    public static void setLog(String log) {
        Log.d("IcanCustomLogger", log);
      /*  try {
            Dao<StructureLogDB, Integer> logDao = App.getFarzinDatabaseHelper().getStructureLogDBDao();
            String date = CustomFunction.getCurentLocalDateTimeAsStringFormat();
            StructureLogDB structureLogDB = new StructureLogDB(date, log);
            try {
                logDao.create(structureLogDB);
            } catch (SQLException e) {
                e.printStackTrace();
                ACRA.getErrorReporter().handleSilentException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static List<StructureLogDB> getLog() {
        Dao<StructureLogDB, Integer> logDao = null;
        List<StructureLogDB> structureLogDBS = new ArrayList<>();
        try {
            logDao = App.getFarzinDatabaseHelper().getStructureLogDBDao();
            QueryBuilder<StructureLogDB, Integer> queryBuilder = logDao.queryBuilder();
            queryBuilder.orderBy("id", false);
            structureLogDBS = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
        return structureLogDBS;
    }

    public static void clearLog() {
        try {
            App.getFarzinDatabaseHelper().clearStructureLogDB();
        } catch (SQLException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
    }
}
