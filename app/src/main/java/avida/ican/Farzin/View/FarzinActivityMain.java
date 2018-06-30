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
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Presenter.FarzinMetaDataSync;
import avida.ican.Farzin.Presenter.SendMessageService;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Fragment.FragmentHome;
import avida.ican.Farzin.View.Fragment.FragmentSecond;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseNavigationDrawerActivity;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

public class FarzinActivityMain extends BaseNavigationDrawerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    Fragment selectedFragment;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @BindView(R.id.fab_msg)
    FloatingActionMenu fabMsg;
    @BindView(R.id.fab_new_msg)
    FloatingActionButton fabNewMsg;
    @BindView(R.id.fab_edit_msg)
    FloatingActionButton fabEditMsg;


    private String Title = "فرزین";
    private boolean menuShow = false;
    private FarzinMetaDataSync farzinMetaDataSync;
    private FarzinBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

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

        if (!isMyServiceRunning(SendMessageService.class)) {
            initBroadcastReceiver();
        }

        bottomNavigation.setOnNavigationItemSelectedListener(this);
        selectedFragment = FragmentHome.newInstance();
        StartFragment();

        fabMsg.showMenu(!menuShow);
        fabEditMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.ShowMessage().ShowToast("ویرایش پیام", ToastEnum.TOAST_SHORT_TIME);
            }
        });
        fabNewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMsg.showMenu(!menuShow);
                goToActivity(FarzinActivityWriteMessage.class);
            }
        });

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
        farzinMetaDataSync = new FarzinMetaDataSync().RunONschedule();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.navigation_home: {
                selectedFragment = FragmentHome.newInstance();
                setTollbarTitle("تب اول");
                changeDrawerMenu(R.menu.main_drawer);
                break;
            }
            case R.id.navigation_message: {
                item.setTitle("ok");
                setTollbarTitle("تب دوم");
                changeDrawerMenu(R.menu.main_drawer2);
                selectedFragment = FragmentSecond.newInstance();

                break;
            }

        }
        StartFragment();
        return true;
    }

    private void StartFragment() {
        final String BACK_STACK_ROOT_TAG = "root_fragment";
        if (selectedFragment != null) {
          /* if(selectedFragment.isVisible()) {
               selectedFragment.setMenuVisibility(false);
           }*/
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frm_main, selectedFragment)
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit();
            /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if(App.canRecreatFragment){
                transaction.replace(R.id.frm_main, selectedFragment);
            }else{
                transaction.show(selectedFragment);
            }

            transaction.commit();*/
            // selectedFragment.setMenuVisibility(true);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        farzinMetaDataSync.onDestory();
        unregisterReceiver(broadcastReceiver);
    }
}
