package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureNodeRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinHistoryListPresenter;
import avida.ican.Farzin.View.Adapter.AdapterCartableHistoryhList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerGraf;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-10-07 at 11:10 AM
 */

public class DialogCartableHistoryList {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private int Etc;
    private int Ec;
    private AdapterCartableHistoryhList adapterCartableHistoryhList;
    private StructureHistoryListRES.StructureGraphs structureGraphs;

    public DialogCartableHistoryList(Activity context) {
        this.context = context;
    }

    private FarzinHistoryListPresenter farzinHistoryListPresenter;


    public class Binding {
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


        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogCartableHistoryList setData(int Etc, int Ec) {
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
    }

    public void Creat() {

        BaseActivity.closeKeboard();
        //App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.layout_cartable_history_list))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(true)
                        //.setContentBackgroundResource(android.R.color.transparent)
                        .create();
                dialog.show();
                viewHolder = new DialogCartableHistoryList.Binding(dialog.getHolderView());

                initView();

            }

        });
    }

    private void initView() {
        viewHolder.srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reGetData();
            }
        });
        initRcv();
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.rcvMain.setLayoutManager(gridLayoutManager);
        initCartableHistoryPresenter();
    }

    private void initCartableHistoryPresenter() {
        farzinHistoryListPresenter = new FarzinHistoryListPresenter(Etc, Ec, new ListenerGraf() {
            @Override
            public void newData(final ArrayList<StructureHistoryListRES> structureHistoryListRES) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.lnLoading.setVisibility(View.GONE);
                        viewHolder.txtNoData.setVisibility(View.GONE);
                        List<StructureNodeRES> structureNodesRES = structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
                        initSpinner(structureHistoryListRES.get(0).getGetHistoryListResult().getGraphs());
                        viewHolder.lnHistoryNumber.setVisibility(View.VISIBLE);
                        adapterCartableHistoryhList.updateData(new ArrayList<>(structureNodesRES));

                    }
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewHolder.lnLoading.getVisibility() == View.VISIBLE) {
                            viewHolder.lnLoading.setVisibility(View.GONE);
                            viewHolder.txtNoData.setVisibility(View.VISIBLE);
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
        viewHolder.spHistoryNumber.setAdapter(adapterSpinner);
        viewHolder.spHistoryNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                List<StructureNodeRES> structureNodesRES = structureGraphs.getGraph().get(i).getStartNode().getNode();
                adapterCartableHistoryhList.updateData(new ArrayList<>(structureNodesRES));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public boolean isShowing() {
        return dialog.isShowing();
    }


    private void reGetData() {
        viewHolder.lnHistoryNumber.setVisibility(View.GONE);
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            if (App.networkStatus != NetworkStatus.Connected) {
                viewHolder.lnHistoryNumber.setVisibility(View.VISIBLE);
            }
            structureNodesRES = structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
            viewHolder.lnLoading.setVisibility(View.GONE);
            viewHolder.srlRefresh.setRefreshing(false);
        }
        if (App.networkStatus != NetworkStatus.Connected) {
            viewHolder.lnLoading.setVisibility(View.GONE);
            viewHolder.txtNoData.setVisibility(View.VISIBLE);
        } else {
            farzinHistoryListPresenter.GetCartableHistoryFromServer();
        }
        adapterCartableHistoryhList.updateData(new ArrayList<>(structureNodesRES));

    }

    private void initData() {
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
        List<StructureNodeRES> structureNodesRES = new ArrayList<>();
        StructureHistoryListRES structureHistoryListRES = new StructureHistoryListRES();
        List<StructureCartableHistoryDB> cartableHistoryDBS = farzinHistoryListPresenter.GetCartableHistoryList();
        if (cartableHistoryDBS.size() > 0) {
            structureHistoryListRES = farzinHistoryListPresenter.initObject(cartableHistoryDBS.get(0).getDataXml());
            initSpinner(structureHistoryListRES.getGetHistoryListResult().getGraphs());
            if (App.networkStatus != NetworkStatus.Connected) {
                viewHolder.lnHistoryNumber.setVisibility(View.VISIBLE);
            }
            structureNodesRES = structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().get(0).getStartNode().getNode();
            viewHolder.lnLoading.setVisibility(View.VISIBLE);
        }
        if (App.networkStatus != NetworkStatus.Connected) {
            viewHolder.lnLoading.setVisibility(View.GONE);
            viewHolder.txtNoData.setVisibility(View.VISIBLE);
        } else {
            farzinHistoryListPresenter.GetCartableHistoryFromServer();
        }

        initAdapter(new ArrayList<>(structureNodesRES));
    }

    private void initAdapter(ArrayList<StructureNodeRES> structureNodeRES) {
        adapterCartableHistoryhList = new AdapterCartableHistoryhList(structureNodeRES);
        viewHolder.rcvMain.setAdapter(adapterCartableHistoryhList);
    }

    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
