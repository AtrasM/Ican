package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import org.ksoap2.serialization.SoapObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.Message.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.StructureUserAndRoleRES;
import avida.ican.Farzin.Model.Structure.Response.StructureUserAndRoleRowsRES;
import avida.ican.Farzin.View.Dialog.DialogFirstMetaDataSync;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;

import static avida.ican.Farzin.Model.Enum.MetaDataNameEnum.SyncUserAndRole;

/**
 * Created by AtrasVida on 2018-04-24 at 9:59 AM
 */

public class FarzinMetaDataQuery {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "";
    private String MetodeName = "";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "FarzinMetaDataQuery";
    private FarzinPrefrences farzinPrefrences;
    private MetaDataSyncListener mainMetaDataSyncListener;
    private Context context;
    //_______________________***Dao***______________________________

    private Dao<StructureUserAndRoleDB, Integer> userAndRoleListDao = null;

    //_______________________***Dao***______________________________

    public FarzinMetaDataQuery(Context context) {
        this.context = context;
        farzinPrefrences = getFarzinPrefrences();
        strSimpleDateFormat = SimpleDateFormatEnum.DateTime_yyyy_MM_dd_hh_mm_ss.getValue();
        initDao();

    }

    private void initDao() {
        try {
            userAndRoleListDao = App.getFarzinDatabaseHelper().getUserAndRoleListDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void Sync(final MetaDataSyncListener metaDataSyncListener) {
        if (App.netWorkStatusListener == null) return;
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.netWorkStatusListener.Syncing();
            }
        });
        SyncUserAndRoleList();
        mainMetaDataSyncListener = new MetaDataSyncListener() {
            @Override
            public void onSuccess(MetaDataNameEnum metaDataNameEnum) {
                SyncTable(metaDataNameEnum.getIntValue() + 1);
            }

            @Override
            public void onFailed(MetaDataNameEnum metaDataNameEnum) {
                SyncTable(metaDataNameEnum.getIntValue());
            }

            @Override
            public void onCancel(MetaDataNameEnum metaDataNameEnum) {
                SyncTable(metaDataNameEnum.getIntValue());
            }

            @Override
            public void onFinish() {

                try {
              /*      App.CurentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });*/
                    String CurentDateTime = CustomFunction.getCurentDateTimeAsStringFormat(strSimpleDateFormat);
                    farzinPrefrences.putMetaDataSyncDate(CurentDateTime);
                    FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
                    String userName = farzinPrefrences.getUserName();
                    StructureUserAndRoleDB UserAndRoleDB = getUserInfo(userName);
                    farzinPrefrences.putUserBaseInfo(UserAndRoleDB.getUser_ID(), UserAndRoleDB.getRole_ID());
                    App.CurentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            metaDataSyncListener.onFinish();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        };

    }

    private void SyncTable(int i) {
        if (i == SyncUserAndRole.getIntValue()) {
            SyncUserAndRoleList();
        } else {
            mainMetaDataSyncListener.onFinish();
        }

    }

    //*******____________________________  UserAndRoleList  ____________________________********

    private void SyncUserAndRoleList() {
        EndPoint = "UserManagment";
        MetodeName = "GetUserAndRoleList";
        SoapObject soapObject = new SoapObject(NameSpace, MetodeName);
        new CallApi(MetodeName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                //App.ShowMessage().ShowToast("داده ها با موفقیت دریافت شد.", ToastEnum.TOAST_LONG_TIME);
                StructureUserAndRoleRowsRES structureUserAndRoleRowsRES = xmlToObject.DeserializationGsonXml(Xml, StructureUserAndRoleRowsRES.class);
                List<StructureUserAndRoleRES> structureUserAndRoleRES = structureUserAndRoleRowsRES.getGetUserAndRoleListResult().getRows().getRowList();
                new SaveUserAndRoleList().execute(structureUserAndRoleRES);

            }

            @Override
            public void onFailed() {
                mainMetaDataSyncListener.onFailed(SyncUserAndRole);
                App.ShowMessage().ShowToast(Resorse.getString(R.string.error_faild), ToastEnum.TOAST_LONG_TIME);
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

    @SuppressLint("StaticFieldLeak")
    private class SaveUserAndRoleList extends AsyncTask<List<StructureUserAndRoleRES>, Void, Void> {

        @Override
        protected Void doInBackground(List<StructureUserAndRoleRES>[] usersAndRolesOPTS) {


            try {
                App.getFarzinDatabaseHelper().ClearUserAndRole();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<StructureUserAndRoleRES> structureUserAndRolesRES = new ArrayList<>();
            if (usersAndRolesOPTS.length > 0) {
                structureUserAndRolesRES = usersAndRolesOPTS[0];
            }
            for (int i = 0; i < structureUserAndRolesRES.size(); i++) {
                StructureUserAndRoleRES structureUserAndRoleRES = structureUserAndRolesRES.get(i);
                //StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB();
                StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB(structureUserAndRoleRES.getUser_ID(), structureUserAndRoleRES.getUserName(), structureUserAndRoleRES.getFirstName(), structureUserAndRoleRES.getLastName(), structureUserAndRoleRES.getRole_ParentID(), structureUserAndRoleRES.getIsDefForCardTable(), structureUserAndRoleRES.getRoleCode(), structureUserAndRoleRES.getRoleName(), structureUserAndRoleRES.getRole_ID(), structureUserAndRoleRES.getOrganCode(), structureUserAndRoleRES.getOrganizationRoleName(), structureUserAndRoleRES.getOrganizationRole_ID(), structureUserAndRoleRES.getDepartmentID(), structureUserAndRoleRES.getMobile(), structureUserAndRoleRES.getGender(), structureUserAndRoleRES.getBirthDate(), structureUserAndRoleRES.getE_Mail(), structureUserAndRoleRES.getNativeID());
                try {
                    userAndRoleListDao.create(structureUserAndRoleDB);
                } catch (SQLException e) {
                    e.printStackTrace();
                    mainMetaDataSyncListener.onFailed(SyncUserAndRole);
                    App.ShowMessage().ShowToast(" مشکل در ذخیره داده ها" + " i= " + i, ToastEnum.TOAST_LONG_TIME);
                    return null;
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

        QueryBuilder<StructureUserAndRoleDB, Integer> queryBuilder = userAndRoleListDao.queryBuilder();
        StructureUserAndRoleDB userAndRoles = new StructureUserAndRoleDB();
        try {
            queryBuilder.where().eq("UserName", user_name).and().eq("IsDefForCardTable", "1");
            userAndRoles = queryBuilder.queryForFirst();
            if (!userAndRoles.getUserName().equals(user_name) ) {
                queryBuilder.reset();
                queryBuilder.where().eq("UserName", user_name).and().eq("IsDefForCardTable", "0");
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
        CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
            String ServerUrl = farzinPrefrences.getServerUrl();
            String BaseUrl = farzinPrefrences.getBaseUrl();
            String SessionId = farzinPrefrences.getCookie();
            new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
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


        CallApi(String MetodeName, String EndPoint, SoapObject soapObject, String MappingNameSpace, String MappingName, Class MappingClass, final DataProcessListener dataProcessListener) {
            String ServerUrl = farzinPrefrences.getServerUrl();
            String BaseUrl = farzinPrefrences.getBaseUrl();
            String SessionId = farzinPrefrences.getCookie();
            new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
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
                Xml = changeXml.CharDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MetodeName);
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
