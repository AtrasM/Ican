package avida.ican.Farzin;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

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
    public void runServices() {
        runGetCartableDocumentService();
        runGetRecieveMessageService();
        runGetSentMessageService();
        runSendMessageService();
        runCartableDocumentAppendQueueService();
        runCartableDocumentTaeedQueueService();
        runOpticalPenQueueService();
    }

    public void runGetCartableDocumentService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, GetCartableDocumentService.class), true));
    }

    public void runGetRecieveMessageService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, GetRecieveMessageService.class), true));
    }

    public void runGetSentMessageService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, GetSentMessageService.class), true));
    }

    public void runSendMessageService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, SendMessageService.class)));
    }


    public void runCartableDocumentAppendQueueService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, CartableDocumentAppendQueueService.class)));
    }

    public void runCartableDocumentTaeedQueueService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, CartableDocumentTaeedQueueService.class)));
    }

    public void runOpticalPenQueueService() {
        Context context = App.getServiceContext();
        context.startService(putIntent(new Intent(context, OpticalPenQueueService.class)));
    }

    public int getServiceSyncCount() {
        return SERVICECOUNT;
    }
}

