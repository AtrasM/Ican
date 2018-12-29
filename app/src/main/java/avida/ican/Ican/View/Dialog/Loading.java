package avida.ican.Ican.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.Nullable;

/**
 * Created by AtrasVida on 2018-04-07 at 2:38 PM
 */

public class Loading {
    private final Activity context;
    private int gravity = Gravity.CENTER;
    private DialogPlus dialogLoading;
    private Binding viewHolder;
    private String indicatore = "BallGridPulseIndicator";

    @SuppressLint("ResourceAsColor")
    static class Binding {

        @Nullable @BindView(R.id.av_loading_indicator_view)
        AVLoadingIndicatorView avLoadingIndicatorView;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public Loading(Activity context) {
        this.context = context;
    }

    public Loading setGravity(int Gravity) {
        this.gravity = Gravity;
        return this;
    }

    public AVLoadingIndicatorView setIndicator(String indicatore) {
        this.indicatore = indicatore;
        viewHolder.avLoadingIndicatorView.setIndicator(indicatore);
        return viewHolder.avLoadingIndicatorView;
    }

    public void Show() {
        dialogLoading.show();
        App.isLoading = true;
    }

    public void Hide() {
        dialogLoading.dismiss();
        App.isLoading = false;
    }

    public Loading Creat() {

        BaseActivity.closeKeboard();
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogLoading = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.item_loading))
                        .setGravity(gravity)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setCancelable(false)
                        .setContentBackgroundResource(android.R.color.transparent)
                        .create();
                viewHolder = new Binding(dialogLoading.getHolderView());
                viewHolder.avLoadingIndicatorView.setIndicator(indicatore);
                viewHolder.avLoadingIndicatorView.setVisibility(View.VISIBLE);
            }

        });
        return this;
    }

}
