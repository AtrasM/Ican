package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.View.Interface.Queue.ListenerAdapterMessageQueue;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-04-30 ar 3:45
 */


public class AdapterQueueMessage extends RecyclerSwipeAdapter<AdapterQueueMessage.ViewHolder> {

    private List<StructureMessageQueueDB> itemList;
    private int layout = R.layout.item_queue_message_list;
    private ImageLoader imageLoader;
    private ListenerAdapterMessageQueue listenerAdapterMessageQueue;

    public AdapterQueueMessage(List<StructureMessageQueueDB> itemList, ListenerAdapterMessageQueue listenerAdapterMessageQueue) {
        imageLoader = App.getImageLoader();
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterMessageQueue = listenerAdapterMessageQueue;
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.swipe_layout)
        SwipeLayout swipeLayout;
        @BindView(R.id.ln_delete)
        LinearLayout lnDelete;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_role_name)
        TextView txtRoleName;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.img_profile)
        ImageView imgProfile;
        @BindView(R.id.img_waiting)
        ImageView imgWaiting;

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
    public AdapterQueueMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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


        final StructureMessageQueueDB item = itemList.get(position);
        final ArrayList<StructureReceiverDB> structureReceiverDBS = new ArrayList<>(item.getMessage().getReceivers());
        String Name = "";
        final ArrayList<StructureMessageFileDB> structureMessageFileDBS = new ArrayList<>(item.getMessage().getMessage_files());
     /*   if (structureMessageFileDBS != null && structureMessageFileDBS.size() > 0) {
            viewHolder.imgAttach.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgAttach.setVisibility(View.GONE);
        }*/

        viewHolder.txtSubject.setText("" + item.getMessage().getSubject());

        if (structureReceiverDBS.size() == 1) {
            viewHolder.txtRoleName.setVisibility(View.VISIBLE);
            StructureUserAndRoleDB structureUserAndRoleDB;
            if (structureReceiverDBS.get(0).getRole_id() <= 0) {
                structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(structureReceiverDBS.get(0).getUser_id());

            } else {
                structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(structureReceiverDBS.get(0).getUser_id(), structureReceiverDBS.get(0).getRole_id());

            }
            Name = "" + structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();

            viewHolder.imgWaiting.setVisibility(View.VISIBLE);


            if (!structureUserAndRoleDB.getLastName().isEmpty() && structureUserAndRoleDB.getLastName().length() > 1) {
                String Char = structureUserAndRoleDB.getLastName().substring(0, 1);
                viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            } else {
                String Char = structureUserAndRoleDB.getFirstName().substring(0, 1);
                viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
            }
            viewHolder.txtName.setText(Name);
            viewHolder.txtRoleName.setText("[ " + structureUserAndRoleDB.getRoleName() + " ] ");

        } else {
            viewHolder.imgProfile.setImageDrawable(Resorse.getDrawable(R.drawable.ic_group));
            viewHolder.txtName.setText(Resorse.getString(R.string.Group_Message));
            viewHolder.txtRoleName.setText("");
            viewHolder.txtRoleName.setVisibility(View.GONE);
        }
        viewHolder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (item.getStatus() != Status.WAITING && item.getStatus() != Status.STOPED) {
                    itemList.get(position).setStatus(Status.READ);
                    StructureDetailMessageBND structureDetailMessageBND = new StructureDetailMessageBND(item.getId(), item.getMain_id(), item.getSender_user_id(), item.getSender_role_id(), viewHolder.txtName.getText().toString(), viewHolder.txtRoleName.getText().toString(), item.getSubject(), item.getContent(), date, time, structureMessageFileDBS, structureReceiverDBS, Type.SENDED);
                    listenerAdapterMessageList.onItemClick(structureDetailMessageBND, position);
                    notifyDataSetChanged();
                }*/

            }
        });
        viewHolder.lnMain.setOnLongClickListener(view -> {
            listenerAdapterMessageQueue.onDelet(item);
            return true;
        });
        viewHolder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (item.getStatus() != Status.WAITING && item.getStatus() != Status.STOPED) {
                listenerAdapterMessageQueue.onDelet(item);
                //}

            }
        });

    }

    public void updateItem(StructureMessageQueueDB structureMessageQueueDB) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == structureMessageQueueDB.getId()) {
                itemList.remove(i);
                notifyItemRemoved(i);
                itemList.add(i, structureMessageQueueDB);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateData(List<StructureMessageQueueDB> structureMessageQueueDB) {
        itemList = new ArrayList<>();
        itemList.addAll(structureMessageQueueDB);
        notifyDataSetChanged();
    }

    public void updateData(int pos, List<StructureMessageQueueDB> structureMessageQueueDB) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureMessageQueueDB);
            notifyItemRangeInserted(start, structureMessageQueueDB.size());
        } else {
            itemList.addAll(pos, structureMessageQueueDB);
            notifyItemRangeInserted(pos, structureMessageQueueDB.size());
        }
        notifyDataSetChanged();
    }

    public void updateData(int pos, StructureMessageQueueDB structureMessageQueueDB) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureMessageQueueDB);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureMessageQueueDB);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
    }

    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    public void filter(List<StructureMessageQueueDB> structureMessageQueueDB) {
        itemList = new ArrayList<>();
        itemList.addAll(structureMessageQueueDB);
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

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }
}