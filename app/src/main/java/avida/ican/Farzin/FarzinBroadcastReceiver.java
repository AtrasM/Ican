package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import avida.ican.Farzin.Presenter.SendMessageService;
import avida.ican.Ican.App;

/**
 * Created by AtrasVida on 2018-06-25 at 4:28 PM
 */

public class FarzinBroadcastReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent iintent) {
        App.setServiceContext(context);
        context.startService(new Intent(context, SendMessageService.class));

    }
}

