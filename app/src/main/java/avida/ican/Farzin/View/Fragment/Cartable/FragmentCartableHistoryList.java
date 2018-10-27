package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureNodeRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinHistoryListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinZanjireMadrakPresenter;
import avida.ican.Farzin.View.Adapter.AdapteZanjireMadrak;
import avida.ican.Farzin.View.Adapter.AdapterCartableHistory;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterZanjireMadrak;
import avida.ican.Farzin.View.Interface.Cartable.ListenerGraf;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
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

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private int Etc;
    private int Ec;
    private AdapterCartableHistory adapterCartableHistory;
    private StructureHistoryListRES.StructureGraphs structureGraphs;
    private FarzinHistoryListPresenter farzinHistoryListPresenter;
    public static String Tag = "FragmentZanjireMadrak";

    public FragmentCartableHistoryList newInstance(Activity context, int Etc, int Ec) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
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
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reGetData();
            }
        });
        initRcv();

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
                        List<StructureNodeRES> structureNodesRES = structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
                        initSpinner(structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs());
                        lnHistoryNumber.setVisibility(View.VISIBLE);
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
                        if (adapterCartableHistory.getDataSize() <= 0) {
                            lnLoading.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        initData();

    }

    private void initSpinner(StructureHistoryListRES.StructureGraphs graphs) {
        this.structureGraphs = graphs;
        ArrayList<String> item = new ArrayList<>();
        for (int i = 1; i <= structureGraphs.getGraph().size(); i++) {
            item.add("شماره " + i);
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

    private void reGetData() {
        lnHistoryNumber.setVisibility(View.GONE);
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                lnHistoryNumber.setVisibility(View.VISIBLE);
            }
            structureNodesRES = structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
            lnLoading.setVisibility(View.GONE);
            srlRefresh.setRefreshing(false);
        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinHistoryListPresenter.GetCartableHistoryFromServer();
        } else {
            lnLoading.setVisibility(View.GONE);
            if (cartableHistoryDBS.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        }

        adapterCartableHistory.updateData(new ArrayList<>(structureNodesRES));

    }

    private void initData() {
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                lnHistoryNumber.setVisibility(View.VISIBLE);
            }
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

    private void initAdapter(ArrayList<StructureNodeRES> structureNodeRES) {
        adapterCartableHistory = new AdapterCartableHistory(structureNodeRES);
        rcvMain.setAdapter(adapterCartableHistory);
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
