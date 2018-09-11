package avida.ican.Farzin.Presenter.Service.Message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.MessageStatus;
import avida.ican.Farzin.Model.Enum.MessageType;
import avida.ican.Farzin.Model.Interface.MessageListListener;
import avida.ican.Farzin.Model.Interface.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Response.StructureMessageRES;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-08-08 at 4:00 PM
 */

public class GetSentMessageService extends Service {
    private final long DELAY = TimeValue.SecondsInMilli() * 35;
    private MessageListListener messageListListener;
    private Context context;
    private Handler handler = new Handler();
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private FarzinMessageQuery farzinMessageQuery;
    private int pageNumber = 1;
    private MessageStatus messageStatus = MessageStatus.UnRead;
    private int Count = 1;
    private final int MaxCount = 10;
    private final int MinCount = 1;


    @Override
    public void onCreate() {
        context = App.getServiceContext();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        Count = MaxCount;
        pageNumber = 1;
// TODO: 2018-08-08 get status fild from server and set to status Message for save it
        getMessageFromServerPresenter = new GetMessageFromServerPresenter();
        farzinMessageQuery = new FarzinMessageQuery();
        messageListListener = new MessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                if (messageList.size() == 0) {
                    reGetMessage();
                } else {
                    SaveMessage(messageList);
                }
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onCancel() {

            }
        };
        GetMessage(pageNumber, Count);

        return Service.START_STICKY;
    }

    private void GetMessage(int pageNumber, int count) {
        getMessageFromServerPresenter.GetSentMessageList(pageNumber, count, messageListListener);
    }


    private void SaveMessage(final ArrayList<StructureMessageRES> messageList) {
        final StructureMessageRES structureMessageRES = messageList.get(0);

        farzinMessageQuery.SaveMessage(structureMessageRES, MessageType.SENDED, messageStatus, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(final StructureMessageDB structureMessageDB) {

                if (App.fragmentMessageList != null) {
                    if (App.CurentActivity != null) {
                        App.CurentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                App.fragmentMessageList.AddSendNewMessage(structureMessageDB);
                                // UpdateAllNewMessageStatusToUnreadStatus();
                            }
                        });

                    }
                }

                if (messageList.size() > 0) {
                    messageList.remove(0);
                }

                if (messageList.size() == 0) {
                    pageNumber = pageNumber + 1;
                    GetMessage(pageNumber, Count);
                } else {
                    SaveMessage(messageList);
                }
            }

            @Override
            public void onExisting() {
                ShowToast("Duplicate message");
                reGetMessage();

            }

            @Override
            public void onFailed(String message) {
                ShowToast("save message onFailed");
            }

            @Override
            public void onCancel() {
                ShowToast("save message onCancel");
            }
        });


    }


    private void reGetMessage() {
        ShowToast("re Get Message");
        pageNumber = 1;
        Count = MinCount;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetMessage(pageNumber, Count);
            }
        }, DELAY);
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
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
