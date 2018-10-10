package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Farzin.View.Adapter.AdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-09-25 at 2:11 PM
 */

public class DialogCartableHameshList {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private int Etc;
    private int Ec;
    private AdapterHameshList adapterHameshList;
    private int start = 0;
    private int COUNT = -1;

    public DialogCartableHameshList(Activity context) {
        this.context = context;
    }

    private FarzinHameshListPresenter farzinHameshListPresenter;


    public class Binding {
        @BindView(R.id.srl_refresh)
        SwipeRefreshLayout srlRefresh;
        @BindView(R.id.rcv_hamesh_list)
        RecyclerView rcvHameshList;
        @BindView(R.id.txt_no_data)
        TextView txtNoData;
        @BindView(R.id.ln_loading)
        LinearLayout lnLoading;


        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogCartableHameshList setData(int Etc, int Ec) {
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
                        .setContentHolder(new ViewHolder(R.layout.layout_hamesh_list))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(true)
                        //.setContentBackgroundResource(android.R.color.transparent)
                        .create();
                dialog.show();
                viewHolder = new DialogCartableHameshList.Binding(dialog.getHolderView());

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
        viewHolder.rcvHameshList.setLayoutManager(gridLayoutManager);
        initHameshPresenter();
    }

    private void initHameshPresenter() {
        farzinHameshListPresenter = new FarzinHameshListPresenter(Etc, Ec, new ListenerHamesh() {
            @Override
            public void newData(final StructureHameshDB structureHameshDB) {
                start = start + 1;
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewHolder.lnLoading.setVisibility(View.GONE);
                        viewHolder.txtNoData.setVisibility(View.GONE);
                        adapterHameshList.updateData(0, structureHameshDB);
                    }
                });


            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(viewHolder.lnLoading.getVisibility()==View.VISIBLE){
                            viewHolder.lnLoading.setVisibility(View.GONE);
                            viewHolder.txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        initData();

    }
    public boolean isShowing() {
       return dialog.isShowing();
    }


    private void reGetData() {
        start = 0;
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
        List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);
        if (structureHameshDBS.size() <= 0) {
            viewHolder.txtNoData.setVisibility(View.VISIBLE);
        }
        farzinHameshListPresenter.GetHameshFromServer();

        start = structureHameshDBS.size();
        adapterHameshList.updateData(structureHameshDBS);
        viewHolder.lnLoading.setVisibility(View.GONE);
        viewHolder.srlRefresh.setRefreshing(false);
    }

    private void initData() {
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
        List<StructureHameshDB> structureHameshDBS = farzinHameshListPresenter.GetHameshList(start, COUNT);
        if (structureHameshDBS.size() <= 0) {
            viewHolder.txtNoData.setVisibility(View.VISIBLE);
        }
        farzinHameshListPresenter.GetHameshFromServer();

        start = structureHameshDBS.size();
        initAdapter(new ArrayList<>(structureHameshDBS));
        viewHolder.lnLoading.setVisibility(View.GONE);
    }

    private void initAdapter(ArrayList<StructureHameshDB> structureHameshDBS) {
        adapterHameshList = new AdapterHameshList(structureHameshDBS, new ListenerAdapterHameshList() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {

            }
        });
        viewHolder.rcvHameshList.setAdapter(adapterHameshList);
    }

    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
