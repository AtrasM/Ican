package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.acra.ACRA;
import org.ksoap2.serialization.SoapObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentActionsListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.GetDateTimeListener;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Interface.UserRoleListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Farzin.Model.Structure.Response.StructureLoginDataRES;
import avida.ican.Farzin.Model.Structure.Response.StructureUserAndRoleRES;
import avida.ican.Farzin.Model.Structure.Response.StructureUserAndRoleRowsRES;
import avida.ican.Farzin.Model.Structure.Response.StructureUserRoleRES;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentActionsPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;

import static avida.ican.Farzin.Model.Enum.DataSyncingNameEnum.SyncDocumentActions;
import static avida.ican.Farzin.Model.Enum.DataSyncingNameEnum.SyncUserAndRole;

/**
 * Created by AtrasVida on 2018-04-24 at 9:59 AM
 */

public class FarzinMetaDataQuery {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "";
    private String MethodName = "";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "FarzinMetaDataQuery";
    private FarzinPrefrences farzinPrefrences;
    private MetaDataSyncListener mainMetaDataSyncListener;
    private Context context;
    //_______________________***Dao***______________________________

    private Dao<StructureUserAndRoleDB, Integer> userAndRoleListDao = null;
    private Dao<StructureUserRoleDB, Integer> userRoleDao = null;
    private UserRoleListener userRoleListener;
    //_______________________***Dao***______________________________

    public FarzinMetaDataQuery(Context context) {
        this.context = context;
        farzinPrefrences = getFarzinPrefrences();
        initDao();

    }

