package avida.ican.Farzin.View;

import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import static avida.ican.Farzin.View.Enum.SettingEnum.CUSTOM;
import static avida.ican.Farzin.View.Enum.SettingEnum.SYNC;

public class ActivitySetting extends BaseToolbarActivity {
    @BindView(R.id.ln_document_setting)
    LinearLayout lnDocumentSetting;
    @BindView(R.id.ln_message_setting)
    LinearLayout lnMessageSetting;
    private int settingType;
    @BindString(R.string.title_setting)
    String Title;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTollBar(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        settingType = getIntent().getIntExtra(PutExtraEnum.Settingtype.getValue(), -1);
        initView();
    }

    private void initView() {
        lnDocumentSetting.setOnClickListener(view -> {
            Intent intent = new Intent(App.CurentActivity, ActivityDocumentSetting.class);
            intent.putExtra(PutExtraEnum.Settingtype.getValue(), settingType);
            goToActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.setting_toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionSuccess = menu.findItem(R.id.action_success);
        CustomFunction customFunction = new CustomFunction(App.CurentActivity);
        Drawable drawable = customFunction.ChengeDrawableColor(R.drawable.ic_success, R.color.color_White);
        actionSuccess.setIcon(drawable);
        actionSuccess.setOnMenuItemClickListener(menuItem -> {

            if (settingType == SYNC.getValue()) {
                Intent returnIntent = new Intent();
                setResult(settingType, returnIntent);
                Finish(App.CurentActivity);
            } else if (settingType == CUSTOM.getValue()) {
                Intent returnIntent = new Intent();
                setResult(settingType, returnIntent);
                Finish(App.CurentActivity);
            }

            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
