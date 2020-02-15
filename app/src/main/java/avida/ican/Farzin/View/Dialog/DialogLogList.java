package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Structure.Database.StructureLogDB;
import avida.ican.Farzin.View.Adapter.AdapterLog;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2019-11-30 at 5:07 PM
 */

public class DialogLogList {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private AdapterLog adapterLog;
    private ArrayList<StructureLogDB> structureLogDB = new ArrayList<>();

    public DialogLogList(Activity context) {
        this.context = context;
    }


    public class Binding {
        @Nullable
        @BindView(R.id.rcv_log)
        RecyclerView rcvLog;
        @BindView(R.id.btn_log_clear)
        Button btnClear;
        @BindView(R.id.btn_log_close)
        Button btnClose;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }


    public void Show(ArrayList<StructureLogDB> structureLogDB) {
        this.structureLogDB = structureLogDB;
        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(() -> {
            dialog = DialogPlus.newDialog(context)
                    .setContentHolder(new ViewHolder(R.layout.dialog_log))
                    .setGravity(Gravity.CENTER)
                    .setCancelable(false)
                    .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setContentBackgroundResource(R.drawable.border_dialog)
                    .create();
            dialog.show();
            viewHolder = new Binding(dialog.getHolderView());
            initRcv();
            viewHolder.btnClose.setOnClickListener(view -> dismiss());
            viewHolder.btnClear.setOnClickListener(view -> clearLog());

        });

    }

    private void clearLog() {
        CustomLogger.clearLog();
        dismiss();
    }

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        viewHolder.rcvLog.setLayoutManager(gridLayoutManager);
        initAdapter();
    }

    private void initAdapter() {
        adapterLog = new AdapterLog(structureLogDB);
        viewHolder.rcvLog.setAdapter(adapterLog);
    }

    public void dismiss() {
        dialog.dismiss();
        App.canBack = true;
    }


}
