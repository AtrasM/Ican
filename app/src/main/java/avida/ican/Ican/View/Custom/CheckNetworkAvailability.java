package avida.ican.Ican.View.Custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import avida.ican.Farzin.Presenter.CheckServerAviablePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.NetWorkState;
import avida.ican.Ican.View.Interface.ListenerNetwork;

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

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    @SuppressLint("StaticFieldLeak")
    public void isServerAvailable(final ListenerNetwork listenerNetwork) {
        CheckServerAviablePresenter checkServerAviablePresenter = new CheckServerAviablePresenter();

        checkServerAviablePresenter.CallRequest(new ListenerNetwork() {
            @Override
            public void onConnected() {
                listenerNetwork.onConnected();
            }

            @Override
            public void disConnected() {
                listenerNetwork.disConnected();
            }

            @Override
            public void onFailed() {
                listenerNetwork.disConnected();
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    public void isInternetAvailable(final ListenerNetwork listenerNetwork) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // ping to googlevto check internet connectivity
                    Socket socket = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress("www.google.com", 80);
                    socket.connect(socketAddress, 1500);
                    socket.close();

                    listenerNetwork.onConnected();
                } catch (Exception e) {
                    // internet not working

                    listenerNetwork.disConnected();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
