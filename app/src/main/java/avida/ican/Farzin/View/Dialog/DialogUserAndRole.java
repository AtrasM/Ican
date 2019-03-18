package avida.ican.Farzin.View.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructurePersonREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureSenderREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentActionsPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.UserAndRolePresenter;
import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleMain;
import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleSelected;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Fragment.FragmentUserAndRoleMain;
import avida.ican.Farzin.View.Fragment.FragmentUserAndRoleSelect;
import avida.ican.Farzin.View.Interface.ListenerAdapterUserAndRole;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Farzin.View.Interface.ListenerUserAndRollSearch;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Interface.ListenerValidate;
import avida.ican.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-04-29 at 11:33 AM
 */

public class DialogUserAndRole {
    private final Activity context;
    private int sendCode;
    private int Ec = 0;
    private int Etc = 0;
    private String Title = "";
    private Loading loading;
    private DialogPlus dialogUserAndRole;
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
    private UserAndRoleEnum userAndRoleEnum;
    private FarzinCartableQuery farzinCartableQuery;
    private FarzinPrefrences farzinPrefrences;
    private ArrayList<StructureCartableDocumentActionsDB> cartableDocumentActionsDBS = new ArrayList<>();
    private boolean canDo = true;

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
        @BindView(R.id.ln_dialog_user_and_role)
        LinearLayout lnDialogUserAndRole;

