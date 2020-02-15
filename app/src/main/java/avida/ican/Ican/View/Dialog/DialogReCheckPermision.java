package avida.ican.Ican.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2019-01-02 at 2:27 PM
 */

public class DialogReCheckPermision {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialogReCheckPermision;
    private ListenerQuestion listenerQuestion;
    private String Title = "";

    public class Binding {

        @BindView(R.id.btn_dialog_re_check_permision_ok)
        Button btnDialogReCheckPermision_ok;
        @BindView(R.id.txt_title_dialog_re_check_permision)
        TextView txtTitleDialogReCheckPermision;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogReCheckPermision(Activity context) {
        this.context = context;
    }


    public DialogReCheckPermision setOnListener(ListenerQuestion listenerQuestion) {
        this.listenerQuestion = listenerQuestion;
        return this;
    }

    public DialogReCheckPermision setTitle(String Title) {
        this.Title = Title;
        return this;
    }


    public void dismiss() {
        dialogReCheckPermision.dismiss();
        App.canBack = true;
    }

    public void Show() {

        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialogReCheckPermision = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_re_check_permision))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialogReCheckPermision.getHolderView());

                viewHolder.txtTitleDialogReCheckPermision.setText(Title);
                viewHolder.btnDialogReCheckPermision_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listenerQuestion != null) listenerQuestion.onSuccess();
                        dismiss();

                    }
                });
                dialogReCheckPermision.show();

            }

        });


    }

}
