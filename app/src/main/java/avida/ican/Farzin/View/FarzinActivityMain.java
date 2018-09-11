package avida.ican.Farzin.View;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;
import java.util.Stack;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Presenter.FarzinMetaDataSync;
import avida.ican.Farzin.Presenter.Service.Message.SendMessageService;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Fragment.FragmentHome;
import avida.ican.Farzin.View.Fragment.FragmentMessageList;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseNavigationDrawerActivity;
import avida.ican.R;
import butterknife.BindView;

public class FarzinActivityMain extends BaseNavigationDrawerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigation;
    private static BottomNavigationViewEx staticbottomNavigation;


    private String Title = "فرزین";
    private boolean menuShow = false;
    private FarzinMetaDataSync farzinMetaDataSync;
    private FarzinBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private static final String TAB_HOME = "tab_home";
    private static final String TAB_DASHBOARD = "tab_dashboard";
    private static final String TAB_Message = "tab_message_list";
    private String mCurrentTab = "";
    private Fragment CurentFragment = null;

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_main;
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNavigationBar(Title, R.menu.main_drawer);
        App.setCurentProject(CurentProject.Farzin);
        initFarzinMetaDataSyncClass();

        initFragmentStack();

        //bottomNavigation.setSelectedItemId(R.id.navigation_home);


    }


    private void initFragmentStack() {
        App.fragmentStacks = new HashMap<>();
        App.fragmentStacks.put(TAB_HOME, new Stack<Fragment>());
        App.fragmentStacks.put(TAB_DASHBOARD, new Stack<Fragment>());
        App.fragmentStacks.put(TAB_Message, new Stack<Fragment>());
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
    }

    private void initFarzinMetaDataSyncClass() {
        farzinMetaDataSync = new FarzinMetaDataSync().RunONschedule(new MetaDataSyncListener() {
            @Override
            public void onSuccess(MetaDataNameEnum metaDataNameEnum) {

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

                    BaseActivity.dialogMataDataSync.dismiss();
                }
                if (!isMyServiceRunning(SendMessageService.class)) {
                    initBroadcastReceiver();
                }
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String Title = "";
        switch (item.getItemId()) {

            case R.id.navigation_home: {
                Title = "خانه";
                selectedTab(Title, R.menu.main_drawer, TAB_HOME);
                break;
            }
            case R.id.navigation_message: {
                Title = "سیستم پیام";
                selectedTab(Title, R.menu.main_drawer2, TAB_Message);

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
            setTollbarTitle(title);
      /*
       *    First time this tab is selected. So add first fragment of that tab.
       *    Dont need animation, so that argument is false.
       *    We are adding a new fragment which is not present in stack. So add to stack is true.
       */

            switch (tabId) {
                case TAB_HOME: {
                    pushFragments(tabId, new FragmentHome(), true);
                    break;
                }
                case TAB_DASHBOARD: {
                    pushFragments(tabId, new FragmentHome(), true);
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
    }

    public void pushFragments(String tag, Fragment fragment, boolean shouldAdd) {
        if (shouldAdd) {
            App.fragmentStacks.get(tag).push(fragment);
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(CurentFragment)
                    .add(R.id.frm_main, fragment)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(CurentFragment)
                    .show(fragment)
                    .commit();
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

    public void openMessageList() {
        if (staticbottomNavigation != null) {
            staticbottomNavigation.setCurrentItem(2);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {

            unregisterReceiver(broadcastReceiver);
        }
        farzinMetaDataSync.onDestory();
    }

    @Override
    public void onBackPressed() {
        if (App.fragmentStacks.get(mCurrentTab).size() == 1) {
            // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
            finish();
            return;
        }

    /* Goto previous fragment in navigation stack of this tab */
        popFragments();
    }
}
