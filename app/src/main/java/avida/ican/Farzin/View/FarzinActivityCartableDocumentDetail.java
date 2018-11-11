package avida.ican.Farzin.View;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byox.drawview.enums.DrawingCapture;
import com.byox.drawview.enums.DrawingMode;
import com.byox.drawview.views.DrawView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentTaeedQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenListener;
import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenQueueQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentTaeedPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.OpticalPenPresenter;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHameshList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHistoryList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentZanjireMadrak;
import avida.ican.Farzin.View.Interface.ListenerFile;
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
import avida.ican.Ican.View.Interface.OpticalPenDialogListener;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import static avida.ican.Farzin.View.Enum.CartableDocumentDetailActionsEnum.TAEED;

public class FarzinActivityCartableDocumentDetail extends BaseToolbarActivity {
    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
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
    private int Etc;
    private int Ec;
    private FragmentManager mfragmentManager;
    private StructureCartableDocumentDetailBND cartableDocumentDetailBND;
    private long FailedDelay = TimeValue.SecondsInMilli() * 3;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private long FAILED_DELAY = TimeValue.SecondsInMilli() * 3;
    DialogOpticalPen dialogOpticalPen;

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

        imTaeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lnLoading.setVisibility(View.VISIBLE);
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    TaeedAddToQueue(cartableDocumentDetailBND.getReceiverCode());
                } else {
                    new CartableDocumentTaeedPresenter().TaeedDocument(cartableDocumentDetailBND.getReceiverCode(), new TaeedListener() {
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
                            Intent returnIntent = new Intent();
                            setResult(TAEED.getValue(), returnIntent);
                            Finish(App.CurentActivity);

                        }
                    });

                }
            }
        });


        initViewPagerFragment();
    }


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

    private void TaeedAddToQueue(final int receiveCode) {
        new FarzinCartableQuery().saveCartableDocumentTaeedQueue(receiveCode, new CartableDocumentTaeedQueueQuerySaveListener() {
            @Override
            public void onSuccess(int receiveCode) {
                lnLoading.setVisibility(View.GONE);
            }

            @Override
            public void onExisting() {
                lnLoading.setVisibility(View.GONE);
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

    private void OpticalPenAddToQueue(final StructureOpticalPenREQ opticalPenREQ) {
        new FarzinCartableQuery().saveOpticalPenQueue(opticalPenREQ, new OpticalPenQueueQuerySaveListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onExisting() {
                lnLoading.setVisibility(View.GONE);
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
        adapter.addFrag(fragmentCartableHameshList, R.string.title_list_hamesh);
        adapter.addFrag(fragmentCartableHistoryList, R.string.title_gardesh_madrak);
        adapter.addFrag(fragmentZanjireMadrak, R.string.title_zanjireh_madrak);
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
    }
}
