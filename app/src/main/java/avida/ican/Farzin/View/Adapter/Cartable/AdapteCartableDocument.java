package avida.ican.Farzin.View.Adapter.Cartable;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterCartableDocumentList;
import avida.ican.Ican.App;
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


public class AdapteCartableDocument extends RecyclerSwipeAdapter<AdapteCartableDocument.ViewHolder> {

    private final int NORMAL = 1;
    private final int IMMEDIATE = 2;
    private final int VERYURGENT = 3;
    private final int MOMENTARY = 4;
    private ArrayList<StructureInboxDocumentDB> itemList;
    private int layout = R.layout.item_cartable_document_list;
    private ImageLoader imageLoader;
    private ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList;
    private String subject = "";

    public AdapteCartableDocument(ArrayList<StructureInboxDocumentDB> itemList, ListenerAdapterCartableDocumentList listenerAdapterCartableDocumentList) {
        imageLoader = App.getImageLoader();
        this.itemList = new ArrayList<>(itemList);
        //itemList.get(0).setbInWorkFlow(true);
        this.listenerAdapterCartableDocumentList = listenerAdapterCartableDocumentList;
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
        @BindView(R.id.txt_document_type)
        TextView txtDocumentType;
        @BindView(R.id.txt_subject)
        TextView txtSubject;
        @BindView(R.id.txt_hint_subject)
        TextView txtHintSubject;
        @BindView(R.id.img_attach)
        ImageView imgAttach;
        @BindView(R.id.img_profile)
        ImageView imgProfile;
        @BindView(R.id.img_waiting)
        ImageView imgWaiting;
        @BindView(R.id.img_seen)
        ImageView imgSeen;
        @BindView(R.id.img_more)
        ImageView imgMore;
        @BindView(R.id.img_confirm)
        ImageView imgConfirm;
        @BindView(R.id.img_inworkflow)
        ImageView imgInWorkFlow;
        @BindView(R.id.img_dastor_erja)
        ImageView imgDastorErja;
        @BindView(R.id.img_tozih_shakhsi)
        ImageView imgTozihShakhsi;
        @BindView(R.id.ln_confirm_inworkflow)
        LinearLayout lnConfirm_InWorkFlow;
        @BindView(R.id.ln_confirm_erja)
        LinearLayout lnConfirmErja;
        @BindView(R.id.ln_more)
        LinearLayout lnMore;
        @BindView(R.id.ln_list_hamesh)
        LinearLayout lnListHamesh;
        @BindView(R.id.ln_history_list)
        LinearLayout lnHistoryList;
        @BindView(R.id.ln_zanjire_madrak)
        LinearLayout lnZanjireMadrak;
        @BindView(R.id.ln_main)
        LinearLayout lnMain;
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandableLayout;
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

        if (item.isbInWorkFlow()) {
            viewHolder.swipeLayout.setLeftSwipeEnabled(false);
            viewHolder.imgInWorkFlow.setVisibility(View.VISIBLE);
            viewHolder.imgConfirm.setVisibility(View.GONE);
        } else {
            viewHolder.swipeLayout.setLeftSwipeEnabled(true);
            viewHolder.imgInWorkFlow.setVisibility(View.GONE);
            viewHolder.imgConfirm.setVisibility(View.VISIBLE);
        }

        if (item.isHaveDependency()) {
            viewHolder.imgAttach.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgAttach.setVisibility(View.GONE);
        }
        if (item.getPrivateHameshContent() != null && !item.getPrivateHameshContent().isEmpty()) {
            viewHolder.imgDastorErja.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgDastorErja.setVisibility(View.GONE);
        }
        if (item.getUserDescription() != null && !item.getUserDescription().isEmpty()) {
            viewHolder.imgTozihShakhsi.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgTozihShakhsi.setVisibility(View.GONE);
        }

        String[] splitDateTime = CustomFunction.MiladyToJalaly(item.getReceiveDate().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        viewHolder.txtDate.setText(date);
        viewHolder.txtTime.setText(time);

        viewHolder.txtDocumentType.setText("" + item.getEntityTypeName());

        if (item.getTitle() != null && !item.getTitle().isEmpty()) {
            new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtSubject, "" + item.getTitle());
        } else {
            viewHolder.txtSubject.setText("-");
        }

        if (item.getStatus() == Status.WAITING) {
            viewHolder.imgWaiting.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgWaiting.setVisibility(View.GONE);
        }
        // new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.imgConfirm, R.color.color_White);
        if (item.isRead()) {
            viewHolder.imgSeen.setVisibility(View.GONE);
            viewHolder.txtName.setTextColor(Resorse.getColor(R.color.color_txt_Normal));
            viewHolder.txtRoleName.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            viewHolder.txtSubject.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
            viewHolder.txtHintSubject.setTextColor(Resorse.getColor(R.color.color_txt_SubTitle));
        } else {
            viewHolder.imgSeen.setVisibility(View.VISIBLE);
            viewHolder.txtName.setTextColor(Resorse.getColor(R.color.color_Info));
            viewHolder.txtRoleName.setTextColor(Resorse.getColor(R.color.color_Info));
            viewHolder.txtSubject.setTextColor(Resorse.getColor(R.color.color_Info));
            viewHolder.txtHintSubject.setTextColor(Resorse.getColor(R.color.color_Info));
        }

        switch (item.getPrioritySend_ID()) {
            case IMMEDIATE: {
                viewHolder.tlv.setVisibility(View.VISIBLE);
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_Warning));
                break;
            }
            case VERYURGENT: {
                viewHolder.tlv.setVisibility(View.VISIBLE);
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_orange));
                break;
            }
            case MOMENTARY: {
                viewHolder.tlv.setVisibility(View.VISIBLE);
                viewHolder.tlv.setTriangleBackgroundColor(Resorse.getColor(R.color.color_Red));
                break;
            }
            default: {
                viewHolder.tlv.setVisibility(View.GONE);
                break;
            }
        }

        if (!item.getSenderLastName().isEmpty() && item.getSenderLastName().length() >= 1) {
            String Char = item.getSenderLastName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        } else {
            String Char = item.getSenderFirstName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        }

        viewHolder.txtName.setText(item.getSenderName());
        viewHolder.txtRoleName.setText("[ " + item.getSenderRoleName() + " ] ");
        if (item.getImportEntityNumber() != null && !item.getImportEntityNumber().isEmpty()) {
            viewHolder.txtCode.setText(Resorse.getString(R.string.hint_import_entity_number) + item.getImportEntityNumber());
        } else {
            viewHolder.txtCode.setText(Resorse.getString(R.string.hint_entity_number) + item.getEntityNumber());
        }

        viewHolder.imgDastorErja.setOnClickListener(view -> listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.DstorErja));
        viewHolder.imgTozihShakhsi.setOnClickListener(view -> listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.TozihShakhsi));

        viewHolder.lnMain.setOnClickListener(v -> {
            if (item.isLnMoreVisible()) {
                item.setLnMoreVisible(!item.isLnMoreVisible());
                checkLnMoreVisible(item.isLnMoreVisible(), viewHolder);
            } else {
                listenerAdapterCartableDocumentList.onClick(item);
            }
        });

        checkLnMoreVisible(item.isLnMoreVisible(), viewHolder);

        viewHolder.imgMore.setOnClickListener(view -> {
            item.setLnMoreVisible(!item.isLnMoreVisible());
            checkLnMoreVisible(item.isLnMoreVisible(), viewHolder);
        });

        viewHolder.lnListHamesh.setOnClickListener(view -> listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.ListHamesh));
        viewHolder.lnHistoryList.setOnClickListener(view -> listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.DocumentFlow));
        viewHolder.lnZanjireMadrak.setOnClickListener(view -> listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.TheChainOfEvidence));
        viewHolder.lnConfirm_InWorkFlow.setOnClickListener(view -> {
            if (item.isbInWorkFlow()) {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.InWorkFlow);
            } else {
                listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.Confirm);
            }
            if (viewHolder.swipeLayout != null) {
                viewHolder.swipeLayout.close();
            }
        });
        viewHolder.lnConfirmErja.setOnClickListener(view -> {
            listenerAdapterCartableDocumentList.onAction(item, CartableActionsEnum.ConfirmSend);
            if (viewHolder.swipeLayout != null) {
                viewHolder.swipeLayout.close();
            }
        });

    }

    private void checkLnMoreVisible(boolean isLnMoreVisible, ViewHolder viewHolder) {
        if (isLnMoreVisible) {
            viewHolder.txtSubject.setMaxLines(Integer.MAX_VALUE);
            viewHolder.expandableLayout.expand();
            viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_up));
        } else {
            viewHolder.txtSubject.setMaxLines(1);
            viewHolder.expandableLayout.collapse();
            viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_down));
        }
    }

    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(() -> App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME));
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
        itemList.clear();
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