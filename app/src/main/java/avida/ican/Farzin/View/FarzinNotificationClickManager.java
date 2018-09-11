package avida.ican.Farzin.View;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;

import static avida.ican.Ican.BaseActivity.getActivityFromStackMap;
import static avida.ican.Ican.BaseActivity.goToActivity;

/**
 * Created by AtrasVida on 2018-09-01 at 5:12 PM
 */

public class FarzinNotificationClickManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String Extra = getIntent().getStringExtra(PutExtraEnum.Notification.getValue());
        if (Extra.equals(PutExtraEnum.MultyMessage.getValue())) {
            Activity activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
            FarzinActivityMain activityMain;
            if (App.activityStacks != null) {

                if (activity != null) {
                    activityMain = (FarzinActivityMain) activity;
                    activityMain.openMessageList();
                }
            } else {
                goToActivity(FarzinActivityMain.class);
            }

        }

        finish();

    }

}
