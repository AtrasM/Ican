package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerDialogWorkFlow;
import avida.ican.Farzin.View.Interface.ListenerDialogNewData;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2020-02-02 at 10:37 AM
 */

public class DialogWorkFlow {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private ListenerDialogWorkFlow listenerDialogWorkFlow;
    private int tmpResponse = 1;

    public DialogWorkFlow(Activity context) {
        this.context = context;
    }


    public class Binding {
        @BindView(R.id.ln_radio_button1)
        LinearLayout lnRadioButton1;
        @BindView(R.id.ln_radio_button0)
        LinearLayout lnRadioButton0;
        @BindView(R.id.ln_radio_button_1)
        LinearLayout lnRadioButton_1;
        @BindView(R.id.img_radio_button1)
        ImageView imgRadioButton1;
        @BindView(R.id.img_radio_button0)
        ImageView imgRadioButton0;
        @BindView(R.id.img_radio_button_1)
        ImageView imgRadioButton_1;
        @BindView(R.id.btn_ok)
        Button btnOK;
        @BindView(R.id.btn_cancel)
        Button btnCancel;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public void setListener(ListenerDialogWorkFlow listenerDialogWorkFlow) {
        this.listenerDialogWorkFlow = listenerDialogWorkFlow;
    }

    public void Show() {
        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {
            dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_workflow))
                    .setHeader(R.layout.item_dialog_header)
                    .setGravity(Gravity.CENTER)
                    .setCancelable(false)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            dialog.show();
            View viewHolderHeader = dialog.getHeaderView();
            TextView txtHeader = viewHolderHeader.findViewById(R.id.txt_dialog_title);
            txtHeader.setText(Resorse.getString(R.string.title_workflow));

            viewHolder = new Binding(dialog.getHolderView());
            initView();

        });


    }

    private void initView() {
        itemSelected(1);
        viewHolder.lnRadioButton1.setOnClickListener(view -> itemSelected(1));
        viewHolder.lnRadioButton0.setOnClickListener(view -> itemSelected(0));
        viewHolder.lnRadioButton_1.setOnClickListener(view -> itemSelected(-1));

        viewHolder.btnCancel.setOnClickListener(view -> {
            listenerDialogWorkFlow.onCancel();
            dismiss();

        });
        viewHolder.btnOK.setOnClickListener(view -> {
            listenerDialogWorkFlow.onSuccess(tmpResponse);
            dismiss();
        });


    }

    private void itemSelected(int response) {
        tmpResponse = response;
        if (response == 1) {
            viewHolder.imgRadioButton1.setImageResource(R.drawable.ic_radio_button_select);
        } else {
            viewHolder.imgRadioButton1.setImageResource(R.drawable.ic_radio_button_unselect);
        }
        if (response == 0) {
            viewHolder.imgRadioButton0.setImageResource(R.drawable.ic_radio_button_select);
        } else {
            viewHolder.imgRadioButton0.setImageResource(R.drawable.ic_radio_button_unselect);
        }
        if (response == -1) {
            viewHolder.imgRadioButton_1.setImageResource(R.drawable.ic_radio_button_select);
        } else {
            viewHolder.imgRadioButton_1.setImageResource(R.drawable.ic_radio_button_unselect);
        }
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
