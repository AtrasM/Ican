package avida.ican.Farzin.View.Fragment.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Presenter.Chat.RoomMessage.FarzinChatRoomMessagePresenter;
import avida.ican.Farzin.View.Adapter.Chat.AdapterChatRoomMessage;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ChatRoomMessageDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

public class FragmentChatRoomMessageList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_chat_room_message)
    RecyclerView rcvChatRoomMessage;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.img_move_down)
    ImageView imgMoveDown;

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private AdapterChatRoomMessage adapterChatRoomMessage;
    private int start = 0;
    private int COUNT = 20;
    private FarzinChatRoomMessagePresenter farzinChatRoomMessagePresenter;
    public final String Tag = "FragmentChatRoomMessageList";
    private boolean showInDialog = false;
    private String chatRoomMessageID;
    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;

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
        return R.layout.fragment_chat_room_message_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srlRefresh.setOnRefreshListener(() -> reGetData(chatRoomMessageID));
        animator = new Animator(App.CurentActivity);
        imgMoveDown.setOnClickListener(v -> {
            //gridLayoutManager.scrollToPositionWithOffset(2, 20);
            rcvChatRoomMessage.smoothScrollToPosition(0);
            imgMoveDown.setVisibility(View.GONE);
            isshow = false;
        });
        initRcv();
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        //mLayoutManager.setReverseLayout(true);
        gridLayoutManager.setReverseLayout(true);
        rcvChatRoomMessage.setLayoutManager(gridLayoutManager);
        initScrollListener();
        initPresenter();
    }

    private void initScrollListener() {
        rcvChatRoomMessage.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = gridLayoutManager.getChildCount();
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPositions(null);
                if (dy > 0) {
                    CustomLogger.setLog("visibleItemCount= " + visibleItemCount + " | pastVisiblesItems= " + pastVisiblesItems[0]);

                  /*  if ((visibleItemCount + pastVisiblesItems[0]) <= 5 && !isshow) {
                        animator.slideInFromDown(imgMoveDown);
                        imgMoveDown.setVisibility(View.VISIBLE);
                        isshow = true;
                    } else*/
                    if ((pastVisiblesItems[0]) < 1 && isshow) {
                        imgMoveDown.setVisibility(View.GONE);
                        isshow = false;
                    }
                } else if (dy < 0) //check for scroll up
                {
                    if ((visibleItemCount + pastVisiblesItems[0]) >= 10 && !isshow) {
                        animator.slideInFromDown(imgMoveDown);
                        imgMoveDown.setVisibility(View.VISIBLE);
                        isshow = true;
                    }
                    if (canLoading) {
                        totalItemCount = gridLayoutManager.getItemCount();
                        if ((visibleItemCount + pastVisiblesItems[0]) >= totalItemCount - 2) {
                            canLoading = false;
                            loadDate();
                            App.ShowMessage().ShowToast("position= " + pastVisiblesItems[0], ToastEnum.TOAST_LONG_TIME);
                        }
                    }
                }
            }
        });
    }


    private void initPresenter() {
        farzinChatRoomMessagePresenter = new FarzinChatRoomMessagePresenter(new ChatRoomMessageDataListener() {
            @Override
            public void newData(StructureChatRoomMessageDB structureChatRoomMessageDB) {
                loadDate();
            }

            @Override
            public void noData() {
                srlRefresh.setRefreshing(true);
                lnLoading.setVisibility(View.GONE);
                canLoading = false;
            }
        });
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

    private void loadDate() {
        App.getHandlerMainThread().post(() -> {
            txtNoData.setVisibility(View.GONE);
            List<StructureChatRoomMessageDB> structureChatRoomMessagesDB = farzinChatRoomMessagePresenter.getDataFromLocal(start, COUNT, chatRoomMessageID);

            srlRefresh.setRefreshing(true);
            if (structureChatRoomMessagesDB.size() > 0) {
                srlRefresh.setRefreshing(false);
                lnLoading.setVisibility(View.GONE);
                start = start + structureChatRoomMessagesDB.size();
                adapterChatRoomMessage.addDataToEnd(structureChatRoomMessagesDB);
                canLoading = true;
            } else {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    srlRefresh.setRefreshing(false);
                    lnLoading.setVisibility(View.GONE);
                    canLoading = true;
                } else {
                    srlRefresh.setRefreshing(true);
                    StructureChatRoomMessageDB structureChatRoomMessageDB = adapterChatRoomMessage.getLastMessage();
                    CustomLogger.setLog("Message Id= " + structureChatRoomMessageDB.getMessageID());
                    if (!structureChatRoomMessageDB.getMessageIDString().isEmpty()) {
                        farzinChatRoomMessagePresenter.getDataFromServer(chatRoomMessageID, structureChatRoomMessageDB.getTableExtension(), structureChatRoomMessageDB.getMessageIDString());
                    } else {
                        farzinChatRoomMessagePresenter.getDataFromServer(chatRoomMessageID);
                    }
                }
            }
        });
    }

    private void initAdapter(List<StructureChatRoomMessageDB> structureChatRoomMessageDBS) {
        adapterChatRoomMessage = new AdapterChatRoomMessage(structureChatRoomMessageDBS);
        rcvChatRoomMessage.setAdapter(adapterChatRoomMessage);
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
