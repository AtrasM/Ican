package avida.ican.Farzin.View.Adapter.Queue;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.acra.ACRA;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureAdapterDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterDocumentQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-09-26 ar 1:10 PM
 */


public class AdapterDocumentOperatorQueue extends RecyclerView.Adapter<AdapterDocumentOperatorQueue.ViewHolder> {
    private ArrayList<StructureAdapterDocumentOperatorsQueueBND> documentOperatorsQueueBNDS;
    private FarzinDocumentOperatorsQueuePresenter farzinDocumentOperatorsQueuePresenter = new FarzinDocumentOperatorsQueuePresenter();
    private ListenerAdapterDocumentQueue listener;
    //private ArrayList<StructureDocumentOperatorsQueueDB> itemList;
    private int layout = R.layout.item_document_operator_queue;

    public AdapterDocumentOperatorQueue(ArrayList<StructureAdapterDocumentOperatorsQueueBND> documentOperatorsQueueBNDS, ListenerAdapterDocumentQueue listener) {
        this.documentOperatorsQueueBNDS = documentOperatorsQueueBNDS;
        this.listener = listener;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_title_subject)
        TextView txtTitleSubject;
        @BindView(R.id.txt_title_document_number)
        TextView txtTitleDocumentNumber;
        @BindView(R.id.txt_title_document_action)
        TextView txtTitleDocumentAction;

        @BindView(R.id.txt_operator_confirm_workflow_count)
        TextView txtOperatorConfirm_WorkFlowCount;
        @BindView(R.id.txt_operator_append_count)
        TextView txtOperatorAppendCount;
        @BindView(R.id.txt_operator_sign_count)
        TextView txtOperatorSignCount;
        @BindView(R.id.txt_operator_optical_pen_count)
        TextView txtOperatorOpticalPenCount;

        @BindView(R.id.txt_operator_confirm_workflow)
        TextView txtOperatorConfirmWorkFlow;
        @BindView(R.id.txt_operator_append)
        TextView txtOperatorAppend;
        @BindView(R.id.txt_operator_sign)
        TextView txtOperatorSign;
        @BindView(R.id.txt_operator_optical_pen)
        TextView txtOperatorOpticalPen;


        @BindView(R.id.img_operator_confirm_workflow_delet)
        ImageView imgOperatorConfirm_WorkFlowDelet;
        @BindView(R.id.img_operator_append_delet)
        ImageView imgOperatorAppendDelet;
        @BindView(R.id.img_operator_sign_delet)
        ImageView imgOperatorSignDelet;
        @BindView(R.id.img_operator_optical_pen_delet)
        ImageView imgOperatorOpticalPenDelete;

        @BindView(R.id.img_main_delete)
        ImageView imgMainDelete;


        @BindView(R.id.img_operator_optical_pen)
        ImageView imgOperatorOpticalPen;
        @BindView(R.id.img_operator_sign)
        ImageView imgOperatorSign;
        @BindView(R.id.img_operator_append)
        ImageView imgOperatorAppend;
        @BindView(R.id.img_operator_confirm_workflow)
        ImageView imgOperatorConfirm_WorkFlow;


        @BindView(R.id.ln_operator_optical_pen)
        LinearLayout lnOperatorOpticalPen;
        @BindView(R.id.ln_operator_sign)
        LinearLayout lnOperatorSign;
        @BindView(R.id.ln_operator_append)
        LinearLayout lnOperatorAppend;
        @BindView(R.id.ln_operator_confirm_workflow)
        LinearLayout lnOperatorConfirm_WorkFlow;
        @BindView(R.id.ln_try)
        LinearLayout lnTry;
        @BindView(R.id.img_try)
        ImageView imgTry;
        @BindView(R.id.img_error)
        ImageView imgError;
        @BindView(R.id.txt_error_message)
        TextView txtErrorMessage;
        @BindView(R.id.ln_error)
        LinearLayout lnError;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterDocumentOperatorQueue.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        StructureAdapterDocumentOperatorsQueueBND structureAdapterDocumentOperatorsQueueBND = documentOperatorsQueueBNDS.get(position);
        ArrayList<StructureDocumentOperatorsQueueDB> itemList = structureAdapterDocumentOperatorsQueueBND.getDocumentOperatorsQueueDBS();

        int ETC = itemList.get(0).getETC();
        int EC = itemList.get(0).getEC();
        StructureInboxDocumentDB structureInboxDocumentDB = new FarzinCartableQuery().getCartableDocument(ETC, EC);

