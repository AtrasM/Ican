package avida.ican.Farzin.View.Fragment.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;
import avida.ican.Farzin.Chat.Room.FarzinChatRoomPresenter;
import avida.ican.Farzin.View.Adapter.Chat.AdapterChatRoom;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityChatRoomMessage;
import avida.ican.Farzin.View.Interface.Chat.Room.ListenerAdapterChatRoom;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

public class FragmentChatRoomList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_chat_room)
    RecyclerView rcvChatRoom;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private AdapterChatRoom adapterChatRoom;
    private int start = 0;
    private int COUNT = 20;
    private FarzinChatRoomPresenter farzinChatRoomPresenter;
    public final String Tag = "FragmentChatRoomList";
    private boolean showInDialog = false;
    private static ChatRoomTypeEnum chatRoomTypeEnum;

    public FragmentChatRoomList newInstance(Activity context, ChatRoomTypeEnum chatRoomTypeEnum) {
        this.context = context;
        this.chatRoomTypeEnum = chatRoomTypeEnum;
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
        srlRefresh.setOnRefreshListener(() -> reGetDataFromLocal());

        initRcv();
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvChatRoom.setLayoutManager(gridLayoutManager);
        initPresenter();
    }

    private void initPresenter() {
        farzinChatRoomPresenter = new FarzinChatRoomPresenter();
        initDataFromLocal();
    }

    public void updateData(List<StructureChatRoomDB> structureChatRoomDB) {
        if (this.adapterChatRoom != null) {
            adapterChatRoom.updateData(structureChatRoomDB);
            adapterChatRoom.notifyDataSetChanged();
        }
    }

    private void initDataFromLocal() {
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureChatRoomDB> structureChatRoomDBS = farzinChatRoomPresenter.getDataFromLocal(start, COUNT, chatRoomTypeEnum);
        lnLoading.setVisibility(View.GONE);
        if (structureChatRoomDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }

        start = structureChatRoomDBS.size();
        initAdapter(new ArrayList<>(structureChatRoomDBS));
    }

    public void reGetDataFromLocal() {
        start = 0;
        txtNoData.setVisibility(View.GONE);
        List<StructureChatRoomDB> structureChatRoomDBS = farzinChatRoomPresenter.getDataFromLocal(start, COUNT, chatRoomTypeEnum);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (structureChatRoomDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);

        start = structureChatRoomDBS.size();
        adapterChatRoom.updateData(structureChatRoomDBS);
    }

    private void initAdapter(List<StructureChatRoomDB> structureChatRoomDBS) {
        adapterChatRoom = new AdapterChatRoom(structureChatRoomDBS, new ListenerAdapterChatRoom() {
            @Override
            public void onDelet(StructureChatRoomDB structureChatRoomDB) {

            }

            @Override
            public void onItemClick(StructureChatRoomDB structureChatRoomDB, int position) {
                gotoActivityChatRoomMessage(structureChatRoomDB);
            }

        });
        rcvChatRoom.setAdapter(adapterChatRoom);
    }

    private void gotoActivityChatRoomMessage(StructureChatRoomDB structureChatRoomDB) {
        App.getHandlerMainThread().post(() -> {
            App.canBack = true;
            // StructureCartableDocumentDetailBND cartableDocumentDetailBND = new StructureCartableDocumentDetailBND(zanjireMadrakEntityDB.getETC(), zanjireMadrakEntityDB.getEC(), zanjireMadrakEntityDB.getTitle(), zanjireMadrakEntityDB.getEntityNumber(), zanjireMadrakEntityDB.getImportEntityNumber());
            //bundleObject.putSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue(), cartableDocumentDetailBND);
            Intent intent = new Intent(App.CurentActivity, FarzinActivityChatRoomMessage.class);
            intent.putExtra(ChatPutExtraEnum.RoomMessageIDString.getValue(), structureChatRoomDB.getChatRoomIDString());
            BaseActivity.goToActivity(intent);

        });
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
