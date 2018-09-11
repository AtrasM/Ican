package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import avida.ican.Farzin.Presenter.Service.Message.GetRecieveMessageService;
import avida.ican.Farzin.Presenter.Service.Message.GetSentMessageService;
import avida.ican.Farzin.Presenter.Service.Message.SendMessageService;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.NetWorkCheckingService;

/**
 * Created by AtrasVida on 2018-06-25 at 4:28 PM
 */

public class FarzinBroadcastReceiver extends BroadcastReceiver {
    Handler handler = new Handler();

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent iintent) {
        App.setServiceContext(context);
        context.startService(new Intent(context, SendMessageService.class));
        context.startService(new Intent(context, NetWorkCheckingService.class));
        context.startService(new Intent(context, GetRecieveMessageService.class));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startService(new Intent(context, GetSentMessageService.class));

            }
        }, 1000);

    }
}

