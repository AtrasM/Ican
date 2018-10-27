package avida.ican.Farzin.View.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterZanjireMadrak;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-10-15 ar 4:32 PM
 */


public class AdapteZanjireMadrak extends RecyclerView.Adapter<AdapteZanjireMadrak.ViewHolder> {

    private ArrayList<StructureZanjireMadrakFileDB> itemList;
    private int layout = R.layout.item_zanjire_madrak;
    private ListenerAdapterZanjireMadrak listenerAdapterZanjireMadrak;
    private ViewBinderHelper binderHelper;

    public AdapteZanjireMadrak(ArrayList<StructureZanjireMadrakFileDB> itemList, ListenerAdapterZanjireMadrak listenerAdapterZanjireMadrak) {
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterZanjireMadrak = listenerAdapterZanjireMadrak;
        binderHelper = new ViewBinderHelper();
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_name)
        TextView txtName;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapteZanjireMadrak.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        final StructureZanjireMadrakFileDB item = itemList.get(position);

        viewHolder.txtName.setText(item.getFile_name());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getFile_binary()!=null){
                    final StructureAttach file = new StructureAttach(item.getFile_binary(), item.getFile_name(), item.getFile_extension(), null);
                    listenerAdapterZanjireMadrak.onOpenFile(file);
                }else{
                    listenerAdapterZanjireMadrak.FileNotExist();
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

    public void updateData(List<StructureZanjireMadrakFileDB> structureZanjireMadrakFileDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureZanjireMadrakFileDBS);
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }

    public int getDataSize() {
        return itemList.size();
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