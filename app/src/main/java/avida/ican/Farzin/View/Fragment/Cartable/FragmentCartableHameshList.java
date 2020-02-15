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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterHamesh;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.QueueEnum;
import avida.ican.Farzin.View.FarzinActivityQueue;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindView;

public class FragmentCartableHameshList extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_hamesh_list)
    RecyclerView rcvHameshList;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
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
    private AdapterHamesh adapterHamesh;
    private int start = 0;
    private int COUNT = -1;
    private ListenerFile listenerFile;
    private FarzinHameshListPresenter farzinHameshListPresenter;
    public final String Tag = "FragmentCartableHameshList";
    private boolean showInDialog = false;
    private DialogCartableHameshList tempDialog;

    public FragmentCartableHameshList newInstance(Activity context, int Etc, int Ec, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerFile = listenerFile;
        return this;
    }

    public void setDialog(DialogCartableHameshList dialog) {
        showInDialog = true;
        tempDialog = dialog;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_hamesh_list;
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
        initRcv();
    }


    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvHameshList.setLayoutManager(gridLayoutManager);
        initHameshPresenter();
    }


    private void initHameshPresenter() {
        farzinHameshListPresenter = new FarzinHameshListPresenter(Etc, Ec, new ListenerHamesh() {
            @Override
            public void newData(final StructureHameshDB structureHameshDB) {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    srlRefresh.setRefreshing(false);
                    List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);
                    adapterHamesh.updateData(structureHameshDBS);
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        if (adapterHamesh.getDataSize() <= 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
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
        checkQueue();
        lnLoading.setVisibility(View.VISIBLE);
        List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);

        if (App.networkStatus == NetworkStatus.Connected) {
            farzinHameshListPresenter.GetHameshFromServer();
        } else {
            lnLoading.setVisibility(View.GONE);
            if (structureHameshDBS.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        }
        start = structureHameshDBS.size();
        initAdapter(new ArrayList<>(structureHameshDBS));
    }


    public void reGetData() {
        checkQueue();
        start = 0;
        txtNoData.setVisibility(View.GONE);
        List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);

        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinHameshListPresenter.GetHameshFromServer();
        } else {
            if (structureHameshDBS.size() <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
            srlRefresh.setRefreshing(false);
            lnLoading.setVisibility(View.GONE);
        }
        start = structureHameshDBS.size();
        adapterHamesh.updateData(structureHameshDBS);
    }

    public void checkQueue() {
        long count = new FarzinDocumentOperatorsQueuePresenter().getDocumentOperatorQueueNotSendedCount(Etc, Ec, DocumentOperatoresTypeEnum.AddHameshOpticalPen);
        if (count > 0) {
            lnOperatorQueueCount.setVisibility(View.VISIBLE);
            txtTitleOperator.setText(Resorse.getString(R.string.title_hamesh_operator));
            txtOperatorQueueCount.setText("" + count);
        } else {
            lnOperatorQueueCount.setVisibility(View.GONE);
        }
    }

    private void initAdapter(ArrayList<StructureHameshDB> structureHameshDBS) {
        adapterHamesh = new AdapterHamesh(structureHameshDBS, new ListenerAdapterHameshList() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }
        });
        rcvHameshList.setAdapter(adapterHamesh);
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
