package avida.ican.Farzin.Model.Prefrences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ksoap2.HeaderProperty;

import java.util.List;

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

    public void putIsRemember(boolean isRemember) {
        sharedPreferencesValue.edit().putBoolean(TAG + "isRemember", isRemember).apply();
    }

    public boolean isRemember() {
        return sharedPreferencesValue.getBoolean(TAG + "isRemember", false);
    }

    public void putServerUrl(String serverUrl) {
        sharedPreferencesValue.edit().putString(TAG + "ServerUrl", serverUrl).apply();
    }

    public String getServerUrl() {
        return sharedPreferencesValue.getString(TAG + "ServerUrl", "");
    }

    public void saveBaseUrl(String baseUrl) {
        sharedPreferencesValue.edit().putString(TAG + "baseUrl", baseUrl).apply();
    }

    public String getBaseUrl() {
        return sharedPreferencesValue.getString(TAG + "baseUrl", "FarzinSoft/WebServices/OA/");
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
        sharedPreferencesValue.edit().putString(TAG + "cookie", SESSION_ID).apply();
    }


    public void putPassword(String password) {
        sharedPreferencesValue.edit().putString(TAG + "password", password).apply();
    }

    private void putUserName(String userName) {
        sharedPreferencesValue.edit().putString(TAG + "userName", userName).apply();
    }

    private void putRoleID(int roleID) {
        sharedPreferencesValue.edit().putInt(TAG + "roleID", roleID).apply();
    }

    private void putUserID(int userID) {
        sharedPreferencesValue.edit().putInt(TAG + "userID", userID).apply();
    }

    public String getCookie() {
        return sharedPreferencesValue.getString(TAG + "cookie", "");
    }

    public String getPassword() {
        return sharedPreferencesValue.getString(TAG + "password", "-1");
    }

    public String getUserName() {
        return sharedPreferencesValue.getString(TAG + "userName", "-1");
    }

    public int getUserID() {
        return sharedPreferencesValue.getInt(TAG + "userID", -1);
    }

    public int getRoleID() {
        return sharedPreferencesValue.getInt(TAG + "roleID", -1);
    }


    public void putMessageViewSyncDate(String DateTime) {
        sharedPreferencesValue.edit().putString(TAG + "MessageViewSyncDate", DateTime).apply();
    }

    public String getMessageViewLastSyncDate() {
        return sharedPreferencesValue.getString(TAG + "MessageViewSyncDate", "");
    }

    public void putMetaDataLastSyncDate(String DateTime) {
        sharedPreferencesValue.edit().putString(TAG + "MetaDataSyncDate", DateTime).apply();
    }

    public String getMetaDataLastSyncDate() {
        return sharedPreferencesValue.getString(TAG + "MetaDataSyncDate", "");
    }

    public void putCartableDocumentDataSyncDate(String DateTime) {
        sharedPreferencesValue.edit().putString(TAG + "CartableDocumentDataSyncDate", DateTime).apply();
    }

    public String getCartableDocumentDataSyncDate() {
        String defDate = CustomFunction.getLastMonthDateTimeAsFormat();
        return sharedPreferencesValue.getString(TAG + "CartableDocumentDataSyncDate", defDate);
    }

    public void putDefultActionCode(int code) {
        sharedPreferencesValue.edit().putInt(TAG + "DefultActionCode", code).apply();
    }

    public int getDefultActionCode() {
        return sharedPreferencesValue.getInt(TAG + "DefultActionCode", -1);
    }

    public void putDefultCountOfGetDocument(int count) {
        sharedPreferencesValue.edit().putInt(TAG + "DefultCountOfGetDocument", count).apply();
    }

    public int getDefultCountOfGetDocument() {
        return sharedPreferencesValue.getInt(TAG + "DefultCountOfGetDocument", 10);
    }

    public void putCartableDocumentForFirstTimeSync(boolean isDataForFirstTimeSync) {
        sharedPreferencesValue.edit().putBoolean(TAG + "isCartableDocumentForFirstTimeSync", isDataForFirstTimeSync).apply();
    }

    public boolean isCartableDocumentForFirstTimeSync() {
        return sharedPreferencesValue.getBoolean(TAG + "isCartableDocumentForFirstTimeSync", false);
    }

    public void putReceiveMessageForFirstTimeSync(boolean isDataForFirstTimeSync) {
        sharedPreferencesValue.edit().putBoolean(TAG + "isReceiveMessageForFirstTimeSync", isDataForFirstTimeSync).apply();
    }

    public boolean isReceiveMessageForFirstTimeSync() {
        return sharedPreferencesValue.getBoolean(TAG + "isReceiveMessageForFirstTimeSync", false);
    }

    public void putSendMessageForFirstTimeSync(boolean isDataForFirstTimeSync) {
        sharedPreferencesValue.edit().putBoolean(TAG + "isSendMessageForFirstTimeSync", isDataForFirstTimeSync).apply();
    }

    public boolean isSendMessageForFirstTimeSync() {
        return sharedPreferencesValue.getBoolean(TAG + "isSendMessageForFirstTimeSync", false);
    }

    public void putMetaDataForFirstTimeSync(boolean isMetaDataForFirstTimeSync) {
        sharedPreferencesValue.edit().putBoolean(TAG + "MetaDataForFirstTimeSync", isMetaDataForFirstTimeSync).apply();
    }

    public boolean isMetaDataForFirstTimeSync() {
        return sharedPreferencesValue.getBoolean(TAG + "MetaDataForFirstTimeSync", false);
    }

    public void putDataForFirstTimeSync(boolean isDataForFirstTimeSync) {
        sharedPreferencesValue.edit().putBoolean(TAG + "isDataForFirstTimeSync", isDataForFirstTimeSync).apply();
    }

    public boolean isDataForFirstTimeSync() {
        return sharedPreferencesValue.getBoolean(TAG + "isDataForFirstTimeSync", false);
    }

    public void putCartableLastCheckDate(String dateTime) {
        sharedPreferencesValue.edit().putString(TAG + "CartableLastCheckDate", dateTime).apply();
    }

    public String getCartableLastCheckDate() {
        return sharedPreferencesValue.getString(TAG + "CartableLastCheckDate", "");
    }

    public void putMessageRecieveLastCheckDate(String dateTime) {
        sharedPreferencesValue.edit().putString(TAG + "MessageRecieveLastCheckDate", dateTime).apply();
    }

    public String getMessageRecieveLastCheckDate() {
        return sharedPreferencesValue.getString(TAG + "MessageRecieveLastCheckDate", "");
    }

    public void putMessageSentLastCheckDate(String dateTime) {
        sharedPreferencesValue.edit().putString(TAG + "MessageSendLastCheckDate", dateTime).apply();
    }

    public String getMessageSentLastCheckDate() {
        return sharedPreferencesValue.getString(TAG + "MessageSendLastCheckDate", "");
    }

    public void clearCatch() {
        putUserAuthenticationInfo("", "", null);
        putUserBaseInfo(-1, -1);
        putDataForFirstTimeSync(false);
        putMetaDataLastSyncDate("");
        putCartableDocumentForFirstTimeSync(false);
        putSendMessageForFirstTimeSync(false);
        putReceiveMessageForFirstTimeSync(false);
        putCartableLastCheckDate("");
        putMessageRecieveLastCheckDate("");
        putMessageSentLastCheckDate("");
        putMessageViewSyncDate("");
        putCartableDocumentDataSyncDate("");
    }


}
