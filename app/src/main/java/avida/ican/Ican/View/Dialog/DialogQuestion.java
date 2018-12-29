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
 * Created by AtrasVida on 2018-05-26 at 4:11 PM
 */

public class DialogQuestion {
    private final Activity context;
    private Binding viewHolder;
    private DialogPlus dialogQuestion;
    private ListenerQuestion listenerQuestion;
    private String Title = "";

    public class Binding {

        @BindView(R.id.btn_dialog_question_ok)
        Button btnDialogQuestionOk;
        @BindView(R.id.txt_title_dialog_question)
        TextView txtTitleDialogQuestion;
        @BindView(R.id.btn_dialog_question_cancel)
        Button btnDialogQuestionCancel;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogQuestion(Activity context) {
        this.context = context;
    }


    public DialogQuestion setOnListener(ListenerQuestion listenerQuestion) {
        this.listenerQuestion = listenerQuestion;
        return this;
    }

    public DialogQuestion setTitle(String Title) {
        this.Title = Title;
        return this;
    }


    public void dismiss() {
        dialogQuestion.dismiss();
        App.canBack = true;
    }

    public void Show() {

        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                dialogQuestion = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_question))
                        .setGravity(Gravity.CENTER)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                viewHolder = new Binding(dialogQuestion.getHolderView());

                viewHolder.txtTitleDialogQuestion.setText(Title);
                viewHolder.btnDialogQuestionOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listenerQuestion != null) listenerQuestion.onSuccess();
                        dismiss();

                    }
                });
                viewHolder.btnDialogQuestionCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listenerQuestion != null) listenerQuestion.onCancel();
                        dismiss();
                    }
                });
                dialogQuestion.show();

            }

        });


    }

}
