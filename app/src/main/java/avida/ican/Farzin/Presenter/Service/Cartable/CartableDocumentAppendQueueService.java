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

import avida.ican.Farzin.Model.Interface.Cartable.SendListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableSendQueueDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendToServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-11-28 at 12:41 PM
 */

public class CartableDocumentAppendQueueService extends Service {

    private final long PERIOD = TimeValue.MinutesInMilli();
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 30;
    private Timer timer;
    private TimerTask timerTask;
    private SendListener sendListener;
    private Context context;
    private Handler handler = new Handler();
    private CartableDocumentAppendToServerPresenter cartableDocumentAppendToServerPresenter;
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
        startSendQueueTimer();
        return Service.START_STICKY;
    }

    private void CartableSend(final StructureCartableSendQueueDB structureCartableSendQueueDB) {
        cartableDocumentAppendToServerPresenter = new CartableDocumentAppendToServerPresenter();
        sendListener = new SendListener() {

            @Override
            public void onSuccess() {
                if (farzinCartableQuery.deletCartableSendQueue(structureCartableSendQueueDB.getId())) {

                    StructureCartableSendQueueDB structureCartableSendQueueDB = new FarzinCartableQuery().getFirstItemCartableSendQueue();

                    if (structureCartableSendQueueDB != null) {
                        if (structureCartableSendQueueDB.getETC() > 0) {
                            CartableSend(structureCartableSendQueueDB);
                        } else {
                            startSendQueueTimer();
                        }
                    } else {
                        startSendQueueTimer();
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
                tryAgain(structureCartableSendQueueDB);
            }

            @Override
            public void onCancel() {
                tryAgain(structureCartableSendQueueDB);
            }

            @Override
            public void onFinish() {
                startSendQueueTimer();
            }


        };

        StructureAppendREQ structureAppendREQ = new CustomFunction().ConvertStringToObject(structureCartableSendQueueDB.getStrStructureAppendREQ(), StructureAppendREQ.class);
        cartableDocumentAppendToServerPresenter.AppendDocument(structureAppendREQ, sendListener);

    }

    private void tryAgain(final StructureCartableSendQueueDB structureCartableSendQueueDB) {
        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                    @Override
                    public void onConnected() {
                        CartableSend(structureCartableSendQueueDB);
                    }

                    @Override
                    public void disConnected() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendListener.onFailed("");
                            }
                        }, FAILED_DELAY);
                    }

                    @Override
                    public void onFailed() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendListener.onFailed("");
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


    private void startSendQueueTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    StructureCartableSendQueueDB structureCartableSendQueueDB = new FarzinCartableQuery().getFirstItemCartableSendQueue();

                    if (structureCartableSendQueueDB != null) {
                        if (structureCartableSendQueueDB.getETC() > 0) {
                            CartableSend(structureCartableSendQueueDB);
                        }
                    }
                    timer.cancel();

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