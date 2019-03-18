package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
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

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Farzin.View.Adapter.AdapterHamesh;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
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

    private Activity context;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private int Etc;
    private int Ec;
    private AdapterHamesh adapterHamesh;
    private int start = 0;
    private int COUNT = -1;
    private ListenerFile listenerFile;
    private FarzinHameshListPresenter farzinHameshListPresenter;
    public static String Tag = "FragmentCartableHameshList";

    public FragmentCartableHameshList newInstance(Activity context, int Etc, int Ec, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerFile = listenerFile;
        return this;
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
        rcvHameshList.setLayoutManager(gridLayoutManager);
        initHameshPresenter();
    }

    private void initHameshPresenter() {
        farzinHameshListPresenter = new FarzinHameshListPresenter(Etc, Ec, new ListenerHamesh() {
            @Override
            public void newData(final StructureHameshDB structureHameshDB) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);
                        adapterHamesh.updateData(structureHameshDBS);
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
                        if (adapterHamesh.getDataSize() <= 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        initData();

    }


    public void reGetData() {
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

    private void initData() {
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
