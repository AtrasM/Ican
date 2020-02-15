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
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureCreatDocumentQueueDB;
import avida.ican.Farzin.Presenter.Queue.FarzinCreateDocumentPresenter;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterDocumentQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2019-07-31 ar 5:32 PM
 */


public class AdapterImportDocumentQueue extends RecyclerView.Adapter<AdapterImportDocumentQueue.ViewHolder> {
    private ArrayList<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS;
    private FarzinCreateDocumentPresenter farzinCreateDocumentPresenter = new FarzinCreateDocumentPresenter();
    private ListenerAdapterDocumentQueue listener;
    private int layout = R.layout.item_import_document_queue;

    public AdapterImportDocumentQueue(ArrayList<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS, ListenerAdapterDocumentQueue listener) {
        this.structureCreatDocumentQueueDBS = structureCreatDocumentQueueDBS;
        this.listener = listener;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_title_subject)
        TextView txtTitleSubject;
        @BindView(R.id.txt_receiver_name)
        TextView txtReceiveName;
        @BindView(R.id.img_delete)
        ImageView imgDelete;
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
    public AdapterImportDocumentQueue.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        StructureCreatDocumentQueueDB structureCreatDocumentQueueDB = structureCreatDocumentQueueDBS.get(position);

        int ETC = structureCreatDocumentQueueDB.getETC();
        int EC = structureCreatDocumentQueueDB.getEC();
        if (structureCreatDocumentQueueDB.getSubject() != null && !structureCreatDocumentQueueDB.getSubject().isEmpty()) {
            viewHolder.txtTitleSubject.setText("" + structureCreatDocumentQueueDB.getSubject());
        } else {
            viewHolder.txtTitleSubject.setText("-");
        }

        viewHolder.imgDelete.setOnClickListener(view -> listener.onDeletItem(ETC, EC));
        if (structureCreatDocumentQueueDB.getQueueStatus() == QueueStatus.ERROR) {
            viewHolder.lnTry.setVisibility(View.VISIBLE);
            viewHolder.lnError.setVisibility(View.VISIBLE);
            new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtErrorMessage, structureCreatDocumentQueueDB.getStrError());

        } else {
            viewHolder.lnTry.setVisibility(View.GONE);
            viewHolder.lnError.setVisibility(View.GONE);
        }

        String receiverName = structureCreatDocumentQueueDB.getReceiverFullName();
        receiverName = receiverName.replace(" || ", "،");
        viewHolder.txtReceiveName.setText(receiverName);
        viewHolder.imgTry.setOnClickListener(view -> listener.onTry(ETC, EC));

    }


    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }


    public void updateData(ArrayList<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS) {
        this.structureCreatDocumentQueueDBS.clear();
        this.structureCreatDocumentQueueDBS = new ArrayList<>();
        this.structureCreatDocumentQueueDBS.addAll(structureCreatDocumentQueueDBS);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return this.structureCreatDocumentQueueDBS.size();
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
        if (this.structureCreatDocumentQueueDBS == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return this.structureCreatDocumentQueueDBS.size();
    }


}