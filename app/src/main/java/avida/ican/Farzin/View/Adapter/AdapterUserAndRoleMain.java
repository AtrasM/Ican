package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Interface.ListenerAdapterUserAndRole;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TextDrawableProvider;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import static avida.ican.Ican.BaseActivity.closeKeboard;


/**
 * Created by AtrasVida in 97-04-30 ar 3:45
 */


public class AdapterUserAndRoleMain extends RecyclerView.Adapter<AdapterUserAndRoleMain.ViewHolder> {

    private List<StructureUserAndRoleDB> itemList;
    private int layout = R.layout.item_user_and_role_main;
    private ImageLoader imageLoader;
    private ListenerAdapterUserAndRole listenerAdapterUserAndRole;

    public AdapterUserAndRoleMain(List<StructureUserAndRoleDB> itemList, ListenerAdapterUserAndRole listenerAdapterUserAndRole) {
        imageLoader = App.getImageLoader();
        this.itemList = new ArrayList<>(itemList);
        this.listenerAdapterUserAndRole = listenerAdapterUserAndRole;
    }

    // inner class to hold a reference to each item of RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_name)
        TextView txtName;

        @BindView(R.id.txt_role_name)
        TextView txtRoleName;

        @BindView(R.id.img_check_box)
        ImageView imgCheck;
        @BindView(R.id.img_profile)
        ImageView imgProfile;

        public ViewHolder(View view) {
            super(view);

            // binding view
            ButterKnife.bind(this, view);
        }


    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public AdapterUserAndRoleMain.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

        final StructureUserAndRoleDB item = itemList.get(position);

        CheckItemSelected(item, viewHolder, true);


        viewHolder.txtName.setText(item.getFirstName() + " " + item.getLastName());
        if (!item.getLastName().isEmpty() && item.getLastName().length() >= 1) {
           String Char = item.getLastName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        }else{
            String Char = item.getFirstName().substring(0, 1);
            viewHolder.imgProfile.setImageDrawable(TextDrawableProvider.getDrawable(Char));
        }

        viewHolder.txtRoleName.setText(" [ " + item.getRoleName() + " ] ");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
               closeKeboard();
                item.setSelected(!item.isSelected());
                CheckItemSelected(item, viewHolder, false);
            }
        });


    }

    private void CheckItemSelected(StructureUserAndRoleDB item, ViewHolder viewHolder, boolean a_System) {
        if (item.isSelected()) {
            viewHolder.imgCheck.setBackground(Resorse.getDrawable(R.drawable.check_square_select));
            if (!a_System) {
                listenerAdapterUserAndRole.onSelect(item);
            }
        } else {
            viewHolder.imgCheck.setBackground(Resorse.getDrawable(R.drawable.check_square_unselect));
            if (!a_System) {
                listenerAdapterUserAndRole.unSelect(item);
            }
        }
    }

    public void filter(List<StructureUserAndRoleDB> structureUserAndRoleDBS) {
        itemList = new ArrayList<>();
        itemList.addAll(structureUserAndRoleDBS);
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public void unSelect(final StructureUserAndRoleDB structureUserAndRoleDB) {
       /* int pos = itemList.indexOf(structureUserAndRoleDB);
        if (pos > -1) {
            itemList.get(pos).setSelected(false);
            App.CurentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }*/


        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected void onPreExecute() {
                listenerAdapterUserAndRole.showLoading();
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... voids) {
                for (int i = 0; i < itemList.size(); i++) {
                    sleep();
                    if (structureUserAndRoleDB.getUser_ID() == itemList.get(i).getUser_ID() && structureUserAndRoleDB.getRole_ID() == itemList.get(i).getRole_ID()) {
                        return i;
                    }
                }
                return -1;
            }

            @Override
            protected void onPostExecute(Integer pos) {
                if (pos > -1) {
                    int position = pos;
                    itemList.get(position).setSelected(false);
                    notifyDataSetChanged();
                }
                super.onPostExecute(pos);
            }
        }.execute();
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