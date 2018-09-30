package avida.ican.Farzin.View;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Adapter.AdapteCartableDocumentList;
import avida.ican.Farzin.View.Dialog.DialogCartableHamesh;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Interface.ListenerAdapterCartableDocumentList;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityCartableDocument extends BaseToolbarActivity {
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;
    @BindString(R.string.cartable_document)
    String Title;
    @BindView(R.id.img_move_up)
    ImageView imgMoveUp;

    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;
    private long start = 0;
    private long COUNT = 10;
    private List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
    private Bundle bundleObject = new Bundle();
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList;
    private AdapteCartableDocumentList adapteCartableDocumentList;
    private int actionCode = -1;
    private DialogCartableHameshList dialogCartableHameshList;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_cartable_document;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleObject = getIntent().getExtras();
        StructureCartableDocumentBND structureCartableDocumentBND = (StructureCartableDocumentBND) bundleObject.getSerializable(PutExtraEnum.BundleCartableDocument.getValue());
        Title = structureCartableDocumentBND.getActionNAme();
        initTollBar(Title);
        actionCode = structureCartableDocumentBND.getActionCode();
        animator = new Animator(App.CurentActivity);
        initData(actionCode);


        rcvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = gridLayoutManager.getChildCount();

                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPositions(null);
                if (dy < 0) {
                    if ((visibleItemCount + pastVisiblesItems[0]) >= 10 && !isshow) {
                        animator.slideInFromDown(imgMoveUp);
                        imgMoveUp.setVisibility(View.VISIBLE);
                        isshow = true;
                    } else if ((visibleItemCount + pastVisiblesItems[0]) < 10 && isshow) {
                        imgMoveUp.setVisibility(View.GONE);
                        isshow = false;
                    }
                }
                if (dy > 0) //check for scroll down
                {
                    if (canLoading) {
                        totalItemCount = gridLayoutManager.getItemCount();
                        if ((visibleItemCount + pastVisiblesItems[0]) >= totalItemCount - 2) {
                            canLoading = false;
                            listenerAdapterCartableDocumentList.onLoadData();
                        }
                    }

                }
            }
        });

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reGetData();
            }
        });
    }

    private void reGetData() {
        start = 0;
        canLoading = false;
        structureInboxDocumentDB.clear();
        structureInboxDocumentDB = new FarzinCartableQuery().getCartableDocuments(actionCode, null, start, COUNT);
        start = structureInboxDocumentDB.size();
        adapteCartableDocumentList.updateData(structureInboxDocumentDB);
        srlRefresh.setRefreshing(false);
        canLoading = true;
    }

    private void initData(int actionCode) {
        structureInboxDocumentDB = new FarzinCartableQuery().getCartableDocuments(actionCode, null, start, COUNT);
        start = structureInboxDocumentDB.size();
        initRcv();
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvMain.setLayoutManager(gridLayoutManager);
        initAdapter(structureInboxDocumentDB);
    }

    private void initAdapter(List<StructureInboxDocumentDB> structureInboxDocumentDB) {
        adapteCartableDocumentList = new AdapteCartableDocumentList(new ArrayList<StructureInboxDocumentDB>(structureInboxDocumentDB), initListener());
        rcvMain.setAdapter(adapteCartableDocumentList);
    }

    private ListenerAdapterCartableDocumentList initListener() {
        listenerAdapterCartableDocumentList = new ListenerAdapterCartableDocumentList() {
            @Override
            public void onConfirmation() {

            }

            @Override
            public void onLoadData() {
                countinugetData();
            }

            @Override
            public void onClick(StructureInboxDocumentDB structureInboxDocumentDB) {
                dialogCartableHameshList = new DialogCartableHameshList(App.CurentActivity).setData(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode());
                dialogCartableHameshList.Creat();
            }

            @Override
            public void onAction(StructureInboxDocumentDB structureInboxDocumentDB, CartableActionsEnum cartableActionsEnum) {
                switch (cartableActionsEnum) {
                    case ListHamesh: {

                        break;
                    }
                    case Hamesh: {
                        DialogCartableHamesh dialogCartableHamesh = new DialogCartableHamesh(App.CurentActivity);
                        dialogCartableHamesh.setData(structureInboxDocumentDB.getSenderName(), structureInboxDocumentDB.getSenderRoleName(), structureInboxDocumentDB.getPrivateHameshContent());
                        dialogCartableHamesh.Creat();
                        break;
                    }
                }
            }
        };
        return listenerAdapterCartableDocumentList;
    }

    private void countinugetData() {
        final Loading loading = new Loading(App.CurentActivity).Creat();
        loading.Show();
        List<StructureInboxDocumentDB> inboxDocumentDBS = new ArrayList<>();
        inboxDocumentDBS = new FarzinCartableQuery().getCartableDocuments(actionCode, null, start, COUNT);
        start = structureInboxDocumentDB.size();
        structureInboxDocumentDB.addAll(inboxDocumentDBS);
        if (inboxDocumentDBS.size() > 0) {
            adapteCartableDocumentList.updateData(-1, inboxDocumentDBS);
            canLoading = true;
        }
        loading.Hide();
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
