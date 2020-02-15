package avida.ican.Farzin.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-09-25 at 2:11 PM
 */

public class DialogCartableTozihSH_DastorErja {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private String Name = "";
    private String RoleName = "";
    private String Hamesh = "";
    private String Title = "";
    private int drawableID = R.drawable.ic_document_white;


    public DialogCartableTozihSH_DastorErja(Activity context) {
        this.context = context;
    }


    public class Binding {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.img_title)
        ImageView imgTitle;
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


    public DialogCartableTozihSH_DastorErja setData(String Title, int drawableID, String Name, String RoleName, String Hamesh) {
        this.Title = Title;
        this.drawableID = drawableID;
        this.Name = Name;
        this.RoleName = RoleName;
        this.Hamesh = Hamesh;
        return this;
    }

    public void Creat() {

        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {
            dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_tozih_shakhsi_dastor_erja))
                    .setGravity(Gravity.CENTER)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setCancelable(false)
                    .setContentBackgroundResource(android.R.color.transparent)
                    .create();
            dialog.show();
            viewHolder = new Binding(dialog.getHolderView());

            initView();

        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        viewHolder.btnClose.setOnClickListener(view -> dismiss());
        viewHolder.txtTitle.setText(Title);
        new CustomFunction(context).ChengeDrawableColorAndSetToImageView(viewHolder.imgTitle, drawableID,R.color.color_White);
        viewHolder.txtName.setText(Name);
        viewHolder.txtRoleName.setText(" [ " + RoleName + " ]");
        new CustomFunction(context).setHtmlText(viewHolder.txtHamesh, Hamesh);
    }

    private void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
