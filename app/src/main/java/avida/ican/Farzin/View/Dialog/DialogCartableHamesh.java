package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-09-25 at 2:11 PM
 */

public class DialogCartableHamesh {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private String Name = "";
    private String RoleName = "";
    private String Hamesh = "";


    public DialogCartableHamesh(Activity context) {
        this.context = context;
    }


    public class Binding {
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_role_name)
        TextView txtRoleName;
        @BindView(R.id.txt_hamesh)
        TextView txtHamesh;
        @BindView(R.id.btn_close)
        Button btnClose;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogCartableHamesh setData(String Name, String RoleName, String Hamesh) {
        this.Name = Name;
        this.RoleName = RoleName;
        this.Hamesh = Hamesh;
        return this;
    }

    public void Creat() {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_hamesh))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                dialog.show();
                viewHolder = new DialogCartableHamesh.Binding(dialog.getHolderView());

                initView();

            }

        });
    }

    private void initView() {
        viewHolder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        viewHolder.txtName.setText(Name);
        viewHolder.txtRoleName.setText("[ " + RoleName + " ]");
        viewHolder.txtHamesh.setText(Hamesh);
    }

    private void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
