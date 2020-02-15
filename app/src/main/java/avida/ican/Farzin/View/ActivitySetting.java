package avida.ican.Farzin.View;

import androidx.appcompat.widget.SwitchCompat;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static avida.ican.Farzin.View.Enum.SettingEnum.AUTOMATIC;
import static avida.ican.Farzin.View.Enum.SettingEnum.MANUALLY;
import static avida.ican.Farzin.View.Enum.SettingEnum.PRIVACY;
import static avida.ican.Farzin.View.Enum.SettingEnum.SYNC;

public class ActivitySetting extends BaseToolbarActivity {
    @BindView(R.id.ln_document_setting)
    LinearLayout lnDocumentSetting;
    @BindView(R.id.ln_message_setting)
    LinearLayout lnMessageSetting;
    @BindView(R.id.ln_privacy)
    LinearLayout lnPrivacy;
    @BindView(R.id.ln_data_setting)
    LinearLayout lnDataSetting;
    private int settingType;
    @BindView(R.id.img_error)
    ImageView imgError;
    @BindView(R.id.txt_error_message)
    TextView txtErrorMessage;
    @BindView(R.id.ln_error)
    LinearLayout lnError;
    @BindView(R.id.ln_finger_print)
    LinearLayout lnFingerPrint;
    @BindView(R.id.ln_lock)
    LinearLayout lnLock;
    @BindView(R.id.txt_title_finger_print)
    TextView txtTitleFingerPrint;
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindView(R.id.switch_finger_print)
    SwitchCompat switchFingerPrint;
    @BindView(R.id.switch_lock)
    SwitchCompat switchLock;


