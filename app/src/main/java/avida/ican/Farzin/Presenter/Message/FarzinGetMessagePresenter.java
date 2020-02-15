package avida.ican.Farzin.Presenter.Message;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageDataListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.Notification.MessageNotificationPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-5-25 at 12:10 PM
 */

public class FarzinGetMessagePresenter {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 2;
    private FarzinMessageQuery farzinMessageQuery;
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private MessageDataListener messageDataListener;
    private MessageListListener messageListListener;
    private Status status;
    private int Count = 100;
    private int existCont = 0;
    private int dataSize = 0;
    private static int newCount = 0;
    private Type type = Type.RECEIVED;
    private Activity context;
    private MessageNotificationPresenter messageNotificationPresenter;

    public FarzinGetMessagePresenter(Activity context, MessageDataListener messageDataListener) {
        this.context = context;
        this.messageDataListener = messageDataListener;
        messageNotificationPresenter = new MessageNotificationPresenter(context);
        initMessageListListener();
    }

    private void initMessageListListener() {
        getMessageFromServerPresenter = new GetMessageFromServerPresenter();
        farzinMessageQuery = new FarzinMessageQuery();
        messageListListener = new MessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                existCont = 0;
                dataSize = messageList.size();
                if (dataSize == 0) {
                    messageDataListener.noData();
                } else {
                    SaveData(messageList);

                }
            }

            @Override
            public void onFailed(String message) {
                messageDataListener.onFailed(message);
            }

            @Override
            public void onCancel() {
                messageDataListener.onFailed("");
            }
        };

    }

    public void GetFromServer(Type type) {
        newCount = 0;
        this.type = type;
        if (type == Type.RECEIVED) {
            getMessageFromServerPresenter.GetReceiveMessageList(Count, messageListListener);
        } else {
            getMessageFromServerPresenter.GetSentMessageList(Count, messageListListener);
        }

    }

    public void GetFromServer(String startDateTime, String finishDateTime, Type type) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            messageDataListener.onFailed(Resorse.getString(R.string.unable_get_data_in_ofline_mode));
        } else {
            newCount = 0;
            this.type = type;
            if (type == Type.RECEIVED) {
                getMessageFromServerPresenter.GetReceiveMessageList(startDateTime, finishDateTime, Count, messageListListener);
            } else {
                getMessageFromServerPresenter.GetSentMessageList(startDateTime, finishDateTime, Count, messageListListener);
            }
        }
    }


    private void SaveData(final ArrayList<StructureMessageRES> messageList) {

        final StructureMessageRES structureMessageRES = messageList.get(0);
        if (this.type == Type.RECEIVED) {

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
        } else {
            @SuppressLint("StaticFieldLeak") AsyncTask<List<StructureReceiverRES>, Void, Void> asyncTask = new AsyncTask<List<StructureReceiverRES>, Void, Void>() {
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
        }
        farzinMessageQuery.SaveMessage(structureMessageRES, this.type, status, new MessageQuerySaveListener() {
            @Override
            public void onSuccess(StructureMessageDB structureMessageDB) {
                if (structureMessageDB.getId() <= 0) {
                    existCont++;
                } else {
                    if (structureMessageDB.getType() == Type.RECEIVED) {
                        if (getFarzinPrefrences().isReceiveMessageForFirstTimeSync() && getFarzinPrefrences().isDataForFirstTimeSync()) {
                            farzinMessageQuery.updateMessageIsNewStatus(structureMessageDB.getId(), true);
                        }
                    }
                    if (structureMessageDB.getStatus() == Status.UnRead) {
                        newCount++;
                    }
                }

                try {
                    messageList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (messageList.size() == 0) {
                    callMulltiNotification();

                } else {
                    SaveData(messageList);
                }


            }

            @Override
            public void onExisting() {
                existCont++;
                try {
                    messageList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (messageList.size() == 0) {
                    if (existCont == dataSize) {
                        messageDataListener.noData();
                    } else {
                        callMulltiNotification();
                    }

                } else {
                    SaveData(messageList);
                }

            }

            @Override
            public void onFailed(String message) {
                App.getHandlerMainThread().postDelayed(() -> {
                    if (messageList.size() == 0) {
                        messageDataListener.onFailed(message);
                    } else {
                        try {
                            messageList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveData(messageList);
                    }
                }, FAILED_DELAY);
            }

            @Override
            public void onCancel() {
                App.getHandlerMainThread().postDelayed(() -> {
                    if (messageList.size() == 0) {
                        messageDataListener.onFailed("");
                    } else {
                        try {
                            messageList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveData(messageList);
                    }
                }, FAILED_DELAY);
            }
        });


    }

    private void GetMessage(int count) {

    }

    private void callMulltiNotification() {

        if (newCount > 0) {
            long count = farzinMessageQuery.getNewMessageCount();
            if (count > 0) {
                String title = Resorse.getString(R.string.newMessage);
                String message = count + " " + Resorse.getString(R.string.NewMessageContent);
                messageNotificationPresenter.callNotification(title, "" + message, messageNotificationPresenter.GetNotificationPendingIntent(messageNotificationPresenter.GetMultiMessageIntent()));

            }

        }
        newCount = 0;
        messageDataListener.newData();
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
