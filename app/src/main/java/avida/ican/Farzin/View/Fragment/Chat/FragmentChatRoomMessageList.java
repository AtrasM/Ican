package avida.ican.Farzin.View.Fragment.Chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.bassaer.chatmessageview.model.ChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.MessageView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Presenter.Chat.Room.FarzinChatRoomPresenter;
import avida.ican.Farzin.Presenter.Chat.RoomMessage.FarzinChatRoomMessagePresenter;
import avida.ican.Farzin.View.Adapter.Chat.AdapterChatRoom;
import avida.ican.Farzin.View.Adapter.Chat.AdapterChatRoomMessage;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Interface.Chat.Room.ListenerAdapterChatRoom;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

public class FragmentChatRoomMessageList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_chat_room)
    RecyclerView rcvChatRoomList;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.message_view)
    MessageView messageView;

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private AdapterChatRoomMessage adapterChatRoomMessage;
    private int start = 0;
    private int COUNT = 2;
    private FarzinChatRoomMessagePresenter farzinChatRoomMessagePresenter;
    public final String Tag = "FragmentChatRoomMessageList";
    private boolean showInDialog = false;
    private String chatRoomMessageID;

    public FragmentChatRoomMessageList newInstance(Activity context, String chatRoomMessageID) {
        this.context = context;
        this.chatRoomMessageID = chatRoomMessageID;
        return this;
    }

    public void setDialog(DialogCartableHameshList dialog) {
        showInDialog = true;
        //tempDialog = dialog;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_room_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srlRefresh.setOnRefreshListener(() -> reGetData(chatRoomMessageID));

        initRcv();
    }

    private void initRcv() {
        //gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rcvChatRoomList.setLayoutManager(mLayoutManager);
        initPresenter();
    }

    private void initPresenter() {
        farzinChatRoomMessagePresenter = new FarzinChatRoomMessagePresenter();
        initData();
    }

    public void updateData(List<StructureChatRoomMessageDB> structureChatRoomMessageDBS) {
        if (this.adapterChatRoomMessage != null) {
            adapterChatRoomMessage.updateData(structureChatRoomMessageDBS);
            adapterChatRoomMessage.notifyDataSetChanged();
        }
    }

    private void initData() {
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureChatRoomMessageDB> structureChatRoomMessageDBS = farzinChatRoomMessagePresenter.getDataFromLocal(start, COUNT, chatRoomMessageID);
        lnLoading.setVisibility(View.GONE);
        if (structureChatRoomMessageDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }

        start = structureChatRoomMessageDBS.size();
        initAdapter(new ArrayList<>(structureChatRoomMessageDBS));
    }

    public void reGetData(String chatRoomMessageID) {
        this.chatRoomMessageID = chatRoomMessageID;
        start = 0;
        txtNoData.setVisibility(View.GONE);
        List<StructureChatRoomMessageDB> structureChatRoomMessageDBS = farzinChatRoomMessagePresenter.getDataFromLocal(start, COUNT, chatRoomMessageID);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (structureChatRoomMessageDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);

        start = structureChatRoomMessageDBS.size();
        adapterChatRoomMessage.updateData(structureChatRoomMessageDBS);
    }

    private void initAdapter(List<StructureChatRoomMessageDB> structureChatRoomMessageDBS) {
        adapterChatRoomMessage = new AdapterChatRoomMessage(structureChatRoomMessageDBS);
        rcvChatRoomList.setAdapter(adapterChatRoomMessage);

        StructureChatRoomMessageDB item = structureChatRoomMessageDBS.get(0);
        Bitmap myIcon = BitmapFactory.decodeResource(App.CurentActivity.getResources(), R.drawable.face_1);
        ChatUser chatUser = new ChatUser(item.getId(), item.getUserFullName(), myIcon);

        //new message

        //Set to chat view
        Message message;

        if (item.isCurrentUserIsWriter()) {
            message = new Message.Builder()
                    .setUser(chatUser)
                    .setText(item.getMessageContent())
                    .setRight(true)
                    .build();
        } else {
            message = new Message.Builder()
                    .setUser(chatUser)
                    .setText(item.getMessageContent())
                    .setRight(true)
                    .build();
        }
        messageView.setMessage(message);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
