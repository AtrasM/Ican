package avida.ican.Farzin.View.Adapter.Chat;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;
import avida.ican.Farzin.View.Interface.Chat.Room.ListenerAdapterChatRoom;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2019-12-28 ar 1:38 PM
 */


public class AdapterChatRoom extends RecyclerView.Adapter<AdapterChatRoom.ViewHolder> {

    private List<StructureChatRoomDB> itemList;
    private int layout = R.layout.item_chat_room_list;
    private ImageLoader imageLoader;
    private ListenerAdapterChatRoom listenerAdapterChatRoom;

    public AdapterChatRoom(List<StructureChatRoomDB> itemList, ListenerAdapterChatRoom listenerAdapterChatRoom) {
        imageLoader = App.getImageLoader();
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterChatRoom = listenerAdapterChatRoom;
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        /*  @BindView(R.id.swipe_layout)
                SwipeRevealLayout swipeLayout;
                @BindView(R.id.ln_delete)
                LinearLayout lnDelete;*/

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_last_message)
        TextView txtLastMessage;
        @BindView(R.id.txt_unseen_count)
        TextView txtUnseenCount;
        @BindView(R.id.img_profile)
        ImageView imgProfile;
        @BindView(R.id.img_mute)
        ImageView imgMute;
        @BindView(R.id.img_waiting)
        ImageView imgWaiting;

        @BindView(R.id.ln_main)
        LinearLayout lnMain;
        @BindView(R.id.ln_date)
        LinearLayout lnDate;
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
    public AdapterChatRoom.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        final StructureChatRoomDB item = itemList.get(position);

        /*String[] splitDateTime = CustomFunction.MiladyToJalaly(item.getLastMessageCreationDate().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);*/

        viewHolder.txtDate.setText("" + item.getLastMessageCreationTime());
        Log.i("ChatData", "itemPosition= " + (position + 1) + " | roomID= " + item.getId() + " | chatRoomIdString= " + item.getChatRoomIDString());
        if (item.getUnseenCount() > 0) {
            viewHolder.txtUnseenCount.setVisibility(View.VISIBLE);
            viewHolder.txtUnseenCount.setText("" + item.getUnseenCount());
        } else {
            viewHolder.txtUnseenCount.setVisibility(View.GONE);
            viewHolder.txtUnseenCount.setText("-");
        }

        new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtLastMessage, item.getLastMessageContent());

        if (item.isMuteNotification()) {
            viewHolder.imgMute.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgMute.setVisibility(View.GONE);
        }

        String Char = item.getName().substring(0, 1);
        viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        viewHolder.txtName.setText(item.getName());

        if (position == itemList.size() - 1) {
            viewHolder.lnDivider.setVisibility(View.GONE);
        } else {
            viewHolder.lnDivider.setVisibility(View.VISIBLE);
        }

        viewHolder.itemView.setOnClickListener(view -> listenerAdapterChatRoom.onItemClick(item, position));
    }

    public void updateItem(StructureChatRoomDB structureChatRoomDBS) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == structureChatRoomDBS.getId()) {
                itemList.remove(i);
                notifyItemRemoved(i);
                itemList.add(i, structureChatRoomDBS);
                notifyDataSetChanged();
                break;
            }
        }
    }


    public void updateData(List<StructureChatRoomDB> structureChatRoomDBS) {
        itemList.clear();
        itemList.addAll(new ArrayList<>(structureChatRoomDBS));
        this.notifyDataSetChanged();
    }

    public int getDataSize() {
        return itemList.size();
    }


    public void AddNewData(int pos, List<StructureChatRoomDB> structureChatRoomDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureChatRoomDBS);
            notifyItemRangeInserted(start, structureChatRoomDBS.size());
        } else {
            itemList.addAll(pos, structureChatRoomDBS);
            notifyItemRangeInserted(pos, structureChatRoomDBS.size());
        }
        notifyDataSetChanged();
    }

    public void AddNewData(int pos, StructureChatRoomDB structureChatRoomDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureChatRoomDBS);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureChatRoomDBS);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
    }

    public void updateData(int pos, StructureChatRoomDB structureChatRoomDBS) {
        if (pos >= 0) {
            itemList.remove(pos);
            notifyItemRemoved(pos);
            itemList.add(pos, structureChatRoomDBS);
            notifyItemRangeInserted(pos, 1);
        }
        notifyDataSetChanged();
    }

    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    public void filter(List<StructureChatRoomDB> structureChatRoomDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureChatRoomDBS);
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