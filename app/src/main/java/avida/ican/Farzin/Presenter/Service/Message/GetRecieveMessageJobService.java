package avida.ican.Farzin.Presenter.Service.Message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Farzin.Presenter.Notification.MessageNotificationPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-07-23 at 5:35 PM
 */

public class GetRecieveMessageJobService extends JobService {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private MessageListListener messageListListener;
    private Context context;
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private FarzinMessageQuery farzinMessageQuery;
    private Status status = Status.IsNew;
    private int Count = 1;
    private final int MaxCount = 50;
    private final int MinCount = 20;
    private static int newCount = 0;
    private boolean canShowNotification = true;
    private int existCont = 0;
    private int dataSize = 0;
    private MessageNotificationPresenter messageNotificationPresenter;
    private JobParameters job;
    private long tempDelay = 30;
    private static JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;

    public GetRecieveMessageJobService() {
    }

    public GetRecieveMessageJobService getInstance(Context context,JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener) {
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
            if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
                Count = MinCount;
                callDataFinish();
            } else {
                Count = MaxCount;
            }

            new Thread(() -> {
                initPresenterAndListener();
                startJob(Count);
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }


        return false;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void initPresenterAndListener() {
        messageNotificationPresenter = new MessageNotificationPresenter(context);
        getMessageFromServerPresenter = new GetMessageFromServerPresenter();
        farzinMessageQuery = new FarzinMessageQuery();
        messageListListener = new MessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                existCont = 0;
                dataSize = messageList.size();
                if (messageList.size() == 0) {
                    finishJob();
                } else {
                    canShowNotification = true;
                    SaveMessage(messageList);
                }
            }

            @Override
            public void onFailed(String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("GetReceiveMessage error= " + message));

                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);

                } else {
                    finishJob();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);

                } else {
                    finishJob();
                }
            }
        };
    }


    private void SaveMessage(final ArrayList<StructureMessageRES> messageList) {

        final StructureMessageRES structureMessageRES = messageList.get(0);

        if (structureMessageRES.isRead()) {
            status = Status.READ;
        } else {
            status = Status.UnRead;
        }

        ArrayList<StructureReceiverRES> receiversRES = new ArrayList<>();
        StructureUserAndRoleDB UserAndRoleDB = new FarzinMetaDataQuery(context).getUserInfo(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID());
        StructureReceiverRES receiverRES = new StructureReceiverRES(UserAndRoleDB.getRole_ID(), UserAndRoleDB.getUser_ID(), UserAndRoleDB.getRoleName(), UserAndRoleDB.getFirstName(), UserAndRoleDB.getLastName(), UserAndRoleDB.getLastName(), false, "");
        receiversRES.add(receiverRES);

        structureMessageRES.setReceivers(receiversRES);


        farzinMessageQuery.SaveMessage(structureMessageRES, Type.RECEIVED, status, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(StructureMessageDB structureMessageDB) {

                if (structureMessageDB.getId() <= 0) {
                    existCont++;
                } else {

                    if (structureMessageDB.getStatus() == Status.UnRead) {
                        newCount++;
                    }

                    if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync() && getFarzinPrefrences().isDataForFirstTimeSync()) {
                        farzinMessageQuery.updateMessageIsNewStatus(structureMessageDB.getId(), true);
                    }
                    if (App.fragmentMessageList != null) {
                        final ArrayList<StructureMessageDB> structureMessagesDB = new ArrayList<>();
                        structureMessagesDB.add(structureMessageDB);
                        if (App.CurentActivity != null) {
                            App.getHandlerMainThread().post(() -> {
                                if (App.fragmentMessageList != null) {
                                    App.fragmentMessageList.AddReceiveNewMessage(structureMessagesDB);
                                }
                                // UpdateAllNewMessageStatusToUnreadStatus();
                            });
                        }
                    }
                }

                continueeProcessGetMessage(messageList);

            }

            @Override
            public void onExisting() {
                existCont++;
                continueeProcessGetMessage(messageList);
            }

            @Override
            public void onFailed(String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("GetReceiveMessage Save Message error= " + message));

                App.getHandlerMainThread().postDelayed(() -> {
                    try {
                        messageList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SaveMessage(messageList);
                }, FAILED_DELAY);
            }

            @Override
            public void onCancel() {
                App.getHandlerMainThread().postDelayed(() -> {
                    try {
                        messageList.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SaveMessage(messageList);
                }, FAILED_DELAY);
            }
        });
    }

    private void continueeProcessGetMessage(ArrayList<StructureMessageRES> messageList) {
        try {
            messageList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (messageList.size() == 0) {
            Log.i("LOG", "RecieveMessage: existCont= " + existCont + " dataSize= " + dataSize);
            if (existCont == dataSize) {
                finishJob();
            } else {
                startJob(Count);
            }

        } else {
            SaveMessage(messageList);
        }
    }

    private void CallMulltiMessageNotification() {
        if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
            newCount = 0;
            return;
        }
        if (!canShowNotification) {
            newCount = 0;
            return;
        }
        try {
            if (newCount > 0) {
                long count = farzinMessageQuery.getNewMessageCount();
                if (count > 0) {
                    String title = Resorse.getString(context, R.string.newMessage);
                    String message = count + " " + Resorse.getString(context, R.string.NewMessageContent);
                    messageNotificationPresenter.callNotification(title, "" + message, messageNotificationPresenter.GetNotificationPendingIntent(messageNotificationPresenter.GetMultiMessageIntent()));
                }
            }
            newCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }

    }


    private void startJob(int count) {
        if (!canGetData()) {
            finishJob();
        } else {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                finishJob();
            } else {
                if (!getFarzinPrefrences().isReceiveMessageForFirstTimeSync()) {
                    getMessageFromServerPresenter.GetReceiveMessageList(count, messageListListener);
                } else {
                    CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getMessageRecieveLastCheckDate(), (TimeValue.SecondsInMilli() * tempDelay));
                    if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                        getMessageFromServerPresenter.GetReceiveMessageList(count, messageListListener);
                    } else {
                        finishJob();
                    }
                }
            }
        }
    }


    private void finishJob() {
        try {
            callDataFinish();
            Count = MinCount;
            CallMulltiMessageNotification();
            Count = MinCount;
            newCount = 0;
            existCont = 0;
            jobFinished(job, false);
            jobServiceMessageSchedulerListener.onSuccess(MessageJobServiceNameEnum.GetRecieveMessage);
        } catch (Exception e) {
            CustomLogger.setLog(e.toString());
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }
    }

    private void callDataFinish() {
        Count = MinCount;
        getFarzinPrefrences().putReceiveMessageForFirstTimeSync(true);
        if (BaseActivity.dialogDataSyncing != null) {
            canShowNotification = false;
            BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncReceiveMessage);
        }
    }

    private boolean canGetData() {
        boolean can = false;
        if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync() && !getFarzinPrefrences().isDataForFirstTimeSync()) {
            can = false;
        } else {
            can = true;
        }
        return can;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
