package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Structure.Bundle.StructureActivityDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureAdapterZanjireMadrakBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureZanjireMadrakHeaderBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinZanjireMadrakEntityPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinZanjireMadrakPresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterZanjireMadrak;
import avida.ican.Farzin.View.Dialog.DialogZanjireMadrak;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.QueueEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocumentDetail;
import avida.ican.Farzin.View.FarzinActivityDocumentAttachFile;
import avida.ican.Farzin.View.FarzinActivityQueue;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterZanjireMadrak;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
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

public class FragmentZanjireMadrak extends BaseFragment {

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_zanjire_madrak)
    RecyclerView rcvZanjireMadrak;
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
    @BindView(R.id.fab_attach_file)
    FloatingActionButton fabAttachFile;
    private Activity context;
    private int Etc;
    private int Ec;
    String Title = "";
    private ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadraksBND = new ArrayList<>();
    private ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadrakAtfsBND = new ArrayList<>();
    private ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadrakPeyvastsBND = new ArrayList<>();
    private ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadrakPeirosBND = new ArrayList<>();
    private ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadrakDarErtebatsBND = new ArrayList<>();
    private AdapterZanjireMadrak adapterZanjireMadrak;
    private int AttachFileCODE = 002;

    private int PosAtf = 0;
    private int PosPeyvast = 0;
    private int PosPeiro = 0;
    private int PosDarErtebat = 0;

    private ListenerFile listenerFile;
    private int counter = 0;
    private FarzinZanjireMadrakPresenter farzinZanjireMadrakPresenter;
    private FarzinZanjireMadrakEntityPresenter farzinZanjireMadrakEntityPresenter;
    public boolean initialized = false;
    private boolean showInDialog = false;
    private DialogZanjireMadrak tempDialog;
    private Bundle bundleObject = new Bundle();
    public final String Tag = "FragmentZanjireMadrak";
    private boolean canAttach = false;

    public FragmentZanjireMadrak newInstance(Activity context, int Etc, int Ec, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        canAttach = false;
        Title = Resorse.getString(R.string.title_attach_file);
        this.listenerFile = listenerFile;
        return this;
    }

    public FragmentZanjireMadrak newInstance(Activity context, int Etc, int Ec, String title, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.Title = title;
        canAttach = true;
        this.listenerFile = listenerFile;
        return this;
    }

    public void setDialog(DialogZanjireMadrak dialog) {
        showInDialog = true;
        tempDialog = dialog;
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
        srlRefresh.setOnRefreshListener(() -> reGetData());
        lnOperatorQueueCount.setOnClickListener(view1 -> {
            checkQueue();
            if (showInDialog) {
                tempDialog.dismiss();
            }
            Intent intent = new Intent(App.CurentActivity, FarzinActivityQueue.class);
            intent.putExtra(PutExtraEnum.QueueType.getValue(), QueueEnum.DocumentAttachFile.getValue());
            BaseActivity.goToActivity(intent);

        });

        if (canAttach) {
            fabAttachFile.setVisibility(View.VISIBLE);
        } else {
            fabAttachFile.setVisibility(View.GONE);
        }
        fabAttachFile.setOnClickListener(view2 -> gotoActivityDocumentAttach());
        if (!initialized) {
            initRcv();
        }
        super.onViewCreated(view, savedInstanceState);
    }


    private void initRcv() {
        rcvZanjireMadrak.setLayoutManager(getNewLayoutManager());
        initAdapter();
        initPresenterFile();
        initPresenterEntity();
        getData();
    }

    private void initPresenterFile() {
        farzinZanjireMadrakPresenter = new FarzinZanjireMadrakPresenter(Etc, Ec, new ListenerZanjireMadrak() {
            @Override
            public void newData() {
                farzinZanjireMadrakEntityPresenter.getZanjireMadrakFromServer();
            }

            @Override
            public void noData() {
                farzinZanjireMadrakEntityPresenter.getZanjireMadrakFromServer();
            }
        });
    }

    private void initPresenterEntity() {
        farzinZanjireMadrakEntityPresenter = new FarzinZanjireMadrakEntityPresenter(Etc, Ec, new ListenerZanjireMadrak() {

            @Override
            public void newData() {
                ProcessData();
            }

            @Override
            public void noData() {
                ProcessData();
            }
        });


    }

    private void ProcessData() {
        counter = 0;
        App.CurentActivity.runOnUiThread(() -> lnLoading.setVisibility(View.VISIBLE));

        zanjireMadraksBND.clear();
        zanjireMadrakAtfsBND.clear();
        zanjireMadrakPeirosBND.clear();
        zanjireMadrakPeyvastsBND.clear();
        zanjireMadrakDarErtebatsBND.clear();

        PosAtf = 0;
        PosPeiro = 0;
        PosPeyvast = 0;
        PosDarErtebat = 0;
        //_____________________________________*******Entity********_______________________________________________

        List<StructureZanjireMadrakEntityDB> structureEntityAtf = farzinZanjireMadrakEntityPresenter.getZanjireMadrakEntityList(ZanjireMadrakFileTypeEnum.ATF);
        List<StructureZanjireMadrakEntityDB> structureEntityPeiro = farzinZanjireMadrakEntityPresenter.getZanjireMadrakEntityList(ZanjireMadrakFileTypeEnum.PEIRO);
        List<StructureZanjireMadrakEntityDB> structureEntityPeyvast = farzinZanjireMadrakEntityPresenter.getZanjireMadrakEntityList(ZanjireMadrakFileTypeEnum.PEYVAST);
        List<StructureZanjireMadrakEntityDB> structureEntityDarErtebat = farzinZanjireMadrakEntityPresenter.getZanjireMadrakEntityList(ZanjireMadrakFileTypeEnum.DARERTEBAT);

        //zanjireMadraksBND.add(getZanjireMadrakHeader(Resorse.getString(R.string.title_peyvast), Resorse.getDrawable(R.drawable.ic_peyvast)));

        for (StructureZanjireMadrakEntityDB zanjireMadrakEntityDB : structureEntityAtf) {
            PosAtf++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakEntityDB, PosAtf);
            zanjireMadrakAtfsBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakEntityDB zanjireMadrakEntityDB : structureEntityPeiro) {
            PosPeiro++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakEntityDB, PosPeiro);
            zanjireMadrakPeirosBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakEntityDB zanjireMadrakEntityDB : structureEntityPeyvast) {
            PosPeyvast++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakEntityDB, PosPeyvast);
            zanjireMadrakPeyvastsBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakEntityDB zanjireMadrakEntityDB : structureEntityDarErtebat) {
            PosDarErtebat++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakEntityDB, PosDarErtebat);
            zanjireMadrakDarErtebatsBND.add(zanjireMadrakBND);
        }


        //_____________________________________*******Entity********_______________________________________________


        //_____________________________________*******File********_______________________________________________

        List<StructureZanjireMadrakFileDB> structureFileAtf = farzinZanjireMadrakPresenter.getZanjireMadrakList(ZanjireMadrakFileTypeEnum.ATF);
        List<StructureZanjireMadrakFileDB> structureFilePeiro = farzinZanjireMadrakPresenter.getZanjireMadrakList(ZanjireMadrakFileTypeEnum.PEIRO);
        List<StructureZanjireMadrakFileDB> structureFilePeyvast = farzinZanjireMadrakPresenter.getZanjireMadrakList(ZanjireMadrakFileTypeEnum.PEYVAST);
        List<StructureZanjireMadrakFileDB> structureFileDarErtebat = farzinZanjireMadrakPresenter.getZanjireMadrakList(ZanjireMadrakFileTypeEnum.DARERTEBAT);

        for (StructureZanjireMadrakFileDB zanjireMadrakFileDB : structureFileAtf) {
            PosAtf++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakFileDB, PosAtf);
            zanjireMadrakAtfsBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakFileDB zanjireMadrakFileDB : structureFilePeiro) {
            PosPeiro++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakFileDB, PosPeiro);
            zanjireMadrakPeirosBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakFileDB zanjireMadrakFileDB : structureFilePeyvast) {
            PosPeyvast++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakFileDB, PosPeyvast);
            zanjireMadrakPeyvastsBND.add(zanjireMadrakBND);
        }
        for (StructureZanjireMadrakFileDB zanjireMadrakFileDB : structureFileDarErtebat) {
            PosDarErtebat++;
            StructureAdapterZanjireMadrakBND zanjireMadrakBND = new StructureAdapterZanjireMadrakBND(zanjireMadrakFileDB, PosDarErtebat);
            zanjireMadrakDarErtebatsBND.add(zanjireMadrakBND);
        }

        //_____________________________________*******File********_______________________________________________


        App.CurentActivity.runOnUiThread(() -> {
            zanjireMadraksBND.clear();
            if (zanjireMadrakPeyvastsBND.size() > 0) {
                counter++;

                zanjireMadraksBND.add(getZanjireMadrakHeader(Resorse.getString(R.string.title_peyvast), Resorse.getDrawable(R.drawable.ic_peyvast)));
                for (StructureAdapterZanjireMadrakBND zanjireMadrakBND : zanjireMadrakPeyvastsBND) {
                    zanjireMadraksBND.add(zanjireMadrakBND);
                }
            }

            if (zanjireMadrakDarErtebatsBND.size() > 0) {
                counter++;
                zanjireMadraksBND.add(getZanjireMadrakHeader(Resorse.getString(R.string.title_dar_ertebat), Resorse.getDrawable(R.drawable.ic_dar_ertebat)));
                for (StructureAdapterZanjireMadrakBND zanjireMadrakBND : zanjireMadrakDarErtebatsBND) {
                    zanjireMadraksBND.add(zanjireMadrakBND);
                }
            }
            if (zanjireMadrakPeirosBND.size() > 0) {
                counter++;
                zanjireMadraksBND.add(getZanjireMadrakHeader(Resorse.getString(R.string.title_peiro), Resorse.getDrawable(R.drawable.ic_peiro)));
                for (StructureAdapterZanjireMadrakBND zanjireMadrakBND : zanjireMadrakPeirosBND) {
                    zanjireMadraksBND.add(zanjireMadrakBND);
                }
            }
            if (zanjireMadrakAtfsBND.size() > 0) {
                counter++;
                zanjireMadraksBND.add(getZanjireMadrakHeader(Resorse.getString(R.string.title_atf), Resorse.getDrawable(R.drawable.ic_atf)));
                for (StructureAdapterZanjireMadrakBND zanjireMadrakBND : zanjireMadrakAtfsBND) {
                    zanjireMadraksBND.add(zanjireMadrakBND);
                }
            }
            adapterZanjireMadrak.updateData(zanjireMadraksBND);
            if (counter == 0) {
                txtNoData.setVisibility(View.VISIBLE);
            } else {
                txtNoData.setVisibility(View.GONE);
            }

            lnLoading.setVisibility(View.GONE);
            srlRefresh.setRefreshing(false);
        });

    }

    private StructureAdapterZanjireMadrakBND getZanjireMadrakHeader(String title, Drawable drawable) {
        StructureZanjireMadrakHeaderBND madrakHeaderBND = new StructureZanjireMadrakHeaderBND(title, drawable);
        return new StructureAdapterZanjireMadrakBND(madrakHeaderBND);

    }


    private void getData() {
        initialized = true;
        checkQueue();
        if (App.networkStatus == NetworkStatus.Connected) {
            lnLoading.setVisibility(View.VISIBLE);
            farzinZanjireMadrakPresenter.getZanjireMadrakFromServer();
        } else {
            ProcessData();
        }

    }

    public void reGetData() {
        App.getHandlerMainThread().post(() -> {
            checkQueue();
            txtNoData.setVisibility(View.GONE);
            if (!srlRefresh.isRefreshing()) {
                lnLoading.setVisibility(View.VISIBLE);
            }
            if (App.networkStatus == NetworkStatus.Connected) {
                farzinZanjireMadrakPresenter.getZanjireMadrakFromServer();
            } else {
                ProcessData();
            }
        });


    }

    public void checkQueue() {
        long count = new FarzinDocumentAttachFilePresenter().getDocumentAttachFileQueueQueueNotSendedCount(Etc, Ec);
        if (count > 0) {
            lnOperatorQueueCount.setVisibility(View.VISIBLE);
            txtTitleOperator.setText(Resorse.getString(R.string.title_attach_file));
            txtOperatorQueueCount.setText("" + count);
        } else {
            lnOperatorQueueCount.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        adapterZanjireMadrak = new AdapterZanjireMadrak(rcvZanjireMadrak, new ArrayList<>(zanjireMadraksBND), new ListenerAdapterZanjireMadrak() {
            @Override
            public void onClickZanjireEntity(StructureZanjireMadrakEntityDB zanjireMadrakEntityDB) {
                gotoActivityDocumentDetaile(zanjireMadrakEntityDB);
            }

            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void FileNotExist() {

            }
        });
        rcvZanjireMadrak.setAdapter(adapterZanjireMadrak);
    }

    private void gotoActivityDocumentDetaile(StructureZanjireMadrakEntityDB zanjireMadrakEntityDB) {
        App.getHandlerMainThread().post(() -> {
            App.canBack = true;
            StructureCartableDocumentDetailBND cartableDocumentDetailBND = new StructureCartableDocumentDetailBND(zanjireMadrakEntityDB.getETC(), zanjireMadrakEntityDB.getEC(), zanjireMadrakEntityDB.getTitle(), zanjireMadrakEntityDB.getEntityNumber(), zanjireMadrakEntityDB.getImportEntityNumber());
            bundleObject.putSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue(), cartableDocumentDetailBND);
            Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocumentDetail.class);
            intent.putExtras(bundleObject);
            BaseActivity.goToActivity(intent);

        });
    }

    private void gotoActivityDocumentAttach() {
        StructureActivityDocumentAttachFileBND structureActivityDocumentAttachFileBND = new StructureActivityDocumentAttachFileBND(Etc, Ec, Title);
        bundleObject.putSerializable(PutExtraEnum.BundleActivityDocumentAttach.getValue(), structureActivityDocumentAttachFileBND);
        Intent intent = new Intent(App.CurentActivity, FarzinActivityDocumentAttachFile.class);
        intent.putExtras(bundleObject);
        BaseActivity.goToActivityForResult(intent, AttachFileCODE);
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
