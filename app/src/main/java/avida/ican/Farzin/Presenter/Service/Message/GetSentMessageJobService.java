package avida.ican.Farzin.Presenter.Service.Message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

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
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-08-08 at 4:00 PM
 */

public class GetSentMessageJobService extends JobService {

    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private MessageListListener messageListListener;
    private Context context;
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private FarzinMessageQuery farzinMessageQuery;
    private Status status = Status.UnRead;
    private int Count = 1;
    private final int MaxCount = 50;
    private final int MinCount = 20;
    private int existCont = 0;
    private int dataSize = 0;
    private JobParameters job;
    private long tempDelay = 30;
    private static JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;

    public GetSentMessageJobService() {
    }

    public GetSentMessageJobService getInstance(Context context,JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener) {
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
            if (getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
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

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void initPresenterAndListener() {
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
                    SaveMessage(messageList);
                }
            }

            @Override
            public void onFailed(String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("GetSentMessage error= " + message));

                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);

                } else {
                    finishJob();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(() -> finishJob(), FAILED_DELAY);

                } else {
                    finishJob();
                }
            }
        };
    }


    @SuppressLint("StaticFieldLeak")
    private void SaveMessage(final ArrayList<StructureMessageRES> messageList) {
        final StructureMessageRES structureMessageRES = messageList.get(0);

        AsyncTask<List<StructureReceiverRES>, Void, Void> asyncTask = new AsyncTask<List<StructureReceiverRES>, Void, Void>() {
            @Override
            protected Void doInBackground(List<StructureReceiverRES>[] lists) {
                int count = 0;
                for (int i = 0; i < lists.length; i++) {
                    if (lists[0].get(i).isRead()) {
                        count++;
                    }
                }
                if (count == lists.length) {
                    status = avida.ican.Farzin.Model.Enum.Status.READ;
                } else {
                    status = avida.ican.Farzin.Model.Enum.Status.UnRead;
                }
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, structureMessageRES.getReceivers());

        farzinMessageQuery.SaveMessage(structureMessageRES, Type.SENDED, status, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(final StructureMessageDB structureMessageDB) {
                if (structureMessageDB.getId() <= 0) {
                    existCont++;
                } else {
                    if (App.fragmentMessageList != null) {
                        if (App.CurentActivity != null) {
                            App.CurentActivity.runOnUiThread(() -> {
                                App.fragmentMessageList.UpdateMessageData();
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
                ACRA.getErrorReporter().handleSilentException(new Exception("GetSentMessage Save Message error= " + message));

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
            Log.i("LOG", "SendMessage: existCont= " + existCont + " dataSize= " + dataSize);
            if (existCont == dataSize) {
                finishJob();
            } else {
                startJob(Count);
            }

        } else {
            SaveMessage(messageList);
        }
    }


    private void startJob(int count) {
        if (!canGetData()) {
            finishJob();
        } else {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                finishJob();
            } else {
                if (!getFarzinPrefrences().isSendMessageForFirstTimeSync()) {
                    getMessageFromServerPresenter.GetSentMessageList(count, messageListListener);
                } else {
                    CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getMessageSentLastCheckDate(), (TimeValue.SecondsInMilli() * tempDelay));
                    if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                        getMessageFromServerPresenter.GetSentMessageList(count, messageListListener);
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
            existCont = 0;
            jobFinished(job, false);
            jobServiceMessageSchedulerListener.onSuccess(MessageJobServiceNameEnum.GetSentMessage);
        } catch (Exception e) {
            CustomLogger.setLog(e.toString());
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }
    }

    private void callDataFinish() {
        Count = MinCount;
        getFarzinPrefrences().putSendMessageForFirstTimeSync(true);
        if (BaseActivity.dialogDataSyncing != null) {
            BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncSendMessage);
        }
    }

    private boolean canGetData() {
        boolean can = false;
        if (getFarzinPrefrences().isSendMessageForFirstTimeSync() && !getFarzinPrefrences().isDataForFirstTimeSync()) {
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
