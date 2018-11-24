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
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableDocumentList;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.shts.android.library.TriangleLabelView;


/**
 * Created by AtrasVida in 97-09-23 ar 1:10 PM
 */


//public class AdapteCartableDocument extends RecyclerView.Adapter<AdapteCartableDocument.ViewHolder> {
public class AdapteCartableDocument extends RecyclerSwipeAdapter<AdapteCartableDocument.ViewHolder> {

    private static final int NORMAL = 1;
    private static final int IMMEDIATE = 2;
    private static final int VERYURGENT = 3;
    private static final int MOMENTARY = 4;
    private ArrayList<StructureInboxDocumentDB> itemList;
    private int layout = R.layout.item_cartable_document_list;
    private ImageLoader imageLoader;
    private ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList;
    private ViewBinderHelper binderHelper;
    private boolean isLnMoreVisible = false;
    private Animator animator;

    public AdapteCartableDocument(ArrayList<StructureInboxDocumentDB> itemList, ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList) {
        imageLoader = App.getImageLoader();
        animator = new Animator(App.CurentActivity);
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterCartableDocumentList = listenerAdapterCartableDocumentList;
        binderHelper = new ViewBinderHelper();
    }


    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.swipe_layout)
        SwipeLayout swipeLayout;
        @BindView(R.id.txt_role_name)
        TextView txtRoleName;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_code)
        TextView txtCode;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.img_attach)
        ImageView imgAttach;
        @BindView(R.id.img_profile)
        ImageView imgProfile;
        @BindView(R.id.img_waiting)
        ImageView imgWaiting;
        @BindView(R.id.ln_hamesh)
        LinearLayout lnHamesh;
        @BindView(R.id.ln_taeed)
        LinearLayout lnTaeed;
        @BindView(R.id.ln_send)
        LinearLayout lnSend;
        @BindView(R.id.ln_main)
        LinearLayout lnMain;
        @BindView(R.id.ln_more)
        LinearLayout lnMore;
        @BindView(R.id.ln_list_hamesh)
        LinearLayout lnListHamesh;
        @BindView(R.id.ln_history_list)
        LinearLayout lnHistoryList;
        @BindView(R.id.ln_zanjire_madrak)
        LinearLayout lnZanjireMadrak;
        @BindView(R.id.tlv)
        TriangleLabelView tlv;


        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapteCartableDocument.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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


        final StructureInboxDocumentDB item = itemList.get(position);
        if (item.isHaveDependency()) {
            viewHolder.imgAttach.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgAttach.setVisibility(View.GONE);
        }
        if (item.getPrivateHameshContent() != null && !item.getPrivateHameshContent().isEmpty()) {
            viewHolder.lnHamesh.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lnHamesh.setVisibility(View.GONE);
        }
        String[] splitDateTime = CustomFunction.MiladyToJalaly(item.getReceiveDate().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);

        if (item.getTitle() != null && !item.getTitle().isEmpty()) {
            viewHolder.txtSubject.setText("" + item.getTitle());
        } else {
            viewHolder.txtSubject.setVisibility(View.GONE);
        }

        if (item.getStatus() == Status.WAITING) {
            viewHolder.imgWaiting.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgWaiting.setVisibility(View.GONE);
        }

        switch (item.getPrioritySend_ID()) {
            case NORMAL: {
                viewHolder.tlv.setVisibility(View.GONE);
                break;
            }
            case IMMEDIATE: {
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_Warning));
                break;
            }
            case VERYURGENT: {
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_orange));
                break;
            }
            case MOMENTARY: {
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_Danger));
                break;
            }
            default: {
                viewHolder.tlv.setVisibility(View.GONE);
                break;
            }
        }

        String Char = item.getSenderName().substring(0, 1);
        viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));

        viewHolder.txtName.setText(item.getSenderName());
        viewHolder.txtRoleName.setText(" [ " + item.getSenderRoleName() + " ] ");
        viewHolder.txtCode.setText("داخلی : " + item.getEntityNumber());
        viewHolder.lnHamesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.Hamesh);
            }
        });

        viewHolder.lnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ShowToast("" + item.getSenderRoleName());
                listenerAdapterCartableDocumentList.onClick(item);
            }
        });

        viewHolder.lnMain.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isLnMoreVisible) {
                    animator.slideInFromDownFast(viewHolder.lnMore);
                    viewHolder.lnMore.setVisibility(View.VISIBLE);
                } else {
                    animator.slideOutToDown(viewHolder.lnMore);
                    viewHolder.lnMore.setVisibility(View.GONE);
                }
                isLnMoreVisible = !isLnMoreVisible;
                return false;
            }
        });

        viewHolder.lnListHamesh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.ListHamesh);
            }
        });
        viewHolder.lnHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.DocumentFlow);
            }
        });
        viewHolder.lnZanjireMadrak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.TheChainOfEvidence);
            }
        });
        viewHolder.lnTaeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.Confirm);
            }
        });
        viewHolder.lnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.Comission);
            }
        });


        //binderHelper.bind(viewHolder.swipeLayout, "" + position);

    }

    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }

    public void updateItem(StructureInboxDocumentDB structureInboxDocumentDB) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getId() == structureInboxDocumentDB.getId()) {
                itemList.remove(i);
                notifyItemRemoved(i);
                itemList.add(i, structureInboxDocumentDB);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void updateData(List<StructureInboxDocumentDB> structureInboxDocumentDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureInboxDocumentDBS);
        notifyDataSetChanged();
    }

    public void deletItem(StructureInboxDocumentDB structureInboxDocumentDB) {
        itemList.remove(structureInboxDocumentDB);
        notifyDataSetChanged();
    }

    public void updateData(int pos, List<StructureInboxDocumentDB> structureInboxDocumentDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.addAll(structureInboxDocumentDBS);
            notifyItemRangeInserted(start, structureInboxDocumentDBS.size());
        } else {
            itemList.addAll(pos, structureInboxDocumentDBS);
            notifyItemRangeInserted(pos, structureInboxDocumentDBS.size());
        }
        notifyDataSetChanged();
    }

    public void updateData(int pos, StructureInboxDocumentDB structureInboxDocumentDBS) {
        //itemList.clear();
        if (pos == -1) {
            int start = itemList.size();
            itemList.add(structureInboxDocumentDBS);
            notifyItemRangeInserted(start, 1);
        } else {
            itemList.add(pos, structureInboxDocumentDBS);
            notifyItemRangeInserted(pos, 1);
        }

        notifyDataSetChanged();
    }

    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
    }

    public void filter(List<StructureInboxDocumentDB> structureInboxDocumentDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureInboxDocumentDBS);
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