package avida.ican.Ican;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;

import androidx.annotation.Nullable;

import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Interface.ChangeActiveRoleListener;
import avida.ican.Farzin.View.ActivityCreateDocument;
import avida.ican.Farzin.View.ActivitySetting;
import avida.ican.Farzin.View.Dialog.DialogLogList;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinActivityChatRoom;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Farzin.View.FarzinActivityQueue;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Farzin.View.Enum.SettingEnum.AUTOMATIC;
import static avida.ican.Farzin.View.Enum.SettingEnum.MANUALLY;
import static avida.ican.Farzin.View.Enum.SettingEnum.PRIVACY;

/**
 * Created by AtrasVida on 2018-04-09 at 10:14 AM
 */


public abstract class BaseNavigationDrawerActivity extends BaseToolbarActivity {


    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @Nullable
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.txt_main_code_version)
    TextView txtMainCodeVersion;

    @Nullable
    Menu nav_Menu;

    @Nullable
    ActionBarDrawerToggle toggle;

    View headerLayout;

    private String title = "";
    private ImageView imgHeaderNav;
    private TextView txtNavHeaderName;
    private TextView txtNavHeaderRoleName;
    public ChangeActiveRoleListener mainChangeActiveRoleListener;
    private boolean doubleBackToExitPressedOnce = false;

    public void setTollbarTitle(String title) {
        this.title = title;
        assert txtTitle != null;
        txtTitle.setText(title);
    }

    @SuppressLint("SetTextI18n")
    public void initNavigationBar(String title, int menuRes) {
        setSupportActionBar(toolbar);
        assert txtTitle != null;
        txtTitle.setText(title);
        //***********************toolbar*****************************
        assert drawer != null;
        drawer.closeDrawers();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setUpNavigationItemSelectedListener(menuRes);
        /*  navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_BG_Light)));*/
        headerLayout = navigationView.getHeaderView(0);
        initNavHeadeView();

        nav_Menu = navigationView.getMenu();

        //change NavigationIcon color
        toolbar.getNavigationIcon().setColorFilter(Resorse.getColor(R.color.color_White), PorterDuff.Mode.SRC_ATOP);
        //toolbar.setNavigationIcon(Resorse.getDrawable(R.drawable.ic_menu));

        //***********************toolbar*****************************
        txtMainCodeVersion.setText(Resorse.getString(R.string.version) + " " + App.getAppVersionName());

    }

    private void initNavHeadeView() {
        imgHeaderNav = (ImageView) headerLayout.findViewById(R.id.img_header);
        txtNavHeaderName = (TextView) headerLayout.findViewById(R.id.txt_header_name);
        txtNavHeaderRoleName = (TextView) headerLayout.findViewById(R.id.txt_header_role_name);
        txtNavHeaderName.setText("نام کاربری");
        txtNavHeaderRoleName.setText("[سمت]");
    }

    public void setNavHeadeViewTitle(String name, String roleName) {
        txtNavHeaderName.setText(name);
        txtNavHeaderRoleName.setText(roleName);
    }

    private void setUpNavigationItemSelectedListener(final int menuRes) {
        navigationView.setNavigationItemSelectedListener(item -> {
            if (drawer != null) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                    App.canBack = true;
                }

            }

            switch (menuRes) {
                case R.menu.main_drawer: {
                    checkInFirstMenu(item.getItemId());
                    break;
                }
                case R.menu.main_drawer2: {
                    checkInSecoundMenu(item.getItemId());
                    break;
                }
            }


            return true;
        });
    }

    private void checkInFirstMenu(int itemId) {
        switch (itemId) {
            case R.id.nav_create_document: {
                goToActivity(ActivityCreateDocument.class);
                break;
            }
            case R.id.nav_inbox: {
                try {
                    final Activity[] activity = {getActivityFromStackMap(FarzinActivityMain.class.getSimpleName())};
                    final FarzinActivityMain[] activityMain = new FarzinActivityMain[1];
                    if (App.activityStacks != null) {
                        if (activity[0] != null) {
                            activityMain[0] = (FarzinActivityMain) activity[0];
                            activityMain[0].selectCartableDocumentFragment();
                        }
                    } else {
                        goToActivity(FarzinActivityMain.class);

                        App.getHandlerMainThread().postDelayed(() -> {
                            activity[0] = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
                            if (activity[0] != null) {
                                activityMain[0] = (FarzinActivityMain) activity[0];
                                activityMain[0].selectCartableDocumentFragment();
                            }
                        }, 1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.nav_get_data_setting: {
                Intent intent = new Intent(App.CurentActivity, ActivitySetting.class);
                intent.putExtra(PutExtraEnum.SettingType.getValue(), MANUALLY.getValue());
                goToActivity(intent);
                break;
            }
            case R.id.nav_automatic_setting: {
                Intent intent = new Intent(App.CurentActivity, ActivitySetting.class);
                intent.putExtra(PutExtraEnum.SettingType.getValue(), AUTOMATIC.getValue());
                goToActivity(intent);
                break;
            }
            case R.id.nav_privacy_setting: {
                Intent intent = new Intent(App.CurentActivity, ActivitySetting.class);
                intent.putExtra(PutExtraEnum.SettingType.getValue(), PRIVACY.getValue());
                goToActivity(intent);
                break;
            }
            case R.id.nav_queue: {
                goToActivity(FarzinActivityQueue.class);
                break;
            }
            case R.id.nav_change_active_role: {
                if (mainChangeActiveRoleListener != null) {
                    mainChangeActiveRoleListener.doProcess();
                }
                break;
            }
            case R.id.nav_about: {
                App.ShowMessage().ShowToast("درباره ما", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_contact_us: {
                App.ShowMessage().ShowToast("تماس با ما", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_log_out: {
                if (App.getFarzinBroadCastReceiver() != null) {
                    App.getFarzinBroadCastReceiver().stopServices();
                }
                Intent intent = new Intent(App.CurentActivity, FarzinActivityLogin.class);
                intent.putExtra("LogOut", true);
                goToActivity(intent);

                Finish(App.CurentActivity);
                break;
            }

            case R.id.nav_show_log: {
                new DialogLogList(App.CurentActivity).Show(new ArrayList<>(CustomLogger.getLog()));
                break;
            }
            case R.id.nav_chat_room: {
                goToActivity(FarzinActivityChatRoom.class);
                break;
            }
        }
    }

    private void checkInSecoundMenu(int itemId) {
        switch (itemId) {
            case R.id.nav_1: {
                App.ShowMessage().ShowToast("آیتم 1", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_2: {
                App.ShowMessage().ShowToast("آیتم 2", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_3: {
                App.ShowMessage().ShowToast("آیتم 3", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
        }
    }

    public DrawerLayout getDrawer() {
        return drawer;
    }

    public void changeDrawerMenu(int menuRes) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(menuRes);
        nav_Menu = navigationView.getMenu();
        setUpNavigationItemSelectedListener(menuRes);
    }

    public void changeVisibilityItem(int itemRes, boolean isVisible) {
        try {
            nav_Menu.findItem(itemRes).setVisible(isVisible);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                FinishNavigationActivity();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            FinishNavigationActivity();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public void FinishNavigationActivity() {
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            if (doubleBackToExitPressedOnce) {
                Finish(App.CurentActivity);
                return;
            } else {
                App.ShowMessage().ShowToast(Resorse.getString(R.string.msg_double_back), ToastEnum.TOAST_LONG_TIME);
            }
            doubleBackToExitPressedOnce = true;
            App.getHandlerMainThread().postDelayed(() -> {
                doubleBackToExitPressedOnce = false;
            }, TimeValue.SecondsInMilli() * 2);
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }
    }
}
