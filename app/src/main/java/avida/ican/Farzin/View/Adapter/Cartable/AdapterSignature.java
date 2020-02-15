package avida.ican.Farzin.View.Adapter.Cartable;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
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

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterSignature;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static avida.ican.Ican.BaseActivity.closeKeyboard;


/**
 * Created by AtrasVida in 2019-05-29 ar 12:55
 */


public class AdapterSignature extends RecyclerView.Adapter<AdapterSignature.ViewHolder> {

    private List<StructureCartableDocumentSignatureDB> itemList;
    private int layout = R.layout.item_signature;
    private ListenerAdapterSignature listenerAdapterSignature;

    public AdapterSignature(List<StructureCartableDocumentSignatureDB> itemList, ListenerAdapterSignature listenerAdapterSignature) {
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterSignature = listenerAdapterSignature;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_check_box)
        ImageView imgCheck;
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
    public AdapterSignature.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        final StructureCartableDocumentSignatureDB item = itemList.get(position);

        CheckItemSelected(item, viewHolder, true);


        viewHolder.txtName.setText(item.getFN());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                closeKeyboard();
                item.setSelected(!item.isSelected());
                CheckItemSelected(item, viewHolder, false);
            }
        });
        if (position == itemList.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.GONE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }

    }

    private void CheckItemSelected(StructureCartableDocumentSignatureDB item, ViewHolder viewHolder, boolean a_System) {
        if (item.isSelected()) {
            viewHolder.imgCheck.setImageDrawable(Resorse.getDrawable(R.drawable.ic_check_box_select));
            if (!a_System) {
                listenerAdapterSignature.onSelect(item);
            }
        } else {
            viewHolder.imgCheck.setImageDrawable(Resorse.getDrawable(R.drawable.ic_check_box_unselect));
            if (!a_System) {
                listenerAdapterSignature.unSelect(item);
            }
        }
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