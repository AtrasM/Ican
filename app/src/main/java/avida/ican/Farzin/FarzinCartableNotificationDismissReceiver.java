package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-12-24 at 11:51 PM
 */

public class FarzinCartableNotificationDismissReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Notif","FarzinCartableNotificationReceiver");
        CustomFunction.DismissNotification(context, NotificationChanelEnum.Cartable);
    }
}

