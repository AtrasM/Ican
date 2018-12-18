package avida.ican.Farzin.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Adapter.AdapterCartableAction;
import avida.ican.Farzin.View.Adapter.AdapterCartableActionPin;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocument;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableAction;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogPin_unPin;
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
    @BindView(R.id.rcv_action)
    RecyclerView rcvAction;
    @BindView(R.id.rcv_pin)
    RecyclerView rcvPin;
    @BindView(R.id.frm_rcv_pin)
    FrameLayout frmRcvPin;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlRefresh;

    private ListenerAdapterCartableAction listenerAdapterCartableAction;
    private AdapterCartableAction adapterCartableAction;
    private AdapterCartableActionPin adapterCartableActionPin;
    private static FragmentManager mfragmentManager;
    private String Tag = "FragmentCartable";
    private ArrayList<StructureCartableAction> structureCartableActions = new ArrayList<>();
    private ArrayList<StructureCartableAction> structureCartableActionsPin = new ArrayList<>();
    private Bundle bundleObject = new Bundle();
    private DialogPin_unPin dialogPin_unPin;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private long cartableDocumentCount = 0;
    private long unreadMessageCount = 0;
    private FarzinActivityMain activityMain;

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
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ReGetData();
            }
        });

        lnAllCartableDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityMain != null && cartableDocumentCount > 0) {
                    activityMain.selectCartableDocumentFragment();
                }
            }
        });
        lnAllUnreadMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityMain != null && unreadMessageCount > 0) {
                    activityMain.selectMessageFragment();
                }
            }
        });
        initData();
    }

    private void initData() {
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, Status.UnRead);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, Status.UnRead);
        if (structureCartableActionsPin.size() > 0) {
            frmRcvPin.setVisibility(View.VISIBLE);
        } else {
            frmRcvPin.setVisibility(View.GONE);
        }
        initBaseData();
        initRcv();
    }

    public void ReGetData() {
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, Status.UnRead);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, Status.UnRead);
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
            lnRcv.setVisibility(View.GONE);
        } else {
            if (structureCartableActions.size() > 0 || structureCartableActionsPin.size() > 0) {
                lnRcv.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
            } else {
                lnRcv.setVisibility(View.GONE);
                txtNoData.setText(Resorse.getString(R.string.no_cartable_unread_data));
                txtNoData.setVisibility(View.VISIBLE);
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
        rcvPin.setNestedScrollingEnabled(false);
        rcvAction.setNestedScrollingEnabled(false);
        initAdapter(structureCartableActions);
    }

    private void initAdapter(final ArrayList<StructureCartableAction> structureCartableActions) {
        listenerAdapterCartableAction = new ListenerAdapterCartableAction() {
            @Override
            public void onPin(final int position, StructureCartableAction structureCartableAction) {
                structureCartableAction.setPin(true);
                new FarzinCartableQuery().pinAction(structureCartableAction.getActionCode(), true);
                ReGetData();
                /*structureCartableActionsPin.add(structureCartableAction);
                structureCartableActions.remove(structureCartableAction);
                adapterCartableActionPin.addItem(structureCartableAction);
                adapterCartableAction.remove(position);
                frmRcvPin.setVisibility(View.VISIBLE);*/


            }

            @Override
            public void unPin(int position, StructureCartableAction structureCartableAction) {
                structureCartableAction.setPin(false);
                new FarzinCartableQuery().pinAction(structureCartableAction.getActionCode(), false);
           /*     structureCartableActionsPin.remove(structureCartableAction);
                structureCartableActions.add(structureCartableAction);
                adapterCartableAction.addItem(structureCartableAction);
                adapterCartableActionPin.remove(position);
                if (structureCartableActionsPin.size() == 0) {
                    frmRcvPin.setVisibility(View.GONE);
                }*/
                ReGetData();
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
                    StructureCartableDocumentBND structureCartableDocumentBND = new StructureCartableDocumentBND(structureCartableAction.getActionCode(), structureCartableAction.getActionName());
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
        /*try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
