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
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentSignaturesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentSignaturesPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Adapter.Cartable.AdapterSignature;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterSignature;
import avida.ican.Farzin.View.Interface.Cartable.ListenerDialogSignature;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2019-05-29 at 12:50 PM
 */

public class DialogSignature {
    private final Activity context;
    private int Etc = 0;
    @BindString(R.string.title_signature)
    String Title;
    private DialogPlus dialogSignature;
    private ListenerDialogSignature listenerDialogSignature;
    private List<StructureCartableDocumentSignatureDB> mstructuresMain = new ArrayList<>();
    private List<StructureCartableDocumentSignatureDB> mstructuresSelect = new ArrayList<>();
    private List<StructureCartableDocumentSignatureDB> mtmpItemSelect = new ArrayList<>();
    private CartableDocumentSignaturesPresenter cartableDocumentSignaturesPresenter;
    private AdapterSignature adapterSignature;
    private DialogSignature.Binding viewHolder;
    private FarzinCartableQuery farzinCartableQuery;
    private FarzinPrefrences farzinPrefrences;
    private boolean canDo = true;

    @SuppressLint("ResourceAsColor")

    public class Binding {
        @BindView(R.id.rcv_signature)
        RecyclerView rcvSignature;
        @BindView(R.id.btn_ok)
        Button btnOk;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.ln_loading)
        LinearLayout lnLoading;
        @BindView(R.id.ln_dialog_signature)
        LinearLayout lnDialogSignature;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogSignature(Activity context, int Etc) {
        this.context = context;
        this.Etc = Etc;
    }

    public DialogSignature setTitle(String title) {
        this.Title = title;
        return this;
    }

    public DialogSignature setOnListener(ListenerDialogSignature listenerDialogSignature) {
        this.listenerDialogSignature = listenerDialogSignature;
        return this;
    }

    public void dismiss() {
        dialogSignature.dismiss();
        BaseActivity.dialog=null;
        App.canBack = true;
    }

    public DialogSignature Show() {
        farzinCartableQuery = new FarzinCartableQuery();
        farzinPrefrences = new FarzinPrefrences().init();
        cartableDocumentSignaturesPresenter = new CartableDocumentSignaturesPresenter(Etc);
        mstructuresMain = cartableDocumentSignaturesPresenter.GetDocumentSignatures();
        initView();

        return this;
    }


    private void initView() {
        App.canBack = false;
        dialogSignature = DialogPlus.newDialog(context)
                .setHeader(R.layout.item_dialog_header)
                .setContentHolder(new ViewHolder(R.layout.dialog_signature))
                .setGravity(Gravity.CENTER)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setContentBackgroundResource(R.drawable.border_dialog)
                .create();
        View viewHolderHeader = dialogSignature.getHeaderView();
        TextView txtHeader = viewHolderHeader.findViewById(R.id.txt_dialog_title);
        txtHeader.setText(Title);
        //------------------------------------------------------------------------------------------------
        viewHolder = new DialogSignature.Binding(dialogSignature.getHolderView());
        BaseActivity.dialog = dialogSignature;
        //------------------------------------------------------------------------------------------------

        viewHolder.lnLoading.setVisibility(View.VISIBLE);
        //viewHolder.lnDialogSignature.setVisibility(View.INVISIBLE);
        viewHolder.btnOk.setOnClickListener(view -> {
            if (canDo) {

                listenerDialogSignature.onSuccess(mstructuresSelect);
                finish();
            }
        });
        viewHolder.btnCancel.setOnClickListener(view -> {
            listenerDialogSignature.onCancel();
            finish();
        });

        initRcv();
        if (mstructuresMain.size() > 0) {
            viewHolder.lnLoading.setVisibility(View.GONE);
            viewHolder.lnDialogSignature.setVisibility(View.VISIBLE);
            initAdapter();
        } else {
            cartableDocumentSignaturesPresenter.GetDocumentSignatureFromServer(new CartableDocumentSignaturesListener() {
                @Override
                public void onSuccess() {
                    viewHolder.lnLoading.setVisibility(View.GONE);
                    viewHolder.lnDialogSignature.setVisibility(View.VISIBLE);
                    initAdapter();
                }

                @Override
                public void onFailed(String message) {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.error_get_data_faild), ToastEnum.TOAST_LONG_TIME);
                    listenerDialogSignature.onCancel();
                }

                @Override
                public void onCancel() {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.error_get_data_faild), ToastEnum.TOAST_LONG_TIME);
                    listenerDialogSignature.onCancel();
                }
            });
        }


        dialogSignature.show();
    }

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.rcvSignature.setLayoutManager(gridLayoutManager);
    }

    private void initAdapter() {
        adapterSignature = new AdapterSignature(mstructuresMain, new ListenerAdapterSignature() {

            @Override
            public void onSelect(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB) {
                mstructuresSelect.add(structureCartableDocumentSignatureDB);
                ManagmentBtnOkEnable();
            }

            @Override
            public void unSelect(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB) {
                mstructuresSelect.remove(structureCartableDocumentSignatureDB);
                ManagmentBtnOkEnable();
            }

            @Override
            public void showLoading() {
                ShowLoading();
            }

            @Override
            public void hideLoading() {
                HideLoading();
                ManagmentBtnOkEnable();
            }
        });
        viewHolder.rcvSignature.setAdapter(adapterSignature);
        ManagmentBtnOkEnable();
    }

    private void ManagmentBtnOkEnable() {
        CustomFunction customFunction = new CustomFunction(context);
        if (mstructuresSelect.size() > 0) {
            viewHolder.btnOk.setEnabled(true);
            customFunction.ChangeBackgroundTintColor(viewHolder.btnOk, R.color.color_Info);
        } else {
            viewHolder.btnOk.setEnabled(false);
            customFunction.ChangeBackgroundTintColor(viewHolder.btnOk, R.color.color_disable);
        }
    }

    private void HideLoading() {
        viewHolder.lnLoading.setVisibility(View.GONE);
    }

    private void ShowLoading() {
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
    }

    private void finish() {
        App.canBack = true;
        if (dialogSignature != null) dismiss();
    }
}
