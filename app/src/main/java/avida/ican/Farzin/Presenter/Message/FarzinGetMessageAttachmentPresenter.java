package avida.ican.Farzin.Presenter.Message;

import android.os.Handler;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Message.MessageAttachmentListListener;
import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.View.Interface.Message.MessageQueryAttachmentListListener;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2019-05-18 at 2:58 PM
 */

public class FarzinGetMessageAttachmentPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private FarzinMessageQuery farzinMessageQuery;
    private GetMessageAttachmentFromServerPresenter getMessageAttachmentFromServerPresenter;
    private MessageAttachmentListListener messageAttachmentListListener;
    private MessageQueryAttachmentListListener messageQueryAttachmentListListener;
    private int MainID;
    private int ID;

    public FarzinGetMessageAttachmentPresenter(MessageQueryAttachmentListListener messageQueryAttachmentListListener) {
        this.messageQueryAttachmentListListener = messageQueryAttachmentListListener;
        initCartableHameshListListener();
    }

    private void initCartableHameshListListener() {
        getMessageAttachmentFromServerPresenter = new GetMessageAttachmentFromServerPresenter();
        farzinMessageQuery = new FarzinMessageQuery();
        messageAttachmentListListener = new MessageAttachmentListListener() {


            @Override
            public void onSuccess(ArrayList<StructureMessageAttachRES> messageList) {
                SaveData(messageList);
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> onFailed(""), 300);
                } else {
                    reGetData();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> onCancel(), 300);
                } else {
                    reGetData();
                }
            }
        };
    }

    public void getData(int id, int mainID) {
        ID = id;
        MainID = mainID;
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            messageQueryAttachmentListListener.onFailed("");
        } else {
            getMessageAttachmentFromServerPresenter.GetMessageAttachment(MainID, messageAttachmentListListener);
        }
    }


    private void SaveData(final ArrayList<StructureMessageAttachRES> structureMessageAttachRES) {
        farzinMessageQuery.SaveMessageAttachment(ID, structureMessageAttachRES, new MessageQuerySaveListener() {
            @Override
            public void onSuccess(StructureMessageDB structureMessageDB) {
                messageQueryAttachmentListListener.newData(new ArrayList<>(structureMessageDB.getMessage_files()));
            }

            @Override
            public void onExisting() {

            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    messageQueryAttachmentListListener.onFailed(message);
                } else {
                    reGetData();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    messageQueryAttachmentListListener.onCancel();
                } else {
                    reGetData();
                }
            }
        });
    }

    private void reGetData() {
        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData(ID,MainID);
            }
        }, DELAY);

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