        if (structureInboxDocumentDB == null) {
            ACRA.getErrorReporter().handleSilentException(new Exception("error AdapterDocumentOperatorQueue line 162: structureInboxDocumentDB is null. ETC= " + ETC + " | EC= " + EC));
            viewHolder.txtTitleSubject.setText(Resorse.getString(R.string.unknown));
            viewHolder.txtTitleDocumentNumber.setText(Resorse.getString(R.string.unknown));
            viewHolder.txtTitleDocumentAction.setText(Resorse.getString(R.string.unknown));
        } else {
            if (structureInboxDocumentDB.getTitle() != null && !structureInboxDocumentDB.getTitle().isEmpty()) {
                viewHolder.txtTitleSubject.setText("" + structureInboxDocumentDB.getTitle());
            } else {
                viewHolder.txtTitleSubject.setText("-");
            }
            if (structureInboxDocumentDB.getImportEntityNumber() != null && !structureInboxDocumentDB.getImportEntityNumber().isEmpty()) {
                viewHolder.txtTitleDocumentNumber.setText(Resorse.getString(R.string.hint_import_entity_number) + structureInboxDocumentDB.getImportEntityNumber());
            } else {
                viewHolder.txtTitleDocumentNumber.setText(Resorse.getString(R.string.hint_entity_number) + structureInboxDocumentDB.getEntityNumber());
            }
            if (structureInboxDocumentDB.getActionName() != null && !structureInboxDocumentDB.getActionName().isEmpty()) {
                viewHolder.txtTitleDocumentAction.setText("" + structureInboxDocumentDB.getActionName());
            } else {
                viewHolder.txtTitleDocumentAction.setText("-");
            }
        }

        viewHolder.imgOperatorOpticalPenDelete.setOnClickListener(view -> listener.onDeletOperator(ETC, EC, DocumentOperatoresTypeEnum.AddHameshOpticalPen));
        viewHolder.imgOperatorSignDelet.setOnClickListener(view -> listener.onDeletOperator(ETC, EC, DocumentOperatoresTypeEnum.SignDocument));
        viewHolder.imgOperatorAppendDelet.setOnClickListener(view -> listener.onDeletOperator(ETC, EC, DocumentOperatoresTypeEnum.Append));
        viewHolder.imgOperatorConfirm_WorkFlowDelet.setOnClickListener(view -> {
            if (structureInboxDocumentDB.isbInWorkFlow()) {
                listener.onDeletOperator(ETC, EC, DocumentOperatoresTypeEnum.WorkFlow);
            } else {
                listener.onDeletOperator(ETC, EC, DocumentOperatoresTypeEnum.Response);
            }
        });
        viewHolder.imgMainDelete.setOnClickListener(view -> listener.onDeletItem(ETC, EC));

        long opticalPenCount = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueueNotSendedCount(ETC, EC, DocumentOperatoresTypeEnum.AddHameshOpticalPen);
        long signCount = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueueNotSendedCount(ETC, EC, DocumentOperatoresTypeEnum.SignDocument);
        long appendCount = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueueNotSendedCount(ETC, EC, DocumentOperatoresTypeEnum.Append);
        long confirmCount = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueueNotSendedCount(ETC, EC, DocumentOperatoresTypeEnum.Response);
        long workFlowCount = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorQueueNotSendedCount(ETC, EC, DocumentOperatoresTypeEnum.WorkFlow);
        boolean haveError = farzinDocumentOperatorsQueuePresenter.isDocumentOperatorError(ETC, EC);

