package avida.ican.Farzin.Presenter;

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

import avida.ican.Farzin.Model.Interface.SendMessageListener;
import avida.ican.Farzin.Model.Interface.SendMessageServiceListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-06-20 at 3:49 PM
 */

public class SendMessageService extends Service {

    private final long PERIOD = TimeValue.MinutesInMilli();
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private Timer timer;
    private TimerTask timerTask;
    private SendMessageServiceListener sendMessageServiceListener;
    private Context context;
    private Handler handler = new Handler();
    private SendMessageToServerPresenter sendMessageToServerPresenter;
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
        startMessageQueueTimer();
        sendMessageServiceListener = new SendMessageServiceListener() {

            @Override
            public void onSuccess(List<StructureMessageQueueDB> structureMessageQueueDBS) {
                if (structureMessageQueueDBS.size() > 0) {
                    SendMessage(structureMessageQueueDBS);
                } else {
                    onFinish();
                }

            }

            @Override
            public void onFailed(String message, final List<StructureMessageQueueDB> structureMessageQueueDBS) {
                // threadSleep(minutesInMilli);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ShowToast("onFailed");
                        if (tryCount == MaxTry) {
                            structureMessageQueueDBS.remove(0);
                            tryCount = 0;
                        }
                        if (structureMessageQueueDBS.size() > 0) {
                            tryCount++;
                            SendMessage(structureMessageQueueDBS);
                        } else {
                            onFinish();
                        }


                    }
                }, DELAY);

            }

            @Override
            public void onCancel(final List<StructureMessageQueueDB> structureMessageQueueDBS) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast("onCancel");
                        SendMessage(structureMessageQueueDBS);
                    }
                }, DELAY);

            }

            @Override
            public void onFinish() {
                startMessageQueueTimer();
            }
        };


        return Service.START_STICKY;
    }

    private void SendMessage(final List<StructureMessageQueueDB> structureMessageQueueDBS) {
        sendMessageToServerPresenter = new SendMessageToServerPresenter();
        final StructureMessageDB structureMessageDBS = structureMessageQueueDBS.get(0).getMessage();
        //___________________-----------------------------_____________start block StructureMessageFileDB____________--------------------------------------_____________________

        ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>(structureMessageDBS.getMessage_files());
        ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
        for (int i = 0; i < structureMessageFileDBS.size(); i++) {
            StructureAttach structureAttach = new StructureAttach(structureMessageFileDBS.get(i).getFile_binary(), structureMessageFileDBS.get(i).getFile_name(), structureMessageFileDBS.get(i).getFile_extension());
            structureAttaches.add(structureAttach);
            threadSleep();
        }

        //___________________-----------------------------_____________end block StructureMessageFileDB____________--------------------------------------_____________________


        //___________________-----------------------------_____________start block StructureReceiverDB____________--------------------------------------_____________________

        ArrayList<StructureReceiverDB> structureReceiverDBS = new ArrayList<>(structureMessageDBS.getReceivers());
        List<StructureUserAndRoleDB> structureUserAndRole = new ArrayList<>();
        for (int i = 0; i < structureReceiverDBS.size(); i++) {
            StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB(structureReceiverDBS.get(i).getUser_name(), structureReceiverDBS.get(i).getUser_id(), structureReceiverDBS.get(i).getRole_id(), structureReceiverDBS.get(i).getNative_id());
            structureUserAndRole.add(structureUserAndRoleDB);
            threadSleep();
        }

        //___________________-----------------------------_____________end block StructureReceiverDB____________--------------------------------------_____________________

        sendMessageToServerPresenter.SendMessage(structureMessageDBS.getSubject(), structureMessageDBS.getContent(), structureAttaches, structureUserAndRole, new SendMessageListener() {


            @Override
            public void onSuccess() {
                ShowToast("onSuccess");
                int id = structureMessageQueueDBS.get(0).getId();
                boolean isdelet = new FarzinMessageQuery().DeletMessageQueueRowWithId(id);
                if (isdelet) {
                    structureMessageQueueDBS.remove(0);
                }

                sendMessageServiceListener.onSuccess(structureMessageQueueDBS);
            }

            @Override
            public void onFailed(String message) {
                ShowToast("onFailed");
                sendMessageServiceListener.onFailed(message, structureMessageQueueDBS);
            }

            @Override
            public void onCancel() {
                ShowToast("onCancel");

                sendMessageServiceListener.onCancel(structureMessageQueueDBS);
            }


        });

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


    private void startMessageQueueTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    StructureUserAndRoleDB structureUserAndRoleDB = new FarzinMetaDataQuery(context).getUserInfo(getFarzinPrefrences().getUserName());

                    if (structureUserAndRoleDB != null) {
                        List<StructureMessageQueueDB> structureMessageQueueDBS = new FarzinMessageQuery().getMessageQueue(structureUserAndRoleDB.getUser_ID(), Integer.parseInt(structureUserAndRoleDB.getRole_ID()));
                        if (structureMessageQueueDBS.size() > 0) {
                            SendMessage(structureMessageQueueDBS);
                            timer.cancel();
                        }
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