package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.ContinueWorkFlowListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureResponseREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureSignDocumentREQ;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureOpticalPenREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureWorkFlowREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentSignaturesPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentConfirmPresenter;
import avida.ican.Farzin.Presenter.Cartable.ContinueWorkFlowPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.OpticalPenPresenter;
import avida.ican.Farzin.Presenter.Cartable.SignDocumentPresenter;
import avida.ican.Farzin.View.Dialog.DialogShowMore;
import avida.ican.Farzin.View.Dialog.DialogSignature;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Dialog.DialogWorkFlow;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableDocumentContent;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHameshList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHistoryList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentZanjireMadrak;
import avida.ican.Farzin.View.Interface.Cartable.ListenerDialogSignature;
import avida.ican.Farzin.View.Interface.Cartable.ListenerDialogWorkFlow;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Model.Structure.StructureOpticalPen;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogDrawingView;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.Ican.View.Interface.OpticalPenDialogListener;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import static avida.ican.Farzin.View.Enum.CartableDocumentDetailActionsEnum.TAEED;

public class FarzinActivityCartableDocumentDetail extends BaseToolbarActivity {
    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.mview_pager)
    ViewPager mViewPager;
    @BindView(R.id.txt_subject)
    TextView txtSubject;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_role_name)
    TextView txtRoleName;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.img_confirm)
    ImageView imgConfirm;
    @BindView(R.id.ln_confirm)
    LinearLayout lnConfirm;
    @BindView(R.id.img_confirm_erja)
    ImageView imgConfirmErja;
    @BindView(R.id.ln_confirm_erja)
    LinearLayout lnConfirmErja;
    @BindView(R.id.ln_erja)
    LinearLayout lnErja;
    @BindView(R.id.img_erja)
    ImageView imgErja;
    @BindView(R.id.ln_inworkflow)
    LinearLayout lnInworkflow;
    @BindView(R.id.img_inworkflow)
    ImageView imgInworkflow;
    @BindView(R.id.img_ghalam_nory)
    ImageView imgOpticalPen;
    @BindView(R.id.img_signature)
    ImageView imgSignature;
    @BindView(R.id.img_scale)
    ImageView imgScale;
    @BindView(R.id.ln_signature)
    LinearLayout lnSignature;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.ln_header)
    LinearLayout lnHeader;
    @BindView(R.id.ln_top)
    LinearLayout lnTop;
    @BindView(R.id.frm_view_pager)
    FrameLayout frmViewPager;
    @BindString(R.string.TitleCartableDocumentDetail)
    String Title;

    private File file;
    private Bundle bundleObject = new Bundle();
    private FragmentCartableHameshList fragmentCartableHameshList;
    private FragmentZanjireMadrak fragmentZanjireMadrak;
    private FragmentCartableHistoryList fragmentCartableHistoryList;
    private FragmentCartableDocumentContent fragmentCartableDocumentContent;
    private int Etc;
    private int Ec;
    private FragmentManager mfragmentManager;
    private StructureCartableDocumentDetailBND cartableDocumentDetailBND;
    private long FailedDelay = TimeValue.SecondsInMilli() * 3;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private long FAILED_DELAY = TimeValue.SecondsInMilli() * 3;
    private DialogDrawingView dialogDrawingView;
    private DialogSignature dialogSignature;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private DialogUserAndRole dialogUserAndRole;
    private int sendCode = -1;
    private CartableDocumentSignaturesPresenter cartableDocumentSignaturesPresenter;
    private boolean isImportEntityNumber = false;
    private boolean isAccessToSignature = true;
    private int AttachFileCODE = 002;
    private boolean windowSizeIsMax = false;
    private Animator animator;
    private DialogWorkFlow dialogWorkFlow;

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
        return R.layout.farzin_activity_cartable_document_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.canBack = true;
        mfragmentManager = getSupportFragmentManager();
        animator = new Animator(App.CurentActivity);
        Bundle bundleObject = getIntent().getExtras();
        cartableDocumentDetailBND = (StructureCartableDocumentDetailBND) bundleObject.getSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue());
        Log.i("LOG", "Etc= " + cartableDocumentDetailBND.getETC() + " EC= " + cartableDocumentDetailBND.getEC());
        if (cartableDocumentDetailBND.getImportEntityNumber() != null && !cartableDocumentDetailBND.getImportEntityNumber().isEmpty()) {
            Title = Title + " " + cartableDocumentDetailBND.getImportEntityNumber();
            isImportEntityNumber = true;
        } else {
            Title = Title + " " + cartableDocumentDetailBND.getEntityNumber();
            isImportEntityNumber = false;
        }

        initTollBar(Title);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        Etc = cartableDocumentDetailBND.getETC();
        Ec = cartableDocumentDetailBND.getEC();


        if (cartableDocumentDetailBND.isbInWorkFlow()) {
            lnConfirm.setVisibility(View.GONE);
            lnConfirmErja.setVisibility(View.GONE);
            lnErja.setVisibility(View.GONE);
            lnInworkflow.setVisibility(View.VISIBLE);
        } else {
            lnConfirm.setVisibility(View.VISIBLE);
            lnConfirmErja.setVisibility(View.VISIBLE);
            lnErja.setVisibility(View.VISIBLE);
            lnInworkflow.setVisibility(View.GONE);
        }

        if (cartableDocumentDetailBND.isFromEntityDependency()) {
            lnHeader.setVisibility(View.GONE);
            lnSignature.setVisibility(View.GONE);
            lnConfirmErja.setVisibility(View.GONE);
            lnConfirm.setVisibility(View.GONE);
            imgScale.setVisibility(View.GONE);
        } else {
            if (!farzinCartableQuery.IsDocumentExist(cartableDocumentDetailBND.getReceiverCode())) {
                Finish(App.CurentActivity);
            }

            lnHeader.setVisibility(View.VISIBLE);
            lnSignature.setVisibility(View.VISIBLE);
            if (!cartableDocumentDetailBND.isbInWorkFlow()) {
                lnConfirmErja.setVisibility(View.VISIBLE);
                lnConfirm.setVisibility(View.VISIBLE);
            }


            App.curentDocumentShowing = cartableDocumentDetailBND.getReceiverCode();
            sendCode = cartableDocumentDetailBND.getSendCode();
            txtName.setText(cartableDocumentDetailBND.getSenderName());
            txtRoleName.setText("[ " + cartableDocumentDetailBND.getSenderRoleName() + " ]");
            String[] splitDateTime = CustomFunction.MiladyToJalaly(cartableDocumentDetailBND.getReceiveDate().toString()).split(" ");
            final String date = splitDateTime[0];
            final String time = splitDateTime[1];
            txtDate.setText(date);
            txtTime.setText(time);

            cartableDocumentSignaturesPresenter = new CartableDocumentSignaturesPresenter(Etc);
            List<StructureCartableDocumentSignatureDB> structureCartableDocumentSignatureDBS = cartableDocumentSignaturesPresenter.GetDocumentSignatures();
            if (structureCartableDocumentSignatureDBS != null && structureCartableDocumentSignatureDBS.size() > 0 && !structureCartableDocumentSignatureDBS.get(0).isEmpety() && !structureCartableDocumentSignatureDBS.get(0).getFN().isEmpty()) {
                if (farzinCartableQuery.isDocumentOperatorNotSendedExist(Etc, Ec, DocumentOperatoresTypeEnum.SignDocument)) {
                    isAccessToSignature = false;
                    //lnSignature.setVisibility(View.GONE);
                } else {
                    isAccessToSignature = true;
                    //lnSignature.setVisibility(View.VISIBLE);
                }

            } else {
                isAccessToSignature = false;
            }

            if (cartableDocumentDetailBND.getTitle() != null && !cartableDocumentDetailBND.getTitle().isEmpty()) {
                new CustomFunction(App.CurentActivity).setHtmlText(txtSubject, cartableDocumentDetailBND.getTitle());
            } else {
                txtSubject.setVisibility(View.GONE);
            }
            imgScale.setOnClickListener(view -> {
                windowSizeIsMax = !windowSizeIsMax;
                if (windowSizeIsMax) {
                    imgScale.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_down));
                    animator.slideOutToUpFast(lnTop);
                    App.getHandlerMainThread().postDelayed(() -> {
                        lnTop.setVisibility(View.GONE);
                    }, 50);

                } else {
                    imgScale.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_up));
                    animator.slideInFromUpFast(lnTop);
                    lnTop.setVisibility(View.VISIBLE);
                }

            });

            imgScale.callOnClick();
        }


        imgInworkflow.setOnClickListener(view -> showWorkFlowDialog());

        imgOpticalPen.setOnClickListener(view -> {
            dialogDrawingView = new DialogDrawingView(App.CurentActivity);
            dialogDrawingView.setOnListener(new OpticalPenDialogListener() {
                @Override
                public void onSuccess(StructureOpticalPen structureOpticalPen) {
                    StructureOpticalPenREQ structureOpticalPenREQ = new StructureOpticalPenREQ(structureOpticalPen.getbFile(), structureOpticalPen.getFileExtension(), structureOpticalPen.getTitle(), false);
                    saveDrawable(structureOpticalPenREQ);
                    fragmentCartableHameshList.reGetData();
                    dialogDrawingView.dismiss();
                }

                @Override
                public void onFaild() {
                    dialogDrawingView.dismiss();
                }

                @Override
                public void onCancel() {
                    dialogDrawingView.dismiss();
                }
            }).Show();
        });
        imgSignature.setOnClickListener(view -> {
            if (isAccessToSignature) {
                showSignatureDialog();
            } else {
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.NotAccessToSignature), SnackBarEnum.SNACKBAR_LONG_TIME);
            }

        });
        imgErja.setOnClickListener(view -> showUserAndRoleDialog(false));
        imgConfirmErja.setOnClickListener(view -> showUserAndRoleDialog(true));
        new CustomFunction(App.CurentActivity).ChengeDrawableColorAndSetToImageView(imgConfirm, R.drawable.ic_taeed, R.color.color_document_detail_actions);
        imgConfirm.setOnClickListener(view -> new DialogQuestion(App.CurentActivity).setTitle(Resorse.getString(R.string.title_question_confirm)).setOnListener(new ListenerQuestion() {
            @Override
            public void onSuccess() {
                lnLoading.setVisibility(View.VISIBLE);
                Confirm();
            }

            @Override
            public void onCancel() {

            }
        }).Show());

        initViewPagerFragment();
    }

    private void initViewPagerFragment() {
        fragmentCartableDocumentContent = new FragmentCartableDocumentContent().newInstance(App.CurentActivity, Etc, Ec, isImportEntityNumber, structureAttach -> checkFile(structureAttach));
        fragmentZanjireMadrak = new FragmentZanjireMadrak().newInstance(App.CurentActivity, Etc, Ec, Title, structureAttach -> checkFile(structureAttach));
        fragmentCartableHameshList = new FragmentCartableHameshList().newInstance(App.CurentActivity, Etc, Ec, structureAttach -> checkFile(structureAttach));
        fragmentCartableHistoryList = new FragmentCartableHistoryList().newInstance(App.CurentActivity, Etc, Ec);
        initTab();
    }

    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        App.fragmentStacks.remove("documentContent");
        Fragment fragment = fragmentCartableDocumentContent;
        App.fragmentStacks.put("documentContent", new Stack<Fragment>());
        App.fragmentStacks.get("documentContent").push(fragment);
        adapter.addFrag(fragmentCartableDocumentContent, R.string.title_document_content);
        adapter.addFrag(fragmentCartableHameshList, R.string.title_list_hamesh);
        adapter.addFrag(fragmentCartableHistoryList, R.string.title_gardesh_madrak);
        adapter.addFrag(fragmentZanjireMadrak, R.string.title_zanjireh_madrak);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);//where 3 is the amount of pages to keep in memory on either side of the current page.
        smartTabLayout.setViewPager(mViewPager);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 1: {
                        fragmentCartableHameshList.checkQueue();
                        break;
                    }
                    case 2: {
                        fragmentCartableHistoryList.checkQueue();
                        break;
                    }
                    case 3: {
                        fragmentZanjireMadrak.checkQueue();
                        break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1: {
                        fragmentCartableHameshList.checkQueue();
                        break;
                    }
                    case 2: {
                        fragmentCartableHistoryList.checkQueue();
                        break;
                    }
                    case 3: {
                        fragmentZanjireMadrak.checkQueue();
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //_________________________________*****___Confirm___*****__________________________________
    private void Confirm() {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            StructureResponseREQ structureResponseREQ = new StructureResponseREQ(cartableDocumentDetailBND.getReceiverCode());
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
            DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Response, strDataREQ);
        } else {
            new CartableDocumentConfirmPresenter().ConfirmDocument(cartableDocumentDetailBND.getReceiverCode(), new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    StructureResponseREQ structureResponseREQ = new StructureResponseREQ(cartableDocumentDetailBND.getReceiverCode());
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Response, strDataREQ);
                }

                @Override
                public void onCancel() {
                    StructureResponseREQ structureResponseREQ = new StructureResponseREQ(cartableDocumentDetailBND.getReceiverCode());
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureResponseREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Response, strDataREQ);
                }

                @Override
                public void onFinish() {
                    farzinCartableQuery.deletCartableDocumentAllContent(cartableDocumentDetailBND.getReceiverCode());
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.document_confirm_successful), ToastEnum.TOAST_LONG_TIME);
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent returnIntent = new Intent();
                    setResult(TAEED.getValue(), returnIntent);
                    Finish(App.CurentActivity);

                }
            });

        }
    }

 /*   private void ConfirmAddToQueue(final int receiveCode) {
        App.getFarzinBroadCastReceiver().runCartableDocumentConfirmQueueService();
        new FarzinCartableQuery().saveCartableDocumentConfirmQueue(receiveCode, new CartableDocumentConfirmQueueQuerySaveListener() {
            @Override
            public void onSuccess(int receiveCode) {
                App.getHandlerMainThread().post(() -> {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                    Intent returnIntent = new Intent();
                    setResult(TAEED.getValue(), returnIntent)
                    ;
                    Finish(App.CurentActivity);
                });

            }

            @Override
            public void onExisting() {
                App.getHandlerMainThread().post(() -> {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                    Intent returnIntent = new Intent();
                    setResult(TAEED.getValue(), returnIntent);
                    Finish(App.CurentActivity);
                });
            }

            @Override
            public void onFailed(String message) {
                tryConfirmAddToQueue(receiveCode);
            }

            @Override
            public void onCancel() {
                tryConfirmAddToQueue(receiveCode);
            }
        });
    }

    private void tryConfirmAddToQueue(final int receiveCode) {
        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConfirmAddToQueue(receiveCode);
            }
        }, FailedDelay);
    }*/
    //_________________________________*****___Confirm___*****__________________________________


    //_________________________________*****___Send___*****__________________________________
    private void Send(final StructureAppendREQ structureAppendREQ, final boolean ConfirmAndSend) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
            DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Append, strDataREQ);
            if (ConfirmAndSend) {
                Confirm();
            }

        } else {
            new CartableDocumentAppendPresenter().AppendDocument(Etc, Ec, structureAppendREQ, new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Append, strDataREQ);
                }

                @Override
                public void onCancel() {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.Append, strDataREQ);
                }

                @Override
                public void onFinish() {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_send_successful), SnackBarEnum.SNACKBAR_LONG_TIME);
                    if (ConfirmAndSend) {
                        Confirm();
                    }
                }
            });
        }
    }


    //_________________________________*****___Send___*****__________________________________


    private void saveDrawable(final StructureOpticalPenREQ structureOpticalPenREQ) {
        lnLoading.setVisibility(View.VISIBLE);
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureOpticalPenREQ);
            DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.AddHameshOpticalPen, strDataREQ);
        } else {
            new OpticalPenPresenter().CallRequest(Etc, Ec, structureOpticalPenREQ, new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureOpticalPenREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.AddHameshOpticalPen, strDataREQ);
                }

                @Override
                public void onCancel() {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureOpticalPenREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.AddHameshOpticalPen, strDataREQ);
                }

                @Override
                public void onFinish() {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.send_opticalpen_successful), ToastEnum.TOAST_LONG_TIME);
                    lnLoading.setVisibility(View.GONE);
                }
            });

        }
    }

    private void SignDocument(final StructureSignDocumentREQ structureSignDocumentREQ) {
        lnLoading.setVisibility(View.VISIBLE);
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureSignDocumentREQ);
            DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.SignDocument, strDataREQ);
        } else {
            new SignDocumentPresenter().SignDocument(Etc, Ec, structureSignDocumentREQ.getENs(), new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.sign_document_successfull), ToastEnum.TOAST_LONG_TIME);
                    lnLoading.setVisibility(View.GONE);
                    farzinCartableQuery.deletCartableDocumentContent(Etc, Ec);
                    fragmentCartableDocumentContent.reGetData();
                }

                @Override
                public void onFailed(String message) {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureSignDocumentREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.SignDocument, strDataREQ);
                }

                @Override
                public void onCancel() {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureSignDocumentREQ);
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.SignDocument, strDataREQ);
                }

                @Override
                public void onFinish() {

                }


            });

        }
    }

    private void DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum documentOpratoresTypeEnum, String strDataREQ) {
        StructureDocumentOperatorsQueueBND structureDocumentOperatorsQueueBND = new StructureDocumentOperatorsQueueBND(Etc, Ec, false, documentOpratoresTypeEnum, strDataREQ);
        new FarzinCartableQuery().saveDocumentOperatorsQueue(structureDocumentOperatorsQueueBND, new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                App.getHandlerMainThread().post(() -> {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);
                    if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.Response) {
                        Intent returnIntent = new Intent();
                        setResult(TAEED.getValue(), returnIntent);
                        Finish(App.CurentActivity);
                    }
                    if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.WorkFlow) {
                        Intent returnIntent = new Intent();
                        setResult(TAEED.getValue(), returnIntent);
                        Finish(App.CurentActivity);
                    } else if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.AddHameshOpticalPen) {
                        fragmentCartableHameshList.checkQueue();
                    } else if (documentOpratoresTypeEnum == DocumentOperatoresTypeEnum.Append) {
                        fragmentCartableHistoryList.checkQueue();
                    }

                });

            }

            @Override
            public void onFailed(String message) {
                tryDocumentOperatorAddToQueue(documentOpratoresTypeEnum, strDataREQ);
            }

            @Override
            public void onCancel() {
                tryDocumentOperatorAddToQueue(documentOpratoresTypeEnum, strDataREQ);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void tryDocumentOperatorAddToQueue(DocumentOperatoresTypeEnum documentOpratoresTypeEnum, String strDataREQ) {
        App.getHandlerMainThread().postDelayed(() -> DocumentOperatorAddToQueue(documentOpratoresTypeEnum, strDataREQ), FailedDelay);
    }

    private void showUserAndRoleDialog(final boolean ConfirmAndSend) {
        String title = "";
        if (ConfirmAndSend) {
            title = Resorse.getString(R.string.title_confirm_erja);
        } else {
            title = Resorse.getString(R.string.title_erja);
        }

        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, Etc, sendCode).setTitle(title).init(mfragmentManager, (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<StructureUserAndRoleDB>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                userAndRolesMain = structureUserAndRolesMain;
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {
                Send(structureAppendREQ, ConfirmAndSend);
            }

            @Override
            public void onFailed() {

            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCancel(final List<StructureUserAndRoleDB> tmpItemSelect) {

            }

        });

    }

    private void showSignatureDialog() {
        dialogSignature = new DialogSignature(App.CurentActivity, Etc).setTitle(Resorse.getString(R.string.title_signature));
        dialogSignature.setOnListener(new ListenerDialogSignature() {
            @Override
            public void onSuccess(List<StructureCartableDocumentSignatureDB> structureCartableDocumentSignaturesDB) {
                ArrayList<String> ENs = new ArrayList<>();
                for (int i = 0; i < structureCartableDocumentSignaturesDB.size(); i++) {
                    ENs.add(structureCartableDocumentSignaturesDB.get(i).getEN());
                }
                StructureSignDocumentREQ structureSignDocumentREQ = new StructureSignDocumentREQ(ENs);
                SignDocument(structureSignDocumentREQ);
                dialogSignature.dismiss();
            }

            @Override
            public void onCancel() {

            }
        }).Show();

    }

    private void showWorkFlowDialog() {
        dialogWorkFlow = new DialogWorkFlow(App.CurentActivity);
        dialogWorkFlow.setListener(new ListenerDialogWorkFlow() {
            @Override
            public void onSuccess(int response) {
                lnLoading.setVisibility(View.VISIBLE);
                App.getHandlerMainThread().postDelayed(() -> continueWorkFlow(cartableDocumentDetailBND.getReceiverCode(), response), TimeValue.SecondsInMilli());
            }

            @Override
            public void onCancel() {

            }
        });
        dialogWorkFlow.Show();
    }

    private void continueWorkFlow(int receiverCode, int response) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            StructureWorkFlowREQ structureWorkFlowREQ = new StructureWorkFlowREQ(receiverCode, response);
            String strDataREQ = new CustomFunction().ConvertObjectToString(structureWorkFlowREQ);
            App.getHandlerMainThread().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DocumentOperatorAddToQueue(DocumentOperatoresTypeEnum.WorkFlow, strDataREQ);
                }
            }, TimeValue.SecondsInMilli());
        } else {
            new ContinueWorkFlowPresenter().ContinueWorkFlow(receiverCode, response, new ContinueWorkFlowListener() {
                @Override
                public void onSuccess() {
                    farzinCartableQuery.deletCartableDocumentAllContent(receiverCode);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.work_flow_continue_successful), ToastEnum.TOAST_LONG_TIME);
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent returnIntent = new Intent();
                    setResult(TAEED.getValue(), returnIntent);
                    Finish(App.CurentActivity);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AttachFileCODE && resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(3);
            fragmentZanjireMadrak.reGetData();
        }
    }

}
