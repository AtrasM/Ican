package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.OpticalPenPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-10-31 at 5:12 PM
 */

public class OpticalPenQueueService extends Service {
    private final long PERIOD = TimeValue.SecondsInMilli() * 30;
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 25;
    private Timer timer;
    private TimerTask timerTask;
    private OpticalPenListener opticalPenListener;
    private Context context;
    private Handler handler = new Handler();
    private OpticalPenPresenter opticalPenPresenter;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private static int tryCount = 0;
    private final static int MaxTry = 3;

    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        ShowToast("Service Start");
        startOpticalPenQueueTimer();
        return Service.START_STICKY;
    }

    private void sendOpticalPen(final StructureOpticalPenQueueDB structureOpticalPenQueueDB) {
        opticalPenPresenter = new OpticalPenPresenter();
        opticalPenListener = new OpticalPenListener() {

            @Override
            public void onSuccess() {
                if (farzinCartableQuery.deletItemOpticalPenQueue(structureOpticalPenQueueDB.getId())) {

                    StructureOpticalPenQueueDB opticalPenQueueDB = new FarzinCartableQuery().getFirstItemOpticalPenQueue();

                    if (opticalPenQueueDB != null) {
                        if (opticalPenQueueDB.getETC() > 0) {
                            sendOpticalPen(opticalPenQueueDB);
                        } else {
                            startOpticalPenQueueTimer();
                        }
                    } else {
                        startOpticalPenQueueTimer();
                    }
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess();
                        }
                    }, FAILED_DELAY);
                }
            }

            @Override
            public void onFailed(String message) {
                tryAgain(structureOpticalPenQueueDB);
            }

            @Override
            public void onCancel() {
                tryAgain(structureOpticalPenQueueDB);
            }

            @Override
            public void onFinish() {
                startOpticalPenQueueTimer();
            }


        };
        StructureOpticalPenREQ structureOpticalPenREQ = new StructureOpticalPenREQ(structureOpticalPenQueueDB.getETC(), structureOpticalPenQueueDB.getEC(), structureOpticalPenQueueDB.getBfile(), structureOpticalPenQueueDB.getStrExtention(), structureOpticalPenQueueDB.getHameshtitle(), structureOpticalPenQueueDB.isHiddenHamesh());
        opticalPenPresenter.CallRequest(structureOpticalPenREQ, opticalPenListener);

    }

    private void tryAgain(final StructureOpticalPenQueueDB structureOpticalPenQueueDB) {
        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                    @Override
                    public void onConnected() {
                        sendOpticalPen(structureOpticalPenQueueDB);
                    }

                    @Override
                    public void disConnected() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                opticalPenListener.onFailed("");
                            }
                        }, FAILED_DELAY);
                    }

                    @Override
                    public void onFailed() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                opticalPenListener.onFailed("");
                            }
                        }, FAILED_DELAY);
                    }
                });
            }
        }, FAILED_DELAY);
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
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void startOpticalPenQueueTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {

                        App.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startOpticalPenQueueTimer();
                            }
                        }, FAILED_DELAY);
                        timer.cancel();
                    } else {
                        StructureOpticalPenQueueDB structureOpticalPenQueueDB = new FarzinCartableQuery().getFirstItemOpticalPenQueue();

                        if (structureOpticalPenQueueDB != null) {
                            if (structureOpticalPenQueueDB.getETC() > 0) {
                                sendOpticalPen(structureOpticalPenQueueDB);
                            }
                        }
                        timer.cancel();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, PERIOD);
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

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}