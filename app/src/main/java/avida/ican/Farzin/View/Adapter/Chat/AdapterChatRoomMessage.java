package avida.ican.Farzin.View.Adapter.Chat;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ListenerAdapterChatRoomMessage;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2020-01-14 ar 5:01 PM
 */


public class AdapterChatRoomMessage extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_DATE = 0;

    private List<StructureChatRoomMessageDB> mMessageList;
    private ListenerAdapterChatRoomMessage listenerAdapterChatRoomMessage;

    public AdapterChatRoomMessage(List<StructureChatRoomMessageDB> messageList, ListenerAdapterChatRoomMessage listenerAdapterChatRoomMessage) {
        mMessageList = messageList;
        this.listenerAdapterChatRoomMessage = listenerAdapterChatRoomMessage;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        StructureChatRoomMessageDB message = mMessageList.get(position);
        if (message.isCurrentUserIsWriter()) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_message_sent, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            return new SentMessageHolder(itemLayoutView);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_text_message_received, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            return new ReceivedMessageHolder(itemLayoutView);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StructureChatRoomMessageDB message = mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT: {
                ((SentMessageHolder) holder).bind(message, position);
                break;
            }

            case VIEW_TYPE_MESSAGE_RECEIVED: {
                ((ReceivedMessageHolder) holder).bind(message, position);
                break;
            }


        }
    }

  /*  class DateHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_chat_message_date)
        TextView txtDate;

        DateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(StructureChatRoomMessageDB message, int position) {
            txtDate.setText(message.getPersianCreationDay());
        }
    }*/

    class SentMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ln_chat_message_date)
        LinearLayout lnDate;
        @BindView(R.id.txt_chat_message_date)
        TextView txtDate;
        @BindView(R.id.ln_reply)
        LinearLayout lnReply;
        @BindView(R.id.txt_reply_message)
        TextView txtReplyMessage;
        @BindView(R.id.txt_reply_name)
        TextView txtReplyName;
        @BindView(R.id.text_message_body)
        TextView messageText;
        @BindView(R.id.text_message_time)
        TextView timeText;
        @BindView(R.id.img_state)
        ImageView imgState;

        SentMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void bind(StructureChatRoomMessageDB message, int position) {

            if (isDateDiff(message, position)) {
                lnDate.setVisibility(View.VISIBLE);
                txtDate.setText(message.getPersianCreationDay());
            } else {
                lnDate.setVisibility(View.GONE);
            }

            if (message.isSendedFromApp()) {
                timeText.setVisibility(View.GONE);
                imgState.setImageDrawable(Resorse.getDrawable(R.drawable.ic_waiting));
            } else {
                if (message.getSeenCount() > 1) {
                    imgState.setImageDrawable(Resorse.getDrawable(R.drawable.ic_d_tick));
                } else {
                    imgState.setImageDrawable(Resorse.getDrawable(R.drawable.ic_tick));
                }
            }

            if (message.getReplyToMessageID() > 0) {
                lnReply.setVisibility(View.VISIBLE);
                txtReplyMessage.setText(message.getReplyToMessageContent());
                txtReplyName.setText(message.getReplyToMessageUser());
            } else {
                lnReply.setVisibility(View.GONE);
            }

            lnReply.setOnClickListener(view -> listenerAdapterChatRoomMessage.onReplyClick(message, mMessageList.size(), position));

            messageText.setText(message.getMessageContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getPersianCreationTime());
        }
    }

    private boolean isDateDiff(StructureChatRoomMessageDB message, int position) {
        int pos = position - 1;
        if (pos >= 0) {
            if (!mMessageList.get(pos).getPersianCreationDay().equals(message.getPersianCreationDay())) {
                return true;
            }
        }
        return false;
    }

    class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ln_chat_message_date)
        LinearLayout lnDate;
        @BindView(R.id.txt_chat_message_date)
        TextView txtDate;
        @BindView(R.id.ln_reply)
        LinearLayout lnReply;
        @BindView(R.id.txt_reply_message)
        TextView txtReplyMessage;
        @BindView(R.id.txt_reply_name)
        TextView txtReplyName;
        @BindView(R.id.text_message_body)
        TextView messageText;
        @BindView(R.id.text_message_time)
        TextView timeText;
        @BindView(R.id.text_message_name)
        TextView nameText;
        @BindView(R.id.image_message_profile)
        ImageView profileImage;
        @BindView(R.id.img_state)
        ImageView imgState;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(StructureChatRoomMessageDB message, int position) {
            if (isDateDiff(message, position)) {
                lnDate.setVisibility(View.VISIBLE);
                txtDate.setText(message.getPersianCreationDay());
            } else {
                lnDate.setVisibility(View.GONE);
            }

            /*if (message.isSeen()) {
                imgState.setImageDrawable(Resorse.getDrawable(R.drawable.ic_d_tick));
            } else {
                imgState.setImageDrawable(Resorse.getDrawable(R.drawable.ic_tick));
            }*/

            if (message.getReplyToMessageID() > 0) {
                lnReply.setVisibility(View.VISIBLE);
                txtReplyMessage.setText(message.getReplyToMessageContent());
                txtReplyName.setText(message.getReplyToMessageUser());
            } else {
                lnReply.setVisibility(View.GONE);
            }

            lnReply.setOnClickListener(view -> listenerAdapterChatRoomMessage.onReplyClick(message, mMessageList.size(), position));

            messageText.setText(message.getMessageContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getPersianCreationTime());
            int pos = position + 1;
            if (pos < mMessageList.size()) {
                if (mMessageList.get(pos).getUserFullName().equals(message.getUserFullName())) {
                    nameText.setVisibility(View.GONE);
                    profileImage.setVisibility(View.GONE);
                } else {
                    nameText.setVisibility(View.VISIBLE);
                    profileImage.setVisibility(View.VISIBLE);
                }
            } else {
                nameText.setVisibility(View.VISIBLE);
                profileImage.setVisibility(View.VISIBLE);
            }

            nameText.setText(message.getUserFullName());

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }


    public void updateData(List<StructureChatRoomMessageDB> messageList) {
        mMessageList.clear();
        mMessageList.addAll(messageList);
        notifyDataSetChanged();
    }

    public void addDataToEnd(List<StructureChatRoomMessageDB> messageList) {
        mMessageList.addAll(mMessageList.size(), messageList);
        notifyDataSetChanged();
    }

    public void addDataToEnd(StructureChatRoomMessageDB message) {
        mMessageList.add(mMessageList.size(), message);
        notifyDataSetChanged();
    }

    public void addDataToFirst(StructureChatRoomMessageDB message) {
        mMessageList.add(0, message);
        notifyDataSetChanged();
    }

    public void deletItem(String messageIDString) {
        //StructureChatRoomMessageDB message = new StructureChatRoomMessageDB();
        //message.setMessageIDString(messageIDString);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mMessageList.removeIf((StructureChatRoomMessageDB message) -> message.getMessageIDString().equals(messageIDString));
        } else {
            for (int i = 0; i < mMessageList.size(); i++) {
                if (mMessageList.get(i).getMessageIDString().equals(messageIDString)) {
                    mMessageList.remove(i);
                    notifyItemRemoved(i);
                    break;
                }
            }
            notifyDataSetChanged();
        }

    }

    public StructureChatRoomMessageDB getLastMessage() {
        if (mMessageList != null && mMessageList.size() > 0) {
            return mMessageList.get(mMessageList.size() - 1);
        }
        return new StructureChatRoomMessageDB();
    }

}