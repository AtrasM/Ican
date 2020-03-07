package avida.ican.Farzin.Presenter.Chat.RoomMessage;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Interface.Chat.RoomMessage.ChatRoomMessageModelQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Chat.RoomMessage.GetChatRoomMessageListListener;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Farzin.Presenter.Chat.FarzinChatQuery;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ChatRoomMessageDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2020-01-12 at 5:26 PM
 */

public class FarzinChatRoomMessagePresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 10;
    private GetChatRoomMessageListListener getChatRoomMessageListListener;
    private GetChatRoomMessagesListFromServerPresenter getChatRoomMessagesListFromServerPresenter;
    private FarzinChatQuery farzinChatQuery;
    private ChatRoomMessageDataListener chatRoomMessageDataListener;
    private static int counterFailed = 0;
    private int MaxTry = 2;
    private StructureDataFromServerBundle structureDataFromServerBundle = new StructureDataFromServerBundle();
    private boolean isFirst = true;
    private boolean isReplyData = false;

    public FarzinChatRoomMessagePresenter(ChatRoomMessageDataListener chatRoomMessageDataListener) {
        if (chatRoomMessageDataListener == null) {
            initDataListener();
        } else {
            this.chatRoomMessageDataListener = chatRoomMessageDataListener;
        }
        farzinChatQuery = new FarzinChatQuery();
        initDataListListener();
    }

    public FarzinChatRoomMessagePresenter() {
        farzinChatQuery = new FarzinChatQuery();
    }

    private void initDataListener() {
        this.chatRoomMessageDataListener = new ChatRoomMessageDataListener() {

            @Override
            public void downloadedReplyData(StructureChatRoomMessageDB structureChatRoomMessageDB) {

            }

            @Override
            public void newData(StructureChatRoomMessageDB structureChatRoomMessageDB) {

            }

            @Override
            public void noData() {

            }
        };
    }

    private void initDataListListener() {
        getChatRoomMessagesListFromServerPresenter = new GetChatRoomMessagesListFromServerPresenter();
        getChatRoomMessageListListener = new GetChatRoomMessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureChatRoomMessageModelRES> structureChatRoomMessagesModelRES) {
                if (structureChatRoomMessagesModelRES.size() <= 0) {
                    chatRoomMessageDataListener.noData();
                    counterFailed = 0;
                } else {
                    if (isFirst) {
                        farzinChatQuery.clearChatRoomMessageList();
                    }
                    SaveData(structureChatRoomMessagesModelRES);
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

    public void getDataFromServer(String chatRoomId) {
        isFirst = true;
        isReplyData=false;
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            chatRoomMessageDataListener.noData();
        } else {
            structureDataFromServerBundle = new StructureDataFromServerBundle(chatRoomId);
            getChatRoomMessagesListFromServerPresenter.getMessageList(chatRoomId, getChatRoomMessageListListener);
        }
    }

    public void getDataFromServer(String chatRoomId, String tableExtension, String lastMessageID) {
        isFirst = false;
        isReplyData=false;
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            chatRoomMessageDataListener.noData();
        } else {
            structureDataFromServerBundle = new StructureDataFromServerBundle(chatRoomId, tableExtension, lastMessageID);
            getChatRoomMessagesListFromServerPresenter.getMessageList(chatRoomId, tableExtension, lastMessageID, getChatRoomMessageListListener);
        }
    }

    public void getDataFromServer(String chatRoomId, String tableExtension, String lastMessageID, String toMessageID, String toTableExtension) {
        isFirst = false;
        isReplyData=true;
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            chatRoomMessageDataListener.noData();
        } else {
            structureDataFromServerBundle = new StructureDataFromServerBundle(chatRoomId, tableExtension, lastMessageID, toMessageID, toTableExtension);
            getChatRoomMessagesListFromServerPresenter.getMessageList(chatRoomId, tableExtension, lastMessageID, toMessageID, toTableExtension, getChatRoomMessageListListener);
        }
    }

    public StructureChatRoomMessageDB getLastDataFromLocal(String chatRoomID) {
        return farzinChatQuery.getLastMessage(chatRoomID);
    }

    public List<StructureChatRoomMessageDB> getDataFromLocal(int Start, int Count, String chatRoomID) {
        return farzinChatQuery.getChatRoomMessageList(Start, Count, chatRoomID);
    }

    public List<StructureChatRoomMessageDB> getDataFromLocalBetween(int fromID, int toID, String chatRoomID) {
        return farzinChatQuery.getChatRoomMessageListBetween(fromID, toID, chatRoomID);
    }

    public int findReplyMessageID(int idCurrentMessage, int replyToMessageID, String chatRoomID) {
        return farzinChatQuery.findReplyMessageID(idCurrentMessage, replyToMessageID, chatRoomID);
    }

    private void SaveData(final ArrayList<StructureChatRoomMessageModelRES> chatRoomMessagesModelRES) {
        final StructureChatRoomMessageModelRES structureChatRoomMessageModelRES = chatRoomMessagesModelRES.get(0);
        farzinChatQuery.saveChatRoomMessage(structureChatRoomMessageModelRES, new ChatRoomMessageModelQuerySaveListener() {
            @Override
            public void onSuccess(StructureChatRoomMessageDB structureChatRoomMessageDB) {
                try {
                    chatRoomMessagesModelRES.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chatRoomMessagesModelRES.size() > 0) {
                    SaveData(chatRoomMessagesModelRES);
                } else {
                    if(isReplyData){
                        chatRoomMessageDataListener.downloadedReplyData(structureChatRoomMessageDB);
                    }else{
                        chatRoomMessageDataListener.newData(structureChatRoomMessageDB);
                    }

                    counterFailed = 0;
                }
            }

            @Override
            public void onExisting() {
                chatRoomMessageDataListener.noData();
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

    private static class StructureDataFromServerBundle {
        private int id;
        private String chatRoomId;
        private String tableExtension;
        private String lastMessageID;
        private String toMessageID;
        private String toTableExtension;

        public StructureDataFromServerBundle() {
            this.id = -1;
        }

        public StructureDataFromServerBundle(String chatRoomId) {
            this.id = 1;
            this.chatRoomId = chatRoomId;
        }

        public StructureDataFromServerBundle(String chatRoomId, String tableExtension, String lastMessageID) {
            this.id = 2;
            this.chatRoomId = chatRoomId;
            this.tableExtension = tableExtension;
            this.lastMessageID = lastMessageID;
        }

        public StructureDataFromServerBundle(String chatRoomId, String tableExtension, String lastMessageID, String toMessageID, String toTableExtension) {
            this.id = 3;
            this.chatRoomId = chatRoomId;
            this.tableExtension = tableExtension;
            this.lastMessageID = lastMessageID;
            this.toMessageID = toMessageID;
            this.toTableExtension = toTableExtension;
        }

        public int getId() {
            return id;
        }

        public String getChatRoomId() {
            return chatRoomId;
        }

        public String getTableExtension() {
            return tableExtension;
        }

        public String getLastMessageID() {
            return lastMessageID;
        }

        public String getToMessageID() {
            return toMessageID;
        }

        public String getToTableExtension() {
            return toTableExtension;
        }
    }

    private void reGetData() {
        counterFailed++;
        if (counterFailed >= MaxTry) {
            chatRoomMessageDataListener.noData();
            counterFailed = 0;
        } else {
            App.getHandlerMainThread().postDelayed(() -> {
                switch (structureDataFromServerBundle.getId()) {
                    case 1: {
                        getDataFromServer(structureDataFromServerBundle.getChatRoomId());
                        break;
                    }
                    case 2: {
                        getDataFromServer(structureDataFromServerBundle.getChatRoomId(), structureDataFromServerBundle.getTableExtension(), structureDataFromServerBundle.getLastMessageID());
                        break;
                    }
                    case 3: {
                        getDataFromServer(structureDataFromServerBundle.getChatRoomId(), structureDataFromServerBundle.getTableExtension(), structureDataFromServerBundle.getLastMessageID(), structureDataFromServerBundle.getToMessageID(), structureDataFromServerBundle.getToTableExtension());
                        break;
                    }
                }
            }, DELAY);
        }
    }


}
