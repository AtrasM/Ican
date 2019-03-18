package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.View.Interface.Message.ListenerAdapterMessageList;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-04-30 ar 3:45
 */


public class AdapterReceiveMessage extends RecyclerView.Adapter<AdapterReceiveMessage.ViewHolder> {

    private List<StructureMessageDB> itemList;
    private int layout = R.layout.item_message_list;
    private ImageLoader imageLoader;
    private ListenerAdapterMessageList listenerAdapterMessageList;
    private ViewBinderHelper binderHelper;
    private StructureUserAndRoleDB structureUserAndRoleDB;

    public AdapterReceiveMessage(List<StructureMessageDB> itemList, ListenerAdapterMessageList listenerAdapterMessageList) {
        imageLoader = App.getImageLoader();
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterMessageList = listenerAdapterMessageList;
        binderHelper = new ViewBinderHelper();
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID(), farzinPrefrences.getRoleID());
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {


        /*   @BindView(R.id.swipe_layout)
        SwipeRevealLayout swipeLayout;
        @BindView(R.id.ln_delete)
        LinearLayout lnDelete;*/

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_role_name)
        TextView txtRoleName;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.img_attach)
        ImageView imgAttach;
        @BindView(R.id.img_profile)
        ImageView imgProfile;
        @BindView(R.id.img_seen)
        ImageView imgSeen;
        @BindView(R.id.ln_main)
        LinearLayout lnMain;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterReceiveMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        final StructureMessageDB item = itemList.get(position);
        ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>();

        final String Name = "" + structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();

        viewHolder.txtName.setText(Name);

        if (!structureUserAndRoleDB.getLastName().isEmpty() && structureUserAndRoleDB.getLastName().length() >= 1) {
            String Char = structureUserAndRoleDB.getLastName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        } else {
            String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        }

        viewHolder.txtRoleName.setText("[ " + structureUserAndRoleDB.getRoleName() + " ] ");
        String[] splitDateTime = CustomFunction.MiladyToJalaly(item.getSent_date().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);

        if (item.getMessage_files() != null) {
            structureMessageFileDBS = new ArrayList<>(item.getMessage_files());
            if (structureMessageFileDBS.size() > 0) {
                viewHolder.imgAttach.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgAttach.setVisibility(View.GONE);
            }
        } else {
            viewHolder.imgAttach.setVisibility(View.GONE);
        }
        if (item.getStatus() == Status.READ) {
            viewHolder.imgSeen.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.imgSeen.setVisibility(View.VISIBLE);
        }
        viewHolder.txtSubject.setText(item.getSubject());
        final ArrayList<StructureMessageFileDB> finalStructureMessageFileDBS = structureMessageFileDBS;
        viewHolder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.get(position).setStatus(Status.READ);
                StructureDetailMessageBND structureDetailMessageBND = new StructureDetailMessageBND(item.getId(), item.getMain_id(), item.getSender_user_id(), item.getSender_role_id(), Name, viewHolder.txtRoleName.getText().toString(), item.getSubject(), item.getContent(), date, time, finalStructureMessageFileDBS,new ArrayList<StructureReceiverDB>(), Type.RECEIVED);
                listenerAdapterMessageList.onItemClick(structureDetailMessageBND,position);
                notifyDataSetChanged();
            }
        });
 /*       viewHolder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterMessageList.onDelet(item);
            }
        });
        binderHelper.bind(viewHolder.swipeLayout, "" + position);*/

    }

    public void updateData(List<StructureMessageDB> mstructuresSentMessages) {
        itemList = new ArrayList<>();
        itemList.addAll(mstructuresSentMessages);
        notifyDataSetChanged();
    }

    public void updateData(int pos, List<StructureMessageDB> mstructuresSentMessages) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(mstructuresSentMessages);
            notifyItemRangeInserted(start, mstructuresSentMessages.size());
        } else {
            itemList.addAll(pos, mstructuresSentMessages);
            notifyItemRangeInserted(pos, mstructuresSentMessages.size());
        }
        notifyDataSetChanged();
    }
   /* public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }*/

    public void filter(List<StructureMessageDB> structureMessageDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureMessageDBS);
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