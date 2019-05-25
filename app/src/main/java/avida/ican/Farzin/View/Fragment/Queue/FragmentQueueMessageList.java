package avida.ican.Farzin.View.Fragment.Queue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.MessageQueue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinQueuesPresenter;
import avida.ican.Farzin.View.Adapter.AdapterHamesh;
import avida.ican.Farzin.View.Adapter.AdapterQueueMessage;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Farzin.View.Interface.Message.ListenerAdapterMessageList;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterMessageQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;

public class FragmentQueueMessageList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_list)
    RecyclerView rcvList;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private AdapterQueueMessage adapterQueueMessage;
    private int start = 0;
    private int COUNT = -1;
    private FarzinQueuesPresenter farzinQueuesPresenter;
    public static String Tag = "FragmentQueueMessageList";

    public FragmentQueueMessageList newInstance(Activity context) {
        this.context = context;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_queue_message_list;
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
        rcvList.setLayoutManager(gridLayoutManager);
        initPresenter();
    }


    private void initPresenter() {
        farzinQueuesPresenter = new FarzinQueuesPresenter();
        initData();

    }


    /*    @Override
        public void setUserVisibleHint(boolean isVisibleToUser) {
            super.setUserVisibleHint(isVisibleToUser);
            if (!isVisibleToUser) {
                initData();
                //do sth..
            }
        }*/
    private void initData() {
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureMessageQueueDB> structureMessageQueueDBS = farzinQueuesPresenter.getQueueMessageList();
        lnLoading.setVisibility(View.GONE);
        if (structureMessageQueueDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }
        initAdapter(new ArrayList<>(structureMessageQueueDBS));
    }


    public void reGetData() {
        txtNoData.setVisibility(View.GONE);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureMessageQueueDB> structureMessageQueueDBS = farzinQueuesPresenter.getQueueMessageList();
        if (structureMessageQueueDBS.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
        adapterQueueMessage.updateData(structureMessageQueueDBS);
    }

    private void initAdapter(ArrayList<StructureMessageQueueDB> structureMessageQueueDBS) {
        adapterQueueMessage = new AdapterQueueMessage(structureMessageQueueDBS, new ListenerAdapterMessageQueue() {

            @Override
            public void onDelet(StructureMessageQueueDB structureMessageQueueDB) {
                new DialogQuestion(context).setTitle(Resorse.getString(R.string.delet_question)).setOnListener(new ListenerQuestion() {
                    @Override
                    public void onSuccess() {
                        farzinQueuesPresenter.deletMessageQueueRowWithId(structureMessageQueueDB.getId());
                        reGetData();
                    }

                    @Override
                    public void onCancel() {

                    }
                }).Show();

            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND, int position) {

            }
        });

        rcvList.setAdapter(adapterQueueMessage);
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
