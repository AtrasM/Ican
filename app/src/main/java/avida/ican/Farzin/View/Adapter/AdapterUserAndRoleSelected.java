package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Interface.ListenerAdapterUserAndRole;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by AtrasVida in 2018-05-15 ar 4:28
 */


public class AdapterUserAndRoleSelected extends RecyclerView.Adapter<AdapterUserAndRoleSelected.ViewHolder> {

    private List<StructureUserAndRoleDB> itemList;
    private int layout = R.layout.item_user_and_role_selected;
    private ImageLoader imageLoader;
    private ListenerAdapterUserAndRole listenerAdapterUserAndRole;
    private Activity context;
    private Loading loading;

    public AdapterUserAndRoleSelected(Activity context, List<StructureUserAndRoleDB> itemList, ListenerAdapterUserAndRole listenerAdapterUserAndRole) {
        imageLoader = App.getImageLoader();
        this.itemList = itemList;
        this.context = context;
        this.listenerAdapterUserAndRole = listenerAdapterUserAndRole;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_role_name)
        TextView txtRoleName;

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
    public AdapterUserAndRoleSelected.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final StructureUserAndRoleDB item = itemList.get(position);
        viewHolder.txtName.setText(item.getFirstName()+" "+item.getLastName());
        viewHolder.txtRoleName.setText(" [ " + item.getRoleName() + " ] ");
        viewHolder.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* new DialogDelet(context).setOnListener(new ListenerDelet() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }).Show();*/
                listenerAdapterUserAndRole.unSelect(item);
            }
        });
    }

    public void Select(StructureUserAndRoleDB structureUserAndRoleDB) {
        itemList.add(structureUserAndRoleDB);
        notifyItemRangeChanged(itemList.size() - 1, 1);
    }


    @SuppressLint("StaticFieldLeak")
    public void delet(final StructureUserAndRoleDB structureUserAndRoleDB) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                listenerAdapterUserAndRole.showLoading();
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                for (int i = 0; i < itemList.size(); i++) {
                    //sleep(10);
                    if (structureUserAndRoleDB.getUser_ID() == itemList.get(i).getUser_ID()&&structureUserAndRoleDB.getRole_ID() == itemList.get(i).getRole_ID()) {
                        return i;
                    }
                }
                return -1;
            }

            @Override
            protected void onPostExecute(Integer pos) {
                if (pos > -1) {
                    int position =  pos;
                    itemList.remove(position);
                    notifyItemRemoved(position);
                }
                listenerAdapterUserAndRole.hideLoading();
                super.onPostExecute(pos);
            }
        }.execute();


    }

    private void sleep(int i) {
        try {
            Thread.sleep(i);
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