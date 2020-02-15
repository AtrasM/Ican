package avida.ican.Farzin.View.Fragment.Queue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureAdapterDocumentAttachFileQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Farzin.View.Adapter.Queue.AdapterDocumentAttachFileQueue;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterDocumentQueue;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;

public class FragmentDocumentAttachFileQueue extends BaseFragment {

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
    private AdapterDocumentAttachFileQueue adapterDocumentAttachFileQueue;
    private ListenerFile listenerFile;
    private FarzinDocumentAttachFilePresenter farzinDocumentAttachFilePresenter;
    private FarzinCartableQuery farzinCartableQuery;
    public final String Tag = "FragmentDocumentAttachFileQueue";

    public FragmentDocumentAttachFileQueue newInstance(Activity context, ListenerFile listenerFile) {
        this.context = context;
        this.listenerFile = listenerFile;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_queue_document_attach_file;
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
        farzinDocumentAttachFilePresenter = new FarzinDocumentAttachFilePresenter();
        farzinCartableQuery = new FarzinCartableQuery();
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
        List<StructureAdapterDocumentAttachFileQueueBND> documentAttachFileQueueBNDS = new ArrayList<>();
        List<StructureDocumentAttachFileQueueDB> documentAttachFileGroupQueueDB = new ArrayList<>();
        documentAttachFileGroupQueueDB = farzinDocumentAttachFilePresenter.getDocumentAttachFileQueueGroup();
        if (documentAttachFileGroupQueueDB.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < documentAttachFileGroupQueueDB.size(); i++) {
                int ETC = documentAttachFileGroupQueueDB.get(i).getETC();
                int EC = documentAttachFileGroupQueueDB.get(i).getEC();
                List<StructureDocumentAttachFileQueueDB> attachFileQueuesDB = farzinDocumentAttachFilePresenter.getDocumentAttachFileNotSendedQueue(ETC, EC);
                StructureAdapterDocumentAttachFileQueueBND structureAdapterDocumentAttachFileQueueBND = new StructureAdapterDocumentAttachFileQueueBND(new ArrayList<>(attachFileQueuesDB));
                documentAttachFileQueueBNDS.add(structureAdapterDocumentAttachFileQueueBND);
            }
        }
        lnLoading.setVisibility(View.GONE);
        initAdapter(new ArrayList<>(documentAttachFileQueueBNDS));
    }

    public void reGetData() {
        txtNoData.setVisibility(View.GONE);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureAdapterDocumentAttachFileQueueBND> documentAttachFileQueueBNDS = new ArrayList<>();
        List<StructureDocumentAttachFileQueueDB> documentAttachFileGroupQueueDB = new ArrayList<>();
        documentAttachFileGroupQueueDB = farzinDocumentAttachFilePresenter.getDocumentAttachFileQueueGroup();
        if (documentAttachFileGroupQueueDB.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < documentAttachFileGroupQueueDB.size(); i++) {
                int ETC = documentAttachFileGroupQueueDB.get(i).getETC();
                int EC = documentAttachFileGroupQueueDB.get(i).getEC();
                List<StructureDocumentAttachFileQueueDB> attachFileQueuesDB = farzinDocumentAttachFilePresenter.getDocumentAttachFileNotSendedQueue(ETC, EC);
                StructureAdapterDocumentAttachFileQueueBND structureAdapterDocumentAttachFileQueueBND = new StructureAdapterDocumentAttachFileQueueBND(new ArrayList<>(attachFileQueuesDB));
                documentAttachFileQueueBNDS.add(structureAdapterDocumentAttachFileQueueBND);
            }
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
        adapterDocumentAttachFileQueue.updateData(new ArrayList<>(documentAttachFileQueueBNDS));
    }

    private void initAdapter(ArrayList<StructureAdapterDocumentAttachFileQueueBND> structureAdapterDocumentAttachFileQueueBNDS) {
        adapterDocumentAttachFileQueue = new AdapterDocumentAttachFileQueue(structureAdapterDocumentAttachFileQueueBNDS, new ListenerAdapterDocumentQueue() {
            @Override
            public void onDeletOperator(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
            }

            @Override
            public void onDeletItem(int ETC, int EC) {
                showDialogDelet(ETC, EC);
            }

            @Override
            public void onTry(int ETC, int EC) {
                farzinCartableQuery.updateDocumentAttachFileQueueStatus(ETC, EC, QueueStatus.ERROR, QueueStatus.WAITING);
                reGetData();
            }
        });


        rcvList.setAdapter(adapterDocumentAttachFileQueue);
    }

    private void showDialogDelet(int etc, int ec) {
        new DialogQuestion(context).setTitle(Resorse.getString(R.string.delet_question)).setOnListener(new ListenerQuestion() {
            @Override
            public void onSuccess() {

                farzinDocumentAttachFilePresenter.deletDocumentAttachFileQueue(etc, ec);
                reGetData();
            }

            @Override
            public void onCancel() {

            }
        }).Show();
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