        if (haveError) {
            viewHolder.lnTry.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lnTry.setVisibility(View.GONE);
        }
        List<StructureDocumentOperatorsQueueDB> itemsError = farzinDocumentOperatorsQueuePresenter.getDocumentOperatorErrorMessage(ETC, EC);
        new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.imgError, R.color.color_White);

        if (itemsError != null && itemsError.size() > 0) {
            viewHolder.lnTry.setVisibility(View.VISIBLE);
            viewHolder.lnError.setVisibility(View.VISIBLE);
            new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtErrorMessage, new ChangeXml().charDecoder(itemsError.get(0).getStrError()));
        } else {
            viewHolder.lnTry.setVisibility(View.GONE);
            viewHolder.lnError.setVisibility(View.GONE);
        }
        for (int i = 0; i < itemList.size(); i++) {
            switch (itemList.get(i).getDocumentOpratoresTypeEnum()) {
                case AddHameshOpticalPen: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorOpticalPen, itemList.get(i));
                    break;
                }
                case SignDocument: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorSign, itemList.get(i));
                    break;
                }
                case Append: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorAppend, itemList.get(i));
                    break;
                }
                case Response: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorConfirm_WorkFlow, itemList.get(i));
                    break;
                }
            }
        }
        for (int i = 0; i < itemsError.size(); i++) {
            switch (itemsError.get(i).getDocumentOpratoresTypeEnum()) {
                case AddHameshOpticalPen: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorOpticalPen, itemList.get(i));
                    break;
                }
                case SignDocument: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorSign, itemList.get(i));
                    break;
                }
                case Append: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorAppend, itemList.get(i));
                    break;
                }
                case Response: {
                    CheckItemStatus(viewHolder, viewHolder.imgOperatorConfirm_WorkFlow, itemList.get(i));
                    break;
                }
            }
        }
        viewHolder.imgTry.setOnClickListener(view -> listener.onTry(ETC, EC));

        viewHolder.txtOperatorOpticalPenCount.setText("" + opticalPenCount);
        viewHolder.txtOperatorSignCount.setText("" + signCount);
        viewHolder.txtOperatorAppendCount.setText("" + appendCount);
        if (structureInboxDocumentDB.isbInWorkFlow()) {
            viewHolder.txtOperatorConfirmWorkFlow.setText(Resorse.getString(R.string.title_workflow_operator));
            viewHolder.txtOperatorConfirm_WorkFlowCount.setText("" + workFlowCount);
        } else {
            viewHolder.txtOperatorConfirmWorkFlow.setText(Resorse.getString(R.string.title_confirm_operator));
            viewHolder.txtOperatorConfirm_WorkFlowCount.setText("" + confirmCount);
        }

        if (opticalPenCount > 0) {
            viewHolder.imgOperatorOpticalPenDelete.setVisibility(View.VISIBLE);
            viewHolder.imgOperatorOpticalPen.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorOpticalPenCount.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorOpticalPen.setTextColor(Resorse.getColor(R.color.color_Info));
            viewHolder.lnOperatorOpticalPen.setBackgroundColor(Resorse.getColor(R.color.color_bg_cadr));
        } else {
            viewHolder.imgOperatorOpticalPenDelete.setVisibility(View.INVISIBLE);
            viewHolder.imgOperatorOpticalPen.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorOpticalPenCount.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorOpticalPen.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            viewHolder.lnOperatorOpticalPen.setBackgroundColor(Resorse.getColor(R.color.color_disable));
        }
        if (signCount > 0) {
            viewHolder.imgOperatorSign.setVisibility(View.VISIBLE);
            viewHolder.imgOperatorSignDelet.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorSignCount.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorSign.setTextColor(Resorse.getColor(R.color.color_Info));
            viewHolder.lnOperatorSign.setBackgroundColor(Resorse.getColor(R.color.color_bg_cadr));
        } else {
            viewHolder.imgOperatorSign.setVisibility(View.INVISIBLE);
            viewHolder.imgOperatorSignDelet.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorSignCount.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorSign.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            viewHolder.lnOperatorSign.setBackgroundColor(Resorse.getColor(R.color.color_disable));
        }
        if (appendCount > 0) {
            viewHolder.imgOperatorAppend.setVisibility(View.VISIBLE);
            viewHolder.imgOperatorAppendDelet.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorAppendCount.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorAppend.setTextColor(Resorse.getColor(R.color.color_Info));
            new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.lnOperatorAppend, R.color.color_bg_cadr);
        } else {
            viewHolder.imgOperatorAppend.setVisibility(View.INVISIBLE);
            viewHolder.imgOperatorAppendDelet.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorAppendCount.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorAppend.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.lnOperatorAppend, R.color.color_disable);
        }
        if (confirmCount > 0 || workFlowCount > 0) {
            viewHolder.imgOperatorConfirm_WorkFlow.setVisibility(View.VISIBLE);
            viewHolder.imgOperatorConfirm_WorkFlowDelet.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorConfirm_WorkFlowCount.setVisibility(View.VISIBLE);
            viewHolder.txtOperatorConfirmWorkFlow.setTextColor(Resorse.getColor(R.color.color_Info));
            new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.lnOperatorConfirm_WorkFlow, R.color.color_bg_cadr);
            viewHolder.lnOperatorConfirm_WorkFlow.setBackgroundColor(Resorse.getColor(R.color.color_bg_cadr));
        } else {
            viewHolder.imgOperatorConfirm_WorkFlow.setVisibility(View.INVISIBLE);
            viewHolder.imgOperatorConfirm_WorkFlowDelet.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorConfirm_WorkFlowCount.setVisibility(View.INVISIBLE);
            viewHolder.txtOperatorConfirmWorkFlow.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.lnOperatorConfirm_WorkFlow, R.color.color_disable);

        }


       /* if (position == documentOperatorsQueueBNDS.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.GONE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }*/
    }

    private void CheckItemStatus(ViewHolder viewHolder, ImageView imageView, StructureDocumentOperatorsQueueDB documentOperatorsQueueDB) {

        switch (documentOperatorsQueueDB.getQueueStatus()) {
            case ERROR: {
                imageView.setImageDrawable(Resorse.getDrawable(R.drawable.ic_error));
                break;
            }
            case SENDED: {
                imageView.setImageDrawable(Resorse.getDrawable(R.drawable.ic_success));
                break;
            }
            case WAITING: {
                imageView.setImageDrawable(Resorse.getDrawable(R.drawable.ic_waiting));
                break;
            }
            default: {
                imageView.setImageDrawable(Resorse.getDrawable(R.drawable.ic_no_action));
            }
        }
    }

    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }


    public void updateData(ArrayList<StructureAdapterDocumentOperatorsQueueBND> documentOperatorsQueueBNDS) {
        this.documentOperatorsQueueBNDS.clear();
        this.documentOperatorsQueueBNDS = new ArrayList<>();
        this.documentOperatorsQueueBNDS.addAll(documentOperatorsQueueBNDS);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return this.documentOperatorsQueueBNDS.size();
    }


    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }


    private void sleep() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Return the size of your itemsGroups (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (this.documentOperatorsQueueBNDS == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return this.documentOperatorsQueueBNDS.size();
    }


}