    @BindString(R.string.title_setting)
    String Title;
    private FarzinPrefrences farzinPrefrences;
    private boolean isValidFingerPrint = false;

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
        farzinPrefrences = new FarzinPrefrences().init();
        settingType = getIntent().getIntExtra(PutExtraEnum.SettingType.getValue(), -1);
        App.getFarzinBroadCastReceiver().stopServices();
        if (settingType == MANUALLY.getValue()) {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                Back();
                App.ShowMessage().ShowToast(Resorse.getString(R.string.unable_get_data_in_ofline_mode), ToastEnum.TOAST_LONG_TIME);
            }
        }
        if (settingType == SYNC.getValue()) {
            txtInfo.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            txtInfo.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initView();
    }

    private void initView() {
        if (settingType == SYNC.getValue()) {
            lnPrivacy.setVisibility(View.GONE);
        } else if (settingType == MANUALLY.getValue()) {
            lnPrivacy.setVisibility(View.GONE);
        } else if (settingType == AUTOMATIC.getValue()) {
            lnMessageSetting.setVisibility(View.GONE);
            lnPrivacy.setVisibility(View.GONE);
        } else if (settingType == PRIVACY.getValue()) {
            initPrivacyView();
        }
        lnDocumentSetting.setOnClickListener(view -> {
            Intent intent = new Intent(App.CurentActivity, ActivityDocumentSetting.class);
            intent.putExtra(PutExtraEnum.SettingType.getValue(), settingType);
            goToActivity(intent);
        });
        lnMessageSetting.setOnClickListener(view -> {
            Intent intent = new Intent(App.CurentActivity, ActivityMessageSetting.class);
            intent.putExtra(PutExtraEnum.SettingType.getValue(), settingType);
            goToActivity(intent);
        });
    }

    private void initPrivacyView() {
        if (farzinPrefrences.isAnableAppLock()) {
            switchLock.setChecked(true);
        } else {
            if (switchLock.isChecked()) {
                switchLock.setChecked(false);
            }
        }
        lnDataSetting.setVisibility(View.GONE);
        lnPrivacy.setVisibility(View.VISIBLE);
        if (switchFingerPrint.isChecked()) {
            switchFingerPrint.setChecked(false);
        }

        isValidFingerPrint = isValidFingerPrint();
        if (isValidFingerPrint) {
            if (switchLock.isChecked()) {
                setAnableFingerPrint(true);
            } else {
                setAnableFingerPrint(false);
            }
            if (farzinPrefrences.isAnableFingerPrint()) {
                switchFingerPrint.setChecked(true);
            } else {
                switchFingerPrint.setChecked(false);
            }
        }

        switchLock.setOnCheckedChangeListener((compoundButton, b) -> {
            switchLockClicked(b);
        });
        lnLock.setOnClickListener(view -> {
            switchLock.setChecked(!switchLock.isChecked());
            //switchLockClicked(switchLock.isChecked());

        });

        switchFingerPrint.setOnCheckedChangeListener((compoundButton, b) -> farzinPrefrences.putIsAnableFingerPrint(b));
        lnFingerPrint.setOnClickListener(view -> {
            if (switchFingerPrint.isClickable()) {
                switchFingerPrint.setChecked(!switchFingerPrint.isChecked());
                farzinPrefrences.putIsAnableFingerPrint(switchFingerPrint.isChecked());
            }

        });
    }

    private void switchLockClicked(boolean isCheck) {
        if (isCheck) {
            if (isValidFingerPrint) {
                setAnableFingerPrint(true);
            } else {
                if (switchFingerPrint.isChecked()) {
                    switchFingerPrint.setChecked(false);
                }
                setAnableFingerPrint(false);
                farzinPrefrences.putIsAnableFingerPrint(false);
            }
        } else {
            if (switchFingerPrint.isChecked()) {
                switchFingerPrint.setChecked(false);
            }
            setAnableFingerPrint(false);
            farzinPrefrences.putIsAnableFingerPrint(false);
        }
        farzinPrefrences.putAnableAppLock(isCheck);
    }

    private boolean isValidFingerPrint() {
        boolean valid = true;
        String strError = "";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Fingerprint API only available on from Android 6.0 (M)
            FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (!fingerprintManager.isHardwareDetected()) {
                strError = Resorse.getString(R.string.error_device_doesn_not_support_fingerprint);
                valid = false;
                farzinPrefrences.putIsSupportFingerPrint(false);
                farzinPrefrences.putIsAnableFingerPrint(false);
                // Device doesn't support fingerprint authentication
            } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                valid = false;
                strError = Resorse.getString(R.string.error_hasn_not_enrolled_any_fingerprints);
                farzinPrefrences.putIsSupportFingerPrint(true);
                farzinPrefrences.putIsAnableFingerPrint(false);
                // User hasn't enrolled any fingerprints to authenticate with
            } else {
                farzinPrefrences.putIsSupportFingerPrint(true);
                // Everything is ready for fingerprint authentication
            }
        } else {
            strError = Resorse.getString(R.string.error_device_doesn_not_support_fingerprint);
            valid = false;
            farzinPrefrences.putIsSupportFingerPrint(false);
            farzinPrefrences.putIsAnableFingerPrint(false);
        }

        if (!valid) {
            txtErrorMessage.setText(strError);
            switchFingerPrint.setChecked(false);
            setAnableFingerPrint(false);
            lnError.setVisibility(View.VISIBLE);
        } else {
            lnError.setVisibility(View.GONE);
            setAnableFingerPrint(true);
        }
        return valid;
    }

    private void setAnableFingerPrint(boolean isAnable) {
        switchFingerPrint.setClickable(isAnable);
        if (isAnable) {
            txtTitleFingerPrint.setTextColor(Resorse.getColor(R.color.color_txt_Normal));
            lnFingerPrint.setEnabled(true);
        } else {
            txtTitleFingerPrint.setTextColor(Resorse.getColor(R.color.color_disable));
            lnFingerPrint.setEnabled(false);
        }


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
        MenuItem actionDownload = menu.findItem(R.id.action_download);
        actionSuccess.setVisible(false);
        if (settingType != SYNC.getValue()) {
            actionDownload.setVisible(false);
        } else {
            actionDownload.setVisible(true);
            CustomFunction customFunction = new CustomFunction(App.CurentActivity);
            Drawable drawable = customFunction.ChengeDrawableColor(R.drawable.ic_download, R.color.color_White);
            actionDownload.setIcon(drawable);
            actionDownload.setOnMenuItemClickListener(menuItem -> {
                Back();
                return true;
            });
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Back();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (settingType != SYNC.getValue()) {
                Back();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Back() {
        if (settingType == MANUALLY.getValue() || settingType == AUTOMATIC.getValue()) {
            App.getFarzinBroadCastReceiver().runServices();
        }
        Intent returnIntent = new Intent();
        setResult(settingType, returnIntent);
        Finish(App.CurentActivity);
    }
}
