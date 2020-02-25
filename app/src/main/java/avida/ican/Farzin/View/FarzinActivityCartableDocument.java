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
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentDataListener;
import avida.ican.Farzin.Model.Interface.Cartable.ContinueWorkFlowListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentActionsListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureResponseREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureWorkFlowREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentActionsPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentConfirmPresenter;
import avida.ican.Farzin.Presenter.Cartable.ContinueWorkFlowPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Adapter.Cartable.AdapteCartableDocument;
import avida.ican.Farzin.View.Dialog.DialogCartableTozihSH_DastorErja;
import avida.ican.Farzin.View.Dialog.DialogCartableHameshList;
import avida.ican.Farzin.View.Dialog.DialogCartableHistoryList;
import avida.ican.Farzin.View.Dialog.DialogShowMore;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Dialog.DialogWorkFlow;
import avida.ican.Farzin.View.Dialog.DialogZanjireMadrak;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableDocumentList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerDialogWorkFlow;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
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
    @BindView(R.id.txt_no_data)
    TextView txtNoData;

    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;
    private long start = 0;
    private long COUNT = 10;
    private List<StructureInboxDocumentDB> structureInboxDocumentsDB = new ArrayList<>();
    private StructureInboxDocumentDB structureInboxDocumentDB = new StructureInboxDocumentDB();
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
    private DialogWorkFlow dialogWorkFlow;

    @Override
    protected void onResume() {
        if (file != null) {
            boolean b = file.delete();
            if (b) {
                file = null;
            }
        }
        if (App.isDocumentConfirm) {
            try {
                App.isDocumentConfirm = false;
                App.curentDocumentShowing = -1;
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal());
            } catch (Exception e) {
                e.printStackTrace();
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
        start = 0;
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
                } else if (dy > 0) //check for scroll down
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

        srlRefresh.setOnRefreshListener(() -> App.CurentActivity.runOnUiThread(() -> reGetDataFromServer()));
    }

    private void initCartableDocumentListPresenter() {
        farzinCartableDocumentListPresenter = new FarzinCartableDocumentListPresenter(App.CurentActivity, new CartableDocumentDataListener() {
            @Override
            public void newData() {
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal());
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal());
            }

            @Override
            public void onFailed(String message) {
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal());
            }
        });
    }


    private void initData(int actionCode) {
        if (isFilter) {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentsDB.size();
        if (structureInboxDocumentsDB.size() > 0) {
            txtNoData.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
        initRcv();
    }

    public void reGetDataFromServer() {
        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        start = 0;
        canLoading = false;
        structureInboxDocumentsDB.clear();
        if (App.networkStatus == NetworkStatus.Connected) {
            CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getCartableLastCheckDate(), (TimeValue.SecondsInMilli() * 10));
            if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                farzinCartableDocumentListPresenter.getFromServer();
            } else {
                reGetDataFromLocal();
            }
        } else {
            reGetDataFromLocal();
        }

    }

    public void reGetDataFromLocal() {
        if (!srlRefresh.isRefreshing()) {
            lnLoading.setVisibility(View.VISIBLE);
        }
        start = 0;
        canLoading = false;
        structureInboxDocumentsDB.clear();
        if (isFilter) {
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentsDB.size();
        if (structureInboxDocumentsDB.size() > 0) {
            txtNoData.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
        adapteCartableDocument.updateData(structureInboxDocumentsDB);
        canLoading = true;
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
    }

    private void initRcv() {
        gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvMain.setLayoutManager(gridLayoutManager);
        initAdapter(structureInboxDocumentsDB);
    }

    private void initAdapter(List<StructureInboxDocumentDB> structureInboxDocumentDB) {
        adapteCartableDocument = new AdapteCartableDocument(new ArrayList<>(structureInboxDocumentDB), initListener());
        rcvMain.setAdapter(adapteCartableDocument);
    }

    private ListenerAdapterCartableDocumentList initListener() {
        listenerAdapterCartableDocumentList = new ListenerAdapterCartableDocumentList() {

            @Override
            public void onConfirmation() {

            }

            @Override
            public void onLoadData() {
                continuegetData();
            }

            @Override
            public void onClick(final StructureInboxDocumentDB item) {

                if (canDoAnyAction) {
                    if (!farzinCartableQuery.IsDocumentActionsExist(item.getEntityTypeCode())) {
                        lnLoading.setVisibility(View.VISIBLE);
                        new CartableDocumentActionsPresenter(item.getEntityTypeCode()).GetDocumentActionsFromServer(new DocumentActionsListener() {
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
                        dialogCartableHameshList.setListenerFile(structureAttach -> checkFile(structureAttach));
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
                        new DialogQuestion(App.CurentActivity).setTitle(Resorse.getString(R.string.title_question_confirm)).setOnListener(new ListenerQuestion() {
                            @Override
                            public void onSuccess() {
                                lnLoading.setVisibility(View.VISIBLE);
                                Confirm(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode(), structureInboxDocumentDB);
                            }

                            @Override
                            public void onCancel() {

                            }
                        }).Show();

                        break;
                    }
                    case ConfirmSend: {
                        showUserAndRoleDialog(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode(), structureInboxDocumentDB.getSendCode(), structureInboxDocumentDB);
                        break;
                    }
                    case InWorkFlow: {
                        showWorkFlowDialog(structureInboxDocumentDB);
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

                    case DstorErja: {
                        DialogCartableTozihSH_DastorErja dialogCartableTozihSHDastorErja = new DialogCartableTozihSH_DastorErja(App.CurentActivity);
                        dialogCartableTozihSHDastorErja.setData(Resorse.getString(R.string.referral_order), R.drawable.ic_document_white, structureInboxDocumentDB.getSenderName(), structureInboxDocumentDB.getSenderRoleName(), structureInboxDocumentDB.getPrivateHameshContent());
                        dialogCartableTozihSHDastorErja.Creat();
                        break;
                    }
                    case TozihShakhsi: {
                        DialogCartableTozihSH_DastorErja dialogCartableTozihSHDastorErja = new DialogCartableTozihSH_DastorErja(App.CurentActivity);
                        dialogCartableTozihSHDastorErja.setData(Resorse.getString(R.string.tozih_shakhsi), R.drawable.ic_tozih_shkhsi, structureInboxDocumentDB.getSenderName(), structureInboxDocumentDB.getSenderRoleName(), structureInboxDocumentDB.getUserDescription());
                        dialogCartableTozihSHDastorErja.Creat();
                        break;
                    }
                }

                App.getHandlerMainThread().postDelayed(() -> canDoAnyAction = true, 1000);
            }
        };
        return listenerAdapterCartableDocumentList;
    }


    private void showUserAndRoleDialog(int ETC, int EC, int sendCode, StructureInboxDocumentDB structureInboxDocumentDB) {
        lnLoading.setVisibility(View.VISIBLE);

        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, ETC, sendCode).setTitle(Resorse.getString(R.string.title_confirm_erja)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                userAndRolesMain = structureUserAndRolesMain;
                lnLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {
                Send(ETC, EC, structureAppendREQ, structureInboxDocumentDB);
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
    private void Send(int ETC, int EC, final StructureAppendREQ structureAppendREQ, StructureInboxDocumentDB structureInboxDocumentDB) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
            DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Append, strDataREQ);
            Confirm(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode(), structureInboxDocumentDB);

        } else {
            new CartableDocumentAppendPresenter().AppendDocument(ETC, EC, structureAppendREQ, new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Append, strDataREQ);
                }

                @Override
                public void onCancel() {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Append, strDataREQ);
                }

                @Override
                public void onFinish() {
                    App.getHandlerMainThread().post(new Runnable() {
                        @Override
                        public void run() {
                            lnLoading.setVisibility(View.GONE);
                            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_send_successful), SnackBarEnum.SNACKBAR_LONG_TIME);
                            Confirm(structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode(), structureInboxDocumentDB);

                        }
                    });


                }
            });

        }
    }

    //_________________________________*****___Send___*****__________________________________


    //_________________________________*****___Confirm___*****__________________________________
    private void Confirm(int ETC, int EC, final StructureInboxDocumentDB structureInboxDocumentDB) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            StructureResponseREQ structureResponseREQ = new StructureResponseREQ(structureInboxDocumentDB.getReceiverCode());
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
            this.structureInboxDocumentDB = structureInboxDocumentDB;
            DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Response, strDataREQ);
        } else {
            new CartableDocumentConfirmPresenter().ConfirmDocument(structureInboxDocumentDB.getReceiverCode(), new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    StructureResponseREQ structureResponseREQ = new StructureResponseREQ(structureInboxDocumentDB.getReceiverCode());
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
                    DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Response, strDataREQ);
                }

                @Override
                public void onCancel() {
                    StructureResponseREQ structureResponseREQ = new StructureResponseREQ(structureInboxDocumentDB.getReceiverCode());
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
                    DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.Response, strDataREQ);
                }

                @Override
                public void onFinish() {
                    farzinCartableQuery.deletCartableDocumentAllContent(structureInboxDocumentDB.getReceiverCode());
                    adapteCartableDocument.deletItem(structureInboxDocumentDB);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_confirm_successful), SnackBarEnum.SNACKBAR_LONG_TIME);
                    lnLoading.setVisibility(View.GONE);
                    checkDocumentCount();
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    //_________________________________*****___Confirm___*****__________________________________

    //_________________________________*****___WorkFlow___*****__________________________________

    private void showWorkFlowDialog(StructureInboxDocumentDB structureInboxDocumentDB) {
        dialogWorkFlow = new DialogWorkFlow(App.CurentActivity);
        dialogWorkFlow.setListener(new ListenerDialogWorkFlow() {
            @Override
            public void onSuccess(int response) {
                lnLoading.setVisibility(View.VISIBLE);
                App.getHandlerMainThread().postDelayed(() -> continueWorkFlow(structureInboxDocumentDB.getReceiverCode(), response, structureInboxDocumentDB.getEntityTypeCode(), structureInboxDocumentDB.getEntityCode()), TimeValue.SecondsInMilli());
            }

            @Override
            public void onCancel() {

            }
        });
        dialogWorkFlow.Show();
    }

    private void continueWorkFlow(int receiverCode, int response, int ETC, int EC) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            StructureWorkFlowREQ structureWorkFlowREQ = new StructureWorkFlowREQ(receiverCode, response);
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureWorkFlowREQ);
            App.getHandlerMainThread().postDelayed(() -> DocumentOperatorAddToQueue(ETC, EC, DocumentOperatoresTypeEnum.WorkFlow, strDataREQ), TimeValue.SecondsInMilli());
        } else {
            new ContinueWorkFlowPresenter().ContinueWorkFlow(receiverCode, response, new ContinueWorkFlowListener() {
                @Override
                public void onSuccess() {
                    farzinCartableQuery.deletCartableDocumentAllContent(structureInboxDocumentDB.getReceiverCode());
                    adapteCartableDocument.deletItem(structureInboxDocumentDB);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.work_flow_continue_successful), ToastEnum.TOAST_LONG_TIME);
                    lnLoading.setVisibility(View.GONE);
                    checkDocumentCount();
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed(String message) {
                    lnLoading.setVisibility(View.GONE);
                    App.getHandlerMainThread().postDelayed(() -> new DialogShowMore(App.CurentActivity).setData(Resorse.getString(R.string.title_error), message).Creat(), TimeValue.SecondsInMilli());
                    //App.ShowMessage().ShowSnackBar(message, SnackBarEnum.SNACKBAR_INDEFINITE);
                }
            });
        }
    }
    //_________________________________*****___WorkFlow___*****__________________________________


    private void DocumentOperatorAddToQueue(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum, String strDataREQ) {

        StructureDocumentOperatorsQueueBND structureDocumentOperatorsQueueBND = new StructureDocumentOperatorsQueueBND(ETC, EC, false, documentOpratoresTypeEnum, strDataREQ);
        new FarzinCartableQuery().saveDocumentOperatorsQueue(structureDocumentOperatorsQueueBND, new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                App.getHandlerMainThread().post(() -> {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);
                    if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.Response) {
                        adapteCartableDocument.deletItem(structureInboxDocumentDB);
                        structureInboxDocumentDB = new StructureInboxDocumentDB();
                        checkDocumentCount();
                    } else if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.WorkFlow) {
                        adapteCartableDocument.deletItem(structureInboxDocumentDB);
                        structureInboxDocumentDB = new StructureInboxDocumentDB();
                        checkDocumentCount();
                    }

                });

            }

            @Override
            public void onFailed(String message) {
                tryDocumentOperatorAddToQueue(ETC, EC, documentOpratoresTypeEnum, strDataREQ);
            }

            @Override
            public void onCancel() {
                tryDocumentOperatorAddToQueue(ETC, EC, documentOpratoresTypeEnum, strDataREQ);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void checkDocumentCount() {
        int count = adapteCartableDocument.getItemCount();
        if (count > 0) {
            txtNoData.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
    }

    private void tryDocumentOperatorAddToQueue(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum, String strDataREQ) {
        App.getHandlerMainThread().postDelayed(() -> DocumentOperatorAddToQueue(ETC, EC, documentOpratoresTypeEnum, strDataREQ), FailedDelay);
    }

    private void gotoDocumentDetail(final StructureInboxDocumentDB item) {

        App.getHandlerMainThread().post(() -> {
            if (!farzinCartableQuery.IsDocumentExist(item.getReceiverCode())) {
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal());
            } else {
                StructureCartableDocumentDetailBND cartableDocumentDetailBND = new StructureCartableDocumentDetailBND(item.getEntityTypeCode(), item.getEntityCode(), item.getSendCode(), item.getReceiverCode(), item.getReceiveDate(), item.getTitle(), item.getSenderName(), item.getSenderRoleName(), item.getEntityNumber(), item.getImportEntityNumber(), item.isbInWorkFlow());
                bundleObject.putSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue(), cartableDocumentDetailBND);
                Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocumentDetail.class);
                intent.putExtras(bundleObject);
                goToActivityForResult(intent, DOCUMENTDETAILCODE);
                curentItem = item;
                lnLoading.setVisibility(View.GONE);
                farzinCartableQuery.updateCartableDocumentStatus(item.getId(), Status.READ);
                if (isFilter) {
                    adapteCartableDocument.deletItem(item);
                    checkDocumentCount();
                } else {
                    item.setStatus(Status.READ);
                    item.setRead(true);
                    adapteCartableDocument.updateItem(item);
                }
            }
        });
    }

    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
    }

    private void continuegetData() {
        final Loading loading = new Loading(App.CurentActivity).Creat();
        loading.Show();
        List<StructureInboxDocumentDB> inboxDocumentDBS = new ArrayList<>();
        if (isFilter) {
            inboxDocumentDBS = farzinCartableDocumentListPresenter.getFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            inboxDocumentDBS = farzinCartableDocumentListPresenter.getFromLocal(actionCode, null, start, COUNT);
        }

        structureInboxDocumentsDB.addAll(inboxDocumentDBS);
        if (inboxDocumentDBS.size() > 0) {
            start = start + inboxDocumentDBS.size();
            adapteCartableDocument.updateData(-1, inboxDocumentDBS);
            canLoading = true;
        } else {
            canLoading = false;
        }
        loading.Hide();
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.filter_toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        actionfilter = menu.findItem(R.id.action_filter);
        actionfilter.setOnMenuItemClickListener(menuItem -> {
            if (lnLoading.getVisibility() == View.GONE) {
                isFilter = !isFilter;
                filterData();
            }

            return false;
        });
        initData(actionCode);
        return super.onPrepareOptionsMenu(menu);
    }

    private void filterData() {
        lnLoading.setVisibility(View.VISIBLE);
        start = 0;
        canLoading = false;
        structureInboxDocumentsDB.clear();
        if (isFilter) {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, Status.UnRead, start, COUNT);
        } else {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            structureInboxDocumentsDB = farzinCartableDocumentListPresenter.getFromLocal(actionCode, start, COUNT);
        }
        start = structureInboxDocumentsDB.size();
        canLoading = true;
        srlRefresh.setRefreshing(false);
        lnLoading.setVisibility(View.GONE);
        if (structureInboxDocumentsDB.size() > 0) {
            txtNoData.setVisibility(View.GONE);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }
        adapteCartableDocument.updateData(structureInboxDocumentsDB);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DOCUMENTDETAILCODE) {
            if (resultCode == TAEED.getValue()) {
                adapteCartableDocument.deletItem(curentItem);
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
