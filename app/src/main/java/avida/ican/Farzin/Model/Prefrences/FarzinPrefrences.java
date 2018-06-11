package avida.ican.Farzin.Model.Prefrences;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.ksoap2.HeaderProperty;

import java.util.List;

import avida.ican.Ican.App;

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
        for (Object header : headerList) {
            HeaderProperty headerProperty = (HeaderProperty) header;
            String headerKey = headerProperty.getKey();
            String headerValue = headerProperty.getValue();
            if (headerKey != null && headerKey.equalsIgnoreCase("set-cookie")) {
                SESSION_ID = headerValue.trim();
                break;
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

    public String getCookie() {
        return sharedPreferencesValue.getString(TAG + "cookie", "");
    }

    public String getPassword() {
        return sharedPreferencesValue.getString(TAG + "password", "-1");
    }

    public String getUserName() {
        return sharedPreferencesValue.getString(TAG + "userName", "-1");
    }



    public void putMetaDataSyncDate(String DateTime) {
        sharedPreferencesValue.edit().putString(TAG + "MetaDataSyncDate", DateTime).apply();
    }

    public String getMetaDataLastSyncDate() {
        return sharedPreferencesValue.getString(TAG + "MetaDataSyncDate", "");
    }


}
