package avida.ican.Farzin.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentBND;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterCartableAction;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterCartableActionPin;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityCartableDocument;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableAction;
import avida.ican.Farzin.View.Interface.ListenerDialogNewData;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static avida.ican.Ican.BaseActivity.goToActivity;

/**
 * Created by AtrasVida on 2019-10-20 at 4:06 PM
 */

public class DialogNewData {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private ListenerAdapterCartableAction listener;
    private AdapterCartableAction adapterNewAction;
    private AdapterCartableActionPin adapterNewActionPin;
    private ArrayList<StructureCartableAction> structureCartableActions = new ArrayList<>();
    private ArrayList<StructureCartableAction> structureCartableActionsPin = new ArrayList<>();
    private Bundle bundleObject = new Bundle();
    private FarzinMessageQuery farzinMessageQuery;
    private FarzinCartableQuery farzinCartableQuery;
    private ListenerDialogNewData listenerDialogNewData;

    public DialogNewData(Activity context, ListenerDialogNewData listenerDialogNewData) {
        this.context = context;
        this.listenerDialogNewData = listenerDialogNewData;
    }

    public class Binding {
        @BindView(R.id.txt_new_message_count)
        TextView txtNewMessageCount;
        @BindView(R.id.txt_new_cartable_document_count)
        TextView txtNewCartableDocumentCount;
        @BindView(R.id.ln_new_message)
        LinearLayout lnNewMessage;
        @BindView(R.id.ln_new_cartable_document)
        LinearLayout lnNewCartableDocument;
        @BindView(R.id.ln_rcv)
        LinearLayout lnNewRcv;
        @BindView(R.id.frm_rcv_pin)
        FrameLayout frmNewRcvPin;
        @BindView(R.id.new_data_rcv_action)
        RecyclerView newDataRcvAction;
        @BindView(R.id.new_data_rcv_action_pin)
        RecyclerView newDataRcvActionPin;
        @BindView(R.id.btn_close)
        Button btnClose;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }


    public DialogNewData init() {
        BaseActivity.closeKeyboard();
        farzinCartableQuery = new FarzinCartableQuery();
        farzinMessageQuery = new FarzinMessageQuery();
        context.runOnUiThread(() -> {
            dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_new_data))
                    .setHeader(R.layout.item_dialog_header)
                    .setGravity(Gravity.CENTER)
                    .setCancelable(false)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            viewHolder = new Binding(dialog.getHolderView());
            View viewHolderHeader = dialog.getHeaderView();
            TextView txtHeader = viewHolderHeader.findViewById(R.id.txt_dialog_title);
            txtHeader.setText(Resorse.getString(R.string.title_dialog_new));
        });
        return this;
    }

    public void show() {
        App.canBack = false;
        dialog.show();
        initData();
        viewHolder.btnClose.setOnClickListener(view -> dismiss());
        viewHolder.lnNewMessage.setOnClickListener(view -> {
            listenerDialogNewData.showMessageList();
            dismiss();
        });
    }


    @SuppressLint("SetTextI18n")
    private void initData() {
        structureCartableActions = farzinCartableQuery.getCartableAction(false, Status.UnRead, true);
        structureCartableActionsPin = farzinCartableQuery.getCartableAction(true, Status.UnRead, true);
        if (structureCartableActionsPin.size() > 0) {
            viewHolder.frmNewRcvPin.setVisibility(View.VISIBLE);
        } else {
            viewHolder.frmNewRcvPin.setVisibility(View.GONE);
        }
        long newMessageCount = farzinMessageQuery.getNewMessageCount();
        long newCartableDocumentCount = new FarzinCartableQuery().getNewCartableDocumentCount();
        viewHolder.txtNewMessageCount.setText("" + newMessageCount);
        viewHolder.txtNewCartableDocumentCount.setText("" + newCartableDocumentCount);
        if (newCartableDocumentCount <= 0) {
            viewHolder.lnNewRcv.setVisibility(View.GONE);
        }
        initRcv();
        farzinCartableQuery.updateAllCartableDocumentIsNewStatusToFalse();
        farzinMessageQuery.updateAllMessageIsNewStatusToFalse();
        CustomFunction.DismissNotification(App.CurentActivity, NotificationChanelEnum.Message);
        CustomFunction.DismissNotification(App.CurentActivity, NotificationChanelEnum.Cartable);

    }

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        final GridLayoutManagerWithSmoothScroller gridLayoutManagerPin = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.newDataRcvAction.setLayoutManager(gridLayoutManager);
        viewHolder.newDataRcvActionPin.setLayoutManager(gridLayoutManagerPin);
        ViewCompat.setNestedScrollingEnabled(viewHolder.newDataRcvAction, false);
        ViewCompat.setNestedScrollingEnabled(viewHolder.newDataRcvActionPin, false);
        initAdapter();
    }

    private void initAdapter() {
        listener = new ListenerAdapterCartableAction() {
            @Override
            public void onPin(final int position, StructureCartableAction structureCartableAction) {
            }

            @Override
            public void unPin(int position, StructureCartableAction structureCartableAction) {
            }

            @Override
            public void onLongClick(int position, StructureCartableAction structureCartableAction) {
            }

            @Override
            public void onClick(StructureCartableAction structureCartableAction) {
                StructureCartableDocumentBND structureCartableDocumentBND = new StructureCartableDocumentBND(structureCartableAction.getActionCode(), structureCartableAction.getActionName(), true);
                bundleObject.putSerializable(PutExtraEnum.BundleCartableDocument.getValue(), structureCartableDocumentBND);
                Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocument.class);
                intent.putExtras(bundleObject);
                dismiss();
                goToActivity(intent);

            }
        };
        adapterNewAction = new AdapterCartableAction(structureCartableActions, listener);
        adapterNewActionPin = new AdapterCartableActionPin(structureCartableActionsPin, listener);
        viewHolder.newDataRcvAction.setAdapter(adapterNewAction);
        viewHolder.newDataRcvActionPin.setAdapter(adapterNewActionPin);


    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }


}
