package avida.ican.Farzin.Presenter;

import java.util.Timer;
import java.util.TimerTask;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.View.Dialog.DialogFirstMetaDataSync;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.DifferenceBetweenTwoDates;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.NoNetworkAccess;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;


/**
 * Created by AtrasVida on 2018-04-21 at 10:02 AM
 */

public class FarzinMetaDataSync {
    private static long secondsInMilli = 1000;
    private static long minutesInMilli = secondsInMilli * 60;
    private static long hoursInMilli = minutesInMilli * 60;
    private static long daysInMilli = hoursInMilli * 24;

    private static final long PERIOD = (hoursInMilli * 24);
    private static final long DELAY = secondsInMilli;
    private FarzinPrefrences farzinPrefrences;
    private String Tag = "FarzinMetaDataSync";
    private Timer timerAsync;
    private TimerTask timerTaskAsync;
    private DialogFirstMetaDataSync dialogFirstMetaDataSync;

    public FarzinMetaDataSync() {
        farzinPrefrences = getFarzinPrefrences();
        //farzinPrefrences.putMetaDataSyncDate("");
        dialogFirstMetaDataSync=new DialogFirstMetaDataSync(App.CurentActivity);
    }

    public FarzinMetaDataSync RunONschedule() {
        timerAsync = new Timer();
        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                try {
                    String strSimpleDateFormat = Resorse.getString(R.string.strSimpleDateFormat);
                    String CurentDateTime = new CustomFunction(App.CurentActivity).getCurentDateTimeAsFormat(strSimpleDateFormat);
                    String MetaDataLastSyncDate = farzinPrefrences.getMetaDataLastSyncDate();
                    if (MetaDataLastSyncDate.isEmpty()) {
                        if (App.networkStatus == NetworkStatus.WatingForNetwork) {
                            new NoNetworkAccess().ShowDialog();
                            onDestory();
                        } else {
                            dialogFirstMetaDataSync.Creat();
                            new FarzinMetaDataQuery().Sync();
                        }

                    } else {
                        long difrence = new DifferenceBetweenTwoDates(strSimpleDateFormat, CurentDateTime, MetaDataLastSyncDate).getElapsedDays();
                        if (difrence > 11) {
                            new FarzinMetaDataQuery().Sync();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timerAsync.schedule(timerTaskAsync, 0, PERIOD);
        return this;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    public void onDestory() {
        timerAsync.cancel();
    }
}
