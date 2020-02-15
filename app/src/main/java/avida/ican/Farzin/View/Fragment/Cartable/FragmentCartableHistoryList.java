package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureNodeRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinHistoryListPresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterCartableHistory;
import avida.ican.Farzin.View.Dialog.DialogCartableHistoryList;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.QueueEnum;
import avida.ican.Farzin.View.FarzinActivityQueue;
import avida.ican.Farzin.View.Interface.Cartable.ListenerGraf;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindView;

public class FragmentCartableHistoryList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_cartable_history_list)
    RecyclerView rcvMain;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.ln_history_number)
    LinearLayout lnHistoryNumber;
    @BindView(R.id.sp_history_number)
    Spinner spHistoryNumber;
    @BindView(R.id.ln_operator_queue_count)
    LinearLayout lnOperatorQueueCount;
    @BindView(R.id.txt_operator_queue_count)
    TextView txtOperatorQueueCount;
    @BindView(R.id.txt_title_operator)
    TextView txtTitleOperator;
    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private int Etc;
    private int Ec;
    private AdapterCartableHistory adapterCartableHistory;
    private StructureHistoryListRES.StructureGraphs structureGraphs;
    private FarzinHistoryListPresenter farzinHistoryListPresenter;
    public final String Tag = "FragmentZanjireMadrak";
    private boolean initialized = false;
    private boolean showInDialog = false;
    private DialogCartableHistoryList tempDialog;


    public FragmentCartableHistoryList newInstance(Activity context, int Etc, int Ec) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
    }

    public void setDialog(DialogCartableHistoryList dialog) {
        showInDialog = true;
        tempDialog = dialog;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cartable_history_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srlRefresh.setOnRefreshListener(() -> reGetData());
        lnOperatorQueueCount.setOnClickListener(view1 -> {
            checkQueue();
            if (showInDialog) {
                tempDialog.dismiss();
            }
            Intent intent = new Intent(App.CurentActivity, FarzinActivityQueue.class);
            intent.putExtra(PutExtraEnum.QueueType.getValue(), QueueEnum.DocumentOperator.getValue());
            BaseActivity.goToActivity(intent);

        });
        if (!initialized) {
            initRcv();
        }

    }


    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvMain.setLayoutManager(gridLayoutManager);
        initCartableHistoryPresenter();
    }


    private void initCartableHistoryPresenter() {
        farzinHistoryListPresenter = new FarzinHistoryListPresenter(Etc, Ec, new ListenerGraf() {
            @Override
            public void newData(final ArrayList<StructureHistoryListRES> structureHistoryListRES) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        txtNoData.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        List<StructureNodeRES> structureNodesRES = structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
                        initSpinner(structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs());
                        adapterCartableHistory.updateData(new ArrayList<>(structureNodesRES));

                    }
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        if (adapterCartableHistory.getDataSize() <= 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        initData();

    }


    private void initData() {
        checkQueue();
        initialized = true;
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            structureNodesRES = structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinHistoryListPresenter.GetCartableHistoryFromServer();
        } else {
            lnLoading.setVisibility(View.GONE);
            if (cartableHistoryDBS.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        }

        initAdapter(new ArrayList<>(structureNodesRES));
    }


    private void reGetData() {
        checkQueue();
        txtNoData.setVisibility(View.GONE);
        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            structureNodesRES = structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
            lnLoading.setVisibility(View.GONE);

        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinHistoryListPresenter.GetCartableHistoryFromServer();
        } else {
            if (cartableHistoryDBS.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
            lnLoading.setVisibility(View.GONE);
            srlRefresh.setRefreshing(false);
        }
        adapterCartableHistory.updateData(new ArrayList<>(structureNodesRES));

    }

    public void checkQueue() {
        long count = new FarzinDocumentOperatorsQueuePresenter().getDocumentOperatorQueueNotSendedCount(Etc, Ec, DocumentOperatoresTypeEnum.Append);
        if (count > 0) {
            lnOperatorQueueCount.setVisibility(View.VISIBLE);
            txtTitleOperator.setText(Resorse.getString(R.string.title_Append_operator));
            txtOperatorQueueCount.setText("" + count);
        } else {
            lnOperatorQueueCount.setVisibility(View.GONE);
        }
    }

    private void initAdapter(ArrayList<StructureNodeRES> structureNodeRES) {
        adapterCartableHistory = new AdapterCartableHistory(structureNodeRES);
        rcvMain.setAdapter(adapterCartableHistory);
    }

    private void initSpinner(StructureHistoryListRES.StructureGraphs graphs) {
        this.structureGraphs = graphs;
        ArrayList<String> item = new ArrayList<>();
       /* for (int i = 1; i <= structureGraphs.getGraph().size(); i++) {
            item.add("شماره " + i);
        }*/
        for (int i = structureGraphs.getGraph().size(); i > 0; i--) {
            item.add("شماره " + i);
        }
        if (item.size() <= 1) {
            lnHistoryNumber.setVisibility(View.GONE);
        } else {
            lnHistoryNumber.setVisibility(View.VISIBLE);
        }
        ArrayAdapter<String> adapterSpinner = new CustomFunction(App.CurentActivity).getSpinnerAdapter(item);
        spHistoryNumber.setAdapter(adapterSpinner);
        spHistoryNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<StructureNodeRES> structureNodesRES = structureGraphs.getGraph().get(i).getStartNode().getNode();
                adapterCartableHistory.updateData(new ArrayList<>(structureNodesRES));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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
