package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Database.StructureLogDB;
import avida.ican.Farzin.Model.Structure.StructureCartableAction;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableAction;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2019-11-30 ar 5:08 PM
 */


public class AdapterLog extends RecyclerView.Adapter<AdapterLog.ViewHolder> {

    private ArrayList<StructureLogDB> itemList;
    private int layout = R.layout.item_log_list;
    private ListenerAdapterCartableAction listenerAdapterCartableAction;

    public AdapterLog(ArrayList<StructureLogDB> itemList) {
        this.itemList = new ArrayList<>(itemList);
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_log)
        TextView txtLog;

        @BindView(R.id.txt_date)
        TextView txtDate;

        @BindView(R.id.txt_counter)
        TextView txtCounter;
        @BindView(R.id.ln_divider)
        LinearLayout lnDivider;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterLog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        viewHolder.txtCounter.setText("" + (position + 1));
        viewHolder.txtLog.setText(itemList.get(position).getLog());
        viewHolder.txtDate.setText("" + itemList.get(position).getDate());

        if (position == itemList.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }
    }


    // Return the size of your itemsGroups (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (itemList == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return itemList.size();
    }


}