package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.List;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.Cartable.CreateDocumentListener;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureCreateDocumentQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureCreatDocumentQueueDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocIndicatorRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.ImportDocServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2019-07-30 at 5:40 PM
 */

public class ImportDocumentJobService extends JobService {

    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 15;
    private Context context;
    private int tryCount = 0;
    private final static int MaxTry = 3;
    private FarzinCartableQuery farzinCartableQuery;
    private JobParameters job;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;

    public ImportDocumentJobService() {
    }

    public ImportDocumentJobService getInstance(Context context,JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
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
            farzinCartableQuery = new FarzinCartableQuery();
            new Thread(() -> {
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

    private void Send(final List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS) {
        tryCount++;
        StructureCreatDocumentQueueDB creatDocumentQueueDB = structureCreatDocumentQueueDBS.get(0);
        StructureCreateDocumentQueueBND createDocumentQueueBND = new StructureCreateDocumentQueueBND(creatDocumentQueueDB.getId(), creatDocumentQueueDB.getETC(), creatDocumentQueueDB.getEC(), creatDocumentQueueDB.getSubject(), creatDocumentQueueDB.getSenderUserName(), creatDocumentQueueDB.getSenderFullName(), creatDocumentQueueDB.getReceiverFullName(), creatDocumentQueueDB.getImportOriginDate());
        new ImportDocServerPresenter().ImportDoc(createDocumentQueueBND, new CreateDocumentListener() {
            @Override
            public void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES) {
                farzinCartableQuery.updateDocumentAttachFileQueueETC_EC(creatDocumentQueueDB.getETC(), creatDocumentQueueDB.getEC(), importDocIndicatorRES.getETC(), importDocIndicatorRES.getEntityCode(), false);
                farzinCartableQuery.updateDocumentOperatorQueueStatusETC_EC(creatDocumentQueueDB.getETC(), creatDocumentQueueDB.getEC(), importDocIndicatorRES.getETC(), importDocIndicatorRES.getEntityCode(), true);
                farzinCartableQuery.deletCreateDocumentQueue(createDocumentQueueBND.getId());
                structureCreatDocumentQueueDBS.remove(0);
                if (structureCreatDocumentQueueDBS.size() > 0) {
                    Send(structureCreatDocumentQueueDBS);
                } else {
                    finishJob();
                }
            }

            @Override
            public void onSuccessAddToQueue() {

            }

            @Override
            public void onFailed(String error) {
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    farzinCartableQuery.setErrorToCreateDocumentQueue(creatDocumentQueueDB.getId(), error);
                    finishJob();
                } else {
                    try {
                        if (structureCreatDocumentQueueDBS.size() > 0) {
                            App.getHandlerMainThread().postDelayed(() -> tryAgain(structureCreatDocumentQueueDBS), FAILED_DELAY);
                        } else {
                            farzinCartableQuery.setErrorToCreateDocumentQueue(creatDocumentQueueDB.getId(), error);
                            finishJob();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        farzinCartableQuery.setErrorToCreateDocumentQueue(creatDocumentQueueDB.getId(), error);
                        finishJob();
                    }
                }
            }

            @Override
            public void onCancel() {
                finishJob();
            }

            @Override
            public void onFinish() {
            }

        });
    }

    private void tryAgain(List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                Send(structureCreatDocumentQueueDBS);
            }

            @Override
            public void disConnected() {
                tryCount++;
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    App.getHandlerMainThread().postDelayed(() -> tryAgain(structureCreatDocumentQueueDBS), FAILED_DELAY);
                }
            }

            @Override

            public void onFailed() {
                finishJob();
            }
        });
    }


    private void startJob() {
        try {
            if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                finishJob();
            } else {
                List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS = new FarzinCartableQuery().getCreateDocumentQueueList(QueueStatus.WAITING);
                if (structureCreatDocumentQueueDBS.size() > 0) {
                    Send(structureCreatDocumentQueueDBS);
                } else {
                    finishJob();
                }
            }

        } catch (Exception e) {
            finishJob();
            e.printStackTrace();
        }
    }

    private void finishJob() {
        tryCount = 0;
        try {
            jobFinished(job, false);
            jobServiceCartableSchedulerListener.onSuccess(CartableJobServiceNameEnum.ImportDocument);
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