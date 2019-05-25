package avida.ican.Farzin.Presenter.Message;

import android.os.Handler;

import java.util.ArrayList;

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
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2019-5-25 at 12:10 PM
 */

public class FarzinGetMessagePresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 2;
    private Handler handler = new Handler();
    private FarzinMessageQuery farzinMessageQuery;
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private MessageDataListener messageDataListener;
    private MessageListListener messageListListener;
    private Status status;
    private int Count = 500;
    private int existCont = 0;
    private int dataSize = 0;
    private static int newCount = 0;
    private Type type;

    public FarzinGetMessagePresenter(MessageDataListener messageDataListener) {
        this.messageDataListener = messageDataListener;
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
        this.type = type;
        if (type == Type.RECEIVED) {
            getMessageFromServerPresenter.GetReceiveMessageList(Count, messageListListener);
        } else {
            getMessageFromServerPresenter.GetSentMessageList(Count, messageListListener);
        }

    }

    public void GetFromServer(String startDateTime, String finishDateTime, Type type) {
        this.type = type;
        if (type == Type.RECEIVED) {
            getMessageFromServerPresenter.GetReceiveMessageList(startDateTime, finishDateTime, Count, messageListListener);
        } else {
            getMessageFromServerPresenter.GetSentMessageList(startDateTime, finishDateTime, Count, messageListListener);
        }
    }


    private void SaveData(final ArrayList<StructureMessageRES> messageList) {
        final StructureMessageRES structureMessageRES = messageList.get(0);

        if (structureMessageRES.isRead()) {
            status = Status.READ;
        } else {
            status = Status.UnRead;
        }

        ArrayList<StructureReceiverRES> receiversRES = new ArrayList<>();
        StructureUserAndRoleDB UserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID());
        StructureReceiverRES receiverRES = new StructureReceiverRES(UserAndRoleDB.getRole_ID(), UserAndRoleDB.getUser_ID(), UserAndRoleDB.getLastName(), false, "");
        receiversRES.add(receiverRES);
        structureMessageRES.setReceivers(receiversRES);
        farzinMessageQuery.SaveMessage(structureMessageRES, Type.RECEIVED, status, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(StructureMessageDB structureMessageDB) {


                try {
                    messageList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (messageList.size() == 0) {
                    messageDataListener.newData();
                } else {
                    SaveData(messageList);
                }


            }

            @Override
            public void onExisting() {
                messageDataListener.noData();

            }

            @Override
            public void onFailed(String message) {
                handler.postDelayed(() -> {
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
                handler.postDelayed(() -> {
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


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
