package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import java.util.ArrayList;

import avida.ican.Farzin.Presenter.Service.Cartable.CartableDocumentAppendQueueService;
import avida.ican.Farzin.Presenter.Service.Cartable.CartableDocumentTaeedQueueService;
import avida.ican.Farzin.Presenter.Service.Cartable.GetCartableDocumentService;
import avida.ican.Farzin.Presenter.Service.Cartable.OpticalPenQueueService;
import avida.ican.Farzin.Presenter.Service.CheckServerAviableService;
import avida.ican.Farzin.Presenter.Service.Message.GetRecieveMessageService;
import avida.ican.Farzin.Presenter.Service.Message.GetSentMessageService;
import avida.ican.Farzin.Presenter.Service.Message.SendMessageService;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-06-25 at 4:28 PM
 */

public class FarzinBroadcastReceiver extends BroadcastReceiver {
    Handler handler = new Handler();
    ArrayList<Intent> intents = new ArrayList<>();
    private int SERVICECOUNT = 0;

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, Intent mintent) {
        App.setServiceContext(context);
        intents.clear();
        //context.startService(new Intent(context, NetWorkCheckingService.class));
        context.startService(new Intent(context, CheckServerAviableService.class));
        context.startService(putIntent(new Intent(context, GetCartableDocumentService.class), true));
        context.startService(putIntent(new Intent(context, SendMessageService.class)));
        context.startService(putIntent(new Intent(context, CartableDocumentAppendQueueService.class)));
        context.startService(putIntent(new Intent(context, CartableDocumentTaeedQueueService.class)));
        context.startService(putIntent(new Intent(context, OpticalPenQueueService.class)));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startService(putIntent(new Intent(context, GetRecieveMessageService.class), true));
            }
        }, (TimeValue.SecondsInMilli()*3));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                context.startService(putIntent(new Intent(context, GetSentMessageService.class), true));
            }
        }, (TimeValue.SecondsInMilli()*5));
    }

    private Intent putIntent(Intent intent) {
        intents.add(intent);
        return intent;
    }

    private Intent putIntent(Intent intent, boolean ServiceCountPlus) {
        SERVICECOUNT++;
        intents.add(intent);
        return intent;
    }

    public void stopServices() {
        for (Intent intent : intents) {
            App.getServiceContext().stopService(intent);
        }
    }

    public int getServiceSyncCount() {
        return SERVICECOUNT;
    }
}

