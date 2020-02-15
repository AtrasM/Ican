package avida.ican.Farzin.Presenter.Cartable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

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

import avida.ican.Farzin.Model.Enum.DocumentContentFileTypeEnum;
import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionsQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentSignatureQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentSignaturesListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CreateDocumentListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentActionsListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureCreateDocumentQueueBND;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentContentBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableHistoryBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureCreatDocumentQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureResponseREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureWorkFlowREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureEntityDependencyRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureFileRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignatureRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireEntityRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableDocumentContent;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.ListenerDelet;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-09-16 at 11:46 AM
 */

public class FarzinCartableQuery {
    private String Tag = "FarzinCartableQuery";
    private CartableDocumentQuerySaveListener cartableDocumentQuerySaveListener;
    private HameshQuerySaveListener hameshQuerySaveListener;
    private CartableDocumentActionsQuerySaveListener cartableDocumentActionsQuerySaveListener;
    private CartableDocumentSignatureQuerySaveListener cartableDocumentSignatureQuerySaveListener;
    private CartableHistoryQuerySaveListener cartableHistoryQuerySaveListener;
    private ZanjireMadrakQuerySaveListener zanjireMadrakQuerySaveListener;
    private ZanjireMadrakQuerySaveListener zanjireMadrakEntityQuerySaveListener;
    private DocumentOperatorQueuesListener documentOperatorQueuesListener;
    private DocumentAttachFileListener documentAttachFileListener;
    private CreateDocumentListener createDocumentListener;
    private CartableDocumentContentQuerySaveListener documentContentQuerySaveListener;
    private ChangeXml changeXml;
    private Status status = Status.WAITING;
    private boolean SendFromApp = false;
    //_______________________***Dao***______________________________

