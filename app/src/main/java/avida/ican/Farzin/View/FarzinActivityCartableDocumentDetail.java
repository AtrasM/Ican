package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentTaeedQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableSendQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenListener;
import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.SendListener;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendToServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentTaeedServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.OpticalPenPresenter;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableDocumentContent;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHameshList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHistoryList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentZanjireMadrak;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Model.Structure.StructureOpticalPen;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Base64EncodeDecodeFile;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogOpticalPen;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
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
    @BindView(R.id.img_taeed)
    ImageView imTaeed;
    @BindView(R.id.img_taeed_erja)
    ImageView imgTaeedErja;
    @BindView(R.id.img_erja)
    ImageView imgErja;
    @BindView(R.id.img_ghalam_nory)
    ImageView imgOpticalPen;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
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
    private DialogOpticalPen dialogOpticalPen;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private DialogUserAndRole dialogUserAndRole;

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
        mfragmentManager = getSupportFragmentManager();
        Bundle bundleObject = getIntent().getExtras();
        cartableDocumentDetailBND = (StructureCartableDocumentDetailBND) bundleObject.getSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue());
        initTollBar(Title + " " + cartableDocumentDetailBND.getEntityNumber());
        initView();
    }

    private void initView() {
        Etc = cartableDocumentDetailBND.getETC();
        Ec = cartableDocumentDetailBND.getEC();
        txtName.setText(cartableDocumentDetailBND.getSenderName());
        txtRoleName.setText("[ " + cartableDocumentDetailBND.getSenderRoleName() + " ]");
        String[] splitDateTime = CustomFunction.MiladyToJalaly(cartableDocumentDetailBND.getReceiveDate().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        txtDate.setText(date);
        txtTime.setText(time);
        if (cartableDocumentDetailBND.getTitle() != null) {
            txtSubject.setText(cartableDocumentDetailBND.getTitle());
        } else {
            txtSubject.setVisibility(View.GONE);
        }
        imgOpticalPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOpticalPen = new DialogOpticalPen(App.CurentActivity);
                dialogOpticalPen.setOnListener(new OpticalPenDialogListener() {
                    @Override
                    public void onSuccess(StructureOpticalPen structureOpticalPen) {
                        StructureOpticalPenREQ structureOpticalPenREQ = new StructureOpticalPenREQ(Etc, Ec, structureOpticalPen.getbFile(), structureOpticalPen.getFileExtension(), structureOpticalPen.getTitle(), false);
                        saveDrawable(structureOpticalPenREQ);
                        fragmentCartableHameshList.reGetData();
                        dialogOpticalPen.dismiss();
                    }

                    @Override
                    public void onFaild() {
                        dialogOpticalPen.dismiss();
                    }

                    @Override
                    public void onCancel() {
                        dialogOpticalPen.dismiss();
                    }
                }).Show();
            }
        });
        imgErja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserAndRoleDialog(false);
            }
        });
        imgTaeedErja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserAndRoleDialog(true);
            }
        });
        imTaeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnLoading.setVisibility(View.VISIBLE);
                Taeed();
            }
        });


        initViewPagerFragment();
    }

    //_________________________________*****___Taeed___*****__________________________________
    private void Taeed() {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            TaeedAddToQueue(cartableDocumentDetailBND.getReceiverCode());
        } else {
            new CartableDocumentTaeedServerPresenter().TaeedDocument(cartableDocumentDetailBND.getReceiverCode(), new TaeedListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    TaeedAddToQueue(cartableDocumentDetailBND.getReceiverCode());
                }

                @Override
                public void onCancel() {
                    TaeedAddToQueue(cartableDocumentDetailBND.getReceiverCode());
                }

                @Override
                public void onFinish() {
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.document_taeed_successfull), ToastEnum.TOAST_LONG_TIME);
                    Intent returnIntent = new Intent();
                    setResult(TAEED.getValue(), returnIntent);
                    Finish(App.CurentActivity);

                }
            });

        }
    }

    private void TaeedAddToQueue(final int receiveCode) {
        new FarzinCartableQuery().saveCartableDocumentTaeedQueue(receiveCode, new CartableDocumentTaeedQueueQuerySaveListener() {
            @Override
            public void onSuccess(int receiveCode) {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

            }

            @Override
            public void onExisting() {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

            }

            @Override
            public void onFailed(String message) {
                tryTaeedAddToQueue(receiveCode);
            }

            @Override
            public void onCancel() {
                tryTaeedAddToQueue(receiveCode);
            }
        });
    }

    private void tryTaeedAddToQueue(final int receiveCode) {
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TaeedAddToQueue(receiveCode);
            }
        }, FailedDelay);
    }
    //_________________________________*****___Taeed___*****__________________________________


    //_________________________________*****___Send___*****__________________________________
    private void Send(final StructureAppendREQ structureAppendREQ, final boolean TaeedAndSend) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            SendAddToQueue(structureAppendREQ);
            if (TaeedAndSend) {
                Taeed();
            }

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
                    lnLoading.setVisibility(View.GONE);
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.document_send_successfull), SnackBarEnum.SNACKBAR_LONG_TIME);
                    if (TaeedAndSend) {
                        Taeed();
                    }
                }
            });

        }
    }

    private void SendAddToQueue(final StructureAppendREQ structureAppendREQ) {
        new FarzinCartableQuery().saveCartableSendQueue(structureAppendREQ, new CartableSendQuerySaveListener() {

            @Override
            public void onSuccess() {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

            }

            @Override
            public void onExisting() {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

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

    private void saveDrawable(final StructureOpticalPenREQ structureOpticalPenREQ) {
        lnLoading.setVisibility(View.VISIBLE);
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            OpticalPenAddToQueue(structureOpticalPenREQ);
        } else {
            new OpticalPenPresenter().CallRequest(structureOpticalPenREQ, new OpticalPenListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    OpticalPenAddToQueue(structureOpticalPenREQ);
                }

                @Override
                public void onCancel() {
                    OpticalPenAddToQueue(structureOpticalPenREQ);
                }

                @Override
                public void onFinish() {
                    lnLoading.setVisibility(View.GONE);

                }
            });

        }
    }

    private void OpticalPenAddToQueue(final StructureOpticalPenREQ opticalPenREQ) {
        new FarzinCartableQuery().saveOpticalPenQueue(opticalPenREQ, new OpticalPenQueueQuerySaveListener() {

            @Override
            public void onSuccess() {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

            }

            @Override
            public void onExisting() {
                lnLoading.setVisibility(View.GONE);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.the_command_was_placed_in_the_queue), SnackBarEnum.SNACKBAR_LONG_TIME);

            }

            @Override
            public void onFailed(String message) {
                tryOpticalPenAddToQueue(opticalPenREQ);
            }

            @Override
            public void onCancel() {
                tryOpticalPenAddToQueue(opticalPenREQ);
            }
        });
    }

    private void tryOpticalPenAddToQueue(final StructureOpticalPenREQ opticalPenREQ) {
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OpticalPenAddToQueue(opticalPenREQ);
            }
        }, FailedDelay);
    }


    private void initViewPagerFragment() {

        fragmentCartableHistoryList = new FragmentCartableHistoryList().newInstance(App.CurentActivity, Etc, Ec);
        fragmentCartableDocumentContent = new FragmentCartableDocumentContent().newInstance(App.CurentActivity, Etc, Ec);
        fragmentZanjireMadrak = new FragmentZanjireMadrak().newInstance(App.CurentActivity, Etc, Ec, new ListenerFile() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                checkFile(structureAttach);
            }
        });
        fragmentCartableHameshList = new FragmentCartableHameshList().newInstance(App.CurentActivity, Etc, Ec, new ListenerFile() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                checkFile(structureAttach);
            }
        });

        initTab();
    }

    private void checkFile(StructureAttach structureAttach) {
        byte[] aByte = new Base64EncodeDecodeFile().DecodeBase64ToByte(structureAttach.getBase64File());
        file = new CustomFunction(App.CurentActivity).OpenFile(aByte, structureAttach.getName(), structureAttach.getFileExtension());
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentCartableDocumentContent, R.string.title_document_content);
        adapter.addFrag(fragmentCartableHameshList, R.string.title_list_hamesh);
        adapter.addFrag(fragmentCartableHistoryList, R.string.title_gardesh_madrak);
        adapter.addFrag(fragmentZanjireMadrak, R.string.title_zanjireh_madrak);
        mViewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(mViewPager);
    }


    private void showUserAndRoleDialog(final boolean TaeedAndSend) {

        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, Etc, Ec).setTitle(Resorse.getString(R.string.title_send)).init(mfragmentManager, (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<StructureUserAndRoleDB>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                userAndRolesMain = structureUserAndRolesMain;
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {
                Send(structureAppendREQ, TaeedAndSend);
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
}
