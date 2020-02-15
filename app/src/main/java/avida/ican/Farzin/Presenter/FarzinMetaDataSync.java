package avida.ican.Farzin.Presenter;

import java.util.Timer;
import java.util.TimerTask;

import avida.ican.Farzin.Model.Interface.GetDateTimeListener;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.DifferenceBetweenTwoDates;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2018-04-21 at 10:02 AM
 */

public class FarzinMetaDataSync {

    private final long PERIOD = TimeValue.HoursInMilli() * 12;
    private final long LONGDELAY = TimeValue.MinutesInMilli();
    private FarzinPrefrences farzinPrefrences;
    private String Tag = "FarzinMetaDataSync";
    private Timer timerAsync;
    private TimerTask timerTaskAsync;
    private DialogDataSyncing dialogDataSyncing;

    public FarzinMetaDataSync() {
        farzinPrefrences = getFarzinPrefrences();
        //farzinPrefrences.putMetaDataLastSyncDate("");
    }

    public FarzinMetaDataSync RunONschedule(final MetaDataSyncListener metaDataSyncListener) {
        timerAsync = new Timer();
        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                try {
                    final String strSimpleDateFormat = SimpleDateFormatEnum.DateTime_as_iso_8601.getValue();
                    new CustomFunction(App.CurentActivity);

                    final String MetaDataLastSyncDate = farzinPrefrences.getMetaDataLastSyncDate();
                    if (MetaDataLastSyncDate.isEmpty()) {
                        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                            @Override
                            public void onConnected() {
                                App.networkStatus = NetworkStatus.Connected;
                                new FarzinMetaDataQuery(App.CurentActivity).Sync(metaDataSyncListener);
                            }

                            @Override
                            public void disConnected() {
                                App.networkStatus = NetworkStatus.WatingForNetwork;
                                onDestory();
                                App.getHandlerMainThread().postDelayed(() -> RunONschedule(metaDataSyncListener), LONGDELAY);
                            }

                            @Override
                            public void onFailed() {
                                App.networkStatus = NetworkStatus.WatingForNetwork;
                            }
                        });

                    } else {
                        final String[] curentDateTime = {CustomFunction.getCurentLocalDateTimeAsStringFormat()};
                        CustomFunction.getCurentDateTimeAsStringFormat(new GetDateTimeListener() {
                            @Override
                            public void onSuccess(String strDateTime) {
                                curentDateTime[0] = strDateTime;
                            }

                            @Override
                            public void onFailed(String message) {
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                            @Override
                            public void onConnected() {
                                App.networkStatus = NetworkStatus.Connected;
                                long difrence = new DifferenceBetweenTwoDates(strSimpleDateFormat, MetaDataLastSyncDate, curentDateTime[0]).getElapsedDays();
                                if (difrence > 11) {
                                    new FarzinMetaDataQuery(App.CurentActivity).Sync(metaDataSyncListener);
                                } else {
                                    App.getHandlerMainThread().post(() -> {
                                        String userName = farzinPrefrences.getUserName();
                                        int roleID = farzinPrefrences.getRoleID();
                                        try {
                                            StructureUserAndRoleDB UserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(userName);
                                            farzinPrefrences.putUserBaseInfo(UserAndRoleDB.getUser_ID(), roleID);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        metaDataSyncListener.onFinish();
                                    });
                                }
                            }

                            @Override
                            public void disConnected() {
                                App.getHandlerMainThread().post(() -> {
                                    App.networkStatus = NetworkStatus.WatingForNetwork;
                                    metaDataSyncListener.onFinish();
                                });
                            }

                            @Override
                            public void onFailed() {
                                App.networkStatus = NetworkStatus.WatingForNetwork;
                            }
                        });
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
        timerTaskAsync.cancel();
    }
}
