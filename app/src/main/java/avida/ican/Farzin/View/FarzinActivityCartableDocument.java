package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentRefreshListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentTaeedQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableSendQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.GetDocumentActionsFromServerListener;
import avida.ican.Farzin.Model.Interface.Cartable.SendListener;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentActionsPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendToServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentTaeedServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Adapter.AdapteCartableDocument;
import avida.ican.Farzin.View.Dialog.DialogCartableHamesh;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Dialog.DialogCartableHistoryList;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Dialog.DialogZanjireMadrak;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableDocumentList;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import static avida.ican.Farzin.View.Enum.CartableDocumentDetailActionsEnum.ERJA;
import static avida.ican.Farzin.View.Enum.CartableDocumentDetailActionsEnum.TAEED;

public class FarzinActivityCartableDocument extends BaseToolbarActivity {
    private static final long FAILED_DELAY = TimeValue.SecondsInMilli() * 3;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;
    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;
    @BindString(R.string.cartable_document)
    String Title;
    @BindView(R.id.img_move_up)
    ImageView imgMoveUp;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;

    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;
    private long start = 0;
    private long COUNT = 10;
    private List<StructureInboxDocumentDB> structureInboxDocumentDB = new ArrayList<>();
    private GridLayoutManagerWithSmoothScroller gridLayoutManager;
    private ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList;
    private AdapteCartableDocument adapteCartableDocument;
    private int actionCode = -1;
    private DialogCartableHameshList dialogCartableHameshList;
    private DialogCartableHistoryList dialogCartableHistoryList;
    private DialogZanjireMadrak dialogZanjireMadrak;
    private final int EnumDialogCartableHistory = 0;
    private final int EnumDialogCartableHamesh = 1;
    private final int EnumdialogZanjireMadrak = 2;
    private int dialogEnum = -1;
    private File file;
    private Bundle bundleObject = new Bundle();
    private long FailedDelay = TimeValue.SecondsInMilli() * 3;
    private boolean canDoAnyAction = true;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private CartableDocumentListListener cartableDocumentListListener;
    private int DOCUMENTDETAILCODE = 001;
    private StructureInboxDocumentDB curentItem = new StructureInboxDocumentDB();
    private DialogUserAndRole dialogUserAndRole;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private FarzinCartableDocumentListPresenter farzinCartableDocumentListPresenter;
    private MenuItem actionfilter;
    private boolean isFilter;

