package avida.ican.Farzin.View.Adapter;

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
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterQueueOpticalPen;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-09-26 ar 1:10 PM
 */


public class AdapterQueueOpticalPen extends RecyclerView.Adapter<AdapterQueueOpticalPen.ViewHolder> {

    private final StructureUserAndRoleDB structureUserAndRoleDB;
    private ArrayList<StructureOpticalPenQueueDB> itemList;
    private int layout = R.layout.item_queue_optical_pen_list;
    private ListenerAdapterQueueOpticalPen listenerAdapterQueueOpticalPen;
    private boolean isLnMoreVisible = false;

    public AdapterQueueOpticalPen(ArrayList<StructureOpticalPenQueueDB> itemList, ListenerAdapterQueueOpticalPen listenerAdapterQueueOpticalPen) {
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterQueueOpticalPen = listenerAdapterQueueOpticalPen;
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID(), farzinPrefrences.getRoleID());

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*  @BindView(R.id.txt_receiver_role_name)
          TextView txtReceiverRoleName; */
        @BindView(R.id.txt_creator_role_name)
        TextView txtCreatorRoleName;
        @BindView(R.id.txt_creator_full_name)
        TextView txtCreatorFullName;
        @BindView(R.id.txt_file_name)
        TextView txtFileName;
        @BindView(R.id.ln_file)
        LinearLayout lnFile;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterQueueOpticalPen.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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


        final StructureOpticalPenQueueDB item = itemList.get(position);

        viewHolder.txtFileName.setText("" + Resorse.getString(R.string.showOpticalPenFile));

        final String Name = "" + structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();

        viewHolder.txtCreatorFullName.setText("" + Name);
        viewHolder.txtCreatorRoleName.setText("[ " + structureUserAndRoleDB.getRoleName() + " ] ");


        viewHolder.itemView.setOnClickListener(view -> {
            String extention = "";
            if (item.getStrExtention() != null && !item.getStrExtention().isEmpty() && !item.getStrExtention().equals("null")) {
                extention = item.getStrExtention();
            } else {
                extention = ".png";
            }
            StructureAttach structureAttach = new StructureAttach(item.getBfile(), "", extention);
            listenerAdapterQueueOpticalPen.onOpenFile(structureAttach);

        });
    }

    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }


    public void updateData(List<StructureOpticalPenQueueDB> structureOpticalPenQueueDBS) {
        itemList.clear();
        itemList = new ArrayList<>();
        itemList.addAll(structureOpticalPenQueueDBS);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return itemList.size();
    }

    public void updateData(int pos, List<StructureOpticalPenQueueDB> structureOpticalPenQueueDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureOpticalPenQueueDBS);
            notifyItemRangeInserted(start, structureOpticalPenQueueDBS.size());
        } else {
            itemList.addAll(pos, structureOpticalPenQueueDBS);
            notifyItemRangeInserted(pos, structureOpticalPenQueueDBS.size());
        }
        notifyDataSetChanged();
    }

    public void updateData(int pos, StructureOpticalPenQueueDB structureOpticalPenQueueDB) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureOpticalPenQueueDB);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureOpticalPenQueueDB);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
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
        if (itemList == null) {
            App.ShowMessage().ShowToast(Resorse.getString(R.string.error_adapter_item_count), ToastEnum.TOAST_SHORT_TIME);
            return 0;
        }
        return itemList.size();
    }


}