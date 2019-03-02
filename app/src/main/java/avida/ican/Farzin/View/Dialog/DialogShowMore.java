package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2019-03-02 at 12:36 PM
 */

public class DialogShowMore {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private String Title = "";
    private String Content = "";


    public DialogShowMore(Activity context) {
        this.context = context;
    }


    public class Binding {
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_content)
        TextView txtContent;
        @BindView(R.id.btn_close)
        Button btnClose;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogShowMore setData(String title, String content) {
        this.Content = content;
        this.Title = title;
        return this;
    }

    public void Creat() {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_show_more))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                dialog.show();
                viewHolder = new DialogShowMore.Binding(dialog.getHolderView());

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
        viewHolder.txtTitle.setText(Title);
        viewHolder.txtContent.setText(Content);
    }

    private void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }

}
