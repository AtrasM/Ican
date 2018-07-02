package avida.ican.Ican.View.Custom;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.NetWorkState;

/**
 * Created by AtrasVida on 2018-03-27 at 12:24 PM
 */

public class CheckNetworkAvailability {
    public boolean execuet() {
        NetWorkState status = readNetworkStatus();
        return (status == NetWorkState.WIFI || status == NetWorkState.MOBILE);
    }

    private NetWorkState readNetworkStatus() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) return NetWorkState.DISCONNECTED;
        boolean isConnected = networkInfo.isConnected();
        if (isConnected) {
            int networkType = networkInfo.getType();
            if (networkType == ConnectivityManager.TYPE_WIFI) {
                return NetWorkState.WIFI;
            } else if (networkType == ConnectivityManager.TYPE_MOBILE) {
                return NetWorkState.MOBILE;
            }
        }
        return NetWorkState.DISCONNECTED;
    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
}
