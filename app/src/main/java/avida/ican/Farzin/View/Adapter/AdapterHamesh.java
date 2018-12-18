package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
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


public class AdapterHamesh extends RecyclerView.Adapter<AdapterHamesh.ViewHolder> {

    private ArrayList<StructureHameshDB> itemList;
    private int layout = R.layout.item_hamesh_list;
    private ListenerAdapterHameshList listenerAdapterHameshList;
    private ViewBinderHelper binderHelper;
    private boolean isLnMoreVisible = false;

    public AdapterHamesh(ArrayList<StructureHameshDB> itemList, ListenerAdapterHameshList listenerAdapterHameshList) {
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterHameshList = listenerAdapterHameshList;
        binderHelper = new ViewBinderHelper();
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
        @BindView(R.id.txt_file_name)
        TextView txtFileName;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_hamesh)
        TextView txtHamesh;
        @BindView(R.id.ln_receiver)
        LinearLayout lnReceiver;
        @BindView(R.id.ln_file)
        LinearLayout lnFile;
        @BindView(R.id.img_receiver)
        ImageView imgReceiver;


        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterHamesh.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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


        final StructureHameshDB item = itemList.get(position);

        if (item.getHameshType().equals("Image")) {
            viewHolder.lnReceiver.setVisibility(View.GONE);
            viewHolder.lnFile.setVisibility(View.VISIBLE);
            viewHolder.txtFileName.setText("" + Resorse.getString(R.string.showOpticalPenFile));
        } else {
            viewHolder.lnFile.setVisibility(View.GONE);
            if (item.isPrivate() || !item.getHameshType().equals("Text")) {
                viewHolder.imgReceiver.setVisibility(View.GONE);
            }
            viewHolder.lnReceiver.setVisibility(View.VISIBLE);
            if (item.getTitle() == null || item.getTitle().isEmpty() || item.getTitle().equals("null")) {
                viewHolder.txtTitle.setVisibility(View.GONE);
            } else {
                viewHolder.txtTitle.setText("" + item.getTitle());
            }
            if (item.getContent() == null || item.getContent().isEmpty() || item.getContent().equals("null")) {
                viewHolder.txtHamesh.setVisibility(View.GONE);
            } else {
                viewHolder.txtHamesh.setText("" + item.getContent());
            }


        }


        String[] splitDateTime = item.getCreationShamsiDate().toString().split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);
        viewHolder.txtCreatorFullName.setText("" + item.getCreatorName());
        viewHolder.txtCreatorRoleName.setText(" [ " + item.getCreatorRoleName() + " ] ");


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getHameshType().equals("Image")) {
                    StructureAttach structureAttach = new StructureAttach(item.getFileBinary(), item.getFileName(), ".png");
                    listenerAdapterHameshList.onOpenFile(structureAttach);
                }
            }
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

    public void updateItem(StructureHameshDB structureHameshDB) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == structureHameshDB.getId()) {
                itemList.remove(i);
                notifyItemRemoved(i);
                itemList.add(i, structureHameshDB);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateData(List<StructureHameshDB> structureHameshDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureHameshDBS);
        notifyDataSetChanged();
    }

    public int getDataSize() {
        return itemList.size();
    }

    public void updateData(int pos, List<StructureHameshDB> structureHameshDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureHameshDBS);
            notifyItemRangeInserted(start, structureHameshDBS.size());
        } else {
            itemList.addAll(pos, structureHameshDBS);
            notifyItemRangeInserted(pos, structureHameshDBS.size());
        }
        notifyDataSetChanged();
    }

    public void updateData(int pos, StructureHameshDB structureHameshDB) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureHameshDB);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureHameshDB);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
    }

    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    public void filter(List<StructureHameshDB> structureHameshDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureHameshDBS);
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