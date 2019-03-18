package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureNodeRES;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-10-07 ar 11:11 AM
 */


public class AdapterCartableHistory extends RecyclerView.Adapter<AdapterCartableHistory.ViewHolder> {

    private ArrayList<StructureNodeRES> itemList;
    private int layout = R.layout.item_cartable_history_list;
    private ViewBinderHelper binderHelper;
    private boolean isLnMoreVisible = false;

    public AdapterCartableHistory(ArrayList<StructureNodeRES> itemList) {
        this.itemList = new ArrayList<>(itemList);
        binderHelper = new ViewBinderHelper();
    }

    public int getDataSize() {
        return itemList.size();
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*  @BindView(R.id.txt_receiver_role_name)
          TextView txtReceiverRoleName;*/
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_creator_role_name)
        TextView txtCreatorRoleName;
        @BindView(R.id.txt_creator_full_name)
        TextView txtCreatorFullName;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.txt_action)
        TextView txtAction;
        @BindView(R.id.ln_receiver)
        LinearLayout lnReceiver;


        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterCartableHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        final StructureNodeRES item = itemList.get(position);

        String receiveDate = CustomFunction.StandardizeTheDateFormat(item.getReceiveDateGeorgian());
        receiveDate = CustomFunction.MiladyToJalaly(receiveDate);
        String[] splitDateTime = receiveDate.split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);
        viewHolder.txtCreatorFullName.setText("" + item.getSenderFirstName() + " " + item.getSenderLastName());
        viewHolder.txtCreatorRoleName.setText("[ " + item.getSenderRoleName() + " ] ");
        viewHolder.txtTitle.setText("" + item.getFirstName() + " " + item.getLastName() + "[ " + item.getRoleName() + " ] ");
        viewHolder.txtAction.setText("" + item.getReceiveAction());
        if (item.getPrivateHamesh().getContent() == null || item.getPrivateHamesh().getContent().isEmpty() || item.getPrivateHamesh().getContent().equals("null")) {
            viewHolder.txtContent.setVisibility(View.GONE);
        } else {
            viewHolder.txtContent.setVisibility(View.VISIBLE);
            String content = item.getPrivateHamesh().getContent();
            viewHolder.txtContent.setText(content);
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

    public void updateData(List<StructureNodeRES> structureNodeRES) {
        itemList = new ArrayList<>();
        itemList.addAll(structureNodeRES);
        notifyDataSetChanged();
    }


    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    public void filter(List<StructureNodeRES> structureNodeRES) {
        itemList = new ArrayList<>();
        itemList.addAll(structureNodeRES);
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

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
        if (itemList == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return itemList.size();
    }


}