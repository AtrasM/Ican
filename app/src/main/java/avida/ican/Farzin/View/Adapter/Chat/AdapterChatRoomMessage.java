package avida.ican.Farzin.View.Adapter.Chat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.bassaer.chatmessageview.model.ChatUser;
import com.github.bassaer.chatmessageview.model.Message;
import com.github.bassaer.chatmessageview.view.MessageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.View.Interface.Chat.Room.ListenerAdapterChatRoom;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2020-01-14 ar 5:01 PM
 */


public class AdapterChatRoomMessage extends RecyclerView.Adapter<AdapterChatRoomMessage.ViewHolder> {

    private List<StructureChatRoomMessageDB> itemList;
    private int layout = R.layout.item_chat_room_message;
    private ImageLoader imageLoader;
    private ListenerAdapterChatRoom listenerAdapterChatRoom;

    public AdapterChatRoomMessage(List<StructureChatRoomMessageDB> itemList) {
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

        @BindView(R.id.message_view)
        MessageView messageView;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterChatRoomMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        final StructureChatRoomMessageDB item = itemList.get(position);
        Bitmap myIcon = BitmapFactory.decodeResource(App.CurentActivity.getResources(), R.drawable.face_1);
        ChatUser chatUser = new ChatUser(item.getId(), item.getUserFullName(),myIcon);

        //new message

        //Set to chat view
        Message message;
        if (item.isCurrentUserIsWriter()) {
             message = new Message.Builder()
                    .setUser(chatUser)
                    .setText(item.getMessageContent())
                    .setRight(true)
                    .build();
        } else {
            message = new Message.Builder()
                    .setUser(chatUser)
                    .setText(item.getMessageContent())
                    .setRight(true)
                    .build();
        }
        viewHolder.messageView.init();
        viewHolder.messageView.setMessage(message);


    }

  /*  public void updateItem(StructureChatRoomListDB structureChatRoomListDBS) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == structureChatRoomListDBS.getId()) {
                itemList.remove(i);
                notifyItemRemoved(i);
                itemList.add(i, structureChatRoomListDBS);
                notifyDataSetChanged();
                break;
            }
        }
    }*/


    public void updateData(List<StructureChatRoomMessageDB> structureChatRoomMessageDBS) {
        itemList.clear();
        itemList.addAll(new ArrayList<>(structureChatRoomMessageDBS));
        this.notifyDataSetChanged();
    }

    public int getDataSize() {
        return itemList.size();
    }


/*
    public void AddNewData(int pos, List<StructureChatRoomListDB> structureChatRoomListDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureChatRoomListDBS);
            notifyItemRangeInserted(start, structureChatRoomListDBS.size());
        } else {
            itemList.addAll(pos, structureChatRoomListDBS);
            notifyItemRangeInserted(pos, structureChatRoomListDBS.size());
        }
        notifyDataSetChanged();
    }
*/

 /*   public void AddNewData(int pos, StructureChatRoomListDB structureChatRoomListDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureChatRoomListDBS);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureChatRoomListDBS);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
    }*/

    /* public void updateData(int pos, StructureChatRoomListDB structureChatRoomListDBS) {
         if (pos >= 0) {
             itemList.remove(pos);
             notifyItemRemoved(pos);
             itemList.add(pos, structureChatRoomListDBS);
             notifyItemRangeInserted(pos, 1);
         }
         notifyDataSetChanged();
     }
 */
    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    /*  public void filter(List<StructureChatRoomListDB> structureChatRoomListDBS) {
          itemList = new ArrayList<>();
          itemList.addAll(structureChatRoomListDBS);
          App.CurentActivity.runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  notifyDataSetChanged();
              }
          });

      }
  */
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