package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Enum.JobServiceIDEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.GetCartableDocumentFromServerPresenter;
import avida.ican.Farzin.Presenter.Notification.CartableDocumentNotificationPresenter;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-09-11 at 1:06 PM
 */

public class GetCartableDocumentJobService extends JobService {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;

    private CartableDocumentListListener cartableDocumentListListener;
    private Context context;
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private Status status = Status.IsNew;
    private int Count = 10;
    private final int MaxCount = 50;
    private final int MinCount = 20;
    private int notifyID = NotificationChanelEnum.Cartable.getValue();
    private Intent NotificationIntent;
    private static int existCont = 0;
    private static int dataSize = 0;
    private static int newCount = 0;
    private boolean canShowNotification = true;
    private CartableDocumentNotificationPresenter cartableDocumentNotificationPresenter;
    private JobParameters job;

    private int tempDelay = 50;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;

    public GetCartableDocumentJobService() {
    }

    public GetCartableDocumentJobService getInstance(Context context,JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
        this.context = context;
        this.jobServiceCartableSchedulerListener=jobServiceCartableSchedulerListener;
        return this;
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        context = this;
        App.setServiceContext(context);
        this.job = job;
        try {
            if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                Count = MinCount;
                callDataFinish();
            } else {
                Count = MaxCount;
            }
            new Thread(() -> {
                initPresenterAndListener();
                startJob(Count);
            }).start();
            Log.i("GetCartableDocument", "onStartService: " + JobServiceIDEnum.GET_CARTABLE_DOCUMENT_SERVICE_JOBID.getStringValue());

        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleException(e);
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }

        return false;
    }

    @SuppressLint("LongLogTag")
    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }

    private void initPresenterAndListener() {
        cartableDocumentNotificationPresenter = new CartableDocumentNotificationPresenter(context);
        getCartableDocumentFromServerPresenter = new GetCartableDocumentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentListListener = new CartableDocumentListListener() {
            @Override
            public void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {
                existCont = 0;
                dataSize = inboxCartableDocumentList.size();
                if (inboxCartableDocumentList.size() == 0) {
                    finishJob(MinCount);
                } else {
                    canShowNotification = true;
                    SaveData(inboxCartableDocumentList);
                }
            }

            @Override
            public void onFailed(String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("getDataCartableDocument error= " + message));

                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(Count), FAILED_DELAY);
                } else {
                    finishJob(Count);
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(Count), FAILED_DELAY);
                } else {
                    finishJob(Count);
                }
            }
        };
    }


    private void SaveData(final ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        final StructureInboxDocumentRES structureInboxDocumentRES = inboxCartableDocumentList.get(0);

        if (structureInboxDocumentRES.isRead()) {
            status = Status.READ;
        } else {
           /* if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
                status = Status.IsNew;
            } else {
                status = Status.UnRead;
            }*/
            status = Status.UnRead;
        }

        farzinCartableQuery.saveCartableDocument(structureInboxDocumentRES, Type.RECEIVED, status, new CartableDocumentQuerySaveListener() {

            @Override
            public void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB) {
                if (structureInboxDocumentDB == null || structureInboxDocumentDB.getId() <= 0) {
                    existCont++;
                } else {
                    if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync() && getFarzinPrefrences().isDataForFirstTimeSync()) {
                        farzinCartableQuery.updateCartableDocumentIsNewStatus(structureInboxDocumentDB.getId(), true);
                    }
                    if (!structureInboxDocumentDB.isRead()) {
                        newCount++;
                    }

                }

                continueeProcessGetData(inboxCartableDocumentList);
            }

            @Override
            public void onExisting() {
                //CallMulltiNotification();
                existCont++;
                continueeProcessGetData(inboxCartableDocumentList);
            }

            @Override
            public void onFailed(final String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("saveCartableDocument error= " + message));

                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(Count), FAILED_DELAY);
                } else {
                    finishJob(Count);
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(Count), FAILED_DELAY);
                } else {
                    finishJob(Count);
                }
            }
        });

    }


    private void continueeProcessGetData(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        try {
            inboxCartableDocumentList.remove(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (inboxCartableDocumentList.size() == 0) {
            Log.i("LOG", "CartableDocument: existCont= " + existCont + " dataSize= " + dataSize);
            if (existCont == dataSize) {
                finishJob(MinCount);
            } else {
                startJob(Count);
            }

        } else {
            SaveData(inboxCartableDocumentList);
        }


    }


    private void startJob(int count) {
        if (!canGetData()) {
            finishJob(Count);
        } else {
            Log.i("GetCartableDocument", "App.networkStatus: " + App.networkStatus);

            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                finishJob(Count);
            } else {
                if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                    getCartableDocumentFromServerPresenter.GetCartableDocumentList(count, cartableDocumentListListener);
                } else {
                    CompareDateTimeEnum compareDateTimeEnum = CustomFunction.compareDateWithCurentDate(getFarzinPrefrences().getCartableLastCheckDate(), (TimeValue.SecondsInMilli() * tempDelay));
                    if (compareDateTimeEnum == CompareDateTimeEnum.isAfter) {
                        getCartableDocumentFromServerPresenter.GetCartableDocumentList(count, cartableDocumentListListener);
                        Log.i("GetCartableDocument", "onStartService GetData: " + JobServiceIDEnum.GET_CARTABLE_DOCUMENT_SERVICE_JOBID.getStringValue() + " count= " + count);

                    } else {
                        finishJob(Count);
                    }
                }
            }
        }
    }


    private void callDataFinish() {
        getFarzinPrefrences().putCartableDocumentForFirstTimeSync(true);
        if (BaseActivity.dialogDataSyncing != null) {
            canShowNotification = false;
            BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncCartableDocument);
        }
    }

    private boolean canGetData() {
        boolean can = false;
        if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync() && !getFarzinPrefrences().isDataForFirstTimeSync()) {
            can = false;
        } else {
            can = true;
        }
        return can;
    }


    private void CallMulltiNotification() {
        if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
            newCount = 0;
            return;
        } /*else {
            if (App.networkStatus != null) {
                if (App.networkStatus == NetworkStatus.Syncing) {
                    return;
                }
            }
        }*/
        if (!canShowNotification) {
            newCount = 0;
            return;
        }

        if (newCount > 0) {
            try {
                if (App.fragmentCartable != null) {
                    App.fragmentCartable.reGetDataFromLocal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            long count = new FarzinCartableQuery().getNewCartableDocumentCount();
            if (count > 0) {
                String title = Resorse.getString(context, R.string.Cartable);
                String message = count + " " + Resorse.getString(context, R.string.NewCartableDocument);
                cartableDocumentNotificationPresenter.callNotification(title, "" + message, cartableDocumentNotificationPresenter.GetNotificationPendingIntent(cartableDocumentNotificationPresenter.GetNotificationManagerIntent()));

            }

        }
        newCount = 0;
    }

    private void finishJob(int count) {

        try {
            callDataFinish();
            CallMulltiNotification();
            Count = count;
            newCount = 0;
            existCont = 0;
            jobFinished(job, false);
            jobServiceCartableSchedulerListener.onSuccess(CartableJobServiceNameEnum.GetCartable);
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}
