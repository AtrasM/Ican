package avida.ican.Farzin.View.Adapter.Cartable;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

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
 * Created by AtrasVida in 97-09-22 ar 3:59 PM
 */


public class AdapterCartableAction extends RecyclerView.Adapter<AdapterCartableAction.ViewHolder> {

    private ArrayList<StructureCartableAction> itemList;
    private int layout = R.layout.item_cartable_action_list;
    private ListenerAdapterCartableAction listenerAdapterCartableAction;

    public AdapterCartableAction(ArrayList<StructureCartableAction> itemList, ListenerAdapterCartableAction listenerAdapterCartableAction) {
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterCartableAction = listenerAdapterCartableAction;
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;

        @BindView(R.id.txt_count)
        TextView txtCount;

        @BindView(R.id.img_profile)
        ImageView imgProfile;
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
    public AdapterCartableAction.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        String Char = itemList.get(position).getActionName().substring(0, 1);
        viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        viewHolder.txtName.setText(itemList.get(position).getActionName());
        viewHolder.txtCount.setText("" + itemList.get(position).getCount());

        viewHolder.itemView.setOnClickListener(view -> listenerAdapterCartableAction.onClick(itemList.get(position)));
        viewHolder.itemView.setOnLongClickListener(view -> {
            listenerAdapterCartableAction.onLongClick(position, itemList.get(position));
            return false;
        });

        if (position == itemList.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }
    }


    public void filter(ArrayList<StructureCartableAction> structureCartableActions) {
        itemList = new ArrayList<>();
        itemList.addAll(structureCartableActions);
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addItem(StructureCartableAction structureCartableAction) {
        itemList.add(structureCartableAction);
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<StructureCartableAction> structureCartableActions) {
        itemList.clear();
        itemList.addAll(structureCartableActions);
        notifyDataSetChanged();
    }

    public void remove(int position) {

        try {
            if (itemList.size() >= position) {
                itemList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
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