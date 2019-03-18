package avida.ican.Ican;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.NetWorkStatusListener;
import avida.ican.R;
import butterknife.BindView;

/**
 * Created by AtrasVida on 2018-04-09 at 10:14 AM
 */


public abstract class BaseToolbarActivity extends BaseActivity {
    private String title = "";
    private boolean DisplayBack = true;

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.ln_toolbar_title)
    public LinearLayout lnToolbarTitle;
    @Nullable
    @BindView(R.id.txt_net_status)
    TextView txtNetworkStatus;

    private NetWorkStatusListener netWorkStatusListener;

    @Override
    protected void onResume() {
        super.onResume();
        if (netWorkStatusListener != null) {
            RefreshNetworkStatus();
        }
        //setlnToolbarTitleMargin(80);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    if (App.netWorkStatusListener == null) {
            RunNetWorkChecking();
        }*/
        RunNetWorkChecking();
    }


    public BaseToolbarActivity setTollbarDisplayBack(boolean DisplayBack) {
        this.DisplayBack = DisplayBack;
        return this;
    }

    @SuppressWarnings("SameParameterValue")
    public void setTollbarTitle(String title) {
        this.title = title;
        txtTitle.setText(title);
    }

    public void initTollBar(String title) {
        setSupportActionBar(toolbar);
        txtTitle.setText(title);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        if (DisplayBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    private void RunNetWorkChecking() {
        netWorkStatusListener = new NetWorkStatusListener() {


            String NetWorkStatusTitle = "";

            @Override
            public void Connected() {
                App.networkStatus = NetworkStatus.Connected;
                NetWorkStatusTitle = App.networkStatus.toLocalizedString(App.CurentActivity);
                SetNetworkStatus(NetWorkStatusTitle, View.VISIBLE);
            }

            @Override
            public void WatingForNetwork() {
                App.networkStatus = NetworkStatus.WatingForNetwork;
                NetWorkStatusTitle = App.networkStatus.toLocalizedString(App.CurentActivity);
                SetNetworkStatus(NetWorkStatusTitle, View.VISIBLE);
            }

            @Override
            public void WatingForConnecting() {
                App.networkStatus = NetworkStatus.WatingForConnecting;
                NetWorkStatusTitle = App.networkStatus.toLocalizedString(App.CurentActivity);
                SetNetworkStatus(NetWorkStatusTitle, View.VISIBLE);
            }

            @Override
            public void Syncing() {
                App.networkStatus = NetworkStatus.Syncing;
                NetWorkStatusTitle = App.networkStatus.toLocalizedString(App.CurentActivity);
                SetNetworkStatus(NetWorkStatusTitle, View.VISIBLE);
            }

            @Override
            public void DontSetStatus() {
                NetWorkStatusTitle = "";
                SetNetworkStatus(NetWorkStatusTitle, View.GONE);
            }
        };
        App.netWorkStatusListener = this.netWorkStatusListener;
       /* if (App.netWorkCheckingService == null) {
            App.netWorkStatusListener = this.netWorkStatusListener;
        } else {
            RefreshNetworkStatus();
        }*/

    }


    private void RefreshNetworkStatus() {
        //App.netWorkCheckingService = this.netWorkCheckingService;
        App.netWorkStatusListener = this.netWorkStatusListener;
        switch (App.networkStatus) {
            case Syncing: {
                App.netWorkStatusListener.Syncing();
                break;
            }
            case NoAction: {
                App.netWorkStatusListener.DontSetStatus();
                break;
            }
            case Connected: {
                App.netWorkStatusListener.Connected();
                break;
            }
            case WatingForNetwork: {
                App.netWorkStatusListener.WatingForNetwork();
                break;
            }
            case WatingForConnecting: {
                App.netWorkStatusListener.WatingForConnecting();
                break;
            }
        }
    }

    private void SetNetworkStatus(String NetWorkStatusTitle, int NetworkStatusVisibility) {
        Activity activity = App.CurentActivity;
        try {
            txtNetworkStatus.setText(NetWorkStatusTitle);
            txtNetworkStatus.setVisibility(NetworkStatusVisibility);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
    @SuppressWarnings("SameParameterValue")
    public void setlnToolbarTitleMargin(int marginLeft) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(marginLeft, 0, 0, 0);
        lnToolbarTitle.setLayoutParams(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Finish(App.CurentActivity);
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Finish(App.CurentActivity);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
