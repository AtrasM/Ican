package avida.ican.Farzin.Presenter;

import java.util.Timer;
import java.util.TimerTask;

import avida.ican.Farzin.Model.Interface.Message.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Dialog.DialogFirstMetaDataSync;
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
    private DialogFirstMetaDataSync dialogFirstMetaDataSync;

    public FarzinMetaDataSync() {
        farzinPrefrences = getFarzinPrefrences();
        //farzinPrefrences.putMetaDataSyncDate("");
    }

    public FarzinMetaDataSync RunONschedule(final MetaDataSyncListener metaDataSyncListener) {
        timerAsync = new Timer();
        timerTaskAsync = new TimerTask() {
            @Override
            public void run() {
                try {
                    final String strSimpleDateFormat = SimpleDateFormatEnum.DateTime_yyyy_MM_dd_hh_mm_ss.getValue();
                    new CustomFunction(App.CurentActivity);
                    final String CurentDateTime = CustomFunction.getCurentDateTimeAsStringFormat(strSimpleDateFormat);
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
                                App.getHandlerMainThread().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        RunONschedule(metaDataSyncListener);
                                    }
                                }, LONGDELAY);
                            }

                            @Override
                            public void onFailed() {
                                App.networkStatus = NetworkStatus.WatingForNetwork;
                            }
                        });

                    } else {
                        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
                            @Override
                            public void onConnected() {
                                App.networkStatus = NetworkStatus.Connected;
                                long difrence = new DifferenceBetweenTwoDates(strSimpleDateFormat, CurentDateTime, MetaDataLastSyncDate).getElapsedDays();
                                if (difrence > 11) {
                                    new FarzinMetaDataQuery(App.CurentActivity).Sync(metaDataSyncListener);
                                } else {
                                    App.getHandlerMainThread().post(new Runnable() {
                                        @Override
                                        public void run() {
                                            String userName = farzinPrefrences.getUserName();
                                            StructureUserAndRoleDB UserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(userName);
                                            farzinPrefrences.putUserBaseInfo(UserAndRoleDB.getUser_ID(), UserAndRoleDB.getRole_ID());
                                            metaDataSyncListener.onFinish();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void disConnected() {
                                App.getHandlerMainThread().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        App.networkStatus = NetworkStatus.WatingForNetwork;
                                        metaDataSyncListener.onFinish();
                                    }
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
    }
}
