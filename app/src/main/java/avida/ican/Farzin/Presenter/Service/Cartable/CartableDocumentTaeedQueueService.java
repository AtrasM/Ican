package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListener;
import avida.ican.Farzin.Model.Interface.Message.SendMessageServiceListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentTaeedQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentTaeedPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.SendMessageToServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerInternet;

/**
 * Created by AtrasVida on 2018-10-24 at 10:01 PM
 */

public class CartableDocumentTaeedQueueService extends Service {

    private final long PERIOD = TimeValue.MinutesInMilli();
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 30;
    private Timer timer;
    private TimerTask timerTask;
    private TaeedListener taeedListener;
    private Context context;
    private Handler handler = new Handler();
    private CartableDocumentTaeedPresenter cartableDocumentTaeedPresenter;
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
        startTaeedQueueTimer();
        return Service.START_STICKY;
    }

    private void TaeedDocument(final int receiveCode) {
        cartableDocumentTaeedPresenter = new CartableDocumentTaeedPresenter();
        taeedListener = new TaeedListener() {

            @Override
            public void onSuccess() {
                if (farzinCartableQuery.deletCartableDocumentTaeedQueue(receiveCode)) {
                    farzinCartableQuery.deletCartableDocumentAllContent(receiveCode);

                    StructureCartableDocumentTaeedQueueDB cartableDocumentTaeedQueueDB = new FarzinCartableQuery().getFirstItemTaeedQueue();

                    if (cartableDocumentTaeedQueueDB != null) {
                        if (cartableDocumentTaeedQueueDB.getReceiveCode() > 0) {
                            TaeedDocument(cartableDocumentTaeedQueueDB.getReceiveCode());
                        } else {
                            startTaeedQueueTimer();
                        }
                    } else {
                        startTaeedQueueTimer();
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
                tryAgain(receiveCode);
            }

            @Override
            public void onCancel() {
                tryAgain(receiveCode);
            }

            @Override
            public void onFinish() {
                startTaeedQueueTimer();
            }


        };
        cartableDocumentTaeedPresenter.TaeedDocument(receiveCode, taeedListener);

    }

    private void tryAgain(final int receiveCode) {
        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckNetworkAvailability().isInternetAvailable(new ListenerInternet() {
                    @Override
                    public void onConnected() {
                        TaeedDocument(receiveCode);
                    }

                    @Override
                    public void disConnected() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                taeedListener.onFailed("");
                            }
                        }, FAILED_DELAY);
                    }

                    @Override
                    public void onFailed() {
                        App.getHandlerMainThread().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                taeedListener.onFailed("");
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
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
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


    private void startTaeedQueueTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    StructureCartableDocumentTaeedQueueDB cartableDocumentTaeedQueueDB = new FarzinCartableQuery().getFirstItemTaeedQueue();

                    if (cartableDocumentTaeedQueueDB != null) {
                        if (cartableDocumentTaeedQueueDB.getReceiveCode() > 0) {
                            TaeedDocument(cartableDocumentTaeedQueueDB.getReceiveCode());
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
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
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