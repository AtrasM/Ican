package avida.ican.Ican.View.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import avida.ican.Ican.App;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Enum.ExtensionEnum;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerAdapterAttach;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2018-06-02 ar 11:57
 */


public class AdapterAttach extends RecyclerView.Adapter<AdapterAttach.ViewHolder> {

    private ArrayList<StructureAttach> itemList;
    private int layout = R.layout.item_attach;
    private ImageLoader imageLoader;
    private Activity context;
    private boolean canDelete;
    private ListenerAdapterAttach listenerAdapterAttach;

    public AdapterAttach(Activity context, ArrayList<StructureAttach> itemList, boolean canDelete, ListenerAdapterAttach listenerAdapterAttach) {
        imageLoader = App.getImageLoader();
        this.itemList = itemList;
        this.context = context;
        this.canDelete = canDelete;
        this.listenerAdapterAttach = listenerAdapterAttach;

    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.img_attach_icon)
        ImageView imgAttachIcon;
        @BindView(R.id.img_delet)
        ImageView imgDelet;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AdapterAttach.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(layout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onViewDetachedFromWindow(ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final StructureAttach item = itemList.get(position);
        if (item.getName() == null || item.getName().isEmpty() || item.getName().contains("null")) {
            item.setName(Resorse.getString(R.string.noName));
        }
        if (!item.getFileExtension().isEmpty()) {
            if (item.getName().contains(".ican")) {
                item.getName().replace(".ican", "");
            }
            if (item.getName().contains(item.getFileExtension())) {
                viewHolder.txtName.setText(item.getName());
            } else {
                viewHolder.txtName.setText(item.getName() + item.getFileExtension());
            }

            ExtensionEnum extensionEnum = new CustomFunction(App.CurentActivity).getExtensionCategory(item.getFileExtension());
            switch (extensionEnum) {
                case IMAGE: {
                    item.setIcon(R.drawable.ic_photo);
                    break;
                }
                case AUDIO: {
                    item.setIcon(R.drawable.ic_voice);
                    break;
                }
                case VIDEO: {
                    item.setIcon(R.drawable.ic_video);
                    break;
                }
                case APPLICATION: {
                    item.setIcon(R.drawable.ic_attach_file);
                    break;
                }
                case NONE: {

                    App.getHandlerMainThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            itemList.remove(position);
                            notifyItemRemoved(position);
                            new Message().ShowSnackBar(Resorse.getString(R.string.error_invalid_file), SnackBarEnum.SNACKBAR_LONG_TIME);
                        }
                    }, 200);
                    return;
                }
                default: {
                    item.setIcon(R.drawable.ic_attach_file);
                    break;
                }
            }
        } else {
            viewHolder.txtName.setText(item.getName());
            item.setIcon(R.drawable.ic_attach_file);
        }


        viewHolder.imgAttachIcon.setBackground(Resorse.getDrawable(item.getIcon()));


        if (canDelete) {
            viewHolder.imgDelet.setVisibility(View.VISIBLE);
        }
        viewHolder.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DialogQuestion(context).setOnListener(new ListenerQuestion() {
                    @Override
                    public void onSuccess() {
                        delet(item);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).setTitle(Resorse.getString(R.string.delet_question)).Show();
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerAdapterAttach.onOpenFile(item);
            }
        });
    }

    public void add(StructureAttach structureAttach) {
        itemList.add(structureAttach);
        notifyDataSetChanged();
        //notifyItemRangeChanged(itemList.size() - 1, 1);
    }

    public void addAll(ArrayList<StructureAttach> structureAttachs) {
        itemList.addAll(structureAttachs);
        notifyDataSetChanged();
        //notifyItemRangeChanged(itemList.size() - 1, structureAttachs.size());
    }


    public void delet(StructureAttach structureAttach) {
        int pos = itemList.indexOf(structureAttach);
        if (pos > -1) {
            itemList.remove(structureAttach);
            listenerAdapterAttach.onDeletFile(structureAttach);
            notifyItemRemoved(pos);
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