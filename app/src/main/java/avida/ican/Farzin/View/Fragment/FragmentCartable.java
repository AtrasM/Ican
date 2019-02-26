package avida.ican.Farzin.View.Fragment;

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

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentRefreshListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Adapter.AdapterCartableAction;
import avida.ican.Farzin.View.Adapter.AdapterCartableActionPin;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocument;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableAction;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.Enum.CompareTimeEnum;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.DialogPin_unPin;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerPin_unPin;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.goToActivity;

public class FragmentCartable extends BaseFragment {


    @BindView(R.id.rcv_action)
    RecyclerView rcvAction;
    @BindView(R.id.rcv_pin)
    RecyclerView rcvPin;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_main)
    LinearLayout lnMain;
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
    private FarzinCartableDocumentListPresenter farzinCartableDocumentListPresenter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cartable;
    }

    public FragmentCartable() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public FragmentCartable newInstance(FragmentManager fragmentManager) {
        mfragmentManager = fragmentManager;
        return this;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reGetDataFromServer();
            }
        });
        initCartableDocumentListPresenter();
        initData();
    }


    private void initCartableDocumentListPresenter() {
        farzinCartableDocumentListPresenter = new FarzinCartableDocumentListPresenter(new CartableDocumentRefreshListener() {
            @Override
            public void newData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reGetDataFromLocal();
                        srlRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        reGetDataFromLocal();
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
                        srlRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }


    public void reGetDataFromServer() {
        if (App.networkStatus == NetworkStatus.Connected) {
            CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getCartableLastCheckDate(), (TimeValue.SecondsInMilli() * 10));
            if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                getFarzinPrefrences().putCartableLastCheckDate(CustomFunction.getCurentDateTime().toString());
                farzinCartableDocumentListPresenter.GetFromServer();
            } else {
                reGetDataFromLocal();
            }
        }else{
            reGetDataFromLocal();
        }


    }

    private void initData() {
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, null);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, null);
        checkData();
        if (structureCartableActionsPin.size() > 0) {
            frmRcvPin.setVisibility(View.VISIBLE);
        } else {
            frmRcvPin.setVisibility(View.GONE);
        }
        initRcv();
    }

    public void reGetDataFromLocal() {
        structureCartableActions = new FarzinCartableQuery().getCartableAction(false, null);
        structureCartableActionsPin = new FarzinCartableQuery().getCartableAction(true, null);
        if (structureCartableActionsPin.size() > 0) {
            frmRcvPin.setVisibility(View.VISIBLE);
        } else {
            frmRcvPin.setVisibility(View.GONE);
        }
        adapterCartableAction.addAll(structureCartableActions);
        adapterCartableActionPin.addAll(structureCartableActionsPin);
        srlRefresh.setRefreshing(false);
    }

    private void checkData() {
        long cartableDocumentCount = new FarzinCartableQuery().getCartableDocumentCount();
        if (cartableDocumentCount <= 0) {
            txtNoData.setText(Resorse.getString(R.string.no_cartable_data));
            txtNoData.setVisibility(View.VISIBLE);
            lnMain.setVisibility(View.GONE);
        }
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
               /* structureCartableActionsPin.add(structureCartableAction);
                structureCartableActions.remove(structureCartableAction);
                adapterCartableActionPin.addItem(structureCartableAction);
                adapterCartableAction.remove(position);
                frmRcvPin.setVisibility(View.VISIBLE);*/
                reGetDataFromLocal();

            }

            @Override
            public void unPin(int position, StructureCartableAction structureCartableAction) {
                structureCartableAction.setPin(false);
                new FarzinCartableQuery().pinAction(structureCartableAction.getActionCode(), false);
            /*    structureCartableActionsPin.remove(structureCartableAction);
                structureCartableActions.add(structureCartableAction);
                adapterCartableAction.addItem(structureCartableAction);
                adapterCartableActionPin.remove(position);
                if (structureCartableActionsPin.size() == 0) {
                    frmRcvPin.setVisibility(View.GONE);
                }*/
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
                    StructureCartableDocumentBND structureCartableDocumentBND = new StructureCartableDocumentBND(structureCartableAction.getActionCode(), structureCartableAction.getActionName(),false);
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
