package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;

import avida.ican.Farzin.Presenter.Service.Cartable.CartableDocumentTaeedQueueService;
import avida.ican.Farzin.Presenter.Service.Cartable.GetCartableDocumentService;
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
    ArrayList<Intent> intents = new ArrayList<>();

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent mintent) {
        App.setServiceContext(context);
        intents.clear();
        context.startService(new Intent(context, NetWorkCheckingService.class));
        context.startService(putIntent(new Intent(context, GetCartableDocumentService.class)));
        context.startService(putIntent(new Intent(context, SendMessageService.class)));
        context.startService(putIntent(new Intent(context, GetRecieveMessageService.class)));
        context.startService(putIntent(new Intent(context, CartableDocumentTaeedQueueService.class)));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startService(putIntent(new Intent(context, GetSentMessageService.class)));
            }
        }, 1000);
    }

    private Intent putIntent(Intent intent) {
        intents.add(intent);
        return intent;
    }

    public void stopServices() {
        for (Intent intent : intents) {
            App.getServiceContext().stopService(intent);
        }
    }
}

