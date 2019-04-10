package avida.ican.Farzin.View;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;
import java.util.Stack;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Interface.Message.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataSync;
import avida.ican.Farzin.Presenter.Service.Cartable.GetCartableDocumentService;
import avida.ican.Farzin.View.Dialog.DialogFirstMetaDataSync;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Fragment.FragmentCartable;
import avida.ican.Farzin.View.Fragment.FragmentHome;
import avida.ican.Farzin.View.Fragment.Message.FragmentMessageList;
import avida.ican.Farzin.View.Interface.ListenerFilter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseNavigationDrawerActivity;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityMain extends BaseNavigationDrawerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigation;
    @BindView(R.id.container)
    LinearLayout container;

    private static BottomNavigationViewEx staticbottomNavigation;
    @BindString(R.string.title_farzin_login)
    String Title;
    private boolean menuShow = false;
    private FarzinMetaDataSync farzinMetaDataSync;
    private FarzinBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private static final String TAB_CARTABLE = "tab_cartable";
    private static final String TAB_DASHBOARD = "tab_dashboard";
    private static final String TAB_Message = "tab_message_list";
    private String mCurrentTab = "";
    private Fragment CurentFragment = null;
    private FarzinPrefrences farzinPrefrences;
    private MenuItem actionfilter;
    private ListenerFilter listenerFilter;
    private boolean isFilter = false;
    private boolean filtering = false;
    private String curentTab = "";

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_main;
    }

    @Override
    protected void onResume() {
        CheckNetWork();
        if (App.fragmentStacks != null && App.fragmentStacks.size() > 0) {
            if (actionfilter != null) {
                if (curentTab == TAB_DASHBOARD) {
                    FragmentHome fragmentHome = (FragmentHome) App.fragmentStacks.get(TAB_DASHBOARD).lastElement();
                    fragmentHome.reGetDataFromLocal();
                    actionfilter.setVisible(false);
                }
            }

        }

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFilter = getIntent().getBooleanExtra(PutExtraEnum.IsFilter.getValue(), false);
        CheckNetWork();
        farzinPrefrences = getFarzinPrefrences();
        initNavigationBar(Title, R.menu.main_drawer);
        App.setCurentProject(CurentProject.Farzin);
        initFragmentStack();
        initFarzinMetaDataSyncClass();
        //bottomNavigation.setSelectedItemId(R.id.navigation_home);

    }


    private void initFragmentStack() {
        App.fragmentStacks = new HashMap<>();
        App.fragmentStacks.put(TAB_DASHBOARD, new Stack<Fragment>());
        App.fragmentStacks.put(TAB_Message, new Stack<Fragment>());
        App.fragmentStacks.put(TAB_CARTABLE, new Stack<Fragment>());
        CurentFragment = new FragmentHome();
     /*   Title = "خانه";
        selectedTab(Title, R.menu.main_drawer, TAB_HOME);*/
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setTextVisibility(false);
        bottomNavigation.setCurrentItem(1);
        staticbottomNavigation = bottomNavigation;
    }

    private void initBroadcastReceiver() {
        broadcastReceiver = new FarzinBroadcastReceiver();
        intentFilter = new IntentFilter();
        String BROADCAST = "avida.ican.android.action.broadcast";
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(BROADCAST);
        registerReceiver(broadcastReceiver, intentFilter);
        Intent intent = new Intent(BROADCAST);
        sendBroadcast(intent);
        App.setFarzinBroadCastReceiver(broadcastReceiver);
    }

    private void initFarzinMetaDataSyncClass() {

        farzinMetaDataSync = new FarzinMetaDataSync().RunONschedule(new MetaDataSyncListener() {
            @Override
            public void onSuccess(MetaDataNameEnum metaDataNameEnum) {
                if (BaseActivity.dialogMataDataSync != null) {
                    BaseActivity.dialogMataDataSync.serviceGetDataFinish(metaDataNameEnum);
                }
                if (!isMyServiceRunning(GetCartableDocumentService.class)) {
                    initBroadcastReceiver();
                }
            }

            @Override
            public void onFailed(MetaDataNameEnum metaDataNameEnum) {

            }

            @Override
            public void onCancel(MetaDataNameEnum metaDataNameEnum) {

            }

            @Override
            public void onFinish() {
                if (BaseActivity.dialogMataDataSync != null) {
                    BaseActivity.dialogMataDataSync.serviceGetDataFinish(MetaDataNameEnum.SyncUserAndRole);
                }
                if (!isMyServiceRunning(GetCartableDocumentService.class)) {
                    initBroadcastReceiver();
                }
                StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB();
                structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID(), farzinPrefrences.getRoleID());
                String title = structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();
                setNavHeadeViewTitle(title);
            }
        });

        if (!farzinPrefrences.isDataForFirstTimeSync()) {
            container.setVisibility(View.GONE);
            // TODO: 2019-04-09 show activity setting
            BaseActivity.dialogMataDataSync = new DialogFirstMetaDataSync(App.CurentActivity, new avida.ican.Farzin.Model.Interface.MetaDataSyncListener() {
                @Override
                public void onFinish() {
                    if (staticbottomNavigation != null) {
                        staticbottomNavigation.setCurrentItem(0);
                        staticbottomNavigation.setCurrentItem(1);
                        container.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCancel() {

                }
            });
            BaseActivity.dialogMataDataSync.Creat();
        } else {
            if (staticbottomNavigation != null) {
                staticbottomNavigation.setCurrentItem(1);
            }
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String Title = "";
        switch (item.getItemId()) {

            case R.id.navigation_home: {
                Title = "خانه";
                selectedTab(Title, R.menu.main_drawer, TAB_DASHBOARD);
                break;
            }
            case R.id.navigation_message: {
                Title = "سیستم پیام";
                selectedTab(Title, R.menu.main_drawer, TAB_Message);

                break;
            }
            case R.id.navigation_cartable: {
                Title = "کارتابل";
                selectedTab(Title, R.menu.main_drawer, TAB_CARTABLE);
                break;
            }

        }
        //StartFragment(selectedFragment, BACK_STACK_ROOT_TAG);
        return true;
    }

    private void selectedTab(String title, int drawer, String tabId) {
        mCurrentTab = tabId;

        if (App.fragmentStacks.get(tabId).size() == 0) {
         /*   if (drawer > 0) {
                changeDrawerMenu(drawer);
            }*/

            /*
             *    First time this tab is selected. So add first fragment of that tab.
             *    Dont need animation, so that argument is false.
             *    We are adding a new fragment which is not present in stack. So add to stack is true.
             */

            switch (tabId) {
                case TAB_DASHBOARD: {
                    pushFragments(tabId, new FragmentHome(), true);
                    break;
                }
                case TAB_CARTABLE: {
                    App.fragmentCartable = new FragmentCartable().newInstance(getSupportFragmentManager());
                    Fragment fragment = App.fragmentCartable;
                    pushFragments(tabId, fragment, true);
                    break;
                }

                case TAB_Message: {
                    App.fragmentMessageList = new FragmentMessageList().newInstance(getSupportFragmentManager());
                    Fragment fragment = App.fragmentMessageList;
                    pushFragments(tabId, fragment, true);
                    break;
                }

            }

        } else {
            /*
             *    We are switching tabs, and target tab is already has atleast one fragment.
             *    No need of animation, no need of stack pushing. Just show the target fragment
             */
            pushFragments(tabId, App.fragmentStacks.get(tabId).lastElement(), false);
        }
        setTollbarTitle(title);
    }

    public void pushFragments(String tab, Fragment fragment, boolean shouldAdd) {


        if (shouldAdd) {
            curentTab = tab;
            App.fragmentStacks.get(tab).push(fragment);
            if (tab == TAB_Message) {
                actionfilter.setVisible(true);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(CurentFragment)
                    .add(R.id.frm_main, fragment)
                    .commit();
        } else {
            if (!tab.equals(curentTab)) {
                curentTab = tab;
                if (tab == TAB_DASHBOARD) {
                    FragmentHome fragmentHome = (FragmentHome) App.fragmentStacks.get(tab).lastElement();
                    fragmentHome.reGetDataFromLocal();
                    fragment = fragmentHome;
                    actionfilter.setVisible(false);
                } else if (tab == TAB_CARTABLE) {
                    FragmentCartable fragmentCartable = (FragmentCartable) App.fragmentStacks.get(tab).lastElement();
                    fragmentCartable.reGetDataFromLocal();
                    fragment = fragmentCartable;
                    actionfilter.setVisible(false);
                } else if (tab == TAB_Message) {
                    if (!filtering) {
                        filterReceiveMessage();
                    }
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(CurentFragment)
                        .show(fragment)
                        .commit();


            }
        }
        CurentFragment = fragment;

    }

    public void popFragments() {
        /*
         *    Select the second last fragment in current tab's stack..
         *    which will be shown after the fragment transaction given below
         */
        Fragment fragment = App.fragmentStacks.get(mCurrentTab).elementAt(App.fragmentStacks.get(mCurrentTab).size() - 2);

        /*pop current fragment from stack.. */
        // fragment=   App.fragmentStacks.get(mCurrentTab).pop();

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frm_main, fragment)
                .commit();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    public void selectMessageFragment(boolean isFilter) {
        if (staticbottomNavigation != null) {
            this.isFilter = isFilter;
            staticbottomNavigation.setCurrentItem(2);
        }
        if (isFilter) {
            filterReceiveMessage();
        }

    }

    public void selectMessageFragment() {
        if (staticbottomNavigation != null) {
            staticbottomNavigation.setCurrentItem(2);
            if (!filtering) {
                filterReceiveMessage();
            }
        }

    }


    public void selectCartableDocumentFragment() {
        if (staticbottomNavigation != null) {
            staticbottomNavigation.setCurrentItem(0);
        }
        actionfilter.setVisible(false);
    }

    private void CheckNetWork() {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                App.networkStatus = NetworkStatus.Connected;
            }

            @Override
            public void disConnected() {
                App.networkStatus = NetworkStatus.WatingForNetwork;
            }

            @Override
            public void onFailed() {
                App.networkStatus = NetworkStatus.WatingForNetwork;
            }
        });
    }

    public void setFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.filter_toolbar_menu, menu);
        // menu.findItem(R.id.action_search).setIntent(new Intent(G.currentActivity, ActivitySearch.class));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        actionfilter = menu.findItem(R.id.action_filter);
        actionfilter.setVisible(false);
        actionfilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (!filtering) {
                    isFilter = !isFilter;
                    selectMessageFragment();
                }

                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void filterReceiveMessage() {
        filtering = true;
        FragmentMessageList fragmentMessageList = (FragmentMessageList) App.fragmentStacks.get(TAB_Message).lastElement();
        actionfilter.setVisible(true);
        listenerFilter = new ListenerFilter() {
            @Override
            public void isFilter(boolean mIsFilter) {
                isFilter = mIsFilter;
                if (isFilter) {
                    actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
                } else {
                    actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
                }
            }
        };
        if (isFilter) {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            fragmentMessageList.filterMessage(Status.UnRead, isFilter, listenerFilter);
        } else {
            actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            fragmentMessageList.filterMessage(null, isFilter, listenerFilter);
        }
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                filtering = false;
            }
        }, 1000);

    }


    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        farzinMetaDataSync.onDestory();
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if (App.fragmentStacks.get(mCurrentTab).size() == 1) {
            // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
            if (mCurrentTab == TAB_DASHBOARD) {
                FinishNavigationActivity();
            } else {
                if (staticbottomNavigation != null) {
                    staticbottomNavigation.setCurrentItem(1);
                }
            }

            return;
        }

        /* Goto previous fragment in navigation stack of this tab */
        popFragments();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (App.fragmentStacks.get(mCurrentTab).size() == 1) {
                // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
                if (mCurrentTab == TAB_DASHBOARD) {
                    FinishNavigationActivity();
                } else {
                    if (staticbottomNavigation != null) {
                        staticbottomNavigation.setCurrentItem(1);
                    }
                }

            }
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}
