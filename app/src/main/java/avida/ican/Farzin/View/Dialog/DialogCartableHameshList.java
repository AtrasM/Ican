package avida.ican.Farzin.View.Dialog;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import avida.ican.Farzin.View.Fragment.Cartable.FragmentCartableHameshList;
import avida.ican.Farzin.View.Fragment.Cartable.FragmentZanjireMadrak;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-09-25 at 2:11 PM
 */

public class DialogCartableHameshList {
    private final Activity context;
    private DialogPlus dialog;
    private Binding viewHolder;
    private int Etc;
    private int Ec;
    private ListenerFile listenerFile;
    private FragmentCartableHameshList fragmentCartableHameshList;

    public DialogCartableHameshList(Activity context) {
        this.context = context;
    }

    private FragmentManager mfragmentManager;

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

    public DialogCartableHameshList setData(int Etc, int Ec) {
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
    }

    public void setListenerFile(ListenerFile listenerFile) {
        this.listenerFile = listenerFile;
    }

    public void Creat(FragmentManager fragmentManager) {
        this.mfragmentManager = fragmentManager;
        BaseActivity.closeKeboard();
        App.canBack = false;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog = DialogPlus.newDialog(context)
                        .setContentHolder(new ViewHolder(R.layout.dialog_cartable_hamesh_list))
                        .setGravity(Gravity.CENTER)
                        .setCancelable(false)
                        .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setContentBackgroundResource(R.drawable.border_dialog)
                        .create();
                dialog.show();
                viewHolder = new DialogCartableHameshList.Binding(dialog.getHolderView());
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

        fragmentCartableHameshList = new FragmentCartableHameshList().newInstance(App.CurentActivity, Etc, Ec, listenerFile);
        initTab();
    }

    private void initTab() {
        assert viewHolder.smartTabLayout != null;
        viewHolder.smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentCartableHameshList, R.string.title_list_hamesh);
        viewHolder.viewPager.setAdapter(adapter);
        viewHolder.smartTabLayout.setViewPager(viewHolder.viewPager);
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
        android.support.v4.app.FragmentTransaction transaction = mfragmentManager.beginTransaction();
        transaction.remove(fragmentCartableHameshList);
        transaction.commitAllowingStateLoss();
    }
}
