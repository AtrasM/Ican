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
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureAdapterDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureResponseREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureWorkFlowREQ;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Farzin.View.Adapter.Queue.AdapterDocumentOperatorQueue;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterDocumentQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;

public class FragmentDocumentOperatorsQueue extends BaseFragment {

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
    private AdapterDocumentOperatorQueue adapterDocumentOperatorQueue;
    private FarzinDocumentOperatorsQueuePresenter farzinDocumentOperatorsQueuePresenter;
    private FarzinCartableQuery farzinCartableQuery;
    public final String Tag = "FragmentDocumentOperatorsQueue";

    public FragmentDocumentOperatorsQueue newInstance(Activity context) {
        this.context = context;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_queue_document_operator;
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
        farzinDocumentOperatorsQueuePresenter = new FarzinDocumentOperatorsQueuePresenter();
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
        List<StructureAdapterDocumentOperatorsQueueBND> documentOperatorsQueueBNDS = new ArrayList<>();
        List<StructureDocumentOperatorsQueueDB> documentOperatorsGroupQueueDB = new ArrayList<>();
        documentOperatorsGroupQueueDB = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorsQueueGroup();
        if (documentOperatorsGroupQueueDB.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < documentOperatorsGroupQueueDB.size(); i++) {
                int ETC = documentOperatorsGroupQueueDB.get(i).getETC();
                int EC = documentOperatorsGroupQueueDB.get(i).getEC();
                List<StructureDocumentOperatorsQueueDB> operatorsQueueDB = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueue(ETC, EC);
                StructureAdapterDocumentOperatorsQueueBND structureAdapterDocumentOperatorsQueueBND = new StructureAdapterDocumentOperatorsQueueBND(new ArrayList<>(operatorsQueueDB));
                documentOperatorsQueueBNDS.add(structureAdapterDocumentOperatorsQueueBND);
            }
        }
        lnLoading.setVisibility(View.GONE);
        initAdapter(new ArrayList<>(documentOperatorsQueueBNDS));
    }

    public void reGetData() {
        txtNoData.setVisibility(View.GONE);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureAdapterDocumentOperatorsQueueBND> documentOperatorsQueueBNDS = new ArrayList<>();
        List<StructureDocumentOperatorsQueueDB> documentOperatorsGroupQueueDB = new ArrayList<>();
        documentOperatorsGroupQueueDB = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorsQueueGroup();
        if (documentOperatorsGroupQueueDB.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
        } else {
            for (int i = 0; i < documentOperatorsGroupQueueDB.size(); i++) {
                int ETC = documentOperatorsGroupQueueDB.get(i).getETC();
                int EC = documentOperatorsGroupQueueDB.get(i).getEC();
                List<StructureDocumentOperatorsQueueDB> operatorsQueueDB = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueue(ETC, EC);
                StructureAdapterDocumentOperatorsQueueBND structureAdapterDocumentOperatorsQueueBND = new StructureAdapterDocumentOperatorsQueueBND(new ArrayList<>(operatorsQueueDB));
                documentOperatorsQueueBNDS.add(structureAdapterDocumentOperatorsQueueBND);
            }
        }
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
        adapterDocumentOperatorQueue.updateData(new ArrayList<>(documentOperatorsQueueBNDS));
    }

    private void initAdapter(ArrayList<StructureAdapterDocumentOperatorsQueueBND> structureAdapterDocumentOperatorsQueueBNDS) {
        //farzinCartableQuery.updateDocumentOperatorQueueStatus(1259,207,QueueStatus.WAITING,QueueStatus.ERROR);
        adapterDocumentOperatorQueue = new AdapterDocumentOperatorQueue(structureAdapterDocumentOperatorsQueueBNDS, new ListenerAdapterDocumentQueue() {
            @Override
            public void onDeletOperator(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
                showDialogDelet(ETC, EC, documentOpratoresTypeEnum);
            }

            @Override
            public void onDeletItem(int ETC, int EC) {
                showDialogDelet(ETC, EC, null);
            }

            @Override
            public void onTry(int ETC, int EC) {
                farzinCartableQuery.updateDocumentOperatorQueueStatus(ETC, EC, QueueStatus.ERROR, QueueStatus.WAITING);
                reGetData();
            }
        });

        rcvList.setAdapter(adapterDocumentOperatorQueue);
    }

    private void showDialogDelet(int etc, int ec, @Nullable DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        new DialogQuestion(context).setTitle(Resorse.getString(R.string.delet_question)).setOnListener(new ListenerQuestion() {
            @Override
            public void onSuccess() {
                if (farzinDocumentOperatorsQueuePresenter.isDocumentOperatorExist(etc, ec, DocumentOperatoresTypeEnum.Response)) {
                    List<StructureDocumentOperatorsQueueDB> documentOperatorsQueueDBS = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueue(etc, ec, DocumentOperatoresTypeEnum.Response);
                    for (int i = 0; i < documentOperatorsQueueDBS.size(); i++) {
                        StructureResponseREQ structureResponseREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDBS.get(i).getStrDataREQ(), StructureResponseREQ.class);
                        farzinCartableQuery.UpdateInboxDocumentConfirm(structureResponseREQ.getReceiverCode(), false);
                    }
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (farzinDocumentOperatorsQueuePresenter.isDocumentOperatorExist(etc, ec, DocumentOperatoresTypeEnum.WorkFlow)) {
                    List<StructureDocumentOperatorsQueueDB> documentOperatorsQueueDBS = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueue(etc, ec, DocumentOperatoresTypeEnum.WorkFlow);
                    for (int i = 0; i < documentOperatorsQueueDBS.size(); i++) {
                        StructureWorkFlowREQ structureWorkFlowREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDBS.get(i).getStrDataREQ(), StructureWorkFlowREQ.class);
                        farzinCartableQuery.UpdateInboxDocumentConfirm(structureWorkFlowREQ.getReceiverCode(), false);
                    }
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (documentOpratoresTypeEnum == null) {
                    farzinDocumentOperatorsQueuePresenter.deletDocumentOperatorsQueue(etc, ec);
                } else {
                    farzinDocumentOperatorsQueuePresenter.deletDocumentOperatorsQueue(etc, ec, documentOpratoresTypeEnum);
                }
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
