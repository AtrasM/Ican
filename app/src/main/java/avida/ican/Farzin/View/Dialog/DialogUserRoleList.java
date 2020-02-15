package avida.ican.Farzin.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Farzin.View.Adapter.AdapterUserRole;
import avida.ican.Farzin.View.Interface.ListenerDialogUserRole;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2019-07-01 at 5:16 PM
 */

public class DialogUserRoleList {
    @BindString(R.string.title_dialog_user_role)
    String Title;
    private final Activity context;
    private DialogPlus dialogUserRole;
    private ListenerDialogUserRole listenerDialogUserRole;
    private List<StructureUserRoleDB> mstructuresMain = new ArrayList<>();

    private AdapterUserRole adapterUserRole;
    private DialogUserRoleList.Binding viewHolder;
    private final boolean canDoCancel;

    @SuppressLint("ResourceAsColor")
    public class Binding {
        @BindView(R.id.rcv_user_role)
        RecyclerView rcvUserRole;
        @BindView(R.id.btn_ok)
        Button btnOk;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.ln_loading)
        LinearLayout lnLoading;
        @BindView(R.id.ln_dialog_user_role)
        LinearLayout lnDialogUserRole;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogUserRoleList(Activity context, List<StructureUserRoleDB> itemList, boolean canDoCancel) {
        this.context = context;
        this.canDoCancel = canDoCancel;
        mstructuresMain = itemList;
    }

    public DialogUserRoleList setTitle(String title) {
        this.Title = title;
        return this;
    }

    public DialogUserRoleList setOnListener(ListenerDialogUserRole listenerDialogUserRole) {
        this.listenerDialogUserRole = listenerDialogUserRole;
        return this;
    }

    public void dismiss() {
        dialogUserRole.dismiss();
        BaseActivity.dialog = null;
        App.canBack = true;
    }

    public DialogUserRoleList Show() {
        initView();
        return this;
    }

    private void initView() {
        App.canBack = false;
        dialogUserRole = DialogPlus.newDialog(context)
                .setHeader(R.layout.item_dialog_header)
                .setContentHolder(new ViewHolder(R.layout.dialog_user_role))
                .setGravity(Gravity.CENTER)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setContentBackgroundResource(R.drawable.border_dialog)
                .create();
        View viewHolderHeader = dialogUserRole.getHeaderView();
        TextView txtHeader = viewHolderHeader.findViewById(R.id.txt_dialog_title);
        txtHeader.setText(Title);

        //------------------------------------------------------------------------------------------------
        viewHolder = new DialogUserRoleList.Binding(dialogUserRole.getHolderView());
        BaseActivity.dialog = dialogUserRole;
        //------------------------------------------------------------------------------------------------

        viewHolder.btnOk.setOnClickListener(view -> {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                listenerDialogUserRole.onCancel();
                App.getHandlerMainThread().postDelayed(() -> App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.server_acces_denied), SnackBarEnum.SNACKBAR_LONG_TIME), 300);
            } else {
                listenerDialogUserRole.onSuccess(adapterUserRole.getItemselected());
            }
            finish();

        });

        viewHolder.btnCancel.setOnClickListener(view -> {
            if ((canDoCancel)) {
                listenerDialogUserRole.onCancel();
                finish();
            }
        });
        if ((!canDoCancel)) {
            viewHolder.btnCancel.setEnabled(false);
            new CustomFunction(context).ChangeBackgroundTintColor(viewHolder.btnCancel, R.color.color_disable);
        }

        initRcv();
        initAdapter();
        dialogUserRole.show();
    }

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.rcvUserRole.setLayoutManager(gridLayoutManager);
    }

    private void initAdapter() {
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        int defRoleID = farzinPrefrences.getRoleID();
        if (defRoleID <= 0) {
            for (int i = 0; i < mstructuresMain.size(); i++) {
                if (mstructuresMain.get(i).isDef()) {
                    defRoleID = mstructuresMain.get(i).getRoleID();
                }
            }
        }
        adapterUserRole = new AdapterUserRole(mstructuresMain, defRoleID);
        viewHolder.rcvUserRole.setAdapter(adapterUserRole);
    }


    private void finish() {
        App.canBack = true;
        if (dialogUserRole != null) dismiss();
    }
}