    private Dao<StructureInboxDocumentDB, Integer> cartableDocumentDao = null;
    private Dao<StructureHameshDB, Integer> hameshDao = null;
    private Dao<StructureCartableHistoryDB, Integer> historyDao = null;
    private Dao<StructureZanjireMadrakFileDB, Integer> zanjireMadrakFileDao = null;
    private Dao<StructureCartableDocumentActionsDB, Integer> mCartableDocumentActionsDao = null;
    private Dao<StructureCartableDocumentContentDB, Integer> mDocumentContentDao = null;
    private Dao<StructureCartableDocumentSignatureDB, Integer> mCartableDocumentSignatureDao = null;
    private Dao<StructureDocumentOperatorsQueueDB, Integer> mDocumentOperatorsQueueDao = null;
    private Dao<StructureDocumentAttachFileQueueDB, Integer> mDocumentAttachFileQueueDao = null;
    private Dao<StructureCreatDocumentQueueDB, Integer> mStructureCreatDocumentQueueDBDao = null;
    private Dao<StructureZanjireMadrakEntityDB, Integer> mStructureZanjireMadrakEntityDBDao = null;
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
            zanjireMadrakFileDao = App.getFarzinDatabaseHelper().getZanjireMadrakDao();
            mCartableDocumentActionsDao = App.getFarzinDatabaseHelper().getDocumentActionsDao();
            mDocumentContentDao = App.getFarzinDatabaseHelper().getDocumentContentDao();
            mCartableDocumentSignatureDao = App.getFarzinDatabaseHelper().getCartableDocumentSignatureDao();
            mDocumentOperatorsQueueDao = App.getFarzinDatabaseHelper().getDocumentOperatorsQueueDBDao();
            mDocumentAttachFileQueueDao = App.getFarzinDatabaseHelper().getDocumentAttachFileDao();
            mStructureCreatDocumentQueueDBDao = App.getFarzinDatabaseHelper().getCreatDocumentDao();
            mStructureZanjireMadrakEntityDBDao = App.getFarzinDatabaseHelper().getZanjireMadrakEntityDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCartableDocument(StructureInboxDocumentRES structureInboxDocumentRES, Type type, final Status status, final CartableDocumentQuerySaveListener cartableDocumentQuerySaveListener) {
        int code = structureInboxDocumentRES.getReceiverCode();
        if (IsDocumentExist(code)) {
            if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                cartableDocumentQuerySaveListener.onSuccess(new StructureInboxDocumentDB());
            } else {
                cartableDocumentQuerySaveListener.onExisting();
            }
            // UpdateMessageView(structureMessageRES, type);

        } else {
            this.status = status;
            this.cartableDocumentQuerySaveListener = cartableDocumentQuerySaveListener;
            new saveCartableDocument().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureInboxDocumentRES);
        }
    }

    public void saveDocumentAction(StructureCartableDocumentActionRES structureCartableDocumentActionRES, int ETC, final CartableDocumentActionsQuerySaveListener cartableDocumentActionsQuerySaveListener) {
        structureCartableDocumentActionRES.setETC(ETC);
        this.cartableDocumentActionsQuerySaveListener = cartableDocumentActionsQuerySaveListener;
        new saveDocumentAction().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureCartableDocumentActionRES);
    }

    public void saveDocumentSignature(StructureSignatureRES structureSignatureRES, int ETC, final CartableDocumentSignatureQuerySaveListener cartableDocumentSignatureQuerySaveListener) {
        structureSignatureRES.setETC(ETC);
        this.cartableDocumentSignatureQuerySaveListener = cartableDocumentSignatureQuerySaveListener;
        new saveDocumentSignature().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureSignatureRES);
    }

    public void saveHamesh(StructureHameshRES structureHameshRES, int ETC, int EC, final HameshQuerySaveListener hameshQuerySaveListener) {
        structureHameshRES.setETC(ETC);
        structureHameshRES.setEC(EC);
        this.hameshQuerySaveListener = hameshQuerySaveListener;
        new saveHamesh().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureHameshRES);
    }

    public void saveCartableDocumentContent(StructureCartableDocumentContentBND cartableDocumentContentBND, final CartableDocumentContentQuerySaveListener documentContentQuerySaveListener) {
        this.documentContentQuerySaveListener = documentContentQuerySaveListener;
        if (cartableDocumentContentBND.getFileExtension() == null || cartableDocumentContentBND.getFileExtension().isEmpty()) {
            cartableDocumentContentBND.setFileExtension(".pdf");
        }
        if (IsDocumentContentExist(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC())) {
            deletCartableDocumentContent(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC());
        }
       /* if (IsDocumentContentExist(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC(),cartableDocumentContentBND.getFileTypeEnum())) {
            deletCartableDocumentContent(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC());
        }*/

        new saveCartableDocumentContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cartableDocumentContentBND);

    }

    public void saveCartableDocumentContent(ArrayList<StructureCartableDocumentContentBND> cartableDocumentContentBNDS, final CartableDocumentContentQuerySaveListener documentContentQuerySaveListener) {

        this.documentContentQuerySaveListener = documentContentQuerySaveListener;
        if (IsDocumentContentExist(cartableDocumentContentBNDS.get(0).getETC(), cartableDocumentContentBNDS.get(0).getEC())) {
            deletCartableDocumentContent(cartableDocumentContentBNDS.get(0).getETC(), cartableDocumentContentBNDS.get(0).getEC());
        }
        for (StructureCartableDocumentContentBND cartableDocumentContentBND : cartableDocumentContentBNDS) {
            new saveCartableDocumentContent().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, cartableDocumentContentBND);
        }


    }

    public void saveCartableHistory(String xml, int ETC, int EC, final CartableHistoryQuerySaveListener cartableHistoryQuerySaveListener) {
        StructureCartableHistoryBND structureCartableHistoryBND = new StructureCartableHistoryBND(xml, ETC, EC);
        this.cartableHistoryQuerySaveListener = cartableHistoryQuerySaveListener;

        if (IsCartableHistoryExist(ETC, EC)) {
            updateCartableHistory(structureCartableHistoryBND);
        } else {
            new saveHistory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureCartableHistoryBND);
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
            Date LastChangeViewStatesDate = null;
            importDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getImportDate()));
            exportDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getExportDate()));
            Log.i("LogDate", "structureInboxDocumentRES[0].getReceiveDate()= " + structureInboxDocumentRES[0].getReceiveDate());
            receiveDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getReceiveDate()));
            expireDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getExpireDate()));
            LastChangeViewStatesDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getLastChangeViewStatesDate()));
            getFarzinPrefrences().putCartableDocumentDataSyncDate(structureInboxDocumentRES[0].getReceiveDate());
            int userRoleID = getFarzinPrefrences().getRoleID();
            final StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB(structureInboxDocumentRES[0], importDate, exportDate, receiveDate, expireDate, LastChangeViewStatesDate, status, userRoleID, false);
            try {
                cartableDocumentDao.create(structureInboxDocumentDB);

                if (IsDocumentActionsExist(structureInboxDocumentDB.getEntityTypeCode())) {
                    if (IsDocumentSignatureExist(structureInboxDocumentDB.getEntityTypeCode())) {
                        cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
                    } else {
                        GetDocumentSignatureListFromServer(structureInboxDocumentDB);
                    }
                } else {
                    GetDocumentActionListFromServer(structureInboxDocumentDB);
                }
                App.needToReGetCartableDocumentList = true;

            } catch (SQLException e) {
                e.printStackTrace();
                cartableDocumentQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private void GetDocumentActionListFromServer(StructureInboxDocumentDB structureInboxDocumentDB) {
        App.getHandlerMainThread().post(() -> new CartableDocumentActionsPresenter(structureInboxDocumentDB.getEntityTypeCode()).GetDocumentActionsFromServer(new DocumentActionsListener() {
            @Override
            public void onSuccess() {
                if (IsDocumentSignatureExist(structureInboxDocumentDB.getEntityTypeCode())) {
                    cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
                } else {
                    GetDocumentSignatureListFromServer(structureInboxDocumentDB);
                }

            }

            @Override
            public void onFailed(String message) {
                cartableDocumentQuerySaveListener.onFailed("can not get actions from server");
            }

            @Override
            public void onCancel() {
                cartableDocumentQuerySaveListener.onCancel();
            }
        }));
    }

    private void GetDocumentSignatureListFromServer(StructureInboxDocumentDB structureInboxDocumentDB) {
        App.getHandlerMainThread().post(() -> new CartableDocumentSignaturesPresenter(structureInboxDocumentDB.getEntityTypeCode()).GetDocumentSignatureFromServer(new CartableDocumentSignaturesListener() {
            @Override
            public void onSuccess() {
                if (IsDocumentActionsExist(structureInboxDocumentDB.getEntityTypeCode())) {
                    cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
                } else {
                    GetDocumentActionListFromServer(structureInboxDocumentDB);
                }
            }

            @Override
            public void onFailed(String message) {
                cartableDocumentQuerySaveListener.onFailed("can not get actions from server");
            }

            @Override
            public void onCancel() {
                cartableDocumentQuerySaveListener.onCancel();
            }
        }));
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
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }
            return null;
        }
    }

    private class saveCartableDocumentContent extends AsyncTask<StructureCartableDocumentContentBND, Void, Void> {
        @Override
        protected Void doInBackground(StructureCartableDocumentContentBND... cartableDocumentContentBND) {
            String fileName = "CDC" + cartableDocumentContentBND[0].getETC() + "" + cartableDocumentContentBND[0].getEC() + CustomFunction.getRandomUUID();
            String filePath = "";
            if (cartableDocumentContentBND[0].getFileAsStringBuilder().length() < 256) {
                filePath = cartableDocumentContentBND[0].getFileAsStringBuilder().toString();
                fileName = CustomFunction.getFileName(filePath);

            } else {
                filePath = new CustomFunction().saveFileToStorage(cartableDocumentContentBND[0].getFileAsStringBuilder(), fileName);
            }
            StructureCartableDocumentContentDB structureCartableDocumentContentDB = new StructureCartableDocumentContentDB(fileName, filePath, cartableDocumentContentBND[0].getFileExtension(), cartableDocumentContentBND[0].getFileTypeEnum(), cartableDocumentContentBND[0].getETC(), cartableDocumentContentBND[0].getEC());
            try {
                mDocumentContentDao.create(structureCartableDocumentContentDB);
                documentContentQuerySaveListener.onSuccess(getDocumentContent(cartableDocumentContentBND[0].getETC(), cartableDocumentContentBND[0].getEC()));
            } catch (SQLException e) {
                e.printStackTrace();
                documentContentQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private class saveDocumentAction extends AsyncTask<StructureCartableDocumentActionRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureCartableDocumentActionRES... cartableDocumentActionRES) {
            StructureCartableDocumentActionsDB cartableDocumentActionsDB = new StructureCartableDocumentActionsDB(cartableDocumentActionRES[0].getETC(), cartableDocumentActionRES[0].getActionCode(), cartableDocumentActionRES[0].getIsActiveForCardTable(), cartableDocumentActionRES[0].getIsActiveForSend(), cartableDocumentActionRES[0].getActionName(), cartableDocumentActionRES[0].getActionOrder(), cartableDocumentActionRES[0].getFarsiDescription());
            try {
                mCartableDocumentActionsDao.create(cartableDocumentActionsDB);
                cartableDocumentActionsQuerySaveListener.onSuccess(cartableDocumentActionsDB);
            } catch (SQLException e) {
                e.printStackTrace();
                cartableDocumentActionsQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private class saveDocumentSignature extends AsyncTask<StructureSignatureRES, Void, Void> {
        @Override
        protected Void doInBackground(StructureSignatureRES... structureSignatureRES) {
            StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB;
            if (structureSignatureRES[0].getEN() != null || !structureSignatureRES[0].getEN().isEmpty()) {
                structureCartableDocumentSignatureDB = new StructureCartableDocumentSignatureDB(structureSignatureRES[0].getETC(), structureSignatureRES[0].getEN(), structureSignatureRES[0].getFN());
            } else {
                structureCartableDocumentSignatureDB = new StructureCartableDocumentSignatureDB(structureSignatureRES[0].getETC(), true);
            }
            try {
                mCartableDocumentSignatureDao.create(structureCartableDocumentSignatureDB);
                cartableDocumentSignatureQuerySaveListener.onSuccess(structureCartableDocumentSignatureDB);
            } catch (SQLException e) {
                e.printStackTrace();
                cartableDocumentSignatureQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    public void saveDocumentOperatorsQueue(StructureDocumentOperatorsQueueBND structureDocumentOperatorsQueueBND, DocumentOperatorQueuesListener documentOperatorQueuesListener) {

        this.documentOperatorQueuesListener = documentOperatorQueuesListener;
        new saveDocumentOperatorsQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureDocumentOperatorsQueueBND);
    }

    private class saveDocumentOperatorsQueue extends AsyncTask<StructureDocumentOperatorsQueueBND, Void, Void> {
        @Override
        protected Void doInBackground(StructureDocumentOperatorsQueueBND... documentOperatorsQueueBNDS) {
            StructureDocumentOperatorsQueueDB structureDocumentOperatorsQueueDB = new StructureDocumentOperatorsQueueDB(documentOperatorsQueueBNDS[0].getETC(), documentOperatorsQueueBNDS[0].getEC(), documentOperatorsQueueBNDS[0].isLock(), documentOperatorsQueueBNDS[0].getDocumentOpratoresTypeEnum(), documentOperatorsQueueBNDS[0].getDocumentOpratoresTypeEnum().getIntValue(), documentOperatorsQueueBNDS[0].getQueueStatus(), documentOperatorsQueueBNDS[0].getStrError(), documentOperatorsQueueBNDS[0].getStrDataREQ());
            try {
                mDocumentOperatorsQueueDao.create(structureDocumentOperatorsQueueDB);
                if (structureDocumentOperatorsQueueDB.getDocumentOpratoresTypeEnum() == DocumentOperatoresTypeEnum.Response) {
                    StructureResponseREQ StructureResponseREQ = new CustomFunction().ConvertStringToObject(structureDocumentOperatorsQueueDB.getStrDataREQ(), StructureResponseREQ.class);
                    UpdateInboxDocumentConfirm(StructureResponseREQ.getReceiverCode(), true);
                } else if (structureDocumentOperatorsQueueDB.getDocumentOpratoresTypeEnum() == DocumentOperatoresTypeEnum.WorkFlow) {
                    StructureWorkFlowREQ structureWorkFlowREQ = new CustomFunction().ConvertStringToObject(structureDocumentOperatorsQueueDB.getStrDataREQ(), StructureWorkFlowREQ.class);
                    UpdateInboxDocumentConfirm(structureWorkFlowREQ.getReceiverCode(), true);
                }
                documentOperatorQueuesListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                documentOperatorQueuesListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    public void saveDocumentAttachFileQueue(StructureDocumentAttachFileBND structureDocumentAttachFileBND, DocumentAttachFileListener documentAttachFileListener) {

        this.documentAttachFileListener = documentAttachFileListener;
        new saveDocumentAttachFileQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureDocumentAttachFileBND);
    }

    private class saveDocumentAttachFileQueue extends AsyncTask<StructureDocumentAttachFileBND, Void, Void> {
        @Override
        protected Void doInBackground(StructureDocumentAttachFileBND... attachFileQueueBND) {
            StructureDocumentAttachFileQueueDB structureDocumentAttachFileQueueDB = new StructureDocumentAttachFileQueueDB(attachFileQueueBND[0].getETC(), attachFileQueueBND[0].getEC(), attachFileQueueBND[0].isLock(), attachFileQueueBND[0].getFile_name(), attachFileQueueBND[0].getFile_path(), attachFileQueueBND[0].getFile_extension(), attachFileQueueBND[0].getDependencyType(), attachFileQueueBND[0].getDescription());
            try {
                String filePath = "";

                StructureAttach Attach = new StructureAttach(attachFileQueueBND[0].getFileAsStringBuilder(), attachFileQueueBND[0].getFile_name(), attachFileQueueBND[0].getFile_extension());
                String fileName = CustomFunction.deletExtentionAsFileName(Attach.getName());

                if (attachFileQueueBND[0].getFileAsStringBuilder() != null && attachFileQueueBND[0].getFileAsStringBuilder().length() > 0) {
                    filePath = new CustomFunction().saveFileToStorage(Attach.getFileAsStringBuilder(), fileName + CustomFunction.getRandomUUID());
                } else {
                    filePath = attachFileQueueBND[0].getFile_path();
                }
                structureDocumentAttachFileQueueDB.setFile_path(filePath);
                threadSleep();

                mDocumentAttachFileQueueDao.create(structureDocumentAttachFileQueueDB);
                documentAttachFileListener.onSuccessAddToQueue();
            } catch (SQLException e) {
                e.printStackTrace();
                documentAttachFileListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }


    public void saveCreatDocumentQueue(StructureCreateDocumentQueueBND createDocumentQueueBND, CreateDocumentListener createDocumentListener) {

        this.createDocumentListener = createDocumentListener;
        new saveCreatDocumentQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, createDocumentQueueBND);
    }

    private class saveCreatDocumentQueue extends AsyncTask<StructureCreateDocumentQueueBND, Void, Void> {
        @Override
        protected Void doInBackground(StructureCreateDocumentQueueBND... createDocumentQueueBND) {
            StructureCreatDocumentQueueDB structureCreatDocumentQueueDB = new StructureCreatDocumentQueueDB(createDocumentQueueBND[0].getETC(), createDocumentQueueBND[0].getEC(), createDocumentQueueBND[0].getSubject(), createDocumentQueueBND[0].getSenderUserName(), createDocumentQueueBND[0].getSenderFullName(), createDocumentQueueBND[0].getReceiverFullName(), createDocumentQueueBND[0].getImportOriginDate());
            try {
                mStructureCreatDocumentQueueDBDao.create(structureCreatDocumentQueueDB);
                createDocumentListener.onSuccessAddToQueue();
            } catch (SQLException e) {
                e.printStackTrace();
                createDocumentListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    public List<StructureCreatDocumentQueueDB> getCreateDocumentQueueList(QueueStatus queueStatus) {
        QueryBuilder<StructureCreatDocumentQueueDB, Integer> queryBuilder = mStructureCreatDocumentQueueDBDao.queryBuilder();
        List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("queueStatus", queueStatus);

            structureCreatDocumentQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCreatDocumentQueueDBS;
    }

    public List<StructureCreatDocumentQueueDB> getCreateDocumentQueueList() {
        QueryBuilder<StructureCreatDocumentQueueDB, Integer> queryBuilder = mStructureCreatDocumentQueueDBDao.queryBuilder();
        List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().ne("queueStatus", QueueStatus.SENDED);
            structureCreatDocumentQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCreatDocumentQueueDBS;
    }

    public long getCreatDocumentQueueCount() {

        QueryBuilder<StructureCreatDocumentQueueDB, Integer> queryBuilder = mStructureCreatDocumentQueueDBDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().ne("queueStatus", QueueStatus.SENDED);
            count = mStructureCreatDocumentQueueDBDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public void setErrorToCreateDocumentQueue(int id, String strError) {
        UpdateBuilder<StructureCreatDocumentQueueDB, Integer> updateBuilder = mStructureCreatDocumentQueueDBDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("strError", strError);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCreateDocumentStatus(int id, QueueStatus queueStatus) {
        try {
            UpdateBuilder<StructureCreatDocumentQueueDB, Integer> updateBuilder = mStructureCreatDocumentQueueDBDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", queueStatus);
            if (status == Status.READ) {
                updateBuilder.updateColumnValue("IsRead", true);
            }
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCreateDocumentQueueStatus(int ETC, int EC, QueueStatus lastStatus, QueueStatus newStatus) {
        UpdateBuilder<StructureCreatDocumentQueueDB, Integer> updateBuilder = mStructureCreatDocumentQueueDBDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("queueStatus", lastStatus);
            updateBuilder.updateColumnValue("queueStatus", newStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCreateDocumentQueueStatus(int ETC, int EC, QueueStatus status) {
        UpdateBuilder<StructureCreatDocumentQueueDB, Integer> updateBuilder = mStructureCreatDocumentQueueDBDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            updateBuilder.updateColumnValue("queueStatus", status);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletCreateDocumentQueue(int id) {

        boolean isDelet = false;
        try {
            DeleteBuilder<StructureCreatDocumentQueueDB, Integer> deleteBuilder = mStructureCreatDocumentQueueDBDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletCreateDocumentQueue(int ETC, int EC) {

        boolean isDelet = false;
        try {
            DeleteBuilder<StructureCreatDocumentQueueDB, Integer> deleteBuilder = mStructureCreatDocumentQueueDBDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }
 /*  public boolean deletDocumentAttachFileQueue(int ETC, int EC) {

        boolean isDelet = false;
        try {
            DeleteBuilder<StructureDocumentAttachFileQueueDB, Integer> deleteBuilder = mDocumentAttachFileQueueDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }*/


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
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }


    public void saveZanjireMadrak(final StructureZanjireMadrakRES structureZanjireMadrakRES, int ETC, int EC, final ZanjireMadrakQuerySaveListener zanjireMadrakQuerySaveListener) {
        structureZanjireMadrakRES.setETC(ETC);
        structureZanjireMadrakRES.setEC(EC);
        this.zanjireMadrakQuerySaveListener = zanjireMadrakQuerySaveListener;
        if (IsZanjireMadrakExist(ETC, EC)) {
            deletZanjireMadrak(ETC, EC, new ListenerDelet() {
                @Override
                public void onSuccess() {
                    new saveZanjireMadrak().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureZanjireMadrakRES);
                }

                @Override
                public void onFailed(String error) {
                    zanjireMadrakQuerySaveListener.onFailed(error);
                }
            });

        } else {
            new saveZanjireMadrak().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureZanjireMadrakRES);
        }
    }


    private class saveZanjireMadrak extends AsyncTask<StructureZanjireMadrakRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureZanjireMadrakRES... zanjireMadrakRES) {

            try {
                String fileName = "";
                String filePath = "";
                for (StructureFileRES structureFileRES : zanjireMadrakRES[0].getPeyvast()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (structureFileRES.getFileAsStringBuilder() != null && structureFileRES.getFileAsStringBuilder().length() > 0) {
                        stringBuilder = structureFileRES.getFileAsStringBuilder();
                    } else {
                        stringBuilder.append(structureFileRES.getFileBinary());

                    }

                    fileName = CustomFunction.deletExtentionAsFileName(structureFileRES.getFileName());
                    if (stringBuilder.length() < 256) {
                        filePath = stringBuilder.toString();
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }

                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), structureFileRES.getCreationFullName(), structureFileRES.getCreationRoleName(), structureFileRES.getCreationDate(), ZanjireMadrakFileTypeEnum.PEYVAST, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                    zanjireMadrakFileDao.create(structureZanjireMadrakFileDB);
                }
                for (StructureFileRES structureFileRES : zanjireMadrakRES[0].getAtf()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (structureFileRES.getFileAsStringBuilder() != null && structureFileRES.getFileAsStringBuilder().length() > 0) {
                        stringBuilder = structureFileRES.getFileAsStringBuilder();
                    } else {
                        stringBuilder.append(structureFileRES.getFileBinary());

                    }

                    fileName = CustomFunction.deletExtentionAsFileName(structureFileRES.getFileName());
                    if (stringBuilder.length() < 256) {
                        filePath = stringBuilder.toString();
                        //}
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), structureFileRES.getCreationFullName(), structureFileRES.getCreationRoleName(), structureFileRES.getCreationDate(), ZanjireMadrakFileTypeEnum.ATF, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                    zanjireMadrakFileDao.create(structureZanjireMadrakFileDB);
                }
                for (StructureFileRES structureFileRES : zanjireMadrakRES[0].getDarErtebat()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (structureFileRES.getFileAsStringBuilder() != null && structureFileRES.getFileAsStringBuilder().length() > 0) {
                        stringBuilder = structureFileRES.getFileAsStringBuilder();
                    } else {
                        stringBuilder.append(structureFileRES.getFileBinary());
                    }

                    fileName = CustomFunction.deletExtentionAsFileName(structureFileRES.getFileName());
                    if (stringBuilder.length() < 256) {
                        filePath = stringBuilder.toString();
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), structureFileRES.getCreationFullName(), structureFileRES.getCreationRoleName(), structureFileRES.getCreationDate(), ZanjireMadrakFileTypeEnum.DARERTEBAT, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                    zanjireMadrakFileDao.create(structureZanjireMadrakFileDB);
                }
                for (StructureFileRES structureFileRES : zanjireMadrakRES[0].getPeyro()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    if (structureFileRES.getFileAsStringBuilder() != null && structureFileRES.getFileAsStringBuilder().length() > 0) {
                        stringBuilder = structureFileRES.getFileAsStringBuilder();
                    } else {
                        stringBuilder.append(structureFileRES.getFileBinary());

                    }
                    fileName = CustomFunction.deletExtentionAsFileName(structureFileRES.getFileName());
                    if (stringBuilder.length() < 256) {
                        filePath = stringBuilder.toString();
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), structureFileRES.getCreationFullName(), structureFileRES.getCreationRoleName(), structureFileRES.getCreationDate(), ZanjireMadrakFileTypeEnum.PEIRO, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                    zanjireMadrakFileDao.create(structureZanjireMadrakFileDB);
                }
                if (zanjireMadrakRES[0].getIndicatorScanedFile() != null && zanjireMadrakRES[0].getIndicatorScanedFile().size() > 0) {
                    ArrayList<StructureCartableDocumentContentBND> structureCartableDocumentContentBNDS = new ArrayList<>();
                    for (StructureFileRES structureFileRES : zanjireMadrakRES[0].getIndicatorScanedFile()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        if (structureFileRES.getFileAsStringBuilder() != null && structureFileRES.getFileAsStringBuilder().length() > 0) {
                            stringBuilder = structureFileRES.getFileAsStringBuilder();
                        } else {
                            stringBuilder.append(structureFileRES.getFileBinary());

                        }
                        StructureCartableDocumentContentBND cartableDocumentContentBND = new StructureCartableDocumentContentBND(stringBuilder, structureFileRES.getFileExtension(), DocumentContentFileTypeEnum.IndicatorScanedFile, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                        structureCartableDocumentContentBNDS.add(cartableDocumentContentBND);
                    }
                    saveCartableDocumentContent(structureCartableDocumentContentBNDS, new CartableDocumentContentQuerySaveListener() {
                        @Override
                        public void onSuccess(List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB) {
                            App.getHandlerMainThread().post(() -> {
                                try {
                                    if (App.fragmentStacks != null && App.fragmentStacks.size() > 0) {
                                        FragmentCartableDocumentContent fragmentCartableDocumentContent = (FragmentCartableDocumentContent) App.fragmentStacks.get("documentContent").lastElement();
                                        if (fragmentCartableDocumentContent != null) {
                                            fragmentCartableDocumentContent.reGetData();
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }

                        @Override
                        public void onExisting() {

                        }

                        @Override
                        public void onFailed(String message) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                zanjireMadrakQuerySaveListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                zanjireMadrakQuerySaveListener.onFailed(e.toString());
                ACRA.getErrorReporter().handleSilentException(e);
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    public void saveZanjireMadrakEnity(int mainETC, int mainEC, final StructureZanjireEntityRES structureZanjireEntityRES, final ZanjireMadrakQuerySaveListener zanjireMadrakEntityQuerySaveListener) {
        structureZanjireEntityRES.setMainETC(mainETC);
        structureZanjireEntityRES.setMainEC(mainEC);
        this.zanjireMadrakEntityQuerySaveListener = zanjireMadrakEntityQuerySaveListener;
        deletZanjireMadrakEntity(mainETC, mainEC);
        new saveZanjireMadrakEntity().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureZanjireEntityRES);
    }


    private class saveZanjireMadrakEntity extends AsyncTask<StructureZanjireEntityRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureZanjireEntityRES... zanjireEntityRES) {
            int mainETC = zanjireEntityRES[0].getMainETC();
            int mainEC = zanjireEntityRES[0].getMainEC();
            try {
                for (StructureEntityDependencyRES entityDependencyRES : zanjireEntityRES[0].getPeyvast()) {
                    StructureZanjireMadrakEntityDB structureZanjireMadrakEntityDB = new StructureZanjireMadrakEntityDB(mainETC, mainEC, entityDependencyRES.getETC(), entityDependencyRES.getEC(), entityDependencyRES.getTitle(), entityDependencyRES.getEntityNumber(), entityDependencyRES.getImportEntityNumber(), entityDependencyRES.getExportEntityNumber(), entityDependencyRES.getEntityFarsiName(), entityDependencyRES.getCreationFullName(), entityDependencyRES.getCreationRoleName(), entityDependencyRES.getCreationDate(), ZanjireMadrakFileTypeEnum.PEYVAST);
                    mStructureZanjireMadrakEntityDBDao.create(structureZanjireMadrakEntityDB);
                }
                for (StructureEntityDependencyRES entityDependencyRES : zanjireEntityRES[0].getAtf()) {
                    StructureZanjireMadrakEntityDB structureZanjireMadrakEntityDB = new StructureZanjireMadrakEntityDB(mainETC, mainEC, entityDependencyRES.getETC(), entityDependencyRES.getEC(), entityDependencyRES.getTitle(), entityDependencyRES.getEntityNumber(), entityDependencyRES.getImportEntityNumber(), entityDependencyRES.getExportEntityNumber(), entityDependencyRES.getEntityFarsiName(), entityDependencyRES.getCreationFullName(), entityDependencyRES.getCreationRoleName(), entityDependencyRES.getCreationDate(), ZanjireMadrakFileTypeEnum.ATF);
                    mStructureZanjireMadrakEntityDBDao.create(structureZanjireMadrakEntityDB);
                }
                for (StructureEntityDependencyRES entityDependencyRES : zanjireEntityRES[0].getPeyro()) {
                    StructureZanjireMadrakEntityDB structureZanjireMadrakEntityDB = new StructureZanjireMadrakEntityDB(mainETC, mainEC, entityDependencyRES.getETC(), entityDependencyRES.getEC(), entityDependencyRES.getTitle(), entityDependencyRES.getEntityNumber(), entityDependencyRES.getImportEntityNumber(), entityDependencyRES.getExportEntityNumber(), entityDependencyRES.getEntityFarsiName(), entityDependencyRES.getCreationFullName(), entityDependencyRES.getCreationRoleName(), entityDependencyRES.getCreationDate(), ZanjireMadrakFileTypeEnum.PEIRO);
                    mStructureZanjireMadrakEntityDBDao.create(structureZanjireMadrakEntityDB);
                }
                for (StructureEntityDependencyRES entityDependencyRES : zanjireEntityRES[0].getDarErtebat()) {
                    StructureZanjireMadrakEntityDB structureZanjireMadrakEntityDB = new StructureZanjireMadrakEntityDB(mainETC, mainEC, entityDependencyRES.getETC(), entityDependencyRES.getEC(), entityDependencyRES.getTitle(), entityDependencyRES.getEntityNumber(), entityDependencyRES.getImportEntityNumber(), entityDependencyRES.getExportEntityNumber(), entityDependencyRES.getEntityFarsiName(), entityDependencyRES.getCreationFullName(), entityDependencyRES.getCreationRoleName(), entityDependencyRES.getCreationDate(), ZanjireMadrakFileTypeEnum.DARERTEBAT);
                    mStructureZanjireMadrakEntityDBDao.create(structureZanjireMadrakEntityDB);
                }
                zanjireMadrakEntityQuerySaveListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                zanjireMadrakEntityQuerySaveListener.onFailed(e.toString());
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

    public List<StructureHameshDB> getImageHamesh(int ETC, int EC) {
        QueryBuilder<StructureHameshDB, Integer> queryBuilder = hameshDao.queryBuilder();
        List<StructureHameshDB> structureHameshsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("hameshType", "Image");

            structureHameshsDB = queryBuilder.orderBy("CreationDate", true).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureHameshsDB;
    }

    public List<StructureHameshDB> getHamesh(int ETC, int EC, long start, long count) {
        QueryBuilder<StructureHameshDB, Integer> queryBuilder = hameshDao.queryBuilder();
        List<StructureHameshDB> structureHameshsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureHameshsDB = queryBuilder.orderBy("CreationDate", true).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureHameshsDB;
    }

    public List<StructureCartableDocumentContentDB> getDocumentContent(int ETC, int EC, DocumentContentFileTypeEnum fileTypeEnum) {
        QueryBuilder<StructureCartableDocumentContentDB, Integer> queryBuilder = mDocumentContentDao.queryBuilder();
        List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("fileTypeEnum", fileTypeEnum);
            structureCartableDocumentContentsDB = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableDocumentContentsDB;
    }

    public List<StructureCartableDocumentContentDB> getDocumentContent(int ETC, int EC) {
        QueryBuilder<StructureCartableDocumentContentDB, Integer> queryBuilder = mDocumentContentDao.queryBuilder();
        List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            structureCartableDocumentContentsDB = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableDocumentContentsDB;
    }

    public StructureCartableDocumentContentDB getDocumentContent(int id) {
        QueryBuilder<StructureCartableDocumentContentDB, Integer> queryBuilder = mDocumentContentDao.queryBuilder();
        StructureCartableDocumentContentDB structureCartableDocumentContentsDB = new StructureCartableDocumentContentDB();
        try {
            queryBuilder.where().eq("id", id);
            structureCartableDocumentContentsDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableDocumentContentsDB;
    }

    public List<StructureCartableDocumentActionsDB> getDocumentActions(int ETC) {
        QueryBuilder<StructureCartableDocumentActionsDB, Integer> queryBuilder = mCartableDocumentActionsDao.queryBuilder();
        List<StructureCartableDocumentActionsDB> documentActionsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC);
            documentActionsDB = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentActionsDB;
    }

    public List<StructureCartableDocumentActionsDB> getAllDocumentActions() {
        QueryBuilder<StructureCartableDocumentActionsDB, Integer> queryBuilder = mCartableDocumentActionsDao.queryBuilder();
        List<StructureCartableDocumentActionsDB> documentActionsDB = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", -1);
            documentActionsDB = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentActionsDB;
    }

    public List<StructureCartableDocumentActionsDB> getAllDocumentActionsForCardTable(boolean isActiveForCardTable) {
        QueryBuilder<StructureCartableDocumentActionsDB, Integer> queryBuilder = mCartableDocumentActionsDao.queryBuilder();
        List<StructureCartableDocumentActionsDB> documentActionsDB = new ArrayList<>();
        int activeForCardTable = 0;
        if (isActiveForCardTable) {
            activeForCardTable = 1;
        }
        try {
            queryBuilder.where().eq("ETC", -1).and().eq("IsActiveForCardTable", activeForCardTable);
            documentActionsDB = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentActionsDB;
    }

    public List<StructureCartableDocumentActionsDB> getAllDocumentActionsForSend(boolean isActiveForSend) {
        QueryBuilder<StructureCartableDocumentActionsDB, Integer> queryBuilder = mCartableDocumentActionsDao.queryBuilder();
        List<StructureCartableDocumentActionsDB> documentActionsDB = new ArrayList<>();
        int activeForSend = 0;
        if (isActiveForSend) {
            activeForSend = 1;
        }
        try {
            queryBuilder.where().eq("ETC", -1).and().eq("IsActiveForSend", activeForSend);
            documentActionsDB = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentActionsDB;
    }

    public List<StructureCartableDocumentSignatureDB> getDocumentSignatures(int ETC) {
        QueryBuilder<StructureCartableDocumentSignatureDB, Integer> queryBuilder = mCartableDocumentSignatureDao.queryBuilder();
        List<StructureCartableDocumentSignatureDB> structureCartableDocumentSignatureDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC);
            structureCartableDocumentSignatureDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableDocumentSignatureDBS;
    }

    public List<StructureCartableDocumentSignatureDB> getAllDocumentSignatures() {
        QueryBuilder<StructureCartableDocumentSignatureDB, Integer> queryBuilder = mCartableDocumentSignatureDao.queryBuilder();
        List<StructureCartableDocumentSignatureDB> structureCartableDocumentSignatureDBS = new ArrayList<>();
        try {
            structureCartableDocumentSignatureDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableDocumentSignatureDBS;
    }

    public List<StructureCartableHistoryDB> getCartableHistory(int ETC, int EC, long start, long count) {
        QueryBuilder<StructureCartableHistoryDB, Integer> queryBuilder = historyDao.queryBuilder();
        List<StructureCartableHistoryDB> structureCartableHistoryDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);

            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            structureCartableHistoryDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableHistoryDBS;
    }

    public List<StructureZanjireMadrakFileDB> getZanjireMadrak(int ETC, int EC, ZanjireMadrakFileTypeEnum fileTypeEnum) {
        QueryBuilder<StructureZanjireMadrakFileDB, Integer> queryBuilder = zanjireMadrakFileDao.queryBuilder();
        List<StructureZanjireMadrakFileDB> structureZanjireMadrakFileDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("fileTypeEnum", fileTypeEnum);

            structureZanjireMadrakFileDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureZanjireMadrakFileDBS;
    }

    public List<StructureZanjireMadrakFileDB> getZanjireMadrakAllType(int ETC, int EC) {
        QueryBuilder<StructureZanjireMadrakFileDB, Integer> queryBuilder = zanjireMadrakFileDao.queryBuilder();
        List<StructureZanjireMadrakFileDB> structureZanjireMadrakFileDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC);

            structureZanjireMadrakFileDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureZanjireMadrakFileDBS;
    }

    public List<StructureZanjireMadrakEntityDB> getZanjireMadrakEntity(int mainETC, int mainEC, ZanjireMadrakFileTypeEnum fileTypeEnum) {
        QueryBuilder<StructureZanjireMadrakEntityDB, Integer> queryBuilder = mStructureZanjireMadrakEntityDBDao.queryBuilder();
        List<StructureZanjireMadrakEntityDB> zanjireMadrakEntityDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("mainETC", mainETC).and().eq("mainEC", mainEC).and().eq("fileTypeEnum", fileTypeEnum);

            zanjireMadrakEntityDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zanjireMadrakEntityDBS;
    }

    public List<StructureZanjireMadrakEntityDB> getZanjireMadrakEntityAllType(int mainETC, int mainEC) {
        QueryBuilder<StructureZanjireMadrakEntityDB, Integer> queryBuilder = mStructureZanjireMadrakEntityDBDao.queryBuilder();
        List<StructureZanjireMadrakEntityDB> zanjireMadrakEntityDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("mainETC", mainETC).and().eq("mainEC", mainEC);

            zanjireMadrakEntityDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zanjireMadrakEntityDBS;
    }


    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorsQueueGroup() {
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("isLock", false);
            structureDocumentOperatorsQueueDBS = queryBuilder.groupBy("ETC").groupBy("EC").distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentOperatorsQueueDBS;
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorQueue(int ETC, int EC) {
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false);
            structureDocumentOperatorsQueueDBS = queryBuilder.orderBy("priority", true).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentOperatorsQueueDBS;
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorQueue(int ETC, int EC, boolean isLock) {
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", isLock);
            structureDocumentOperatorsQueueDBS = queryBuilder.orderBy("priority", true).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentOperatorsQueueDBS;
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorErrorMessage(int ETC, int EC) {
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", QueueStatus.ERROR);
            structureDocumentOperatorsQueueDBS = queryBuilder.orderBy("priority", true).distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentOperatorsQueueDBS;
    }

    public long getDocumentOperatorQueueNotSendedCount(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {

        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("documentOpratoresTypeEnum", documentOpratoresTypeEnum).and().ne("queueStatus", QueueStatus.SENDED);
            count = mDocumentOperatorsQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getDocumentOperatorQueueNotSendedCount(int ETC, int EC) {

        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED);
            count = mDocumentOperatorsQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getDocumentOperatorQueueNotSendedCount() {

        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED);

            count = mDocumentOperatorsQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    public void setDocumentOperatorQueueLock(int ETC, int EC, boolean isLock) {
        UpdateBuilder<StructureDocumentOperatorsQueueDB, Integer> updateBuilder = mDocumentOperatorsQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            updateBuilder.updateColumnValue("isLock", isLock);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentOperatorQueueStatus(int id, QueueStatus queueStatus) {
        UpdateBuilder<StructureDocumentOperatorsQueueDB, Integer> updateBuilder = mDocumentOperatorsQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", queueStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentOperatorQueueStatus(int ETC, int EC, QueueStatus lastStatus, QueueStatus newStatus) {
        UpdateBuilder<StructureDocumentOperatorsQueueDB, Integer> updateBuilder = mDocumentOperatorsQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", lastStatus);
            updateBuilder.updateColumnValue("queueStatus", newStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentOperatorQueueStatusETC_EC(int lastETC, int lastEC, int newETC, int newEC, boolean isLock) {
        UpdateBuilder<StructureDocumentOperatorsQueueDB, Integer> updateBuilder = mDocumentOperatorsQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", lastETC).and().eq("EC", lastEC);
            updateBuilder.updateColumnValue("ETC", newETC);
            updateBuilder.updateColumnValue("EC", newEC);
            updateBuilder.updateColumnValue("isLock", isLock);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setErrorToDocumentOperatorQueue(int id, String strError) {
        UpdateBuilder<StructureDocumentOperatorsQueueDB, Integer> updateBuilder = mDocumentOperatorsQueueDao.updateBuilder();
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

    public boolean deletDocumentOperatorsQueue(int ETC, int EC) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureDocumentOperatorsQueueDB, Integer> deleteBuilder = mDocumentOperatorsQueueDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletDocumentOperatorsQueue(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureDocumentOperatorsQueueDB, Integer> deleteBuilder = mDocumentOperatorsQueueDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("documentOpratoresTypeEnum", documentOpratoresTypeEnum);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletDocumentOperatorsQueue(int ETC, int EC, boolean isLock, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureDocumentOperatorsQueueDB, Integer> deleteBuilder = mDocumentOperatorsQueueDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", isLock).and().eq("documentOpratoresTypeEnum", documentOpratoresTypeEnum);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorsQueueGroup(int ETC, int EC, DocumentOperatoresTypeEnum opratoresTypeEnum) {

        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("documentOpratoresTypeEnum", opratoresTypeEnum);
            structureDocumentOperatorsQueueDBS = queryBuilder.groupBy("ETC").groupBy("EC").distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentOperatorsQueueDBS;
    }

    public boolean isDocumentOperatorNotSendedExist(int ETC, int EC, DocumentOperatoresTypeEnum opratoresTypeEnum) {
        boolean existing = false;
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        try {

            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("documentOpratoresTypeEnum", opratoresTypeEnum).and().ne("queueStatus", QueueStatus.SENDED));
            queryBuilder.setCountOf(true);
            long count = mDocumentOperatorsQueueDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean isDocumentOperatorNotSendedExist(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        try {

            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED));
            queryBuilder.setCountOf(true);
            long count = mDocumentOperatorsQueueDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean isDocumentOperatorHasError(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureDocumentOperatorsQueueDB, Integer> queryBuilder = mDocumentOperatorsQueueDao.queryBuilder();
        try {

            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", QueueStatus.ERROR));
            queryBuilder.setCountOf(true);
            long count = mDocumentOperatorsQueueDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileQueueGroup() {
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("isLock", false);

            structureDocumentAttachFileQueueDBS = queryBuilder.groupBy("ETC").groupBy("EC").distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileNotSendedQueue(int ETC, int EC) {
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED);
            structureDocumentAttachFileQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileNotSendedQueue(int ETC, int EC, boolean isLock) {
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", isLock).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED);
            structureDocumentAttachFileQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileQueue(int ETC, int EC, boolean isLock) {
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", isLock);
            structureDocumentAttachFileQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileQueue(int ETC, int EC) {
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueueDBS = new ArrayList<>();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false);
            structureDocumentAttachFileQueueDBS = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public long getDocumentAttachFileQueueQueueNotSendedCount(int ETC, int EC) {

        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED);
            count = mDocumentAttachFileQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getDocumentAttachFileQueueQueueNotSendedCount() {

        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        long count = 0;
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().ne("queueStatus", QueueStatus.SENDED);
            count = mDocumentAttachFileQueueDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public StructureDocumentAttachFileQueueDB getDocumentAttachFileErrorMessage(int ETC, int EC) {

        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        StructureDocumentAttachFileQueueDB structureDocumentAttachFileQueueDBS = new StructureDocumentAttachFileQueueDB();
        try {
            queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", QueueStatus.ERROR);
            structureDocumentAttachFileQueueDBS = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureDocumentAttachFileQueueDBS;
    }

    public void updateDocumentAttachFileQueueStatus(int id, QueueStatus queueStatus) {
        UpdateBuilder<StructureDocumentAttachFileQueueDB, Integer> updateBuilder = mDocumentAttachFileQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", queueStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentAttachFileQueueStatus(int ETC, int EC, QueueStatus lastStatus, QueueStatus newStatus) {
        UpdateBuilder<StructureDocumentAttachFileQueueDB, Integer> updateBuilder = mDocumentAttachFileQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", lastStatus);
            updateBuilder.updateColumnValue("queueStatus", newStatus);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateDocumentAttachFileQueueETC_EC(int lastETC, int lastEC, int newETC, int newEC, boolean isLock) {
        UpdateBuilder<StructureDocumentAttachFileQueueDB, Integer> updateBuilder = mDocumentAttachFileQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", lastETC).and().eq("EC", lastEC);
            updateBuilder.updateColumnValue("ETC", newETC);
            updateBuilder.updateColumnValue("EC", newEC);
            updateBuilder.updateColumnValue("isLock", isLock);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDocumentAttachFileQueueLock(int ETC, int EC, boolean isLock) {
        UpdateBuilder<StructureDocumentAttachFileQueueDB, Integer> updateBuilder = mDocumentAttachFileQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            updateBuilder.updateColumnValue("isLock", isLock);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isDocumentAttachFileNotSendedExist(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().ne("queueStatus", QueueStatus.SENDED));
            queryBuilder.setCountOf(true);
            long count = mDocumentAttachFileQueueDao.countOf(queryBuilder.prepare());
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public void setErrorToDocumentAttachFileQueue(int id, String strError) {
        UpdateBuilder<StructureDocumentAttachFileQueueDB, Integer> updateBuilder = mDocumentAttachFileQueueDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("queueStatus", QueueStatus.ERROR);
            updateBuilder.updateColumnValue("strError", strError);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletDocumentAttachFileQueue(int ETC, int EC) {
        List<StructureDocumentAttachFileQueueDB> attachFileQueuesDB = getDocumentAttachFileQueue(ETC, EC);
        for (int i = 0; i < attachFileQueuesDB.size(); i++) {
            try {
                File file = new File(attachFileQueuesDB.get(i).getFile_path());
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureDocumentAttachFileQueueDB, Integer> deleteBuilder = mDocumentAttachFileQueueDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }


    public boolean isDocumentAttachFileHasError(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureDocumentAttachFileQueueDB, Integer> queryBuilder = mDocumentAttachFileQueueDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC).and().eq("isLock", false).and().eq("queueStatus", QueueStatus.ERROR));
            queryBuilder.setCountOf(true);
            long count = mDocumentAttachFileQueueDao.countOf(queryBuilder.prepare());
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }


    public List<StructureInboxDocumentDB> getCartableDocuments(int actionCode, Status status, long start, long count) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = queryBuilder.where();
        List<StructureInboxDocumentDB> structureInboxDocumentsDB = new ArrayList<>();
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            where.eq("ActionCode", actionCode).and().eq("isConfirm", false).and().eq("UserRoleID", userRoleID);
            if (status != null) {
                where.and().eq("status", status);
            }
            if (count > 0) {
                queryBuilder.offset(start).limit(count);
            }
            queryBuilder.setWhere(where);
            structureInboxDocumentsDB = queryBuilder.distinct().orderBy("ReceiveDate", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentsDB;
    }


    public StructureInboxDocumentDB getFirstItemCartableDocument() {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        try {
            structureInboxDocumentDB = inboxDocumentQB.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public StructureInboxDocumentDB getLastItemCartableDocument() {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = inboxDocumentQB.where();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            where.eq("isConfirm", false).and().eq("UserRoleID", userRoleID);
            structureInboxDocumentDB = inboxDocumentQB.distinct().orderBy("id", false).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public StructureInboxDocumentDB getCartableDocument(int ETC, int EC) {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        try {
            inboxDocumentQB.where().eq("EntityTypeCode", ETC).and().eq("EntityCode", EC);
            structureInboxDocumentDB = inboxDocumentQB.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public StructureInboxDocumentDB getNewCartableDocument() {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            inboxDocumentQB.where().eq("isNew", true).and().eq("IsRead", false).and().eq("UserRoleID", userRoleID);
            structureInboxDocumentDB = inboxDocumentQB.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public List<StructureInboxDocumentDB> getNewCartableDocuments() {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            inboxDocumentQB.where().eq("isNew", true).and().eq("IsRead", false).and().eq("UserRoleID", userRoleID);
            structureInboxDocumentDB = inboxDocumentQB.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
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


    public StructureInboxDocumentDB getCartableDocumentWithReceiveCode(int receiveCode) {
        QueryBuilder<StructureInboxDocumentDB, Integer> inboxDocumentQB = cartableDocumentDao.queryBuilder();
        StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
        try {
            inboxDocumentQB.where().eq("ReceiverCode", receiveCode);
            structureInboxDocumentDB = inboxDocumentQB.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureInboxDocumentDB;
    }

    public void updateCartableDocumentIsNewStatus(int id, boolean isNew) {
        UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
        try {
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("isNew", isNew);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllCartableDocumentIsNewStatusToFalse() {
        UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
        try {
            updateBuilder.updateColumnValue("isNew", false);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long getNewCartableDocumentCount() {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        long count = 0;
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("isNew", true).and().eq("IsRead", false).and().eq("UserRoleID", userRoleID);
            count = cartableDocumentDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }
        return count;
    }


    public ArrayList<StructureCartableAction> getCartableAction(boolean isPin, @Nullable Status status, @Nullable Boolean isNew) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = queryBuilder.where();
        List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
        ArrayList<StructureCartableAction> cartableActions = new ArrayList<>();
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            where.eq("Pin", isPin).and().eq("isConfirm", false).and().eq("UserRoleID", userRoleID);
            if (isNew != null) {
                where.and().eq("isNew", isNew.booleanValue());
            }
            queryBuilder.setWhere(where);
            structureInboxDocumentDB = queryBuilder.groupBy("ActionCode").distinct().query();
            for (StructureInboxDocumentDB inboxDocumentDB : structureInboxDocumentDB) {
                StructureCartableAction structureCartableAction = new StructureCartableAction(inboxDocumentDB.getActionCode(), inboxDocumentDB.getActionName(), getCartableDocumentCount(inboxDocumentDB.getActionCode(), status, isNew), inboxDocumentDB.isPin());
                if (structureCartableAction.getCount() > 0) {
                    cartableActions.add(structureCartableAction);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return cartableActions;
    }

    public long getCartableDocumentCount(int ActionCode, @Nullable Status status, @Nullable Boolean isNew) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = queryBuilder.where();
        int userRoleID = getFarzinPrefrences().getRoleID();
        long count = 0;
        try {
            where.eq("ActionCode", ActionCode).and().eq("UserRoleID", userRoleID);
            if (status != null) {
                if (status == Status.UnRead || status == Status.IsNew) {
                    //  where.and(where.eq("ActionCode", ActionCode), where.or(where.eq("IsRead", Status.UnRead), where.eq("status", Status.IsNew)));
                    where.and().eq("IsRead", false).and().eq("isConfirm", false);
                } else {
                    where.and().eq("IsRead", true).and().eq("isConfirm", false);
                }
            } else {
                where.and().eq("isConfirm", false);
            }
            if (isNew != null) {
                where.and().eq("isNew", isNew.booleanValue());
            }
            queryBuilder.setWhere(where);
            queryBuilder.setCountOf(true);
            count = cartableDocumentDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public long getCartableDocumentCount() {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        long count = 0;
        int userRoleID = getFarzinPrefrences().getRoleID();
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("isConfirm", false).and().eq("UserRoleID", userRoleID);
            count = cartableDocumentDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }


    public void deletCartableDocumentAllContent(int receiveCode) {
        Log.i("LOG", "receiveCode= " + receiveCode);
        StructureInboxDocumentDB item = getCartableDocumentWithReceiveCode(receiveCode);
        deletCartableDocument(receiveCode);
        if (!IsMoreThanOneDocument(item.getEntityTypeCode(), item.getEntityCode())) {
            deletCartableDocumentContent(item.getEntityTypeCode(), item.getEntityCode());
            deletZanjireMadrak(item.getEntityTypeCode(), item.getEntityCode(), new ListenerDelet() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailed(String error) {

                }
            });
            deletCartableHameshList(item.getEntityTypeCode(), item.getEntityCode());
            deletCartableHistoryList(item.getEntityTypeCode(), item.getEntityCode());
        }

    }

    public boolean deletCartableDocumentContent(int ETC, int EC) {
        boolean isDelet = false;
        List<StructureCartableDocumentContentDB> cartableDocumentContentsDB = getDocumentContent(ETC, EC);

        try {
            DeleteBuilder<StructureCartableDocumentContentDB, Integer> deleteBuilder = mDocumentContentDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            for (StructureCartableDocumentContentDB cartableDocumentContentDB : cartableDocumentContentsDB) {
                File file = new File(cartableDocumentContentDB.getFile_path());
                if (file.exists()) {
                    file.delete();
                }
            }

            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletCartableDocumentContent(int id) {
        boolean isDelet = false;
        StructureCartableDocumentContentDB cartableDocumentContentsDB = getDocumentContent(id);

        try {
            DeleteBuilder<StructureCartableDocumentContentDB, Integer> deleteBuilder = mDocumentContentDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            File file = new File(cartableDocumentContentsDB.getFile_path());
            if (file.exists()) {
                file.delete();
            }

            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletCartableDocument(int ReceiverCode) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureInboxDocumentDB, Integer> deleteBuilder = cartableDocumentDao.deleteBuilder();
            deleteBuilder.where().eq("ReceiverCode", ReceiverCode);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public void deletZanjireMadrak(int ETC, int EC, ListenerDelet listenerDelet) {
        List<StructureZanjireMadrakFileDB> structureZanjireMadrakFileDBS = getZanjireMadrakAllType(ETC, EC);
        for (StructureZanjireMadrakFileDB zanjireMadrakFileDB : structureZanjireMadrakFileDBS) {
            if (zanjireMadrakFileDB.getFile_path().length() < 256) {
                File file = new File(zanjireMadrakFileDB.getFile_path());
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        try {
            DeleteBuilder<StructureZanjireMadrakFileDB, Integer> deleteBuilder = zanjireMadrakFileDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            listenerDelet.onSuccess();
        } catch (SQLException e) {
            e.printStackTrace();
            listenerDelet.onFailed(e.toString());
        }
    }

    public void deletZanjireMadrakEntity(int mainETC, int mainEC) {

        try {
            DeleteBuilder<StructureZanjireMadrakEntityDB, Integer> deleteBuilder = mStructureZanjireMadrakEntityDBDao.deleteBuilder();
            deleteBuilder.where().eq("mainETC", mainETC).and().eq("mainEC", mainEC);
            deleteBuilder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deletCartableHameshList(int ETC, int EC) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureHameshDB, Integer> deleteBuilder = hameshDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletCartableHistoryList(int ETC, int EC) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureCartableHistoryDB, Integer> deleteBuilder = historyDao.deleteBuilder();
            deleteBuilder.where().eq("ETC", ETC).and().eq("EC", EC);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
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

    public void UpdateInboxDocumentConfirm(int receiverCode, boolean confirm) {
        try {
            UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
            updateBuilder.where().eq("ReceiverCode", receiverCode);
            updateBuilder.updateColumnValue("isConfirm", confirm);
            updateBuilder.update();
            App.needToReGetCartableDocumentList = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCartableDocumentStatus(int id, Status status) {
        try {
            UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("status", status);
            if (status == Status.READ) {
                updateBuilder.updateColumnValue("IsRead", true);
            }
            updateBuilder.update();
            App.needToReGetCartableDocumentList = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateAllNewCartableDocumentStatusToUnreadStatus() {
        try {
            UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
            updateBuilder.where().eq("status", Status.IsNew);
            updateBuilder.updateColumnValue("status", Status.UnRead);
            updateBuilder.update();
            Log.i("Notify", "CartableDocument Status change to UnRead");
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

    public boolean IsDocumentContentExist(int Etc, int Ec) {
        boolean existing = false;
        QueryBuilder<StructureCartableDocumentContentDB, Integer> queryBuilder = mDocumentContentDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", Etc).and().eq("EC", Ec));
            queryBuilder.setCountOf(true);
            long count = mDocumentContentDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsDocumentContentExist(int Etc, int Ec, DocumentContentFileTypeEnum fileTypeEnum) {
        boolean existing = false;
        QueryBuilder<StructureCartableDocumentContentDB, Integer> queryBuilder = mDocumentContentDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", Etc).and().eq("EC", Ec).and().eq("fileTypeEnum", fileTypeEnum));
            queryBuilder.setCountOf(true);
            long count = mDocumentContentDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsDocumentActionsExist(int Etc) {
        boolean existing = false;
        QueryBuilder<StructureCartableDocumentActionsDB, Integer> queryBuilder = mCartableDocumentActionsDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", Etc));
            queryBuilder.setCountOf(true);
            long count = mCartableDocumentActionsDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsDocumentSignatureExist(int Etc) {
        boolean existing = false;
        QueryBuilder<StructureCartableDocumentSignatureDB, Integer> queryBuilder = mCartableDocumentSignatureDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", Etc));
            queryBuilder.setCountOf(true);
            long count = mCartableDocumentSignatureDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsDocumentExist(int receiverCode) {
        boolean existing = false;
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ReceiverCode", receiverCode));
            queryBuilder.setCountOf(true);
            long count = cartableDocumentDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsMoreThanOneDocument(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("EntityTypeCode", ETC).and().eq("EntityCode", EC));
            queryBuilder.setCountOf(true);
            long count = cartableDocumentDao.countOf(queryBuilder.prepare());
            // long count = queryBuilder.countOf();
            if (count > 1) existing = true;
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

    public boolean IsZanjireMadrakExist(int ETC, int EC) {
        boolean existing = false;
        QueryBuilder<StructureZanjireMadrakFileDB, Integer> queryBuilder = zanjireMadrakFileDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("ETC", ETC).and().eq("EC", EC));
            queryBuilder.setCountOf(true);
            long count = zanjireMadrakFileDao.countOf(queryBuilder.prepare());
            if (count > 0) existing = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existing;
    }

    public boolean IsZanjireMadrakEntityExist(int mainETC, int mainEC) {
        boolean existing = false;
        QueryBuilder<StructureZanjireMadrakEntityDB, Integer> queryBuilder = mStructureZanjireMadrakEntityDBDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("mainETC", mainETC).and().eq("mainEC", mainEC));
            queryBuilder.setCountOf(true);
            long count = mStructureZanjireMadrakEntityDBDao.countOf(queryBuilder.prepare());
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






