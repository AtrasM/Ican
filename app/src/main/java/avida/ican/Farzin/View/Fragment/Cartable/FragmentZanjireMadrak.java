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

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinZanjireMadrakPresenter;
import avida.ican.Farzin.View.Adapter.AdapteZanjireMadrak;
import avida.ican.Farzin.View.Fragment.FragmentHome;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterZanjireMadrak;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindView;

public class FragmentZanjireMadrak extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_peiro)
    RecyclerView rcvPeiro;
    @BindView(R.id.rcv_peyvast)
    RecyclerView rcvPeyvast;
    @BindView(R.id.rcv_dar_ertebat)
    RecyclerView rcvDarErtebat;
    @BindView(R.id.rcv_atf)
    RecyclerView rcvAtf;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.ln_atf)
    LinearLayout lnAtf;
    @BindView(R.id.ln_peiro)
    LinearLayout lnPeiro;
    @BindView(R.id.ln_peyvast)
    LinearLayout lnPeyvast;
    @BindView(R.id.ln_dar_ertebat)
    LinearLayout lnDarErtebat;
    private Activity context;
    private int Etc;
    private int Ec;
    private AdapteZanjireMadrak adaptePeyvast;
    private AdapteZanjireMadrak adapteDarErtebatBa;
    private AdapteZanjireMadrak adaptePeiro;
    private AdapteZanjireMadrak adapteAtf;
    private List<StructureZanjireMadrakFileDB> structurePeiro = new ArrayList<>();
    private List<StructureZanjireMadrakFileDB> structurePeyvast = new ArrayList<>();
    private List<StructureZanjireMadrakFileDB> structureDarErtebat = new ArrayList<>();
    private List<StructureZanjireMadrakFileDB> structureAtf = new ArrayList<>();
    private ListenerFile listenerFile;
    private int counter = 0;
    private FarzinZanjireMadrakPresenter farzinZanjireMadrakPresenter;
    public static String Tag = "FragmentZanjireMadrak";

    public FragmentZanjireMadrak newInstance(Activity context, int Etc, int Ec, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerFile = listenerFile;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_zanjire_madrak_list;
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
        rcvAtf.setLayoutManager(getNewLayoutManager());
        rcvDarErtebat.setLayoutManager(getNewLayoutManager());
        rcvPeiro.setLayoutManager(getNewLayoutManager());
        rcvPeyvast.setLayoutManager(getNewLayoutManager());
        rcvAtf.setNestedScrollingEnabled(false);
        rcvDarErtebat.setNestedScrollingEnabled(false);
        rcvPeiro.setNestedScrollingEnabled(false);
        rcvPeyvast.setNestedScrollingEnabled(false);
        initAdapter();
        initPresenter();
    }

    private void initPresenter() {
        farzinZanjireMadrakPresenter = new FarzinZanjireMadrakPresenter(Etc, Ec, new ListenerZanjireMadrak() {

            @Override
            public void newData() {
                ProcessData();
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (counter == 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        } else {
                            txtNoData.setVisibility(View.GONE);
                        }

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
                        if (adapteAtf.getDataSize() <= 0 && adapteDarErtebatBa.getDataSize() <= 0 && adaptePeiro.getDataSize() <= 0 && adaptePeyvast.getDataSize() <= 0) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });

        getData();

    }

    private void ProcessData() {
        counter = 0;
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lnLoading.setVisibility(View.VISIBLE);
            }
        });

        structureAtf = farzinZanjireMadrakPresenter.GetZanjireMadrakList(ZanjireMadrakFileTypeEnum.ATF);
        structureDarErtebat = farzinZanjireMadrakPresenter.GetZanjireMadrakList(ZanjireMadrakFileTypeEnum.DARERTEBAT);
        structurePeiro = farzinZanjireMadrakPresenter.GetZanjireMadrakList(ZanjireMadrakFileTypeEnum.PEIRO);
        structurePeyvast = farzinZanjireMadrakPresenter.GetZanjireMadrakList(ZanjireMadrakFileTypeEnum.PEYVAST);

        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (structureAtf.size() <= 0) {
                    lnAtf.setVisibility(View.GONE);
                } else {
                    counter++;
                    adapteAtf.updateData(structureAtf);
                    lnAtf.setVisibility(View.VISIBLE);
                }
                if (structurePeyvast.size() <= 0) {
                    lnPeyvast.setVisibility(View.GONE);
                } else {
                    counter++;
                    adaptePeyvast.updateData(structurePeyvast);
                    lnPeyvast.setVisibility(View.VISIBLE);
                }
                if (structurePeiro.size() <= 0) {
                    lnPeiro.setVisibility(View.GONE);
                } else {
                    counter++;
                    adaptePeiro.updateData(structurePeiro);
                    lnPeiro.setVisibility(View.VISIBLE);
                }
                if (structureDarErtebat.size() <= 0) {
                    lnDarErtebat.setVisibility(View.GONE);
                } else {
                    counter++;
                    adapteDarErtebatBa.updateData(structureDarErtebat);
                    lnDarErtebat.setVisibility(View.VISIBLE);
                }

                lnLoading.setVisibility(View.GONE);
                srlRefresh.setRefreshing(false);
            }
        });

    }


    private void getData() {


        if (App.networkStatus == NetworkStatus.Connected) {
            lnLoading.setVisibility(View.VISIBLE);
            farzinZanjireMadrakPresenter.GetZanjireMadrakFromServer();
        } else {
            ProcessData();
            if (counter <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            }
        }

    }

    private void reGetData() {
        txtNoData.setVisibility(View.GONE);
        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinZanjireMadrakPresenter.GetZanjireMadrakFromServer();
        } else {
            ProcessData();
            if (counter <= 0) {
                txtNoData.setVisibility(View.VISIBLE);
            } else {
                txtNoData.setVisibility(View.GONE);
            }
            srlRefresh.setRefreshing(false);
        }

    }


    private void initAdapter() {
        adapteAtf = new AdapteZanjireMadrak(new ArrayList<>(structureAtf), new ListenerAdapterZanjireMadrak() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void FileNotExist() {

            }
        });
        rcvAtf.setAdapter(adapteAtf);

        adapteDarErtebatBa = new AdapteZanjireMadrak(new ArrayList<>(structureDarErtebat), new ListenerAdapterZanjireMadrak() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void FileNotExist() {

            }
        });
        rcvDarErtebat.setAdapter(adapteDarErtebatBa);

        adaptePeiro = new AdapteZanjireMadrak(new ArrayList<>(structurePeiro), new ListenerAdapterZanjireMadrak() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void FileNotExist() {

            }
        });
        rcvPeiro.setAdapter(adaptePeiro);

        adaptePeyvast = new AdapteZanjireMadrak(new ArrayList<>(structurePeyvast), new ListenerAdapterZanjireMadrak() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void FileNotExist() {

            }
        });
        rcvPeyvast.setAdapter(adaptePeyvast);

    }

    private GridLayoutManagerWithSmoothScroller getNewLayoutManager() {
        return new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);

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
