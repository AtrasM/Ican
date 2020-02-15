package avida.ican.Farzin.Presenter.Service.Message;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListener;
import avida.ican.Farzin.Model.Interface.Message.SendMessageServiceListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.SendMessageToServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-06-20 at 3:49 PM
 */

public class SendMessageJobService extends JobService {

    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private SendMessageServiceListener sendMessageServiceListener;
    private SendMessageToServerPresenter sendMessageToServerPresenter;
    private static int tryCount = 0;
    private final static int MaxTry = 3;
    private Context context;
    private JobParameters job;
    private static JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;

    public SendMessageJobService() {
    }

    public SendMessageJobService getInstance(Context context,JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener) {
        this.context = context;
        this.jobServiceMessageSchedulerListener = jobServiceMessageSchedulerListener;
        return this;
    }


    @Override
    public boolean onStartJob(JobParameters job) {
        context = this;
        App.setServiceContext(context);
        this.job = job;
        try {
            new Thread(() -> {
                initPresenterAndListener();
                startJob();
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void initPresenterAndListener() {
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
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    new FarzinMessageQuery().setErrorToMessageQueue(structureMessageQueueDBS.get(0).getId(), message);

                    structureMessageQueueDBS.remove(0);
                    if (structureMessageQueueDBS.size() > 0) {
                        SendMessage(structureMessageQueueDBS);
                    } else {
                        onFinish();
                    }
                } else {
                    try {
                        if (structureMessageQueueDBS.size() > 0) {
                            App.getHandlerMainThread().postDelayed(() -> tryAgain(structureMessageQueueDBS), FAILED_DELAY);
                        } else {
                            onFinish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        onFinish();
                    }
                }

            }

            @Override
            public void onCancel(final List<StructureMessageQueueDB> structureMessageQueueDBS) {
                App.getHandlerMainThread().postDelayed(() -> {
                    SendMessage(structureMessageQueueDBS);
                }, FAILED_DELAY);

            }

            @Override
            public void onFinish() {
                tryCount = 0;
                finishJob();
            }
        };
    }

    private void SendMessage(final List<StructureMessageQueueDB> structureMessageQueueDBS) {
        tryCount++;
        sendMessageToServerPresenter = new SendMessageToServerPresenter();
        final StructureMessageDB structureMessageDBS = structureMessageQueueDBS.get(0).getMessage();
        if (structureMessageDBS == null) {
            new FarzinMessageQuery().DeletMessageQueue(structureMessageQueueDBS.get(0).getId(), structureMessageQueueDBS.get(0).getMessage());
            structureMessageQueueDBS.remove(0);
            sendMessageServiceListener.onSuccess(structureMessageQueueDBS);
            return;
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
            StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB(structureReceiverDBS.get(i).getUser_name(), structureReceiverDBS.get(i).getUser_id(), structureReceiverDBS.get(i).getRole_id(), structureReceiverDBS.get(i).getRole_name(), structureReceiverDBS.get(i).getFirst_name(), structureReceiverDBS.get(i).getLast_name());
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
                boolean isdelet = farzinMessageQuery.DeletMessageQueue(idMessageQueue, structureMessageQueueDBS.get(0).getMessage());
                /*   boolean isdelet = farzinMessageQuery.DeletMessageQueueRowWithId(idMessageQueue);
                 *//*if (isdelet) {
                }*//*
                StructureMessageDB MessageDB = farzinMessageQuery.GetMessageWithId(Id);
                isdelet = farzinMessageQuery.DeletMessageFile(MessageDB);
                isdelet = farzinMessageQuery.DeletMessageRowWithId(Id);*/
                structureMessageQueueDBS.remove(0);
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

    private void tryAgain(final List<StructureMessageQueueDB> structureMessageQueueDBS) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                SendMessage(structureMessageQueueDBS);
            }

            @Override
            public void disConnected() {
                tryCount++;
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    App.getHandlerMainThread().postDelayed(() -> tryAgain(structureMessageQueueDBS), FAILED_DELAY);
                }
            }

            @Override
            public void onFailed() {
                finishJob();

            }
        });
    }

    private void ShowToast(final String s) {
        App.getHandlerMainThread().post(new Runnable() {
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

    private void startJob() {
        try {

            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                finishJob();
            } else {
                List<StructureMessageQueueDB> structureMessageQueueDBS = new FarzinMessageQuery().getMessageQueue(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID(), QueueStatus.WAITING);
                if (structureMessageQueueDBS.size() > 0) {
                    SendMessage(structureMessageQueueDBS);
                } else {
                    finishJob();
                }
            }

        } catch (Exception e) {
            finishJob();
            e.printStackTrace();
        }
    }

    private void finishJob() {
        tryCount = 0;
        try {
            jobFinished(job, false);
            jobServiceMessageSchedulerListener.onSuccess(MessageJobServiceNameEnum.SendMessage);
        } catch (Exception e) {
            CustomLogger.setLog(e.toString());
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}