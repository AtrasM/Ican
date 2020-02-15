package avida.ican.Farzin.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Interface.ListenerCloseActivitys;

import static avida.ican.Ican.BaseActivity.getActivityFromStackMap;
import static avida.ican.Ican.BaseActivity.goToActivity;

/**
 * Created by AtrasVida on 2018-09-01 at 5:12 PM
 */

public class FarzinNotificationManager extends AppCompatActivity {
    private FarzinActivityMain activityMain;
    private Activity activity;
    private FarzinPrefrences farzinPrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.CurentActivity = this;
        farzinPrefrences = new FarzinPrefrences().init();
        String extra = getIntent().getStringExtra(PutExtraEnum.Notification.getValue());
        App.getHandlerMainThread().post(() -> BaseActivity.CloseActivitys(true, new ListenerCloseActivitys() {
            @Override
            public void onFinish() {
                continuProccess(extra);
            }

            @Override
            public void onCancel() {
                continuProccess(extra);
            }
        }));
    }

    private void continuProccess(String extra) {

        activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
        if (farzinPrefrences.isAnableAppLock()) {
            if (App.activityStacks == null || activity == null) {
                extra = PutExtraEnum.IsAppLock.getValue();
            }
        }
        if (!farzinPrefrences.isRemember()) {
            if (App.activityStacks == null || activity == null) {
                extra = PutExtraEnum.LogOut.getValue();
            }
        }

        try {
            if (extra.equals(PutExtraEnum.IsAppLock.getValue())) {
                goToActivity(FarzinActivityLogin.class);
            } else if (extra.equals(PutExtraEnum.LogOut.getValue())) {
                Intent intent = new Intent(this, FarzinActivityLogin.class);
                intent.putExtra("LogOut", true);
                goToActivity(intent);
            } else if (extra.equals(PutExtraEnum.MultyMessage.getValue())) {
                if (App.activityStacks != null && activity != null) {
                    activityMain = (FarzinActivityMain) activity;
                    activityMain.setFilter(true);
                    activityMain.selectMessageFragment();
                } else {
                    Intent intent = new Intent(this, FarzinActivityMain.class);
                    intent.putExtra(PutExtraEnum.IsFilter.getValue(), true);
                    intent.putExtra(PutExtraEnum.Notification.getValue(), PutExtraEnum.MultyMessage.getValue());
                    goToActivity(intent);
                }

            } else if (extra.equals(PutExtraEnum.MultyCartableDocument.getValue())) {
                if (App.activityStacks != null && activity != null) {
                    activityMain = (FarzinActivityMain) activity;
                    activityMain.selectCartableDocumentFragment();
                } else {
                    Intent intent = new Intent(this, FarzinActivityMain.class);
                    intent.putExtra(PutExtraEnum.Notification.getValue(), PutExtraEnum.MultyCartableDocument.getValue());
                    goToActivity(intent);
                }

            }

            finish();
        } catch (Exception e) {
            Log.i("FarzinError", "error = " + e.toString());
            e.printStackTrace();
            finish();
        }

    }


}
