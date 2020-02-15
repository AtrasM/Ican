package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Interface.Cartable.ConfirmationListListener;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureConfirmationItemRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.GetConfirmationListFromServerPresenter;
import avida.ican.Farzin.View.FarzinActivityCartableDocumentDetail;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2019-06-26 at 5:19 PM
 */

public class GetConfirmationListJobService extends JobService {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 20;
    private Context context;
    private GetConfirmationListFromServerPresenter getConfirmationListFromServerPresenter;
    private ConfirmationListListener confirmationListListener;
    private FarzinCartableQuery farzinCartableQuery;
    private boolean haveData = false;
    private AsyncTask<Void, Void, Void> task;
    private JobParameters job;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;


    public GetConfirmationListJobService() {
    }
    public  GetConfirmationListJobService getInstance(Context context,JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
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
            new Thread(() -> {
                initPresenterAndListener();
                startJob();
            }).start();
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
        getConfirmationListFromServerPresenter = new GetConfirmationListFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        confirmationListListener = new ConfirmationListListener() {
            @Override
            public void onSuccess(ArrayList<StructureConfirmationItemRES> confirmationItemRES) {
                if (confirmationItemRES.size() > 0) {
                    ConfirmDocument(confirmationItemRES);
                } else {
                    finishJob();
                }
            }

            @Override
            public void onFailed(String message) {
                ACRA.getErrorReporter().handleSilentException(new Exception("GetConfirmationList error= " + message));

                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    finishJob();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    finishJob();
                }
            }
        };
    }





    @SuppressLint("StaticFieldLeak")
    private void ConfirmDocument(ArrayList<StructureConfirmationItemRES> structureConfirmationItemRES) {
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Log.i("Log", "doInBackground ConfirmDocument.size= " + structureConfirmationItemRES.size());
                for (int i = 0; i < structureConfirmationItemRES.size(); i++) {
                    if (farzinCartableQuery.IsDocumentExist(structureConfirmationItemRES.get(i).getReceiverCode())) {
                        if (App.CurentActivity != null && App.activityStacks != null) {
                            try {
                                if (App.CurentActivity.getClass().getSimpleName().equals(FarzinActivityCartableDocumentDetail.class.getSimpleName())) {
                                    if (App.curentDocumentShowing > 0) {
                                        App.isDocumentConfirm = true;
                                        App.curentDocumentShowing = -1;
                                        App.isLoading = false;
                                        App.canBack = true;
                                        App.CurentActivity.finish();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ACRA.getErrorReporter().handleSilentException(e);

                            }

                        }
                        haveData = true;
                        farzinCartableQuery.deletCartableDocumentAllContent(structureConfirmationItemRES.get(i).getReceiverCode());
                        getFarzinPrefrences().putConfirmationListDataSyncDate(structureConfirmationItemRES.get(i).getResponseDate());
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.i("Log", "onPostExecute ConfirmDocument.size= " + structureConfirmationItemRES.size());
                if (haveData) {
                    try {
                        if (App.fragmentCartable != null) {
                            App.fragmentCartable.reGetDataFromLocal();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                finishJob();
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void startJob() {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            finishJob();
        } else {
            long documentCount = farzinCartableQuery.getCartableDocumentCount();
            if (documentCount > 0) {
                getConfirmationListFromServerPresenter.GetCartableDocumentList(confirmationListListener);
            } else {
                finishJob();
            }
        }
    }

    private void finishJob() {
        haveData = false;
        try {
            jobFinished(job, false);
            jobServiceCartableSchedulerListener.onSuccess(CartableJobServiceNameEnum.GetConfirmation);
        } catch (Exception e) {
            CustomLogger.setLog(e.toString());
            jobFinished(job, false);
            App.initBroadcastReceiver();
        }
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}
