package avida.ican.Farzin.Presenter.Service.Message;

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
import avida.ican.Farzin.Model.Interface.Message.MessageListener;
import avida.ican.Farzin.Model.Interface.Message.SendMessageServiceListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.SendMessageToServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-06-20 at 3:49 PM
 */

public class SendMessageService extends Service {

    private final long PERIOD = TimeValue.SecondsInMilli() * 30;
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 30;
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
                ShowToast("onSuccess");
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
                        //ShowToast("onFailed -->> " + App.networkStatus + " ****** " + App.netWorkStatusListener);
                        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                            App.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onFailed("", structureMessageQueueDBS);
                                }
                            }, FAILED_DELAY);

                        } else {
                            if (tryCount == MaxTry) {
                                new FarzinMessageQuery().UpdateMessageQueueStatus(structureMessageQueueDBS.get(0).getId(), Status.STOPED);
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
        if (structureMessageDBS == null) {
            new FarzinMessageQuery().DeletMessageQueueRowWithId(structureMessageQueueDBS.get(0).getId());
        }
        //___________________-----------------------------_____________start block StructureMessageFileDB____________--------------------------------------_____________________
        ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
        if (structureMessageDBS.getMessage_files() != null) {
            ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>(structureMessageDBS.getMessage_files());
            for (int i = 0; i < structureMessageFileDBS.size(); i++) {
                StructureAttach structureAttach = new StructureAttach(structureMessageFileDBS.get(i).getFile_path(), structureMessageFileDBS.get(i).getFile_name(), structureMessageFileDBS.get(i).getFile_extension());
                structureAttaches.add(structureAttach);
                threadSleep();
            }
        }
        //___________________-----------------------------_____________end block StructureMessageFileDB____________--------------------------------------_____________________


        //___________________-----------------------------_____________start block StructureReceiverDB____________--------------------------------------_____________________

        ArrayList<StructureReceiverDB> structureReceiverDBS = new ArrayList<>(structureMessageDBS.getReceivers());
        List<StructureUserAndRoleDB> structureUserAndRole = new ArrayList<>();
        for (int i = 0; i < structureReceiverDBS.size(); i++) {
            StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB(structureReceiverDBS.get(i).getUser_name(), structureReceiverDBS.get(i).getUser_id(), structureReceiverDBS.get(i).getRole_id());
            structureUserAndRole.add(structureUserAndRoleDB);
            threadSleep();
        }

        //___________________-----------------------------_____________end block StructureReceiverDB____________--------------------------------------_____________________

        sendMessageToServerPresenter.SendMessage(structureMessageDBS.getSubject(), structureMessageDBS.getContent(), structureAttaches, structureUserAndRole, new MessageListener() {


            @Override
            public void onSuccess(int MessageID) {
                FarzinMessageQuery farzinMessageQuery = new FarzinMessageQuery();
                int idMessageQueue = structureMessageQueueDBS.get(0).getId();
                int Id = structureMessageQueueDBS.get(0).getMessage().getId();
                boolean isdelet = farzinMessageQuery.DeletMessageQueueRowWithId(idMessageQueue);
                if (isdelet) {
                    structureMessageQueueDBS.remove(0);
                }
                StructureMessageDB MessageDB = farzinMessageQuery.GetMessageWithId(Id);
                isdelet = farzinMessageQuery.DeletMessageFile(MessageDB);
                farzinMessageQuery.DeletMessageRowWithId(Id);
                sendMessageServiceListener.onSuccess(structureMessageQueueDBS);
            }

            @Override
            public void onFailed(String message) {
                sendMessageServiceListener.onFailed(message, structureMessageQueueDBS);
            }

            @Override
            public void onCancel() {
                sendMessageServiceListener.onCancel(structureMessageQueueDBS);
            }


        });

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


    private void startMessageQueueTimer() {

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {

                    if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {

                        App.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startMessageQueueTimer();
                            }
                        }, FAILED_DELAY);
                        timer.cancel();
                    } else {
                        List<StructureMessageQueueDB> structureMessageQueueDBS = new FarzinMessageQuery().getMessageQueue(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID(), Status.WAITING);

                        if (structureMessageQueueDBS.size() > 0) {
                            SendMessage(structureMessageQueueDBS);
                            timer.cancel();
                        } else {
                            structureMessageQueueDBS = new FarzinMessageQuery().getMessageQueue(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID(), Status.STOPED);
                            if (structureMessageQueueDBS.size() > 0) {
                                SendMessage(structureMessageQueueDBS);
                                timer.cancel();
                            }
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