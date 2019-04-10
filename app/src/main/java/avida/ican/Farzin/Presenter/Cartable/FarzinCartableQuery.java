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

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionsQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentTaeedQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableSendQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.GetDocumentActionsFromServerListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentContentBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableHistoryBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentTaeedQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableSendQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureFileRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableDocumentContent;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.ListenerDelet;
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
    private CartableHistoryQuerySaveListener cartableHistoryQuerySaveListener;
    private ZanjireMadrakQuerySaveListener zanjireMadrakQuerySaveListener;
    private CartableDocumentTaeedQueueQuerySaveListener cartableDocumentTaeedQueueQuerySaveListener;
    private OpticalPenQueueQuerySaveListener opticalPenQueueQuerySaveListener;
    private CartableSendQuerySaveListener cartableSendQuerySaveListener;
    private CartableDocumentContentQuerySaveListener documentContentQuerySaveListener;
    private ChangeXml changeXml;
    private Status status = Status.WAITING;
    private boolean SendFromApp = false;
    //_______________________***Dao***______________________________

    private Dao<StructureInboxDocumentDB, Integer> cartableDocumentDao = null;
    private Dao<StructureHameshDB, Integer> hameshDao = null;
    private Dao<StructureCartableHistoryDB, Integer> historyDao = null;
    private Dao<StructureZanjireMadrakFileDB, Integer> zanjireMadrakFileDao = null;
    private Dao<StructureCartableDocumentTaeedQueueDB, Integer> mCartableDocumentTaeedQueueDao = null;
    private Dao<StructureOpticalPenQueueDB, Integer> mOpticalPenQueueDao = null;
    private Dao<StructureCartableDocumentActionsDB, Integer> mCartableDocumentActionsDao = null;
    private Dao<StructureCartableSendQueueDB, Integer> mCartableSendQueueDao = null;
    private Dao<StructureCartableDocumentContentDB, Integer> mDocumentContentDao = null;


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
            mCartableDocumentTaeedQueueDao = App.getFarzinDatabaseHelper().getCartableDocumentTaeedDao();
            mOpticalPenQueueDao = App.getFarzinDatabaseHelper().getOpticalPenDao();
            mCartableDocumentActionsDao = App.getFarzinDatabaseHelper().getDocumentActionsDao();
            mCartableSendQueueDao = App.getFarzinDatabaseHelper().getCartableSendQueueDao();
            mDocumentContentDao = App.getFarzinDatabaseHelper().getDocumentContentDao();
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

    public void saveHamesh(StructureHameshRES structureHameshRES, int ETC, int EC, final HameshQuerySaveListener hameshQuerySaveListener) {
        structureHameshRES.setETC(ETC);
        structureHameshRES.setEC(EC);
        this.hameshQuerySaveListener = hameshQuerySaveListener;
        new saveHamesh().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureHameshRES);
    }

    public void saveCartableDocumentContent(StructureCartableDocumentContentBND cartableDocumentContentBND, final CartableDocumentContentQuerySaveListener documentContentQuerySaveListener) {
        this.documentContentQuerySaveListener = documentContentQuerySaveListener;
        if (IsDocumentContentExist(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC())) {

            deletCartableDocumentContent(cartableDocumentContentBND.getETC(), cartableDocumentContentBND.getEC());
        }

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

    public void saveCartableDocumentTaeedQueue(int receiveCode, CartableDocumentTaeedQueueQuerySaveListener cartableDocumentTaeedQueueQuerySaveListener) {

        this.cartableDocumentTaeedQueueQuerySaveListener = cartableDocumentTaeedQueueQuerySaveListener;
        if (IsCartableDocumentTaeedQueueExist(receiveCode)) {
            cartableDocumentTaeedQueueQuerySaveListener.onExisting();
        } else {
            new saveCartableDocumentTaeed().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, receiveCode);
        }

    }

    public void saveOpticalPenQueue(StructureOpticalPenREQ opticalPenREQ, OpticalPenQueueQuerySaveListener opticalPenQueueQuerySaveListener) {
        this.opticalPenQueueQuerySaveListener = opticalPenQueueQuerySaveListener;
        new saveOpticalPenQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, opticalPenREQ);

    }

    public void saveCartableSendQueue(StructureAppendREQ structureAppendREQ, CartableSendQuerySaveListener cartableSendQuerySaveListener) {
        this.cartableSendQuerySaveListener = cartableSendQuerySaveListener;
        new saveCartableSendQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureAppendREQ);

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
            receiveDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getReceiveDate()));
            expireDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getExpireDate()));
            LastChangeViewStatesDate = new Date(CustomFunction.StandardizeTheDateFormat(structureInboxDocumentRES[0].getLastChangeViewStatesDate()));
            getFarzinPrefrences().putCartableDocumentDataSyncDate(structureInboxDocumentRES[0].getReceiveDate());
            final StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB(structureInboxDocumentRES[0], importDate, exportDate, receiveDate, expireDate, LastChangeViewStatesDate, status, false);
            try {
                cartableDocumentDao.create(structureInboxDocumentDB);

                if (SendFromApp) {

                }


                if (IsDocumentActionsExist(structureInboxDocumentDB.getEntityTypeCode())) {
                    cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
                } else {
                    App.getHandlerMainThread().post(new Runnable() {
                        @Override
                        public void run() {
                            new CartableDocumentActionsPresenter(structureInboxDocumentDB.getEntityTypeCode()).GetDocumentActionsFromServer(new GetDocumentActionsFromServerListener() {
                                @Override
                                public void onSuccess() {
                                    cartableDocumentQuerySaveListener.onSuccess(getCartableDocument(structureInboxDocumentDB.getId()));
                                }

                                @Override
                                public void onFailed(String message) {
                                    cartableDocumentQuerySaveListener.onFailed("can not get actions from server");
                                }

                                @Override
                                public void onCancel() {
                                    cartableDocumentQuerySaveListener.onCancel();
                                }
                            });
                        }
                    });


                }


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


    private class saveCartableDocumentContent extends AsyncTask<StructureCartableDocumentContentBND, Void, Void> {

        @Override
        protected Void doInBackground(StructureCartableDocumentContentBND... cartableDocumentContentBND) {
            String fileName = "CDC" + cartableDocumentContentBND[0].getETC() + "" + cartableDocumentContentBND[0].getEC() + CustomFunction.getRandomUUID();
            //fileName = fileName.replace(" ", "");
            String filePath = "";
            Log.i("largeFile", "getFileAsStringBuilder().length()= " + cartableDocumentContentBND[0].getFileAsStringBuilder().length());
            if (cartableDocumentContentBND[0].getFileAsStringBuilder().length() < 256) {
                filePath = cartableDocumentContentBND[0].getFileAsStringBuilder().toString();
                fileName = CustomFunction.getFileName(filePath);

                //}
            } else {
                filePath = new CustomFunction().saveFileToStorage(cartableDocumentContentBND[0].getFileAsStringBuilder(), fileName);
            }
            StructureCartableDocumentContentDB structureCartableDocumentContentDB = new StructureCartableDocumentContentDB(fileName, filePath, ".pdf", cartableDocumentContentBND[0].getETC(), cartableDocumentContentBND[0].getEC());
            try {
                mDocumentContentDao.create(structureCartableDocumentContentDB);
                documentContentQuerySaveListener.onSuccess(getDocumentContent(cartableDocumentContentBND[0].getETC(), cartableDocumentContentBND[0].getEC()));
            } catch (SQLException e) {
                e.printStackTrace();
                documentContentQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private class saveDocumentAction extends AsyncTask<StructureCartableDocumentActionRES, Void, Void> {

        @Override
        protected Void doInBackground(StructureCartableDocumentActionRES... cartableDocumentActionRES) {
            StructureCartableDocumentActionsDB cartableDocumentActionsDB = new StructureCartableDocumentActionsDB(cartableDocumentActionRES[0].getETC(), cartableDocumentActionRES[0].getActionCode(), cartableDocumentActionRES[0].getActionName(), cartableDocumentActionRES[0].getActionOrder(), cartableDocumentActionRES[0].getFarsiDescription());
            try {
                mCartableDocumentActionsDao.create(cartableDocumentActionsDB);
                cartableDocumentActionsQuerySaveListener.onSuccess(cartableDocumentActionsDB);
            } catch (SQLException e) {
                e.printStackTrace();
                hameshQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }


    private class saveCartableDocumentTaeed extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... receiverCode) {
            StructureCartableDocumentTaeedQueueDB cartableDocumentTaeedQueueDB = new StructureCartableDocumentTaeedQueueDB(receiverCode[0]);
            try {
                mCartableDocumentTaeedQueueDao.create(cartableDocumentTaeedQueueDB);
                UpdateInboxDocumentTaeed(receiverCode[0], true);
                cartableDocumentTaeedQueueQuerySaveListener.onSuccess(receiverCode[0]);
            } catch (SQLException e) {
                e.printStackTrace();
                cartableDocumentTaeedQueueQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private class saveOpticalPenQueue extends AsyncTask<StructureOpticalPenREQ, Void, Void> {

        @Override
        protected Void doInBackground(StructureOpticalPenREQ... opticalPenREQS) {

            StructureOpticalPenQueueDB opticalPenQueueDB = new StructureOpticalPenQueueDB(opticalPenREQS[0].getETC(), opticalPenREQS[0].getEC(), opticalPenREQS[0].getBfile(), opticalPenREQS[0].getStrExtention(), opticalPenREQS[0].getHameshtitle(), opticalPenREQS[0].isHiddenHamesh());
            try {
                mOpticalPenQueueDao.create(opticalPenQueueDB);
                opticalPenQueueQuerySaveListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                opticalPenQueueQuerySaveListener.onFailed(e.toString());
                App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                return null;
            }

            return null;
        }
    }

    private class saveCartableSendQueue extends AsyncTask<StructureAppendREQ, Void, Void> {
        @Override
        protected Void doInBackground(StructureAppendREQ... structureAppendREQS) {
            StructureCartableSendQueueDB structureCartableSendQueueDB = new StructureCartableSendQueueDB();
            structureCartableSendQueueDB.setETC(structureAppendREQS[0].getETC());
            structureCartableSendQueueDB.setEC(structureAppendREQS[0].getEC());
            String data = new CustomFunction().ConvertObjectToString(structureAppendREQS);
            structureCartableSendQueueDB.setStrStructureAppendREQ(data);
            try {
                mCartableSendQueueDao.create(structureCartableSendQueueDB);
                cartableSendQuerySaveListener.onSuccess();
            } catch (SQLException e) {
                e.printStackTrace();
                cartableSendQuerySaveListener.onFailed(e.toString());
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
                    //fileName = fileName.replace(" ", "");
                    if (stringBuilder.length() < 256) {
                        //if (stringBuilder.indexOf(App.DEFAULTPATH) > 0) {
                        //filePath = CustomFunction.reNameFile(stringBuilder.toString(), fileName);
                        filePath = stringBuilder.toString();
                        //fileName = CustomFunction.getFileName(filePath);
                        //}
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }

                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), ZanjireMadrakFileTypeEnum.PEYVAST, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
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
                    //fileName = fileName.replace(" ", "");
                    if (stringBuilder.length() < 256) {
                        //if (stringBuilder.indexOf(App.DEFAULTPATH) > 0) {
                        //filePath = CustomFunction.reNameFile(stringBuilder.toString(), fileName);
                        filePath = stringBuilder.toString();
                        //fileName = CustomFunction.getFileName(filePath);
                        //}
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), ZanjireMadrakFileTypeEnum.ATF, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
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
                    //fileName = fileName.replace(" ", "");
                    if (stringBuilder.length() < 256) {
                        // if (stringBuilder.indexOf(App.DEFAULTPATH) > 0) {
                        // filePath = CustomFunction.reNameFile(stringBuilder.toString(), fileName);
                        filePath = stringBuilder.toString();
                        //fileName = CustomFunction.getFileName(filePath);
                        // }
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), ZanjireMadrakFileTypeEnum.DARERTEBAT, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
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
                    // fileName = fileName.replace(" ", "");
                    if (stringBuilder.length() < 256) {
                        // if (stringBuilder.indexOf(App.DEFAULTPATH) > 0) {
                        //filePath = CustomFunction.reNameFile(stringBuilder.toString(), fileName);
                        filePath = stringBuilder.toString();
                        //fileName = CustomFunction.getFileName(filePath);
                        // }
                    } else {
                        filePath = new CustomFunction().saveFileToStorage(stringBuilder, fileName);
                    }
                    StructureZanjireMadrakFileDB structureZanjireMadrakFileDB = new StructureZanjireMadrakFileDB(fileName, filePath, structureFileRES.getFileExtension(), ZanjireMadrakFileTypeEnum.PEIRO, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
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
                        StructureCartableDocumentContentBND cartableDocumentContentBND = new StructureCartableDocumentContentBND(stringBuilder, zanjireMadrakRES[0].getETC(), zanjireMadrakRES[0].getEC());
                        structureCartableDocumentContentBNDS.add(cartableDocumentContentBND);
                    }
                    saveCartableDocumentContent(structureCartableDocumentContentBNDS, new CartableDocumentContentQuerySaveListener() {
                        @Override
                        public void onSuccess(List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB) {

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

            structureHameshsDB = queryBuilder.distinct().orderBy("CreationDate", true).query();
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
            structureHameshsDB = queryBuilder.distinct().orderBy("CreationDate", true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureHameshsDB;
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
            documentActionsDB = queryBuilder.distinct().query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documentActionsDB;
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

    public StructureCartableDocumentTaeedQueueDB getFirstItemTaeedQueue() {
        QueryBuilder<StructureCartableDocumentTaeedQueueDB, Integer> queryBuilder = mCartableDocumentTaeedQueueDao.queryBuilder();
        StructureCartableDocumentTaeedQueueDB cartableDocumentTaeedQueueDB = new StructureCartableDocumentTaeedQueueDB();
        try {
            cartableDocumentTaeedQueueDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartableDocumentTaeedQueueDB;
    }

    public StructureOpticalPenQueueDB getFirstItemOpticalPenQueue() {
        QueryBuilder<StructureOpticalPenQueueDB, Integer> queryBuilder = mOpticalPenQueueDao.queryBuilder();
        StructureOpticalPenQueueDB structureOpticalPenQueueDB = new StructureOpticalPenQueueDB();
        try {
            structureOpticalPenQueueDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureOpticalPenQueueDB;
    }


    public List<StructureInboxDocumentDB> getCartableDocuments(int actionCode, Status status, long start, long count) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = queryBuilder.where();
        List<StructureInboxDocumentDB> structureInboxDocumentsDB = new ArrayList<>();
        try {
            where.eq("ActionCode", actionCode).and().eq("isTaeed", false);
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


    public StructureCartableSendQueueDB getFirstItemCartableSendQueue() {
        QueryBuilder<StructureCartableSendQueueDB, Integer> queryBuilder = mCartableSendQueueDao.queryBuilder();
        StructureCartableSendQueueDB structureCartableSendQueueDB = new StructureCartableSendQueueDB();
        try {
            structureCartableSendQueueDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableSendQueueDB;
    }

    public StructureCartableSendQueueDB getCartableSendQueue(int Etc, int EC) {
        QueryBuilder<StructureCartableSendQueueDB, Integer> queryBuilder = mCartableSendQueueDao.queryBuilder();
        StructureCartableSendQueueDB structureCartableSendQueueDB = new StructureCartableSendQueueDB();
        try {
            queryBuilder.where().eq("ETC", Etc).and().eq("EC", EC);
            structureCartableSendQueueDB = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return structureCartableSendQueueDB;
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

    public ArrayList<StructureCartableAction> getCartableAction(boolean isPin, @Nullable Status status) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
        ArrayList<StructureCartableAction> cartableActions = new ArrayList<>();
        try {
            queryBuilder.where().eq("Pin", isPin).and().eq("isTaeed", false);
            structureInboxDocumentDB = queryBuilder.groupBy("ActionCode").query();
            for (StructureInboxDocumentDB inboxDocumentDB : structureInboxDocumentDB) {
                StructureCartableAction structureCartableAction = new StructureCartableAction(inboxDocumentDB.getActionCode(), inboxDocumentDB.getActionName(), getCartableDocumentCount(inboxDocumentDB.getActionCode(), status), inboxDocumentDB.isPin());
                if (structureCartableAction.getCount() > 0) {
                    cartableActions.add(structureCartableAction);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return cartableActions;
    }

    public long getCartableDocumentCount(int ActionCode, @Nullable Status status) {
        QueryBuilder<StructureInboxDocumentDB, Integer> queryBuilder = cartableDocumentDao.queryBuilder();
        Where<StructureInboxDocumentDB, Integer> where = queryBuilder.where();

        long count = 0;
        try {
            //where.eq("ActionCode", ActionCode);
            if (status != null) {
                if (status == Status.UnRead || status == Status.IsNew) {
                    //  where.and(where.eq("ActionCode", ActionCode), where.or(where.eq("IsRead", Status.UnRead), where.eq("status", Status.IsNew)));
                    where.eq("ActionCode", ActionCode).and().eq("IsRead", false).and().eq("isTaeed", false);
                } else {
                    where.eq("ActionCode", ActionCode).and().eq("IsRead", true).and().eq("isTaeed", false);
                }
            } else {
                where.eq("ActionCode", ActionCode).and().eq("isTaeed", false);
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
        try {
            queryBuilder.setCountOf(true);
            queryBuilder.where().eq("isTaeed", false);
            count = cartableDocumentDao.countOf(queryBuilder.distinct().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public boolean deletItemCartableDocumentTaeedQueue(int receiveCode) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureCartableDocumentTaeedQueueDB, Integer> deleteBuilder = mCartableDocumentTaeedQueueDao.deleteBuilder();
            deleteBuilder.where().eq("receiveCode", receiveCode);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public boolean deletItemOpticalPenQueue(int id) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureOpticalPenQueueDB, Integer> deleteBuilder = mOpticalPenQueueDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
    }

    public void deletCartableDocumentAllContent(int receiveCode) {
        StructureInboxDocumentDB item = getCartableDocumentWithReceiveCode(receiveCode);
        deletCartableDocument(item.getEntityTypeCode(), item.getEntityCode());
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

    public boolean deletCartableDocument(int ETC, int EC) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureInboxDocumentDB, Integer> deleteBuilder = cartableDocumentDao.deleteBuilder();
            deleteBuilder.where().eq("EntityTypeCode", ETC).and().eq("EntityCode", EC);
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

    public boolean deletCartableSendQueue(int id) {
        boolean isDelet = false;
        try {
            DeleteBuilder<StructureCartableSendQueueDB, Integer> deleteBuilder = mCartableSendQueueDao.deleteBuilder();
            deleteBuilder.where().eq("id", id);
            deleteBuilder.delete();
            isDelet = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDelet;
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

    public void UpdateInboxDocumentTaeed(int receiverCode, boolean taeed) {
        try {
            UpdateBuilder<StructureInboxDocumentDB, Integer> updateBuilder = cartableDocumentDao.updateBuilder();
            updateBuilder.where().eq("ReceiverCode", receiverCode);
            updateBuilder.updateColumnValue("isTaeed", taeed);
            updateBuilder.update();
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

    private boolean IsCartableDocumentTaeedQueueExist(int receiveCode) {
        boolean existing = false;
        QueryBuilder<StructureCartableDocumentTaeedQueueDB, Integer> queryBuilder = mCartableDocumentTaeedQueueDao.queryBuilder();
        try {
            queryBuilder.setWhere(queryBuilder.where().eq("receiveCode", receiveCode));
            queryBuilder.setCountOf(true);
            long count = mCartableDocumentTaeedQueueDao.countOf(queryBuilder.prepare());
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






