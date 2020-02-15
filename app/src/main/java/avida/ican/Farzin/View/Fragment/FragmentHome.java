package avida.ican.Farzin.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentDataListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.ActivityCreateDocument;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterCartableAction;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterCartableActionPin;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocument;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableAction;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogPin_unPin;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerPin_unPin;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.getActivityFromStackMap;
import static avida.ican.Ican.BaseActivity.goToActivity;

public class FragmentHome extends BaseFragment {
    @BindView(R.id.txt_all_cartable_document_count)
    TextView txtAllCartableDocumentCount;
    @BindView(R.id.txt_all_unread_message_count)
    TextView txtAllUnreadMessageCount;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_all_cartable_document)
    LinearLayout lnAllCartableDocument;
    @BindView(R.id.ln_all_unread_message)
    LinearLayout lnAllUnreadMessage;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.ln_main)
    LinearLayout lnMain;
    @BindView(R.id.ln_rcv)
    LinearLayout lnRcv;
    @BindView(R.id.ln_top)
    LinearLayout lnTop;
    @BindView(R.id.rcv_action)
    RecyclerView rcvAction;
    @BindView(R.id.rcv_pin)
    RecyclerView rcvPin;
    @BindView(R.id.frm_rcv_pin)
    FrameLayout frmRcvPin;
    @BindView(R.id.img_scale)
    ImageView imgScale;
    @BindView(R.id.fab_create_document)
    FloatingActionButton fabCreateDocument;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;

    private Animator animator;
    private boolean windowSizeIsMax = false;
    private ListenerAdapterCartableAction listenerAdapterCartableAction;
    private AdapterCartableAction adapterCartableAction;
    private AdapterCartableActionPin adapterCartableActionPin;
    private FragmentManager mfragmentManager;
    private String Tag = "";
    private ArrayList<StructureCartableAction> structureCartableActions = new ArrayList<>();
    private ArrayList<StructureCartableAction> structureCartableActionsPin = new ArrayList<>();
    private Bundle bundleObject = new Bundle();
    private DialogPin_unPin dialogPin_unPin;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private long cartableDocumentCount = 0;
    private long unreadMessageCount = 0;
    private FarzinActivityMain activityMain;
    private FarzinCartableDocumentListPresenter farzinCartableDocumentListPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_home;
    }

    public FragmentHome() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animator = new Animator(App.CurentActivity);
        Activity activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
        if (activity != null) {
            activityMain = (FarzinActivityMain) activity;
        }
        setRetainInstance(true);
    }

    public FragmentHome newInstance(FragmentManager fragmentManager) {
        mfragmentManager = fragmentManager;

        return this;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lnLoading.setVisibility(View.VISIBLE);
        srlRefresh.setOnRefreshListener(() -> reGetDataFromServer());

        lnAllCartableDocument.setOnClickListener(view12 -> {
            if (activityMain != null && cartableDocumentCount > 0) {
                activityMain.selectCartableDocumentFragment();
            }
        });
        lnAllUnreadMessage.setOnClickListener(view1 -> {
            if (activityMain != null && unreadMessageCount > 0) {
                activityMain.selectMessageFragment(true);

            }
        });

        fabCreateDocument.setOnClickListener(view13 -> goToActivity(ActivityCreateDocument.class));
        imgScale.setOnClickListener(view14 -> {
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
        initCartableDocumentListPresenter();
        initData();
    }

    private void initCartableDocumentListPresenter() {
        farzinCartableDocumentListPresenter = new FarzinCartableDocumentListPresenter(App.CurentActivity, new CartableDocumentDataListener() {
            @Override
            public void newData() {
                App.CurentActivity.runOnUiThread(() -> {
                    reGetDataFromLocal();
                    lnLoading.setVisibility(View.GONE);
                    srlRefresh.setRefreshing(false);
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reGetDataFromLocal();
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailed(String message) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reGetDataFromLocal();
                        lnLoading.setVisibility(View.GONE);
                        srlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void initData() {
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, Status.UnRead, null);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, Status.UnRead, null);
        if (structureCartableActionsPin.size() > 0) {
            frmRcvPin.setVisibility(View.VISIBLE);
        } else {
            frmRcvPin.setVisibility(View.GONE);
        }
        initBaseData();
        initRcv();
    }

    public void reGetDataFromServer() {
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
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, Status.UnRead, null);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, Status.UnRead, null);
        if (structureCartableActionsPin.size() > 0) {
            frmRcvPin.setVisibility(View.VISIBLE);
        } else {
            frmRcvPin.setVisibility(View.GONE);
        }
        initBaseData();
        adapterCartableAction.addAll(structureCartableActions);
        adapterCartableActionPin.addAll(structureCartableActionsPin);
        srlRefresh.setRefreshing(false);
    }

    private void checkData() {
        if (cartableDocumentCount <= 0) {
            txtNoData.setText(Resorse.getString(R.string.no_cartable_data));
            txtNoData.setVisibility(View.VISIBLE);
            imgScale.setVisibility(View.GONE);
            lnRcv.setVisibility(View.GONE);
        } else {
            if (structureCartableActions.size() > 0 || structureCartableActionsPin.size() > 0) {
                imgScale.setVisibility(View.VISIBLE);
                lnRcv.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
            } else {
                imgScale.setVisibility(View.GONE);
                lnRcv.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                txtNoData.setText(Resorse.getString(R.string.no_cartable_unread_data));
            }

        }
    }

    private void initBaseData() {
        cartableDocumentCount = new FarzinCartableQuery().getCartableDocumentCount();
        checkData();
        int UserId = getFarzinPrefrences().getUserID();
        unreadMessageCount = new FarzinMessageQuery().GetMessageCount(UserId, Type.RECEIVED, Status.UnRead);
        txtAllCartableDocumentCount.setText("" + cartableDocumentCount);
        txtAllUnreadMessageCount.setText("" + unreadMessageCount);
        lnMain.setVisibility(View.VISIBLE);
        lnLoading.setVisibility(View.GONE);
    }

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        final GridLayoutManagerWithSmoothScroller gridLayoutManagerPin = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvAction.setLayoutManager(gridLayoutManager);
        rcvPin.setLayoutManager(gridLayoutManagerPin);
        ViewCompat.setNestedScrollingEnabled(rcvPin, false);
        ViewCompat.setNestedScrollingEnabled(rcvAction, false);
        initAdapter(structureCartableActions);
    }

    private void initAdapter(final ArrayList<StructureCartableAction> structureCartableActions) {
        listenerAdapterCartableAction = new ListenerAdapterCartableAction() {
            @Override
            public void onPin(final int position, StructureCartableAction structureCartableAction) {
                structureCartableAction.setPin(true);
                new FarzinCartableQuery().pinAction(structureCartableAction.getActionCode(), true);
                reGetDataFromLocal();
            }

            @Override
            public void unPin(int position, StructureCartableAction structureCartableAction) {
                structureCartableAction.setPin(false);
                new FarzinCartableQuery().pinAction(structureCartableAction.getActionCode(), false);
                reGetDataFromLocal();
            }

            @Override
            public void onLongClick(int position, StructureCartableAction structureCartableAction) {
                initDialogPin_unPin(position, structureCartableAction, structureCartableAction.isPin());
            }

            @Override
            public void onClick(StructureCartableAction structureCartableAction) {
                if (App.canBack) {
                    if (dialogPin_unPin != null && dialogPin_unPin.isShowing()) {
                        dialogPin_unPin.dismiss();
                    }
                    StructureCartableDocumentBND structureCartableDocumentBND = new StructureCartableDocumentBND(structureCartableAction.getActionCode(), structureCartableAction.getActionName(), true);
                    bundleObject.putSerializable(PutExtraEnum.BundleCartableDocument.getValue(), structureCartableDocumentBND);
                    Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocument.class);
                    intent.putExtras(bundleObject);
                    goToActivity(intent);
                }
            }
        };
        adapterCartableAction = new AdapterCartableAction(structureCartableActions, listenerAdapterCartableAction);
        adapterCartableActionPin = new AdapterCartableActionPin(structureCartableActionsPin, listenerAdapterCartableAction);
        rcvAction.setAdapter(adapterCartableAction);
        rcvPin.setAdapter(adapterCartableActionPin);


    }

    private void initDialogPin_unPin(final int position, final StructureCartableAction structureCartableAction, boolean isPin) {

        dialogPin_unPin = new DialogPin_unPin(App.CurentActivity).setOnListener(new ListenerPin_unPin() {
            @Override
            public void onPin() {
                listenerAdapterCartableAction.onPin(position, structureCartableAction);
            }

            @Override
            public void unPin() {
                listenerAdapterCartableAction.unPin(position, structureCartableAction);
            }

            @Override
            public void onCancel() {

            }
        });
        dialogPin_unPin.Show(isPin);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
