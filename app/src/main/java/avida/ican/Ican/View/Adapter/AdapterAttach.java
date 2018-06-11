package avida.ican.Ican.View.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogDelet;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerDelet;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2018-06-02 ar 11:57
 */


public class AdapterAttach extends RecyclerView.Adapter<AdapterAttach.ViewHolder> {

    private ArrayList<StructureAttach> itemList;
    private int layout = R.layout.item_attach;
    private ImageLoader imageLoader;
    private Activity context;

    public AdapterAttach(Activity context, ArrayList<StructureAttach> itemList) {
        imageLoader = App.getImageLoader();
        this.itemList = itemList;
        this.context = context;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_attach_icon)
        ImageView imgAttachIcon;
        @BindView(R.id.img_delet)
        ImageView imgDelet;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterAttach.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final StructureAttach item = itemList.get(position);
        viewHolder.txtName.setText(item.getName());
       viewHolder.imgAttachIcon.setBackground( Resorse.getDrawable(item.getIcon()));
        viewHolder.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogDelet(context).setOnListener(new ListenerDelet() {
                    @Override
                    public void onSuccess() {
                        delet(item);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).Show();
            }
        });
    }

    public void add(StructureAttach structureAttach) {
        itemList.add(structureAttach);
        notifyItemRangeChanged(itemList.size() - 1, 1);
    }
  public void addAll(ArrayList<StructureAttach> structureAttachs) {
        itemList.addAll(structureAttachs);
        notifyItemRangeChanged(itemList.size() - 1, structureAttachs.size());
    }


    public void delet(StructureAttach structureAttach) {
        int pos = itemList.indexOf(structureAttach);
        if (pos > -1) {
            itemList.remove(structureAttach);
            notifyItemRemoved(pos);
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