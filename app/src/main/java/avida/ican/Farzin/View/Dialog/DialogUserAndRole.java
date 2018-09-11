package avida.ican.Farzin.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.UserAndRolePresenter;
import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleMain;
import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleSelected;
import avida.ican.Farzin.View.Fragment.FragmentUserAndRoleMain;
import avida.ican.Farzin.View.Fragment.FragmentUserAndRoleSelect;
import avida.ican.Farzin.View.Interface.ListenerAdapterUserAndRole;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Farzin.View.Interface.ListenerUserAndRollSearch;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-04-29 at 11:33 AM
 */

public class DialogUserAndRole {
    private final Activity context;
    private String Title = "";
    private Loading loading;
    private DialogPlus dialog;
    private List<StructureUserAndRoleDB> mstructuresMain = new ArrayList<>();
    private List<StructureUserAndRoleDB> mstructuresSelect = new ArrayList<>();
    private List<StructureUserAndRoleDB> mstructuresSearch = new ArrayList<>();
    private List<StructureUserAndRoleDB> mtmpItemSelect = new ArrayList<>();
    private UserAndRolePresenter userAndRolePresenter;
    private ListenerUserAndRoll listenerUserAndRollMain;
    private AdapterUserAndRoleMain adapterUserAndRoleMain;
    private AdapterUserAndRoleSelected adapterUserAndRoleSelected;
    private DialogUserAndRole.Binding viewHolder;
    private FragmentUserAndRoleMain fragmentUserAndRoleMain;
    private FragmentUserAndRoleSelect fragmentUserAndRoleSelect;
    private FragmentManager mfragmentManager;

    @SuppressLint("ResourceAsColor")

    public class Binding {

        @Nullable
        @BindView(R.id.smart_tabLayout)
        SmartTabLayout smartTabLayout;
        @BindView(R.id.view_pager)
        ViewPager viewPager;
        @BindView(R.id.btn_ok)
        Button btnOk;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.ln_loading)
        LinearLayout lnLoading;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogUserAndRole(Activity context) {
        this.context = context;
        loading = new Loading(this.context).Creat();

    }

    public DialogUserAndRole setTitle(String title) {
        this.Title = title;
        return this;
    }


    public DialogUserAndRole init(FragmentManager fragmentManager, List<StructureUserAndRoleDB> structuresMain, List<StructureUserAndRoleDB> structuresSelect, final ListenerUserAndRoll listenerUserAndRoll) {
        mtmpItemSelect = new ArrayList<>();
        this.mfragmentManager = fragmentManager;
        this.listenerUserAndRollMain = listenerUserAndRoll;

        userAndRolePresenter = new UserAndRolePresenter(structuresMain, structuresSelect).onListener(new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                mstructuresMain = structureUserAndRolesMain;
                mstructuresSelect = structureUserAndRolesSelect;
                BaseActivity.closeKeboard();
                initView();
            }

            @Override
            public void onFailed() {
                listenerUserAndRollMain.onFailed();
            }

            @Override
            public void onCancel(List<StructureUserAndRoleDB> tmpItemSelect) {
                listenerUserAndRollMain.onCancel(mtmpItemSelect);
            }


        }).execute();


        return this;
    }


    private void initView() {
        App.canBack = false;
        dialog = DialogPlus.newDialog(context)
                .setHeader(R.layout.item_dialog_header)
                .setContentHolder(new ViewHolder(R.layout.dialog_activity_user_and_role))
                .setGravity(Gravity.CENTER)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setContentBackgroundResource(R.drawable.border_dialog)
                .create();
        View viewheder = dialog.getHeaderView();
        TextView txtHeader = viewheder.findViewById(R.id.txt_dialog_title);
        txtHeader.setText(Title);
        //------------------------------------------------------------------------------------------------
        viewHolder = new DialogUserAndRole.Binding(dialog.getHolderView());
        BaseActivity.dialog = dialog;
        //------------------------------------------------------------------------------------------------

        initTab();

        viewHolder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerUserAndRollMain.onSuccess(mstructuresMain, mstructuresSelect);
                finish();
            }
        });
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerUserAndRollMain.onCancel(mtmpItemSelect);
                finish();
            }
        });

        viewheder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.show();
    }


    private void initTab() {
        initAdapter();
        assert viewHolder.smartTabLayout != null;
        viewHolder.smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentUserAndRoleMain, R.string.title_user_and_role_main);
        adapter.addFrag(fragmentUserAndRoleSelect, R.string.title_selects);

        viewHolder.viewPager.setAdapter(adapter);
        viewHolder.smartTabLayout.setViewPager(viewHolder.viewPager);
    }

    private void initAdapter() {
        adapterUserAndRoleMain = new AdapterUserAndRoleMain(mstructuresMain, new ListenerAdapterUserAndRole() {
            @Override
            public void onSelect(StructureUserAndRoleDB structureUserAndRoleDB) {
                mtmpItemSelect.add(structureUserAndRoleDB);
                adapterUserAndRoleSelected.Select(structureUserAndRoleDB);
            }

            @Override
            public void unSelect(final StructureUserAndRoleDB structureUserAndRoleDB) {

                //mtmpItemSelect.remove(structureUserAndRoleDB);
                adapterUserAndRoleSelected.delet(structureUserAndRoleDB);


            }

            @Override
            public void showLoading() {
                ShowLoading();
            }

            @Override
            public void hideLoading() {
                HideLoading();
            }
        });

        adapterUserAndRoleSelected = new AdapterUserAndRoleSelected(context, mstructuresSelect, new ListenerAdapterUserAndRole() {
            @Override
            public void onSelect(StructureUserAndRoleDB structureUserAndRoleDB) {

            }

            @Override
            public void unSelect(StructureUserAndRoleDB structureUserAndRoleDB) {
                adapterUserAndRoleMain.unSelect(structureUserAndRoleDB);
                adapterUserAndRoleSelected.delet(structureUserAndRoleDB);
            }

            @Override
            public void showLoading() {
                ShowLoading();
            }

            @Override
            public void hideLoading() {
                HideLoading();
            }
        });

        initViewPagerFragment();
    }

    private void HideLoading() {
        viewHolder.lnLoading.setVisibility(View.GONE);
    }

    private void ShowLoading() {
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
    }

    private void initViewPagerFragment() {

        fragmentUserAndRoleMain = new FragmentUserAndRoleMain().newInstance(App.CurentActivity, adapterUserAndRoleMain, this);
        fragmentUserAndRoleSelect = new FragmentUserAndRoleSelect().newInstance(App.CurentActivity, adapterUserAndRoleSelected);
    }


    public void performSearch(String Query, boolean reset) {
        if (!reset) {
            userAndRolePresenter.Search(Query, new ListenerUserAndRollSearch() {
                @Override
                public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesSearch) {
                    mstructuresSearch = structureUserAndRolesSearch;
                    adapterUserAndRoleMain.filter(mstructuresSearch);
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            adapterUserAndRoleMain.filter(mstructuresMain);

        }

    }

    private void clearFragment() {
        android.support.v4.app.FragmentTransaction transaction = mfragmentManager.beginTransaction();
        transaction.remove(fragmentUserAndRoleMain);
        transaction.remove(fragmentUserAndRoleSelect);
        transaction.commitAllowingStateLoss();
    }

    private void finish() {
        App.canBack = true;
        clearFragment();
        if (dialog != null) dialog.dismiss();
    }
}
