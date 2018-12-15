package avida.ican.Farzin.View.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructurePersonREQ;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.ListenerAdapterUserAndRole;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.CustomFunction;
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

    private ArrayList<StructureCartableDocumentActionsDB> cartableDocumentActionsDBS = new ArrayList<>();
    private List<StructureUserAndRoleDB> itemList;
    private int layout = R.layout.item_user_and_role_selected;
    private ImageLoader imageLoader;
    private ListenerAdapterUserAndRole listenerAdapterUserAndRole;
    private Activity context;
    private Loading loading;
    private UserAndRoleEnum userAndRoleEnum;
    private Animator animator;
    private ArrayList<String> spList = new ArrayList<>();
    private ArrayList<StructurePersonREQ> structurePersons = new ArrayList<>();

    public AdapterUserAndRoleSelected(Activity context, List<StructureUserAndRoleDB> itemList, UserAndRoleEnum userAndRoleEnum, ArrayList<StructureCartableDocumentActionsDB> cartableDocumentActionsDBS) {
        imageLoader = App.getImageLoader();
        this.itemList = itemList;
        this.context = context;
        animator = new Animator(context);
        this.userAndRoleEnum = userAndRoleEnum;
        this.cartableDocumentActionsDBS = cartableDocumentActionsDBS;
        structurePersons = new ArrayList<>(itemList.size());
        initSpList();

    }


    public AdapterUserAndRoleSelected(Activity context, List<StructureUserAndRoleDB> itemList, UserAndRoleEnum userAndRoleEnum) {
        imageLoader = App.getImageLoader();
        this.itemList = itemList;
        this.context = context;
        animator = new Animator(context);
        this.userAndRoleEnum = userAndRoleEnum;

    }


    public void setListener(ListenerAdapterUserAndRole listenerAdapterUserAndRole) {
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
        @BindView(R.id.edt_private_discription)
        EditText edtPrivateDiscription;
        @BindView(R.id.edt_referral_order)
        EditText edtReferralOrder;
        @BindView(R.id.sp_actions)
        Spinner spActions;
        @BindView(R.id.ln_more)
        LinearLayout lnMore;
        @BindView(R.id.ln_img_more)
        LinearLayout lnImgMore;
        @BindView(R.id.img_more)
        ImageView imgMore;

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
        viewHolder.txtName.setText(item.getFirstName() + " " + item.getLastName());
        viewHolder.txtRoleName.setText(" [ " + item.getRoleName() + " ] ");
        if (userAndRoleEnum == UserAndRoleEnum.SEND) {
            structurePersons.get(position).setRoleId(item.getRole_ID());
            String hameshTitle = "" + item.getFirstName() + " " + item.getLastName() + " [ " + item.getRoleName() + " ] ";
            structurePersons.get(position).setHameshTitle(hameshTitle);
            ArrayAdapter<String> adapterSize = new CustomFunction(App.CurentActivity).getSpinnerAdapter(spList);
            viewHolder.spActions.setAdapter(adapterSize);
            viewHolder.spActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    structurePersons.get(position).setAction(cartableDocumentActionsDBS.get(i).getActionCode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            viewHolder.edtPrivateDiscription.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    structurePersons.get(position).setDescription(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            viewHolder.edtReferralOrder.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    structurePersons.get(position).setHameshContent(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            viewHolder.lnImgMore.setVisibility(View.VISIBLE);
            viewHolder.imgMore.setBackground(Resorse.getDrawable(R.drawable.ic_arrow_down));
        } else {
            viewHolder.lnImgMore.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.imgMore.getVisibility() == View.VISIBLE) {
                    if (viewHolder.lnMore.getVisibility() == View.GONE) {
                        animator.slideInFromDownFast(viewHolder.lnMore);
                        viewHolder.lnMore.setVisibility(View.VISIBLE);
                        viewHolder.imgMore.setBackground(Resorse.getDrawable(R.drawable.ic_arrow_up));

                    } else {
                        animator.slideOutToDown(viewHolder.lnMore);
                        viewHolder.imgMore.setBackground(Resorse.getDrawable(R.drawable.ic_arrow_down));
                        viewHolder.lnMore.setVisibility(View.GONE);
                    }
                }
            }
        });

        viewHolder.imgDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listenerAdapterUserAndRole.unSelect(item);
            }
        });
    }

    public void Select(StructureUserAndRoleDB structureUserAndRoleDB) {
        itemList.add(structureUserAndRoleDB);
        structurePersons.add(new StructurePersonREQ());
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
                    itemList.remove(position);
                    structurePersons.remove(position);
                    notifyItemRemoved(position);
                }
                listenerAdapterUserAndRole.hideLoading();
                super.onPostExecute(pos);
            }
        }.execute();


    }

    private void initSpList() {
        for (int i = 0; i < cartableDocumentActionsDBS.size(); i++) {
            spList.add(cartableDocumentActionsDBS.get(i).getActionName());
        }
    }

    public ArrayList<StructurePersonREQ> getStructurePersonList() {
        return structurePersons;
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