package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2018-08-6 at 16:30 PM
 */

public class FarzinMessageNotificationReceiver extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Notif","FarzinMessageNotificationReceiver");
        CustomFunction.DismissNotification(context,intent.getIntExtra(PutExtraEnum.ID.name(), -1));

    }
}