    private void initDao() {
        try {
            userAndRoleListDao = App.getFarzinDatabaseHelper().getUserAndRoleListDao();
            userRoleDao = App.getFarzinDatabaseHelper().getUserRolesDBDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void Sync(final MetaDataSyncListener metaDataSyncListener) {
        if (App.netWorkStatusListener == null) return;
        App.CurentActivity.runOnUiThread(() -> App.netWorkStatusListener.Syncing());
        SyncUserAndRoleList();
        mainMetaDataSyncListener = new MetaDataSyncListener() {
            @Override
            public void onSuccess(DataSyncingNameEnum dataSyncingNameEnum) {
                metaDataSyncListener.onSuccess(dataSyncingNameEnum);
                SyncTable(dataSyncingNameEnum.ordinal() + 1);
            }

            @Override
            public void onFailed(DataSyncingNameEnum dataSyncingNameEnum) {
                SyncTable(dataSyncingNameEnum.ordinal());
            }

            @Override
            public void onCancel(DataSyncingNameEnum dataSyncingNameEnum) {
                SyncTable(dataSyncingNameEnum.ordinal());
            }

            @Override
            public void onFinish() {

                try {
                    CustomFunction.getCurentDateTimeAsStringFormat(new GetDateTimeListener() {
                        @Override
                        public void onSuccess(String strDateTime) {
                            doProccess(metaDataSyncListener);
                        }

                        @Override
                        public void onFailed(String message) {
                            doProccess(metaDataSyncListener);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                    ACRA.getErrorReporter().handleSilentException(e);
                }
            }


        };

    }

    private void doProccess(final MetaDataSyncListener metaDataSyncListener) {
        CustomFunction.getCurentDateTimeAsStringFormat(new GetDateTimeListener() {
            @Override
            public void onSuccess(String strDateTime) {
                countinuProcess(strDateTime, metaDataSyncListener);
            }

            @Override
            public void onFailed(String message) {
                String strDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
                countinuProcess(strDate, metaDataSyncListener);

            }

            @Override
            public void onCancel() {
                String strDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
                countinuProcess(strDate, metaDataSyncListener);
            }
        });
    }

    private void countinuProcess(String strDateTime, final MetaDataSyncListener metaDataSyncListener) {
        farzinPrefrences.putMetaDataLastSyncDate(strDateTime);
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        String userName = farzinPrefrences.getUserName();
        int roleID = farzinPrefrences.getRoleID();
        StructureUserAndRoleDB UserAndRoleDB = getUserInfo(userName);
        farzinPrefrences.putUserBaseInfo(UserAndRoleDB.getUser_ID(), roleID);
        App.CurentActivity.runOnUiThread(() -> metaDataSyncListener.onFinish());

    }

    private void SyncTable(int i) {
        if (i == SyncUserAndRole.ordinal()) {
            SyncUserAndRoleList();
        } else if (i == SyncDocumentActions.ordinal()) {
            SyncDocumentActionList();
        } else {
            mainMetaDataSyncListener.onFinish();
        }

    }

    //*******____________________________  UserAndRoleList  ____________________________********

    private void SyncDocumentActionList() {
        App.getHandlerMainThread().post(() -> {
            new CartableDocumentActionsPresenter(-1).GetDocumentActionsFromServer(new DocumentActionsListener() {
                @Override
                public void onSuccess() {
                    mainMetaDataSyncListener.onSuccess(SyncDocumentActions);
                }

                @Override
                public void onFailed(String message) {
                    mainMetaDataSyncListener.onFailed(SyncDocumentActions);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.error_get_data_faild), ToastEnum.TOAST_LONG_TIME);
                }

                @Override
                public void onCancel() {
                    mainMetaDataSyncListener.onCancel(SyncDocumentActions);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.rule_cancel), ToastEnum.TOAST_LONG_TIME);
                }
            });
        });

    }

    private void SyncUserAndRoleList() {
        EndPoint = "UserManagment";
        MethodName = "GetUserAndRoleList";
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        new CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {

                StructureUserAndRoleRowsRES structureUserAndRoleRowsRES = xmlToObject.DeserializationGsonXml(Xml, StructureUserAndRoleRowsRES.class);
                List<StructureUserAndRoleRES> structureUserAndRoleRES = structureUserAndRoleRowsRES.getGetUserAndRoleListResult().getRows().getRowList();
                new SaveUserAndRoleList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureUserAndRoleRES);


            }

            @Override
            public void onFailed() {
                mainMetaDataSyncListener.onFailed(SyncUserAndRole);
                App.ShowMessage().ShowToast(Resorse.getString(R.string.error_get_data_faild), ToastEnum.TOAST_LONG_TIME);
            }

            @Override
            public void onCancel() {
                mainMetaDataSyncListener.onCancel(SyncUserAndRole);
                App.ShowMessage().ShowToast(Resorse.getString(R.string.rule_cancel), ToastEnum.TOAST_LONG_TIME);
            }
        });
    }

    public int getUserID(String userName) {
        QueryBuilder<StructureUserAndRoleDB, Integer> queryBuilder = userAndRoleListDao.queryBuilder();
        StructureUserAndRoleDB userAndRoles = new StructureUserAndRoleDB();
        try {
            queryBuilder.where().eq("UserName", userName);
            userAndRoles = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (userAndRoles == null) {
            return -1;
        }
        return userAndRoles.getUser_ID();
    }


    public void saveUserRole(StructureLoginDataRES structureLoginDataRES, UserRoleListener userRoleListener) {
        this.userRoleListener = userRoleListener;
        new SaveUserRole().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureLoginDataRES);


    }

    @SuppressLint("StaticFieldLeak")
    private class SaveUserRole extends AsyncTask<StructureLoginDataRES, Void, StructureLoginDataRES> {

        @Override
        protected StructureLoginDataRES doInBackground(StructureLoginDataRES... structureLoginDataRES) {


            try {
                App.getFarzinDatabaseHelper().clearUserRoles();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<StructureUserRoleRES> structureUserRolesRES = new ArrayList<>();
            if (structureLoginDataRES.length > 0) {
                structureUserRolesRES = structureLoginDataRES[0].getActiveRoles();
            }
            ArrayList<StructureUserRoleDB> structureUserRolesDB = new ArrayList<>();
            for (int i = 0; i < structureUserRolesRES.size(); i++) {

                StructureUserRoleRES structureUserRoleRES = structureUserRolesRES.get(i);
                String roleName = structureUserRoleRES.getRoleName();
                roleName = new ChangeXml().viewCharDecoder(roleName);
                StructureUserRoleDB structureUserRoleDB = new StructureUserRoleDB(structureUserRoleRES.getRoleID(), structureUserRoleRES.getRoleIDToken(), structureUserRoleRES.getActorIDToken(), roleName, structureUserRoleRES.getIsDef());
                structureUserRolesDB.add(structureUserRoleDB);
            }
            if (structureUserRolesDB.size() > 0) {
                try {
                    userRoleDao.create(structureUserRolesDB);
                } catch (SQLException e) {
                    e.printStackTrace();
                    userRoleListener.onFailed(e.toString());
                    ACRA.getErrorReporter().handleSilentException(e);
                    return null;
                }
            }

            return structureLoginDataRES[0];
        }

        @Override
        protected void onPostExecute(StructureLoginDataRES structureLoginDataRES) {
            super.onPostExecute(structureLoginDataRES);
            userRoleListener.onSuccess(structureLoginDataRES);
        }
    }

    public ArrayList<StructureUserRoleDB> getUserRole() {
        QueryBuilder<StructureUserRoleDB, Integer> queryBuilder = userRoleDao.queryBuilder();
        List<StructureUserRoleDB> structureUserRolesDB = new ArrayList<>();
        try {
            structureUserRolesDB = queryBuilder.query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(structureUserRolesDB);
    }

    @SuppressLint("StaticFieldLeak")
    private class SaveUserAndRoleList extends AsyncTask<List<StructureUserAndRoleRES>, Void, Void> {

        @Override
        protected Void doInBackground(List<StructureUserAndRoleRES>[] usersAndRolesOPTS) {


            try {
                App.getFarzinDatabaseHelper().clearUserAndRole();
            } catch (SQLException e) {
                e.printStackTrace();

            }
            List<StructureUserAndRoleRES> structureUserAndRolesRES = new ArrayList<>();
            if (usersAndRolesOPTS.length > 0) {
                structureUserAndRolesRES = usersAndRolesOPTS[0];
            }
            ArrayList<StructureUserAndRoleDB> structureUserAndRolesDB = new ArrayList<>();
            for (int i = 0; i < structureUserAndRolesRES.size(); i++) {
                StructureUserAndRoleRES structureUserAndRoleRES = structureUserAndRolesRES.get(i);
                StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB(structureUserAndRoleRES.getUser_ID(), structureUserAndRoleRES.getUserName(), structureUserAndRoleRES.getFirstName(), structureUserAndRoleRES.getLastName(), structureUserAndRoleRES.getRole_ParentID(), structureUserAndRoleRES.getIsDefForCardTable(), structureUserAndRoleRES.getRoleCode(), structureUserAndRoleRES.getRoleName(), structureUserAndRoleRES.getRole_ID(), structureUserAndRoleRES.getOrganCode(), structureUserAndRoleRES.getOrganizationRoleName(), structureUserAndRoleRES.getOrganizationRole_ID(), structureUserAndRoleRES.getDepartmentID(), structureUserAndRoleRES.getMobile(), structureUserAndRoleRES.getGender(), structureUserAndRoleRES.getBirthDate(), structureUserAndRoleRES.getE_Mail(), structureUserAndRoleRES.getNativeID());
                structureUserAndRolesDB.add(structureUserAndRoleDB);
            }
            if (structureUserAndRolesDB.size() > 0) {
                try {
                    userAndRoleListDao.create(structureUserAndRolesDB);
                    Thread.sleep(2);
                } catch (SQLException e) {
                    e.printStackTrace();
                    mainMetaDataSyncListener.onFailed(SyncUserAndRole);
                    App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                    ACRA.getErrorReporter().handleSilentException(e);
                    return null;
                } catch (InterruptedException e) {
                    mainMetaDataSyncListener.onFailed(SyncUserAndRole);
                    App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها", ToastEnum.TOAST_LONG_TIME);
                    e.printStackTrace();
                    ACRA.getErrorReporter().handleSilentException(e);
                }
            }
            mainMetaDataSyncListener.onSuccess(SyncUserAndRole);
            //App.ShowMessage().ShowToast("داده ها با موفقیت ذخیر شدن.", ToastEnum.TOAST_LONG_TIME);
            return null;
        }
    }

    List<StructureUserAndRoleDB> getUserAndRoleList() {
        List<StructureUserAndRoleDB> userAndRoles = new ArrayList<>();
        try {
            userAndRoles = userAndRoleListDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAndRoles;
    }

    public StructureUserAndRoleDB getUserInfo(String user_name) {
        user_name = user_name.toLowerCase();
        QueryBuilder<StructureUserAndRoleDB, Integer> queryBuilder = userAndRoleListDao.queryBuilder();
        StructureUserAndRoleDB userAndRoles = new StructureUserAndRoleDB();
        try {
            queryBuilder.where().eq("UserName", user_name).and().eq("IsDefForCardTable", "1");
            userAndRoles = queryBuilder.queryForFirst();
            if (userAndRoles == null || !userAndRoles.getUserName().equals(user_name)) {
                queryBuilder.reset();
                queryBuilder.where().eq("UserName", user_name).and().eq("IsDefForCardTable", "0").prepare();
                userAndRoles = queryBuilder.queryForFirst();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAndRoles;
    }

    public StructureUserAndRoleDB getUserInfo(int user_id) {
        QueryBuilder<StructureUserAndRoleDB, Integer> queryBuilder = userAndRoleListDao.queryBuilder();
        StructureUserAndRoleDB userAndRoles = new StructureUserAndRoleDB();
        try {
            queryBuilder.where().eq("User_ID", user_id);
            userAndRoles = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAndRoles;
    }

    public StructureUserAndRoleDB getUserInfo(int user_id, int role_id) {
        QueryBuilder<StructureUserAndRoleDB, Integer> queryBuilder = userAndRoleListDao.queryBuilder();
        StructureUserAndRoleDB userAndRoles = new StructureUserAndRoleDB();
        try {
            queryBuilder.where().eq("User_ID", user_id).and().eq("Role_ID", role_id);
            userAndRoles = queryBuilder.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAndRoles;
    }

    //*******____________________________  UserAndRoleList  ____________________________********

    private class CallApi {
        CallApi(String MethodName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
            String ServerUrl = farzinPrefrences.getServerUrl();
            String BaseUrl = farzinPrefrences.getBaseUrl();
            String SessionId = farzinPrefrences.getCookie();
            new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
                    .setSoapObject(soapObject)
                    .setSessionId(SessionId)
                    .setOnListener(new WebserviceResponseListener() {

                        @Override
                        public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                            new processData(webServiceResponse, dataProcessListener);
                        }

                        @Override
                        public void NetworkAccessDenied() {
                            dataProcessListener.onFailed();
                        }
                    }).execute();

        }


        CallApi(String MethodName, String EndPoint, SoapObject soapObject, String MappingNameSpace, String MappingName, Class MappingClass, final DataProcessListener dataProcessListener) {
            String ServerUrl = farzinPrefrences.getServerUrl();
            String BaseUrl = farzinPrefrences.getBaseUrl();
            String SessionId = farzinPrefrences.getCookie();
            new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
                    .setSoapObject(soapObject)
                    .setSessionId(SessionId)
                    .addMapping(MappingNameSpace, MappingName, MappingClass)
                    .setOnListener(new WebserviceResponseListener() {

                        @Override
                        public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                            new processData(webServiceResponse, dataProcessListener);
                        }

                        @Override
                        public void NetworkAccessDenied() {
                            dataProcessListener.onFailed();
                        }
                    }).execute();

        }
    }

    private class processData {
        processData(WebServiceResponse webServiceResponse, DataProcessListener dataProcessListener) {
            if (!webServiceResponse.isResponse()) {
                dataProcessListener.onFailed();
                return;
            }
            String Xml = webServiceResponse.getHttpTransportSE().responseDump;
            try {
                Xml = changeXml.charDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
                //Log.i(Tag, "XmlCropAsResponseTag= " + Xml);
                if (!Xml.isEmpty()) {
                    dataProcessListener.onSuccess(Xml);
                } else {
                    dataProcessListener.onFailed();
                }
            } catch (Exception e) {
                dataProcessListener.onFailed();
                e.printStackTrace();
            }
        }

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
