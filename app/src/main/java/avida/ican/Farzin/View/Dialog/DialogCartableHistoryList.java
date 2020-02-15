package avida.ican.Farzin.View.Dialog;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHistoryList;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-10-07 at 11:10 AM
 */

public class DialogCartableHistoryList {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private int Etc;
    private int Ec;
    private FragmentCartableHistoryList fragmentCartableHistoryList;
    private FragmentManager mfragmentManager;

    public DialogCartableHistoryList(Activity context) {
        this.context = context;
    }


    public class Binding {
        @Nullable
        @BindView(R.id.smart_tabLayout)
        SmartTabLayout smartTabLayout;
        @BindView(R.id.view_pager)
        ViewPager viewPager;
        @BindView(R.id.btn_close)
        Button btnClose;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogCartableHistoryList setData(int Etc, int Ec) {
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
    }

    public void Creat(FragmentManager fragmentManager) {
        this.mfragmentManager = fragmentManager;
        BaseActivity.closeKeyboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_cartable_history_list))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                dialog.show();
                viewHolder = new DialogCartableHistoryList.Binding(dialog.getHolderView());

                initViewPagerFragment();
                viewHolder.btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }

        });
    }

    private void initViewPagerFragment() {

        fragmentCartableHistoryList = new FragmentCartableHistoryList().newInstance(App.CurentActivity, Etc, Ec);
        initTab();
    }

    private void initTab() {
        assert viewHolder.smartTabLayout != null;
        viewHolder.smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentCartableHistoryList, R.string.title_gardesh_madrak);
        viewHolder.viewPager.setAdapter(adapter);
        viewHolder.smartTabLayout.setViewPager(viewHolder.viewPager);
        fragmentCartableHistoryList.setDialog(this);
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }


    public void dismiss() {
        dialog.dismiss();
        clearFragment();
        App.canBack = true;
    }

    private void clearFragment() {
        FragmentTransaction transaction = mfragmentManager.beginTransaction();
        transaction.remove(fragmentCartableHistoryList);
        transaction.commitAllowingStateLoss();
    }
}
