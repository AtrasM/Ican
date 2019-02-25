package avida.ican.Farzin.Presenter.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import avida.ican.Farzin.Presenter.CheckServerAviablePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-12-18 at 6:36 PM
 */

public class CheckServerAviableService extends Service {
    private final long DELAYWhenAppClose = TimeValue.SecondsInMilli() * 35;
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 15;
    private Context context;
    private ListenerNetwork listenerNetwork;
    private Handler handler = new Handler();
    private CheckServerAviablePresenter checkServerAviablePresenter;
    private long tempDelay;


    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        checkServerAviablePresenter = new CheckServerAviablePresenter();
        listenerNetwork = new ListenerNetwork() {

            @Override
            public void onConnected() {
                if (App.networkStatus != null) {
                    if (App.networkStatus == NetworkStatus.Syncing) {
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.Syncing();
                        }
                    } else {
                        App.networkStatus = NetworkStatus.Connected;
                        if (App.netWorkStatusListener != null) {
                            App.netWorkStatusListener.Connected();
                        }
                    }

                }

                reCheck();
            }

            @Override
            public void disConnected() {
                if (App.networkStatus != null) {
                    App.networkStatus = NetworkStatus.WatingForNetwork;
                    if (App.netWorkStatusListener != null) {
                        App.netWorkStatusListener.WatingForNetwork();
                    }
                }
                reCheck();
            }

            @Override
            public void onFailed() {
                if (App.networkStatus != null) {
                    App.networkStatus = NetworkStatus.WatingForNetwork;
                    if (App.netWorkStatusListener != null) {
                        App.netWorkStatusListener.WatingForNetwork();
                    }
                }
                reCheck();
            }
        };

        Check();

        return Service.START_STICKY;
    }

    private void Check() {
        checkServerAviablePresenter.CallRequest(listenerNetwork);
    }


    private void reCheck() {
        if (App.activityStacks == null) {
            tempDelay = DELAYWhenAppClose;
        } else {
            tempDelay = DELAY;
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Check();
            }
        }, tempDelay);
    }

    private void ShowToast(final String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (App.isTestMod) {
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void threadSleep() {
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (App.isTestMod) {
            Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        }
        //timer.cancel();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

}
