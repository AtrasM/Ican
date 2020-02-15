package avida.ican.Farzin.View.Adapter.Queue;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.acra.ACRA;

import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureAdapterDocumentAttachFileQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterDocumentQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2019-07-24 ar 10:28 PM
 */


public class AdapterDocumentAttachFileQueue extends RecyclerView.Adapter<AdapterDocumentAttachFileQueue.ViewHolder> {
    private ArrayList<StructureAdapterDocumentAttachFileQueueBND> documentAttachFileQueuesBND;
    private FarzinDocumentAttachFilePresenter farzinDocumentAttachFilePresenter = new FarzinDocumentAttachFilePresenter();
    private ListenerAdapterDocumentQueue listener;
    //private ArrayList<StructureDocumentOperatorsQueueDB> itemList;
    private int layout = R.layout.item_document_attach_file_queue;

    public AdapterDocumentAttachFileQueue(ArrayList<StructureAdapterDocumentAttachFileQueueBND> documentAttachFileQueuesBND, ListenerAdapterDocumentQueue listener) {
        this.documentAttachFileQueuesBND = documentAttachFileQueuesBND;
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

        @BindView(R.id.txt_count)
        TextView txtCount;

        @BindView(R.id.txt_name)
        TextView txtName;

        @BindView(R.id.img_delete)
        ImageView imgDelete;


        @BindView(R.id.img_attach_icon)
        ImageView imgAttachIcon;

        @BindView(R.id.ln_try)
        LinearLayout lnTry;
        @BindView(R.id.img_try)
        ImageView imgTry;
        @BindView(R.id.ln_error)
        LinearLayout lnError;
        @BindView(R.id.img_error)
        ImageView imgError;
        @BindView(R.id.txt_error_message)
        TextView txtErrorMessage;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterDocumentAttachFileQueue.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        StructureAdapterDocumentAttachFileQueueBND documentAttachFileQueueBND = documentAttachFileQueuesBND.get(position);
        ArrayList<StructureDocumentAttachFileQueueDB> itemList = documentAttachFileQueueBND.getStructureDocumentAttachFileQueueDBS();

        int ETC = itemList.get(0).getETC();
        int EC = itemList.get(0).getEC();

        StructureInboxDocumentDB structureInboxDocumentDB = new FarzinCartableQuery().getCartableDocument(ETC, EC);

        if(structureInboxDocumentDB==null){
            ACRA.getErrorReporter().handleSilentException(new Exception("error AdapterDocumentAttachFileQueue line 124: structureInboxDocumentDB is null. ETC= " + ETC + " | EC= " + EC));
            viewHolder.txtTitleSubject.setText(Resorse.getString(R.string.unknown));
            viewHolder.txtTitleDocumentNumber.setText(Resorse.getString(R.string.unknown));
            viewHolder.txtTitleDocumentAction.setText(Resorse.getString(R.string.unknown));
        }else{
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
        viewHolder.imgDelete.setOnClickListener(view -> listener.onDeletItem(ETC, EC));
        StructureDocumentAttachFileQueueDB structureDocumentAttachFileQueueDB = farzinDocumentAttachFilePresenter.getDocumentAttachFileErrorMessage(ETC, EC);
        new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.imgError, R.color.color_White);

        if (structureDocumentAttachFileQueueDB != null && structureDocumentAttachFileQueueDB.getId() > 0) {
            viewHolder.lnTry.setVisibility(View.VISIBLE);
            viewHolder.lnError.setVisibility(View.VISIBLE);
            viewHolder.imgAttachIcon.setImageDrawable(Resorse.getDrawable(R.drawable.ic_error));
            new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtErrorMessage, structureDocumentAttachFileQueueDB.getStrError());

        } else {
            viewHolder.imgAttachIcon.setImageDrawable(Resorse.getDrawable(R.drawable.ic_waiting));
            viewHolder.lnTry.setVisibility(View.GONE);
            viewHolder.lnError.setVisibility(View.GONE);
        }
        viewHolder.imgTry.setOnClickListener(view -> listener.onTry(ETC, EC));

        viewHolder.txtCount.setText("" + itemList.size());

        //ExtensionEnum extensionEnum = new CustomFunction(App.CurentActivity).getExtensionCategory(item.getFileExtension());
/*        switch (extensionEnum) {
            case IMAGE: {
                item.setIcon(R.drawable.ic_photo);
                break;
            }
            case AUDIO: {
                item.setIcon(R.drawable.ic_voice);
                break;
            }
            case VIDEO: {
                item.setIcon(R.drawable.ic_video);
                break;
            }
            case APPLICATION: {
                item.setIcon(R.drawable.ic_attach_document);
                break;
            }
            case NONE: {
                item.setIcon(R.drawable.ic_attach_document);
            }
            default: {
                item.setIcon(R.drawable.ic_attach_document);
                break;
            }
        }*/


    }


    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }


    public void updateData(ArrayList<StructureAdapterDocumentAttachFileQueueBND> documentAttachFileQueuesBND) {
        this.documentAttachFileQueuesBND.clear();
        this.documentAttachFileQueuesBND = new ArrayList<>();
        this.documentAttachFileQueuesBND.addAll(documentAttachFileQueuesBND);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return this.documentAttachFileQueuesBND.size();
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
        if (this.documentAttachFileQueuesBND == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return this.documentAttachFileQueuesBND.size();
    }


}