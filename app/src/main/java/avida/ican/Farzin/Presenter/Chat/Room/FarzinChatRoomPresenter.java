package avida.ican.Farzin.Presenter.Chat.Room;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Interface.Chat.Room.ChatRoomModelQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Chat.Room.GetChatRoomListListener;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Farzin.Presenter.Chat.FarzinChatQuery;
import avida.ican.Farzin.View.Interface.Chat.Room.ChatRoomDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2019-12-24 at 11:21 AM
 */

public class FarzinChatRoomPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 10;
    private GetChatRoomListListener getChatRoomListListener;
    private GetChatRoomListFromServerPresenter getChatRoomListFromServerPresenter;
    private FarzinChatQuery farzinChatQuery;
    private ChatRoomDataListener chatRoomDataListener;
    private static int counterFailed = 0;
    private int MaxTry = 2;

    public FarzinChatRoomPresenter(ChatRoomDataListener chatRoomDataListener) {
        if (chatRoomDataListener == null) {
            initDataListener();
        } else {
            this.chatRoomDataListener = chatRoomDataListener;
        }
        farzinChatQuery = new FarzinChatQuery();
        initDataListListener();
    }

    public FarzinChatRoomPresenter() {
        farzinChatQuery = new FarzinChatQuery();
    }

    private void initDataListener() {
        this.chatRoomDataListener = new ChatRoomDataListener() {
            @Override
            public void newData(StructureChatRoomDB structureChatRoomDB) {

            }

            @Override
            public void noData() {

            }
        };
    }

    private void initDataListListener() {
        getChatRoomListFromServerPresenter = new GetChatRoomListFromServerPresenter();
        getChatRoomListListener = new GetChatRoomListListener() {
            @Override
            public void onSuccess(ArrayList<StructureChatRoomModelRES> structureChatRoomModelListRES) {
                if (structureChatRoomModelListRES.size() <= 0) {
                    chatRoomDataListener.noData();
                    counterFailed = 0;
                } else {
                    farzinChatQuery.clearChatRoomList();
                    SaveData(structureChatRoomModelListRES);
                }
            }

            @Override
            public void onFailed(String message) {
                reGetData();
            }

            @Override
            public void onCancel() {
                reGetData();
            }
        };
    }

    public void getDataFromServer() {
        getChatRoomListFromServerPresenter.getRoomList(getChatRoomListListener);
    }

    public List<StructureChatRoomDB> getDataFromLocal(int Start, int Count, ChatRoomTypeEnum chatRoomTypeEnum) {
        return farzinChatQuery.getChatRoomList(Start, Count, chatRoomTypeEnum);
    }

    private void SaveData(final ArrayList<StructureChatRoomModelRES> chatRoomModelListRES) {

        final StructureChatRoomModelRES structureChatRoomModelRES = chatRoomModelListRES.get(0);
        farzinChatQuery.saveChatRoom(structureChatRoomModelRES, new ChatRoomModelQuerySaveListener() {

            @Override
            public void onSuccess(StructureChatRoomDB structureChatRoomDB) {

                try {
                    chatRoomModelListRES.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chatRoomModelListRES.size() > 0) {
                    SaveData(chatRoomModelListRES);
                } else {
                    chatRoomDataListener.newData(structureChatRoomDB);
                    counterFailed = 0;
                }
            }

            @Override
            public void onExisting() {
                chatRoomDataListener.noData();
                counterFailed = 0;
            }

            @Override
            public void onFailed(String message) {
                reGetData();
            }

            @Override
            public void onCancel() {
                reGetData();
            }
        });

    }


    private void reGetData() {
        counterFailed++;
        if (counterFailed >= MaxTry) {
            chatRoomDataListener.noData();
            counterFailed = 0;
        } else {
            App.getHandlerMainThread().postDelayed(() -> getDataFromServer(), DELAY);
        }
    }


}