    @Override
    protected void onResume() {
        if (file != null) {
            boolean b = file.delete();
            if (b) {
                file = null;
            }

        }
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_cartable_document;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.activity = this;
        Bundle bundleObject = getIntent().getExtras();
        StructureCartableDocumentBND structureCartableDocumentBND = (StructureCartableDocumentBND) bundleObject.getSerializable(PutExtraEnum.BundleCartableDocument.getValue());
        Title = structureCartableDocumentBND.getActionNAme();
        isFilter = structureCartableDocumentBND.isFilter();
        initTollBar(Title);
        actionCode = structureCartableDocumentBND.getActionCode();
        animator = new Animator(App.CurentActivity);
        initCartableDocumentListPresenter();
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

    private void initCartableDocumentListPresenter() {
        farzinCartableDocumentListPresenter = new FarzinCartableDocumentListPresenter(new CartableDocumentRefreshListener() {
            @Override
            public void newData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        canLoading = true;
                        structureInboxDocumentDB.clear();
                        structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, start, COUNT);
                        adapteCartableDocument.updateData(structureInboxDocumentDB);
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
                        canLoading = true;
                    }
                });
            }

            @Override
            public void onFailed(String message) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                        canLoading = true;
                    }
                });
            }
        });
    }



    private void initData(int actionCode) {
        if (isFilter) {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentDB.size();
        initRcv();
    }

    private void reGetData() {
        start = 0;
        canLoading = false;
        structureInboxDocumentDB.clear();
        if (isFilter) {
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentDB.size();
        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinCartableDocumentListPresenter.GetFromServer();
        } else {
            canLoading = true;
            srlRefresh.setRefreshing(false);
            lnLoading.setVisibility(View.GONE);
        }
        adapteCartableDocument.updateData(structureInboxDocumentDB);

    }
    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvMain.setLayoutManager(gridLayoutManager);
        initAdapter(structureInboxDocumentDB);
    }

    private void initAdapter(List<StructureInboxDocumentDB> structureInboxDocumentDB) {
        adapteCartableDocument = new AdapteCartableDocument(new ArrayList<StructureInboxDocumentDB>(structureInboxDocumentDB), initListener());
        rcvMain.setAdapter(adapteCartableDocument);
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
            public void onClick(final StructureInboxDocumentDB item) {

                if (canDoAnyAction) {
                    if (!farzinCartableQuery.IsDocumentActionsExist(item.getEntityTypeCode())) {
                        lnLoading.setVisibility(View.VISIBLE);
                        new CartableDocumentActionsPresenter(item.getEntityTypeCode()).GetDocumentActionsFromServer(new GetDocumentActionsFromServerListener() {
                            @Override
                            public void onSuccess() {
                                gotoDocumentDetail(item);
                            }

                            @Override
                            public void onFailed(String message) {
                                lnLoading.setVisibility(View.GONE);
                                App.ShowMessage().ShowToast(Resorse.getString(R.string.wrong_to_load_document_actions), ToastEnum.TOAST_LONG_TIME);
                            }

                            @Override
                            public void onCancel() {
                                lnLoading.setVisibility(View.GONE);
                                App.ShowMessage().ShowToast(Resorse.getString(R.string.wrong_to_load_document_actions), ToastEnum.TOAST_LONG_TIME);
                            }
                        });
                    } else {
                        gotoDocumentDetail(item);
                    }

                }

            }

            @Override
            public void onAction(final StructureInboxDocumentDB structureInboxDocumentDB, CartableActionsEnum cartableActionsEnum) {
                canDoAnyAction = false;
                switch (cartableActionsEnum) {
                    case ListHamesh: {
                        dialogCartableHameshList = new DialogCartableHameshList(App.CurentActivity).setData(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode());
                        dialogCartableHameshList.setListenerFile(new ListenerFile() {
                            @Override
                            public void onOpenFile(StructureAttach structureAttach) {
                                checkFile(structureAttach);
                            }
                        });
                        dialogCartableHameshList.Creat(getSupportFragmentManager());
                        dialogEnum = EnumDialogCartableHamesh;
                        break;
                    }
                    case DocumentFlow: {
                        dialogCartableHistoryList = new DialogCartableHistoryList(App.CurentActivity).setData(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode());
                        dialogCartableHistoryList.Creat(getSupportFragmentManager());
                        dialogEnum = EnumDialogCartableHistory;
                        break;
                    }
                    case Confirm: {
                        new DialogQuestion(App.CurentActivity).setTitle(Resorse.getString(R.string.title_question)).setOnListener(new ListenerQuestion() {
                            @Override
                            public void onSuccess() {
                                lnLoading.setVisibility(View.VISIBLE);
                                Taeed(structureInboxDocumentDB);
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).Show();

                        break;
                    }
                    case Comission: {

                        showUserAndRoleDialog(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode(), structureInboxDocumentDB.getSendCode());
                        break;
                    }
                    case TheChainOfEvidence: {
                        dialogZanjireMadrak = new DialogZanjireMadrak(App.CurentActivity).setData(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode());
                        dialogZanjireMadrak.setListenerFile(new ListenerFile() {
                            @Override
                            public void onOpenFile(StructureAttach structureAttach) {
                                checkFile(structureAttach);
                            }
                        });
                        dialogZanjireMadrak.Creat(getSupportFragmentManager());
                        dialogEnum = EnumdialogZanjireMadrak;
                        break;
                    }

                    case Hamesh: {
                        DialogCartableHamesh dialogCartableHamesh = new DialogCartableHamesh(App.CurentActivity);
                        dialogCartableHamesh.setData(structureInboxDocumentDB.getSenderName(), structureInboxDocumentDB.getSenderRoleName(), structureInboxDocumentDB.getPrivateHameshContent());
                        dialogCartableHamesh.Creat();
                        break;
                    }
                }

                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canDoAnyAction = true;
                    }
                }, 1000);
            }
        }

        ;
        return listenerAdapterCartableDocumentList;
    }


    private void showUserAndRoleDialog(int ETC, int EC, int sendCode) {
        lnLoading.setVisibility(View.VISIBLE);
        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, ETC, EC, sendCode).setTitle(Resorse.getString(R.string.title_send)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<StructureUserAndRoleDB>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                userAndRolesMain = structureUserAndRolesMain;
                lnLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {
                Send(structureAppendREQ);
            }

            @Override
            public void onFailed() {
                lnLoading.setVisibility(View.GONE);
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCancel(final List<StructureUserAndRoleDB> tmpItemSelect) {
                lnLoading.setVisibility(View.GONE);
            }


        });

    }

    //_________________________________*****___Send___*****__________________________________
    private void Send(final StructureAppendREQ structureAppendREQ) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            SendAddToQueue(structureAppendREQ);
        } else {
            new CartableDocumentAppendToServerPresenter().AppendDocument(structureAppendREQ, new SendListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    SendAddToQueue(structureAppendREQ);
                }

                @Override
                public void onCancel() {
                    SendAddToQueue(structureAppendREQ);
                }

                @Override
                public void onFinish() {
                    App.getHandlerMainThread().post(new Runnable() {
                        @Override
                        public void run() {
                            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_send_successfull), SnackBarEnum.SNACKBAR_LONG_TIME);
                            lnLoading.setVisibility(View.GONE);
                        }
                    });


                }
            });

        }
    }

    private void SendAddToQueue(final StructureAppendREQ structureAppendREQ) {
        new FarzinCartableQuery().saveCartableSendQueue(structureAppendREQ, new CartableSendQuerySaveListener() {

            @Override
            public void onSuccess() {
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

                    }
                });

            }

            @Override
            public void onExisting() {
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

                    }
                });

            }

            @Override
            public void onFailed(String message) {
                trySendAddToQueue(structureAppendREQ);
            }

            @Override
            public void onCancel() {
                trySendAddToQueue(structureAppendREQ);
            }
        });
    }

    private void trySendAddToQueue(final StructureAppendREQ structureAppendREQ) {
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SendAddToQueue(structureAppendREQ);
            }
        }, FailedDelay);
    }
    //_________________________________*****___Send___*****__________________________________

    private void gotoDocumentDetail(final StructureInboxDocumentDB item) {


        App.getHandlerMainThread().post(new Runnable() {
            @Override
            public void run() {
                StructureCartableDocumentDetailBND cartableDocumentDetailBND = new StructureCartableDocumentDetailBND(item.getEntityTypeCode(), item.getEntityCode(),item.getSendCode(), item.getReceiverCode(), item.getReceiveDate(), item.getTitle(), item.getSenderName(), item.getSenderRoleName(), item.getEntityNumber());
                bundleObject.putSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue(), cartableDocumentDetailBND);
                Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocumentDetail.class);
                intent.putExtras(bundleObject);
                goToActivityForResult(intent, DOCUMENTDETAILCODE);
                curentItem = item;
                lnLoading.setVisibility(View.GONE);
            }
        });
        farzinCartableQuery.updateCartableDocumentStatus(item.getId(), Status.READ);
        if (isFilter) {
            adapteCartableDocument.deletItem(item);
        } else {
            item.setStatus(Status.READ);
            item.setRead(true);
            adapteCartableDocument.updateItem(item);
        }
    }

    //_________________________________*****___Taeed___*****__________________________________
    private void Taeed(final StructureInboxDocumentDB structureInboxDocumentDB) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            TaeedAddToQueue(structureInboxDocumentDB);
        } else {
            new CartableDocumentTaeedServerPresenter().TaeedDocument(structureInboxDocumentDB.getReceiverCode(), new TaeedListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    TaeedAddToQueue(structureInboxDocumentDB);
                }

                @Override
                public void onCancel() {
                    TaeedAddToQueue(structureInboxDocumentDB);
                }

                @Override
                public void onFinish() {
                    farzinCartableQuery.deletCartableDocumentAllContent(structureInboxDocumentDB.getReceiverCode());
                    adapteCartableDocument.deletItem(structureInboxDocumentDB);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_taeed_successfull), SnackBarEnum.SNACKBAR_LONG_TIME);
                    lnLoading.setVisibility(View.GONE);

                }
            });

        }
    }

    private void TaeedAddToQueue(final StructureInboxDocumentDB structureInboxDocumentDB) {
        new FarzinCartableQuery().saveCartableDocumentTaeedQueue(structureInboxDocumentDB.getReceiverCode(), new CartableDocumentTaeedQueueQuerySaveListener() {
            @Override
            public void onSuccess(int receiveCode) {
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        adapteCartableDocument.deletItem(structureInboxDocumentDB);
                        lnLoading.setVisibility(View.GONE);
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

                    }
                });

            }

            @Override
            public void onExisting() {
                App.getHandlerMainThread().post(new Runnable() {
                    @Override
                    public void run() {
                        adapteCartableDocument.deletItem(structureInboxDocumentDB);
                        lnLoading.setVisibility(View.GONE);
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

                    }
                });

            }

            @Override
            public void onFailed(String message) {
                tryTaeedAddToQueue(structureInboxDocumentDB);
            }

            @Override
            public void onCancel() {
                tryTaeedAddToQueue(structureInboxDocumentDB);
            }
        });
    }

    private void tryTaeedAddToQueue(final StructureInboxDocumentDB structureInboxDocumentDB) {
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TaeedAddToQueue(structureInboxDocumentDB);
            }
        }, FailedDelay);
    }
    //_________________________________*****___Taeed___*****__________________________________


    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
    }

    private void countinugetData() {
        final Loading loading = new Loading(App.CurentActivity).Creat();
        loading.Show();
        List<StructureInboxDocumentDB> inboxDocumentDBS = new ArrayList<>();
        if (isFilter) {
            inboxDocumentDBS = new FarzinCartableQuery().getCartableDocuments(actionCode, Status.UnRead, start, COUNT);
        } else {
            inboxDocumentDBS = new FarzinCartableQuery().getCartableDocuments(actionCode, null, start, COUNT);
        }
        start = structureInboxDocumentDB.size();
        structureInboxDocumentDB.addAll(inboxDocumentDBS);
        if (inboxDocumentDBS.size() > 0) {
            adapteCartableDocument.updateData(-1, inboxDocumentDBS);
            canLoading = true;
        }
        loading.Hide();
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DOCUMENTDETAILCODE) {
            if (resultCode == TAEED.getValue()) {
                adapteCartableDocument.deletItem(curentItem);
            } else if (resultCode == ERJA.getValue()) {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Back();
                break;
            }
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.filter_toolbar_menu, menu);
        // menu.findItem(R.id.action_search).setIntent(new Intent(G.currentActivity, ActivitySearch.class));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        actionfilter = menu.findItem(R.id.action_filter);
        actionfilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (lnLoading.getVisibility() == View.GONE) {
                    isFilter = !isFilter;
                    filterData();
                }

                return false;
            }
        });
        initData(actionCode);
        return super.onPrepareOptionsMenu(menu);
    }

    private void filterData() {
        lnLoading.setVisibility(View.VISIBLE);
        start = 0;
        canLoading = false;
        structureInboxDocumentDB.clear();
        if (isFilter) {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            structureInboxDocumentDB = farzinCartableDocumentListPresenter.GetFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentDB.size();
        canLoading = true;
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
        adapteCartableDocument.updateData(structureInboxDocumentDB);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Back();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Back() {
        switch (dialogEnum) {
            case EnumDialogCartableHamesh: {
                dialogCartableHameshList.dismiss();
                dialogEnum = -1;
                break;
            }
            case EnumDialogCartableHistory: {
                dialogCartableHistoryList.dismiss();
                dialogEnum = -1;
                break;
            }
            case EnumdialogZanjireMadrak: {
                dialogZanjireMadrak.dismiss();
                dialogEnum = -1;
                break;
            }
            default: {
                Finish(App.CurentActivity);
            }

        }


    }

}
