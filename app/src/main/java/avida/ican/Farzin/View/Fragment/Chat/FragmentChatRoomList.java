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
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;
import avida.ican.Farzin.Presenter.Chat.Room.FarzinChatRoomPresenter;
import avida.ican.Farzin.View.Adapter.Chat.AdapterChatRoom;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Enum.ChatPutExtraEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocumentDetail;
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
    private ChatRoomTypeEnum chatRoomTypeEnum;

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
        srlRefresh.setOnRefreshListener(() -> reGetData());

        initRcv();
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvChatRoom.setLayoutManager(gridLayoutManager);
        initPresenter();
    }

    private void initPresenter() {
        farzinChatRoomPresenter = new FarzinChatRoomPresenter();
        initData();
    }

    public void updateData(List<StructureChatRoomListDB> structureChatRoomListDB) {
        if (this.adapterChatRoom != null) {
            adapterChatRoom.updateData(structureChatRoomListDB);
            adapterChatRoom.notifyDataSetChanged();
        }
    }

    private void initData() {
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureChatRoomListDB> structureChatRoomListDBS = farzinChatRoomPresenter.getDataFromLocal(start, COUNT, chatRoomTypeEnum);
        lnLoading.setVisibility(View.GONE);
        if (structureChatRoomListDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }

        start = structureChatRoomListDBS.size();
        initAdapter(new ArrayList<>(structureChatRoomListDBS));
    }

    public void reGetData() {
        start = 0;
        txtNoData.setVisibility(View.GONE);
        List<StructureChatRoomListDB> structureChatRoomListDBS = farzinChatRoomPresenter.getDataFromLocal(start, COUNT, chatRoomTypeEnum);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (structureChatRoomListDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);

        start = structureChatRoomListDBS.size();
        adapterChatRoom.updateData(structureChatRoomListDBS);
    }

    private void initAdapter(List<StructureChatRoomListDB> structureChatRoomListDBS) {
        adapterChatRoom = new AdapterChatRoom(structureChatRoomListDBS, new ListenerAdapterChatRoom() {
            @Override
            public void onDelet(StructureChatRoomListDB structureChatRoomListDB) {

            }

            @Override
            public void onItemClick(StructureChatRoomListDB structureChatRoomDB, int position) {
                gotoActivityChatRoomMessage(structureChatRoomDB);
            }

        });
        rcvChatRoom.setAdapter(adapterChatRoom);
    }

    private void gotoActivityChatRoomMessage(StructureChatRoomListDB structureChatRoomDB) {
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
