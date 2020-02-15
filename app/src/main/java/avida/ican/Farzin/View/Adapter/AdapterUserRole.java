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

import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static avida.ican.Ican.BaseActivity.closeKeyboard;


/**
 * Created by AtrasVida in 2019-07-01 ar 5:41 PM
 */


public class AdapterUserRole extends RecyclerView.Adapter<AdapterUserRole.ViewHolder> {

    private List<StructureUserRoleDB> itemList;
    private StructureUserRoleDB itemSelect = new StructureUserRoleDB();
    private ImageView lastImageViewSelected;
    private int lastItemSelectPos = -1;
    private int layout = R.layout.item_user_role;
    private final int curentRoleID;

    public AdapterUserRole(List<StructureUserRoleDB> itemList, int curentRoleID) {
        this.itemList = new ArrayList<>(itemList);
        this.curentRoleID = curentRoleID;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_radio_button)
        ImageView imgRadioButton;
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
    public AdapterUserRole.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        final StructureUserRoleDB item = itemList.get(position);
        if (lastItemSelectPos == -1) {
            if (item.getRoleID() == curentRoleID) {
                CheckItemSelected(item, position, viewHolder);
            }
        }
        if (item.isSelected()) {
            viewHolder.imgRadioButton.setImageResource(R.drawable.ic_radio_button_select);
        } else {
            viewHolder.imgRadioButton.setImageResource(R.drawable.ic_radio_button_unselect);
        }
        viewHolder.txtName.setText(item.getRoleName());
        viewHolder.itemView.setOnClickListener(v -> {
            closeKeyboard();
            item.setSelected(true);
            CheckItemSelected(item, position, viewHolder);
        });
        if (position == itemList.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.GONE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }
    }

    private void CheckItemSelected(StructureUserRoleDB item, int position, ViewHolder viewHolder) {
        if (position != lastItemSelectPos) {
            viewHolder.imgRadioButton.setImageResource(R.drawable.ic_radio_button_select);
            if (lastImageViewSelected != null) {
                lastImageViewSelected.setImageResource(R.drawable.ic_radio_button_unselect);
            }
            lastImageViewSelected = viewHolder.imgRadioButton;
            itemSelect = item;
            if (lastItemSelectPos >= 0) {
                itemList.get(lastItemSelectPos).setSelected(false);
            }
            itemList.get(position).setSelected(true);
            lastItemSelectPos = position;
        }
    }

    public StructureUserRoleDB getItemselected() {
        return itemSelect;
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