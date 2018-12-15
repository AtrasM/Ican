package avida.ican.Ican.View.Custom;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AtrasVida on 2018-04-03 at 4:50 PM
 */

public class NetWorkCheckingService extends Service {
    private Context context;

    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        Checking();
        return Service.START_STICKY;
    }

    private void Checking() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) {

                        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                            @Override
                            public void onConnected() {
                                if (App.netWorkStatusListener != null) {
                                    App.netWorkStatusListener.Connected();
                                }

                                App.networkStatus = NetworkStatus.Connected;

                            }

                            @Override
                            public void disConnected() {
                                if (App.netWorkStatusListener != null) {
                                    App.netWorkStatusListener.WatingForNetwork();
                                }

                                App.networkStatus = NetworkStatus.WatingForNetwork;
                            }

                            @Override
                            public void onFailed() {
                                if (App.netWorkStatusListener != null) {
                                    App.netWorkStatusListener.WatingForNetwork();
                                }

                                App.networkStatus = NetworkStatus.WatingForNetwork;
                            }
                        });

                    }

                });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
