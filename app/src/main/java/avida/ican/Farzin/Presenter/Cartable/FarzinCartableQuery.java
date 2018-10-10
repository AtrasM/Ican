package avida.ican.Farzin.Presenter.Cartable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableHistoryBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-09-16 at 11:46 AM
 */

public class FarzinCartableQuery {
    private String Tag = "FarzinCartableQuery";
    private CartableDocumentQuerySaveListener cartableDocumentQuerySaveListener;
    private HameshQuerySaveListener hameshQuerySaveListener;
    private CartableHistoryQuerySaveListener cartableHistoryQuerySaveListener;
    private ChangeXml changeXml;
    private Status status = Status.WAITING;
    private boolean SendFromApp = false;
    //_______________________***Dao***______________________________

    private Dao<StructureInboxDocumentDB, Integer> cartableDocumentDao = null;
    private Dao<StructureHameshDB, Integer> hameshDao = null;
    private Dao<StructureCartableHistoryDB, Integer> historyDao = null;


    //_______________________***Dao***______________________________

    public FarzinCartableQuery() {
        changeXml = new ChangeXml();
        initDao();
    }

    private void initDao() {
        try {
            cartableDocumentDao = App.getFarzinDatabaseHelper().getCartableDocumentDao();
            hameshDao = App.getFarzinDatabaseHelper().getHameshDao();
            historyDao = App.getFarzinDatabaseHelper().getHistoryDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

/*
    public void SaveSendLocalCartableDocument(int sender_user_id, int sender_role_id, String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureReceiver, MessageQuerySaveListener messageQuerySaveListener) {
        this.structureUserAndRole.clear();
        this.structureAttaches.clear();
        this.sender_user_id = sender_user_id;
        this.sender_role_id = sender_role_id;
        this.subject = subject;
        this.content = content;
        this.structureAttaches = structureAttaches;
        this.structureUserAndRole = structureReceiver;
        this.cartableDocumentQuerySaveListener = messageQuerySaveListener;
        this.status = Status.WAITING;
        this.Date = CustomFunction.getCurentDateTimeAsDateFormat(SimpleDateFormatEnum.DateTime_yyyy_MM_dd_hh_mm_ss.getValue()).toString();
        this.SendFromApp = true;
        new SaveMessage().execute(Type.SENDED);
    }
*/

    public void saveCartableDocument(StructureInboxDocumentRES structureInboxDocumentRES, Type type, final Status status, final CartableDocumentQuerySaveListener cartableDocumentQuerySaveListener) {
        int code = structureInboxDocumentRES.getReceiverCode();

        if (IsDocumentExist(code)) {
            // UpdateMessageView(structureMessageRES, type);
            cartableDocumentQuerySaveListener.onExisting();
        } else {
            this.status = status;
            this.cartableDocumentQuerySaveListener = cartableDocumentQuerySaveListener;
            new saveCartableDocument().execute(structureInboxDocumentRES);
        }

    }

    public void saveHamesh(StructureHameshRES structureHameshRES, int ETC, int EC, final HameshQuerySaveListener hameshQuerySaveListener) {

        structureHameshRES.setETC(ETC);
        structureHameshRES.setEC(EC);
        if (IsHameshExist(structureHameshRES.getHameshID())) {
            // UpdateMessageView(structureMessageRES, type);
            hameshQuerySaveListener.onExisting();
        } else {
            this.hameshQuerySaveListener = hameshQuerySaveListener;
            new saveHamesh().execute(structureHameshRES);
        }

    }

    public void saveCartableHistory(String xml, int ETC, int EC, final CartableHistoryQuerySaveListener cartableHistoryQuerySaveListener) {
        StructureCartableHistoryBND structureCartableHistoryBND = new StructureCartableHistoryBND(xml, ETC, EC);
        this.cartableHistoryQuerySaveListener = cartableHistoryQuerySaveListener;

        if (IsCartableHistoryExist(ETC, EC)) {
            updateCartableHistory(structureCartableHistoryBND);
        } else {
            new saveHistory().execute(structureCartableHistoryBND);
        }


    }


    @SuppressLint("StaticFieldLeak")
    private class saveCartableDocument extends AsyncTask<StructureInboxDocumentRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureInboxDocumentRES... structureInboxDocumentRES) {
            Date importDate = null;
            Date exportDate = null;
            Date receiveDate = null;
            Date expireDate = null;

            importDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getImportDate()));
            exportDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getExportDate()));
            receiveDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getReceiveDate()));
            expireDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getExpireDate()));
            getFarzinPrefrences().putCartableDocumentDataSyncDate(structureInboxDocumentRES[0].getReceiveDate());
            StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB(structureInboxDocumentRES[0], importDate, exportDate, receiveDate, expireDate, status, false);
            try {
                cartableDocumentDao.create(structureInboxDocumentDB);

                if (SendFromApp) {

                }
                cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
            } catch (SQLException e) {
                e.printStackTrace();
                cartableDocumentQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")


    private class saveHamesh extends AsyncTask<StructureHameshRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureHameshRES... structureHameshRES) {
            Date creationShamsiDate = null;
            Date creationDate = null;

            creationDate = new Date(CustomFunction.StandardizeTheDateFormat(structureHameshRES[0].getCreationDate()));
            creationShamsiDate = new Date(CustomFunction.StandardizeTheDateFormat(structureHameshRES[0].getCreationShamsiDate()));
            StructureHameshDB structureHameshDB = new StructureHameshDB(structureHameshRES[0], structureHameshRES[0].getETC(), structureHameshRES[0].getEC());
            try {
                hameshDao.create(structureHameshDB);

                hameshQuerySaveListener.onSuccess(structureHameshDB);
            } catch (SQLException e) {
                e.printStackTrace();
                hameshQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }


    private class saveHistory extends AsyncTask<StructureCartableHistoryBND, Void, Void> {

        @Override
        protected Void doInBackground(StructureCartableHistoryBND... HistoryBND) {
            StructureCartableHistoryDB cartableHistoryDB = new StructureCartableHistoryDB(HistoryBND[0].getXml(), HistoryBND[0].getETC(), HistoryBND[0].getEC());
            try {
                historyDao.create(cartableHistoryDB);
                cartableHistoryQuerySaveListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                hameshQuerySaveListener.onFailed(e.toString());
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

    public List<StructureHameshDB> getHamesh(int ETC, int EC, long start, long count) {
        QueryBuilder<StructureHameshDB, Integer> queryBuilder = hameshDao.queryBuilder();
        List<StructureHameshDB> structureHameshDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureHameshDBS = queryBuilder.orderBy("CreationDate", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureHameshDBS;
    }
    public List<StructureCartableHistoryDB> getCartableHistory(int ETC, int EC, long start, long count) {
        QueryBuilder<StructureCartableHistoryDB, Integer> queryBuilder = historyDao.queryBuilder();
        List<StructureCartableHistoryDB> structureCartableHistoryDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureCartableHistoryDBS = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableHistoryDBS;
    }

    public List<StructureInboxDocumentDB> getCartableDocuments(int actionCode, Status status, long start, long count) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        List<StructureInboxDocumentDB> structureInboxDocumentsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ActionCode", actionCode);
            if (status != null) {
                queryBuilder.where().eq("status", status);
            }
            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureInboxDocumentsDB = queryBuilder.orderBy("ReceiveDate", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentsDB;
    }

    public long getCartableDocumentCounts(int actionCode, Status status) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("actionCode", actionCode).and().eq("status", status));
            queryBuilder.setCountOf(true);
            count = cartableDocumentDao.countOf(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public StructureInboxDocumentDB getCartableDocument(int id) {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        try {
            inboxDocumentQB.where().eq("id", id);
            structureInboxDocumentDB = inboxDocumentQB.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public ArrayList<StructureCartableAction> getCartableAction(boolean isPin) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
        ArrayList<StructureCartableAction> cartableActions = new ArrayList<>();
        try {
            queryBuilder.where().eq("Pin", isPin);
            structureInboxDocumentDB = queryBuilder.groupBy("ActionCode").query();
            for (StructureInboxDocumentDB inboxDocumentDB : structureInboxDocumentDB) {
                StructureCartableAction structureCartableAction = new StructureCartableAction(inboxDocumentDB.getActionCode(), inboxDocumentDB.getActionName(), getCartableActionCount(inboxDocumentDB.getActionCode()), inboxDocumentDB.isPin());
                cartableActions.add(structureCartableAction);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return cartableActions;
    }

    public long getCartableActionCount(int ActionCode) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ActionCode", ActionCode));
            queryBuilder.setCountOf(true);
            count = cartableDocumentDao.countOf(queryBuilder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void updateCartableHistory(StructureCartableHistoryBND HistoryBND) {
        try {
            UpdateBuilder<StructureCartableHistoryDB, Integer> updateBuilder = historyDao.updateBuilder();
            updateBuilder.where().eq("ETC", HistoryBND.getETC()).and().eq("EC", HistoryBND.getEC());
            updateBuilder.updateColumnValue("DataXml", HistoryBND.getXml());
            updateBuilder.update();
            cartableHistoryQuerySaveListener.onSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            cartableHistoryQuerySaveListener.onFailed(e.toString());
        }
    }

    public void pinAction(int actionCode, boolean isPin) {
        try {
            UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
            updateBuilder.where().eq("ActionCode", actionCode);
            updateBuilder.updateColumnValue("Pin", isPin);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean IsHameshExist(int HameshID) {
        boolean existing = false;
        QueryBuilder<StructureHameshDB, Integer> queryBuilder = hameshDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("HameshID", HameshID));
            queryBuilder.setCountOf(true);
            long count = hameshDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsDocumentExist(int code) {
        boolean existing = false;
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ReceiverCode", code));
            queryBuilder.setCountOf(true);
            long count = cartableDocumentDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsCartableHistoryExist(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureCartableHistoryDB, Integer> queryBuilder = historyDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC));
            queryBuilder.setCountOf(true);
            long count = historyDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
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






