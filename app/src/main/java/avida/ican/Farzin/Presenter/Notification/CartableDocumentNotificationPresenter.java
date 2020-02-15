package avida.ican.Farzin.Presenter.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import org.acra.ACRA;

import avida.ican.Farzin.FarzinCartableNotificationDismissReceiver;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.FarzinNotificationManager;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-08-17 at 1:42 PM
 */
public class CartableDocumentNotificationPresenter {

    private int notifyID = NotificationChanelEnum.Cartable.getValue();
    private Intent NotificationIntent;
    private Context context;

    public CartableDocumentNotificationPresenter(Context context) {
        this.context = context;
    }

    public void callNotification(final String title, final String message, final PendingIntent pendingIntent) {
        try {
            String CHANNEL_ID = "Ican_FarzinCartable_Document_Notif_CHID";// The id of the channel.

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Notification notification =
                    new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setDeleteIntent(getDeleteIntent())
                            .setSmallIcon(R.drawable.ic_notification_ican)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setSound(soundUri) //This sets the sound to play
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setChannelId(CHANNEL_ID).build();

            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "IcanFarzinCartableDocumentNotification";// The user-visible name of the channel.
                int importance = NotificationManager.IMPORTANCE_HIGH;
                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                NotificationChannel mChannel = null;
                mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                // Sets whether notifications posted to this channel should display notification lights
                mChannel.enableLights(true);
                mChannel.setSound(soundUri,audioAttributes);
                // Sets whether notification posted to this channel should vibrate.
                mChannel.enableVibration(true);
                // Sets the notification light color for notifications posted to this channel
                mChannel.setLightColor(R.color.colorPrimaryDark);
                // Sets whether notifications posted to this channel appear on the lockscreen or not
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

                assert mNotificationManager != null;
                mNotificationManager.createNotificationChannel(mChannel);
            }

            // Issue the notification.
            mNotificationManager.notify(notifyID, notification);


        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }


    }

    public PendingIntent getDeleteIntent() {

        Intent intentCancell = new Intent(context, FarzinCartableNotificationDismissReceiver.class);
        intentCancell.putExtra(PutExtraEnum.ID.name(), notifyID);
        PendingIntent pendingIntentCancell = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intentCancell, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntentCancell;
    }

    public Intent GetNotificationManagerIntent() {
        NotificationIntent = new Intent(context, FarzinNotificationManager.class);
        NotificationIntent.putExtra(PutExtraEnum.Notification.getValue(), PutExtraEnum.MultyCartableDocument.getValue());
        NotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return NotificationIntent;
    }

    public PendingIntent GetNotificationPendingIntent(Intent intent) {
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        1,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        return pendingIntent;
    }

}