        Binding(View rootView) {
            ButterKnife.bind(this, rootView);
        }
    }

    public DialogUserAndRole(Activity context) {
        this.context = context;
        loading = new Loading(this.context).Creat();

    }

    public DialogUserAndRole(Activity context, int Etc, int Ec, int SendCode) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.sendCode = SendCode;
        loading = new Loading(this.context).Creat();
    }

    public DialogUserAndRole setTitle(String title) {
        this.Title = title;
        return this;
    }


    public DialogUserAndRole init(FragmentManager fragmentManager, List<StructureUserAndRoleDB> structuresMain, List<StructureUserAndRoleDB> structuresSelect, UserAndRoleEnum userAndRoleEnum, final ListenerUserAndRoll listenerUserAndRoll) {
        farzinCartableQuery = new FarzinCartableQuery();
        farzinPrefrences = new FarzinPrefrences().init();
        mtmpItemSelect = new ArrayList<>();
        this.mfragmentManager = fragmentManager;
        this.listenerUserAndRollMain = listenerUserAndRoll;
        this.userAndRoleEnum = userAndRoleEnum;
        if (userAndRoleEnum == UserAndRoleEnum.SEND && Etc > 0) {
            cartableDocumentActionsDBS = (ArrayList<StructureCartableDocumentActionsDB>) new CartableDocumentActionsPresenter(Etc).GetDocumentActions();
            StructureCartableDocumentActionsDB structureCartableDocumentActionsDB = new StructureCartableDocumentActionsDB(-1, -1, "انتخاب", -1, "");
            cartableDocumentActionsDBS.add(0, structureCartableDocumentActionsDB);
        }
        this.userAndRoleEnum = userAndRoleEnum;
        userAndRolePresenter = new UserAndRolePresenter(structuresMain, structuresSelect).onListener(new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                mstructuresMain = structureUserAndRolesMain;
                mstructuresSelect = structureUserAndRolesSelect;
                BaseActivity.closeKeboard();
                initView();
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {

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

        dialogUserAndRole = DialogPlus.newDialog(context)
                .setHeader(R.layout.item_dialog_header)
                .setContentHolder(new ViewHolder(R.layout.dialog_activity_user_and_role))
                .setGravity(Gravity.CENTER)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setCancelable(false)
                .setContentBackgroundResource(R.drawable.border_dialog)
                .create();
        View viewhelder = dialogUserAndRole.getHeaderView();
        TextView txtHeader = viewhelder.findViewById(R.id.txt_dialog_title);
        txtHeader.setText(Title);
        //------------------------------------------------------------------------------------------------
        viewHolder = new DialogUserAndRole.Binding(dialogUserAndRole.getHolderView());
        BaseActivity.dialog = dialogUserAndRole;
        //------------------------------------------------------------------------------------------------

        initTab();

        viewHolder.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canDo) {
                    if (userAndRoleEnum == UserAndRoleEnum.SEND && Etc > 0) {
                        final ArrayList<StructurePersonREQ> structurePersonsREQ = adapterUserAndRoleSelected.getStructurePersonList();
                        ValidationPerson(structurePersonsREQ, new ListenerValidate() {
                            @Override
                            public void onValid() {
                                StructureSenderREQ structureSenderREQ = new StructureSenderREQ(farzinPrefrences.getRoleID(), sendCode);
                                StructureAppendREQ structureAppendREQ = new StructureAppendREQ(Etc, Ec, structureSenderREQ, structurePersonsREQ);
                                listenerUserAndRollMain.onSuccess(structureAppendREQ);
                                finish();
                            }

                            @Override
                            public void unValid() {
                                viewHolder.viewPager.setCurrentItem(1);
                            }
                        });


                    } else {
                        listenerUserAndRollMain.onSuccess(mstructuresMain, mstructuresSelect);
                        finish();
                    }
                }


            }
        });
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenerUserAndRollMain.onCancel(mtmpItemSelect);
                finish();
            }
        });
        viewHolder.lnDialogUserAndRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.closeKeboard();
            }
        });

        dialogUserAndRole.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void ValidationPerson(final ArrayList<StructurePersonREQ> structurePersonsREQ, final ListenerValidate listenerValidate) {
        new AsyncTask<Void, Void, Boolean>() {


            public String message = "";

            @Override
            protected void onPreExecute() {
                ShowLoading();
                canDo = false;
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... voids) {
                for (StructurePersonREQ structurePerson : structurePersonsREQ) {
                    if (structurePerson.getAction() == -1) {
                        message = Resorse.getString(R.string.error_erja_action_code_validate_p1) + " " + structurePerson.getFullName() + " " + Resorse.getString(R.string.error_erja_action_code_validate_p2);
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {

                if (aBoolean) {
                    listenerValidate.onValid();
                } else {
                    App.ShowMessage().ShowToast(message, Toast.LENGTH_LONG);
                    listenerValidate.unValid();
                }
                HideLoading();
                canDo = true;

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
        viewHolder.smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                BaseActivity.closeKeboard();
            }

            @Override
            public void onPageSelected(int position) {
                BaseActivity.closeKeboard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                BaseActivity.closeKeboard();
            }
        });
    }

    private void initAdapter() {
        adapterUserAndRoleMain = new AdapterUserAndRoleMain(mstructuresMain, new ListenerAdapterUserAndRole() {
            @Override
            public void onSelect(final StructureUserAndRoleDB structureUserAndRoleDB) {
                mtmpItemSelect.add(structureUserAndRoleDB);
                adapterUserAndRoleSelected.Select(structureUserAndRoleDB);
                ManagmentBtnOkEnable();
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
                ManagmentBtnOkEnable();
            }
        });

        if (userAndRoleEnum == UserAndRoleEnum.SEND && Etc > 0) {
            adapterUserAndRoleSelected = new AdapterUserAndRoleSelected(context, mstructuresSelect, userAndRoleEnum, cartableDocumentActionsDBS);
            viewHolder.btnOk.setText(Resorse.getString(R.string.title_send));
        } else {
            adapterUserAndRoleSelected = new AdapterUserAndRoleSelected(context, mstructuresSelect, userAndRoleEnum);
        }
        adapterUserAndRoleSelected.setListener(new ListenerAdapterUserAndRole() {
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
                ManagmentBtnOkEnable();
            }
        });
        ManagmentBtnOkEnable();
        initViewPagerFragment();
    }

    private void ManagmentBtnOkEnable() {
        CustomFunction customFunction = new CustomFunction(context);
        if (mstructuresSelect.size() > 0) {
            viewHolder.btnOk.setEnabled(true);
            customFunction.ChangeBackgroundTintColor(viewHolder.btnOk, R.color.color_Info);
        } else {
            viewHolder.btnOk.setEnabled(false);
            customFunction.ChangeBackgroundTintColor(viewHolder.btnOk, R.color.color_disable);
        }
    }

    private void HideLoading() {
        viewHolder.lnLoading.setVisibility(View.GONE);
    }

    private void ShowLoading() {
        viewHolder.lnLoading.setVisibility(View.VISIBLE);
    }

    private void initViewPagerFragment() {

        fragmentUserAndRoleMain = new FragmentUserAndRoleMain().newInstance(context, adapterUserAndRoleMain, this);
        fragmentUserAndRoleSelect = new FragmentUserAndRoleSelect().newInstance(context, adapterUserAndRoleSelected);
    }


    public void performSearch(String Query, boolean reset) {
        if (!reset) {
            userAndRolePresenter.Search(Query, new ListenerUserAndRollSearch() {
                @Override
                public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesSearch) {
                    mstructuresSearch.clear();
                    mstructuresSearch = new ArrayList<>(structureUserAndRolesSearch);
                    adapterUserAndRoleMain.filter(structureUserAndRolesSearch);
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
        FragmentTransaction transaction = mfragmentManager.beginTransaction();
        transaction.remove(fragmentUserAndRoleMain);
        transaction.remove(fragmentUserAndRoleSelect);
        transaction.commitAllowingStateLoss();
    }

    private void finish() {
        App.canBack = true;
        clearFragment();
        if (dialogUserAndRole != null) dialogUserAndRole.dismiss();
    }
}
