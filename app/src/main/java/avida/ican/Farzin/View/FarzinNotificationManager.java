package avida.ican.Farzin.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.CurentActivity = this;
        String Extra = getIntent().getStringExtra(PutExtraEnum.Notification.getValue());
        if (Extra.equals(PutExtraEnum.MultyMessage.getValue())) {
            activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());

            if (App.activityStacks != null) {
                if (activity != null) {
                    activityMain = (FarzinActivityMain) activity;
                    activityMain.setFilter(true);
                    activityMain.selectMessageFragment();
                }
            } else {
                Intent intent = new Intent(this, FarzinActivityMain.class);
                intent.putExtra(PutExtraEnum.IsFilter.getValue(), true);
                goToActivity(intent);
                App.getHandlerMainThread().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
                        if (activity != null) {
                            closeActivity();
                            activityMain = (FarzinActivityMain) activity;
                            activityMain.selectMessageFragment();
                        }
                    }
                }, 1000);
            }

        } else if (Extra.equals(PutExtraEnum.MultyCartableDocument.getValue())) {
            activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
            if (App.activityStacks != null) {
                if (activity != null) {
                    closeActivity();
                    activityMain = (FarzinActivityMain) activity;
                    activityMain.selectCartableDocumentFragment();
                }
            } else {
                goToActivity(FarzinActivityMain.class);

                App.getHandlerMainThread().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity = getActivityFromStackMap(FarzinActivityMain.class.getSimpleName());
                        if (activity != null) {
                            activityMain = (FarzinActivityMain) activity;
                            activityMain.selectMessageFragment();
                        }
                    }
                }, 500);
            }

        } else if (Extra.equals(PutExtraEnum.LogOut.getValue())) {
            Intent intent = new Intent(this, FarzinActivityLogin.class);
            intent.putExtra("LogOut", true);
            goToActivity(intent);

        }

        finish();

    }

    void closeActivity() {

        BaseActivity.CloseActivitys(new ListenerCloseActivitys() {
            @Override
            public void onFinish() {
            }

            @Override
            public void onCancel() {
            }
        });
    }

}
