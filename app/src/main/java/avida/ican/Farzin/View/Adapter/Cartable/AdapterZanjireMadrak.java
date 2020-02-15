package avida.ican.Farzin.View.Adapter.Cartable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Structure.Bundle.StructureAdapterZanjireMadrakBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.View.Enum.StructureAdapterZanjireMadrakEnum;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterZanjireMadrak;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 97-10-15 ar 4:32 PM
 */


public class AdapterZanjireMadrak extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<StructureAdapterZanjireMadrakBND> itemList;
    private int layoutFileEntity = R.layout.item_zanjire_madrak;
    private int layoutHeader = R.layout.item_adapter_zanjire_madrak_header;
    private ListenerAdapterZanjireMadrak listenerAdapterZanjireMadrak;
    private Animator animator;
    private RecyclerView recyclerView;

    public AdapterZanjireMadrak(RecyclerView recyclerView, ArrayList<StructureAdapterZanjireMadrakBND> itemList, ListenerAdapterZanjireMadrak listenerAdapterZanjireMadrak) {
        animator = new Animator(App.CurentActivity);
        this.itemList = new ArrayList<>(itemList);
        this.recyclerView = recyclerView;
        this.listenerAdapterZanjireMadrak = listenerAdapterZanjireMadrak;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolderFileEntity extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_data_name)
        TextView txtDataName;
        @BindView(R.id.txt_full_name)
        TextView txtFullName;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_document_number)
        TextView txtDocumentNumber;
        @BindView(R.id.txt_counter)
        TextView txtCounter;
        @BindView(R.id.txt_entity_name)
        TextView txtEntityName;
        @BindView(R.id.txt_document_number_name)
        TextView txtDocumentNumberName;
        @BindView(R.id.img_attach_icon)
        ImageView imgAttachIcon;
        /* @BindView(R.id.ln_divider)
         LinearLayout lnDivider;*/
        @BindView(R.id.ln_entity)
        LinearLayout lnEntity;
        @BindView(R.id.img_more)
        ImageView imgMore;
        @BindView(R.id.ln_date_time)
        LinearLayout lnDateTime;
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandableLayout;

        public ViewHolderFileEntity(View view) {
            super(view);
            // binding view
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_type)
        TextView txtType;
        @BindView(R.id.img_type)
        ImageView imgType;


        public ViewHolderHeader(View view) {
            super(view);
            // binding view
            ButterKnife.bind(this, view);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == StructureAdapterZanjireMadrakEnum.Header.ordinal()) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layoutHeader, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            return new ViewHolderHeader(itemLayoutView);
        } else {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layoutFileEntity, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemLayoutView.setLayoutParams(lp);
            return new ViewHolderFileEntity(itemLayoutView);
        }

    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final StructureAdapterZanjireMadrakBND item = itemList.get(position);
        if (holder.getItemViewType() == StructureAdapterZanjireMadrakEnum.Header.ordinal()) {
            ViewHolderHeader viewHolder = (ViewHolderHeader) holder;
            viewHolder.txtType.setText(item.getZanjireMadrakHeaderBND().getTitle());
            viewHolder.imgType.setImageDrawable(item.getZanjireMadrakHeaderBND().getDrawable());
        } else {
            ViewHolderFileEntity viewHolder = (ViewHolderFileEntity) holder;
            if (item.getZanjireMadrakEnum() == StructureAdapterZanjireMadrakEnum.ZanjireMadrakFile) {
                viewHolder.lnEntity.setVisibility(View.GONE);
                viewHolder.imgMore.setVisibility(View.GONE);
                viewHolder.imgAttachIcon.setImageDrawable(Resorse.getDrawable(R.drawable.ic_attach2));
                StructureZanjireMadrakFileDB zanjireMadrakFileDB = item.getZanjireMadrakFileDB();
                viewHolder.txtFullName.setText(zanjireMadrakFileDB.getCreationFullName() + " [ " + zanjireMadrakFileDB.getCreationRoleName() + " ] ");
                try {
                    if (zanjireMadrakFileDB.getCreationDate() != null) {
                        viewHolder.lnDateTime.setVisibility(View.VISIBLE);
                        String[] splitDateTime = CustomFunction.MiladyToJalaly(zanjireMadrakFileDB.getCreationDate()).split(" ");
                        final String date = splitDateTime[0];
                        final String time = splitDateTime[1];
                        viewHolder.txtDate.setText(date);
                        viewHolder.txtTime.setText(time);
                    } else {
                        viewHolder.lnDateTime.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.lnDateTime.setVisibility(View.GONE);
                }
                String fullName = zanjireMadrakFileDB.getFile_name() + "" + zanjireMadrakFileDB.getFile_extension();
                viewHolder.txtDataName.setText(fullName);
                viewHolder.itemView.setOnClickListener(view -> {
                    if (zanjireMadrakFileDB.getFile_path() != null) {
                        final StructureAttach file = new StructureAttach(zanjireMadrakFileDB.getFile_path(), zanjireMadrakFileDB.getFile_name(), zanjireMadrakFileDB.getFile_extension());
                        listenerAdapterZanjireMadrak.onOpenFile(file);
                    } else {
                        listenerAdapterZanjireMadrak.FileNotExist();
                    }
                });
            } else if (item.getZanjireMadrakEnum() == StructureAdapterZanjireMadrakEnum.ZanjireMadrakEntity) {
                viewHolder.imgMore.setVisibility(View.VISIBLE);
                viewHolder.lnEntity.setVisibility(View.VISIBLE);
                viewHolder.expandableLayout.setInterpolator(new OvershootInterpolator());

                viewHolder.imgAttachIcon.setImageDrawable(Resorse.getDrawable(R.drawable.ic_document));
                StructureZanjireMadrakEntityDB zanjireMadrakEntityDB = item.getZanjireMadrakEntityDB();
                viewHolder.txtFullName.setText(zanjireMadrakEntityDB.getCreationFullName() + " [ " + zanjireMadrakEntityDB.getCreationRoleName() + " ] ");
                try {
                    if (zanjireMadrakEntityDB.getCreationDate() != null) {
                        viewHolder.lnDateTime.setVisibility(View.VISIBLE);
                        String[] splitDateTime = CustomFunction.MiladyToJalaly(zanjireMadrakEntityDB.getCreationDate()).split(" ");
                        final String date = splitDateTime[0];
                        final String time = splitDateTime[1];
                        viewHolder.txtDate.setText(date);
                        viewHolder.txtTime.setText(time);
                    } else {
                        viewHolder.lnDateTime.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    viewHolder.lnDateTime.setVisibility(View.INVISIBLE);
                }
                if (item.isLnMoreVisible()) {
                    viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_up));
                    viewHolder.expandableLayout.expand();
                } else {
                    viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_down));
                    viewHolder.expandableLayout.collapse();
                }
                if (zanjireMadrakEntityDB.getImportEntityNumber() != null && !zanjireMadrakEntityDB.getImportEntityNumber().isEmpty()) {
                    viewHolder.txtDocumentNumber.setText(zanjireMadrakEntityDB.getImportEntityNumber());
                    viewHolder.txtDocumentNumberName.setText(Resorse.getString(R.string.hint_import_entity_number));
                } else {
                    viewHolder.txtDocumentNumber.setText(zanjireMadrakEntityDB.getEntityNumber());
                    viewHolder.txtDocumentNumberName.setText(Resorse.getString(R.string.hint_entity_number));
                }

                viewHolder.txtEntityName.setText(zanjireMadrakEntityDB.getEntityFarsiName());
                new CustomFunction(App.CurentActivity).setHtmlText(viewHolder.txtDataName, zanjireMadrakEntityDB.getTitle());

                viewHolder.itemView.setOnClickListener(view -> {
                    if (item.isLnMoreVisible()) {
                        item.setLnMoreVisible(!item.isLnMoreVisible());
                        if (item.isLnMoreVisible()) {
                            viewHolder.expandableLayout.expand();
                            notifyDataSetChanged();
                            viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_up));
                        } else {
                            viewHolder.expandableLayout.collapse();
                            viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_down));
                        }
                    } else {
                        listenerAdapterZanjireMadrak.onClickZanjireEntity(zanjireMadrakEntityDB);
                    }

                });


                viewHolder.imgMore.setOnClickListener(view -> {
                    item.setLnMoreVisible(!item.isLnMoreVisible());
                    if (item.isLnMoreVisible()) {
                        viewHolder.expandableLayout.expand();
                        notifyDataSetChanged();
                        viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_up));
                    } else {
                        viewHolder.expandableLayout.collapse();
                        viewHolder.imgMore.setImageDrawable(Resorse.getDrawable(R.drawable.ic_arrow_down));
                    }
                });

            }

            viewHolder.txtCounter.setText("" + (item.getPosition()));
            new CustomFunction(App.CurentActivity).ChangeBackgroundTintColor(viewHolder.imgAttachIcon, R.color.color_txt_Normal);
          /*  if (position == itemList.size() - 1 && item.getZanjireMadrakEnum().ordinal() == StructureAdapterZanjireMadrakEnum.Header.ordinal()) {
                viewHolder.lnDivider.setVisibility(View.GONE);
            } else {
                viewHolder.lnDivider.setVisibility(View.VISIBLE);
            }*/
        }


    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getZanjireMadrakEnum().ordinal();
    }

    private void ShowToast(final String s) {
        App.CurentActivity.runOnUiThread(() -> App.ShowMessage().ShowToast(s, ToastEnum.TOAST_SHORT_TIME));
    }

    public void updateData(ArrayList<StructureAdapterZanjireMadrakBND> zanjireMadrakBNDS) {
        itemList.clear();
        itemList = new ArrayList<>();
        itemList.addAll(zanjireMadrakBNDS);
        App.CurentActivity.runOnUiThread(() -> notifyDataSetChanged());
    }

    public int getDataSize() {
        return itemList.size();
    }

    public void itemRangeChanged(int start, int count) {
        notifyItemRangeChanged(start, count);
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