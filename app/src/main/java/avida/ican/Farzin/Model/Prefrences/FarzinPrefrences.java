package avida.ican.Farzin.Model.Prefrences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.ksoap2.HeaderProperty;

import java.util.List;
import java.util.Objects;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-03-13 at 1:27 PM
 */

public class FarzinPrefrences {
    private SharedPreferences sharedPreferencesValue;
    private String TAG = "Farzin_";


    public FarzinPrefrences init() {
        sharedPreferencesValue = PreferenceManager.getDefaultSharedPreferences(App.getAppContext());
        return this;
    }

    public void putUserAuthenticationInfo(String UserName, String Password, List headerList) {
        putUserName(UserName);
        putPassword(Password);
        putCookie(headerList);

    }

    public void putUserBaseInfo(int UserID, int RoleID) {
        putUserID(UserID);
        putRoleID(RoleID);
    }

    public void putUserBaseInfo(String UserName, int RoleID, String ActorIDToken) {
        putUserName(UserName);
        putRoleID(RoleID);
        putActorIDToken(ActorIDToken);
    }

    public void putIsRemember(boolean isRemember) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isRemember", isRemember).apply();
        }
    }

    public boolean isRemember() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isRemember", false);
        }
    }

    public void putIsAnableFingerPrint(boolean isAnableFingerPrint) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isAnableFingerPrint", isAnableFingerPrint).apply();
        }
    }

    public boolean isAnableFingerPrint() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isAnableFingerPrint", false);
        }
    }

    public void putAnableAppLock(boolean isAnableAppLock) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isAnableAppLock", isAnableAppLock).apply();
        }
    }

    public boolean isAnableAppLock() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isAnableAppLock", false);
        }
    }

    public void putIsSupportFingerPrint(boolean isSupportFingerPrint) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isSupportFingerPrint", isSupportFingerPrint).apply();
        }
    }

    public boolean isSupportFingerPrint() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isSupportFingerPrint", true);
        }
    }

    public void putServerUrl(String serverUrl) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "ServerUrl", serverUrl).apply();
        }
    }

    public String getServerUrl() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "ServerUrl", "");
        }
    }

    public void saveBaseUrl(String baseUrl) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "baseUrl", baseUrl).apply();
        }
    }

    public void saveBaseChatUrl(String baseUrl) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "baseChatUrl", baseUrl).apply();
        }
    }

    public String getBaseUrl() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "baseUrl", "FarzinSoft/WebServices/OA/");
        }
    }

    public String getBaseChatUrl() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "baseChatUrl", "FarzinSoft/WebServices/Chat/");
        }
    }

    private void putCookie(List headerList) {
        String SESSION_ID = "";
        if (headerList != null) {
            for (Object header : headerList) {
                HeaderProperty headerProperty = (HeaderProperty) header;
                String headerKey = headerProperty.getKey();
                String headerValue = headerProperty.getValue();
                if (headerKey != null && headerKey.equalsIgnoreCase("set-cookie")) {
                    SESSION_ID = headerValue.trim();
                    break;
                }
            }
        }
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "cookie", SESSION_ID).apply();
        }
        //Log.i("LOG", "cookie = " + SESSION_ID);
    }


    public void putPassword(String password) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "password", password).apply();
        }
    }

    private void putUserName(String userName) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "userName", userName).apply();
        }
    }

    private void putRoleID(int roleID) {
        synchronized (this) {
            sharedPreferencesValue.edit().putInt(TAG + "roleID", roleID).apply();
        }
    }

    public void putRoleIDToken(String roleIDToken) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "roleIDToken", roleIDToken).apply();
        }
    }

    public String getRoleIDToken() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "roleIDToken", "");
        }
    }

    public void putUserIDToken(String mainUserCodeToken) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "Main_UserCodeToken", mainUserCodeToken).apply();
        }
    }

    public String getUserIDToken() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "Main_UserCodeToken", "");
        }
    }

    public void putActorIDToken(String ActorIDToken) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "ActorIDToken", ActorIDToken).apply();
        }
    }

    public String getActorIDToken() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "ActorIDToken", "");
        }
    }


    private void putUserID(int userID) {
        synchronized (this) {
            sharedPreferencesValue.edit().putInt(TAG + "userID", userID).apply();
        }
    }

    public String getCookie() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "cookie", "");
        }
    }


    public String getPassword() {
        synchronized (this) {
            String password = CustomFunction.arabicToDecimal(Objects.requireNonNull(sharedPreferencesValue.getString(TAG + "password", "-1")));
            return password;
        }
    }

    public String getUserName() {
        synchronized (this) {
            String userName = CustomFunction.arabicToDecimal(Objects.requireNonNull(sharedPreferencesValue.getString(TAG + "userName", "-1")));
            return userName;
        }
    }

    public int getUserID() {
        synchronized (this) {
            return sharedPreferencesValue.getInt(TAG + "userID", -1);
        }
    }

    public int getRoleID() {
        synchronized (this) {
            return sharedPreferencesValue.getInt(TAG + "roleID", -1);
        }
    }


    public void putMessageViewSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MessageViewSyncDate", DateTime).apply();
        }
    }

    public String getMessageViewLastSyncDate() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MessageViewSyncDate", "");
        }
    }

    public void putMetaDataLastSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MetaDataSyncDate", DateTime).apply();
        }
    }

    public String getMetaDataLastSyncDate() {
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MetaDataSyncDate", "");
        }
    }

    public void putConfirmationListDataSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "ConfirmationListDataSyncDate", DateTime).apply();
        }
    }

    public String getConfirmationListDataSyncDate() {
        synchronized (this) {
            //String defDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
            return sharedPreferencesValue.getString(TAG + "ConfirmationListDataSyncDate", "");
        }
    }

    public void putCartableDocumentDataSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "CartableDocumentDataSyncDate", DateTime).apply();
        }
    }

    public String getCartableDocumentDataSyncDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "CartableDocumentDataSyncDate", defDate);
        }
    }

    //_______________*****CartableDocumentSettingDate******_____________________________
    public void putCartableDocumentSettingStartDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "CartableDocumentSettingStartDate", DateTime).apply();
        }
    }

    public String getCartableDocumentSettingStartDate() {
        String defDate = getCartableDocumentDataSyncDate();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "CartableDocumentSettingStartDate", defDate);
        }
    }

    public void putCartableDocumentSettingEndDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "CartableDocumentSettingEndDate", DateTime).apply();
        }
    }

    public String getCartableDocumentSettingEndDate() {
        String defDate = getCartableDocumentDataSyncDate();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "CartableDocumentSettingEndDate", defDate);
        }
    }
    //_______________*****CartableDocumentSettingDate******_____________________________

    //_______________*****MessageSettingDate******_____________________________
    public void putMessageSettingStartDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MessageSettingStartDate", DateTime).apply();
        }
    }

    public String getMessageSettingStartDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MessageSettingStartDate", defDate);
        }
    }

    public void putMessageSettingEndDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MessageSettingEndDate", DateTime).apply();
        }
    }

    public String getMessageSettingEndDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MessageSettingEndDate", defDate);
        }
    }
    //_______________*****MessageSettingDate******_____________________________


    public void putReceiveMessageDataSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "ReceiveMessageDataSyncDate", DateTime).apply();
        }
    }

    public String getReceiveMessageDataSyncDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "ReceiveMessageDataSyncDate", defDate);
        }
    }

    public void putSendMessageDataSyncDate(String DateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "SendMessageDataSyncDate", DateTime).apply();
        }
    }

    public String getSendMessageDataSyncDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "SendMessageDataSyncDate", defDate);
        }
    }

    public void putDefultActionCode(int code) {
        synchronized (this) {
            sharedPreferencesValue.edit().putInt(TAG + "DefultActionCode", code).apply();
        }
    }

    public int getDefultActionCode() {
        synchronized (this) {
            return sharedPreferencesValue.getInt(TAG + "DefultActionCode", -1);
        }
    }

    public void putDefultCountOfGetDocument(int count) {
        synchronized (this) {
            sharedPreferencesValue.edit().putInt(TAG + "DefultCountOfGetDocument", count).apply();
        }
    }

    public int getDefultCountOfGetDocument() {
        synchronized (this) {
            return sharedPreferencesValue.getInt(TAG + "DefultCountOfGetDocument", 10);
        }
    }

    public void putCartableDocumentForFirstTimeSync(boolean isDataForFirstTimeSync) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isCartableDocumentForFirstTimeSync", isDataForFirstTimeSync).apply();
        }
    }

    public boolean isCartableDocumentForFirstTimeSync() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isCartableDocumentForFirstTimeSync", false);
        }
    }

    public void putReceiveMessageForFirstTimeSync(boolean isDataForFirstTimeSync) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isReceiveMessageForFirstTimeSync", isDataForFirstTimeSync).apply();
        }
    }

    public boolean isReceiveMessageForFirstTimeSync() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isReceiveMessageForFirstTimeSync", false);
        }
    }

    public void putSendMessageForFirstTimeSync(boolean isDataForFirstTimeSync) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isSendMessageForFirstTimeSync", isDataForFirstTimeSync).apply();
        }
    }

    public boolean isSendMessageForFirstTimeSync() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isSendMessageForFirstTimeSync", false);
        }
    }

    public void putMetaDataForFirstTimeSync(boolean isMetaDataForFirstTimeSync) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isMetaDataForFirstTimeSync", isMetaDataForFirstTimeSync).apply();
        }
    }

    public boolean isMetaDataForFirstTimeSync() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isMetaDataForFirstTimeSync", false);
        }
    }

    public void putDataForFirstTimeSync(boolean isDataForFirstTimeSync) {
        synchronized (this) {
            sharedPreferencesValue.edit().putBoolean(TAG + "isDataForFirstTimeSync", isDataForFirstTimeSync).apply();
        }
    }

    public boolean isDataForFirstTimeSync() {
        synchronized (this) {
            return sharedPreferencesValue.getBoolean(TAG + "isDataForFirstTimeSync", false);
        }
    }

    public void putCartableLastCheckDate(String dateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "CartableLastCheckDate", dateTime).apply();
        }
    }

    public String getCartableLastCheckDate() {
        String def=CustomFunction.getCurentLocalDateTimeAsStringFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "CartableLastCheckDate", def);
        }
    }

    public void putMessageRecieveLastCheckDate(String dateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MessageRecieveLastCheckDate", dateTime).apply();
        }
    }

    public String getMessageRecieveLastCheckDate() {
        String def=CustomFunction.getCurentLocalDateTimeAsStringFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MessageRecieveLastCheckDate", def);
        }
    }

    public void putMessageSentLastCheckDate(String dateTime) {
        synchronized (this) {
            sharedPreferencesValue.edit().putString(TAG + "MessageSendLastCheckDate", dateTime).apply();
        }
    }

    public String getMessageSentLastCheckDate() {
        String def=CustomFunction.getCurentLocalDateTimeAsStringFormat();
        synchronized (this) {
            return sharedPreferencesValue.getString(TAG + "MessageSendLastCheckDate", def);
        }
    }

    public void clearCatch() {
        synchronized (this) {
            sharedPreferencesValue.edit().remove(TAG + "userName").apply();
            sharedPreferencesValue.edit().remove(TAG + "password").apply();
            sharedPreferencesValue.edit().remove(TAG + "cookie").apply();
            sharedPreferencesValue.edit().remove(TAG + "userID").apply();
            sharedPreferencesValue.edit().remove(TAG + "roleID").apply();
            sharedPreferencesValue.edit().remove(TAG + "ServerUrl").apply();
            sharedPreferencesValue.edit().remove(TAG + "isMetaDataForFirstTimeSync").apply();
            sharedPreferencesValue.edit().remove(TAG + "isDataForFirstTimeSync").apply();
            sharedPreferencesValue.edit().remove(TAG + "MetaDataSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "isCartableDocumentForFirstTimeSync").apply();
            sharedPreferencesValue.edit().remove(TAG + "isSendMessageForFirstTimeSync").apply();
            sharedPreferencesValue.edit().remove(TAG + "isReceiveMessageForFirstTimeSync").apply();
            sharedPreferencesValue.edit().remove(TAG + "MessageViewSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "CartableLastCheckDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "MessageRecieveLastCheckDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "MessageSendLastCheckDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "DefultActionCode").apply();
            sharedPreferencesValue.edit().remove(TAG + "SendMessageDataSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "ReceiveMessageDataSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "CartableDocumentDataSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "ConfirmationListDataSyncDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "MessageSettingEndDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "MessageSettingStartDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "CartableDocumentSettingStartDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "CartableDocumentSettingEndDate").apply();
            sharedPreferencesValue.edit().remove(TAG + "isAnableAppLock").apply();
            sharedPreferencesValue.edit().remove(TAG + "isAnableFingerPrint").apply();
            sharedPreferencesValue.edit().remove(TAG + "isRemember").apply();
        }

    }

}
