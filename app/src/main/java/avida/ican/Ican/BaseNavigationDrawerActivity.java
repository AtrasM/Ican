package avida.ican.Ican;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

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

    @Nullable
    Menu nav_Menu;

    @Nullable
    ActionBarDrawerToggle toggle;

    View headerLayout;

    private String title = "";

    public BaseNavigationDrawerActivity setTollbarTitle(String title) {
        this.title = title;
        txtTitle.setText(title);
        return this;
    }

    public void initNavigationBar(String title, int menuRes) {
        setSupportActionBar(toolbar);
        txtTitle.setText(title);
        //***********************toolbar*****************************
        drawer.closeDrawers();
       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setUpNavigationItemSelectedListener(menuRes);
       /*  navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_BG_Light)));*/
        headerLayout = navigationView.getHeaderView(0);
        nav_Menu = navigationView.getMenu();
        //***********************toolbar*****************************

    }


    private void setUpNavigationItemSelectedListener(final int menuRes) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
            }
        });
    }

    private void checkInFirstMenu(int itemId) {
        switch (itemId) {
            case R.id.nav_about: {
                App.ShowMessage().ShowToast("درباره ما", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_contact_us: {
                App.ShowMessage().ShowToast("تماس با ما", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.nav_log_out: {
                //App.canBack = true;
                Finish();
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

    public void changeDrawerMenu(int menuRes) {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(menuRes);
        nav_Menu = navigationView.getMenu();
        setUpNavigationItemSelectedListener(menuRes);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {

                    Finish();
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                Finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
