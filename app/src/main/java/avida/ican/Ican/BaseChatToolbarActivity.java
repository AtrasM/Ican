package avida.ican.Ican;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import avida.ican.Farzin.View.Enum.Chat.ChatConnectionStateEnum;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Ican.View.Interface.NetWorkStatusListener;
import avida.ican.R;
import butterknife.BindView;

/**
 * Created by AtrasVida on 2018-04-09 at 10:14 AM
 */


public abstract class BaseChatToolbarActivity extends BaseActivity {
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

    private BroadcastReceiver chatConnectionReciver;
    private ChatConnectionStateEnum connectionState = ChatConnectionStateEnum.NoAction;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initChatConnectionReciver();
    }


    public BaseChatToolbarActivity setTollbarDisplayBack(boolean DisplayBack) {
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
        setConnectionStatus(connectionState.getValue());
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        if (DisplayBack) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    private void initChatConnectionReciver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatPutExtraEnum.ConnectionReceiver.getValue());
        chatConnectionReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null) {
                    if (intent.getAction().equals(ChatPutExtraEnum.ConnectionReceiver.getValue())) {
                        connectionState = (ChatConnectionStateEnum) intent.getSerializableExtra(ChatPutExtraEnum.ConnectionStatus.getValue());
                        //String connectionState = intent.getStringExtra(ChatPutExtraEnum.ConnectionState.getValue());
                        setConnectionStatus(connectionState.getValue());
                    }

                }
            }
        };
        registerReceiver(chatConnectionReciver, filter);

    }


    private void setConnectionStatus(String NetWorkStatusTitle) {
        runOnUiThread(() -> {
            try {
                txtNetworkStatus.setText(NetWorkStatusTitle);
                txtNetworkStatus.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        });
        Activity activity = App.CurentActivity;


    }

    public ChatConnectionStateEnum getConnectionStatus() {
        return connectionState;


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
