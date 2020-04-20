package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.content.Context;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Interface.DocumentProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentOperatorsQueuePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-07-10 at 2:47 PM
 */

public class DocumentOpreratorsQueueJobService extends JobService {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 15;
    private Context context;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private int tryCount = 0;
    private final static int MaxTry = 3;
    private static DocumentProcessListener subGroupProcesslistener;
    private static DocumentProcessListener groupProcesslistener;
    private JobParameters job;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;

    public DocumentOpreratorsQueueJobService() {
    }

    public DocumentOpreratorsQueueJobService getInstance(Context context, JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
        this.context = context;
        this.jobServiceCartableSchedulerListener = jobServiceCartableSchedulerListener;
        return this;
    }

    @Override
    public boolean onStartJob(JobParameters job) {
        context = this;
        App.setServiceContext(context);
        this.job = job;
        try {
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

    private void doGroupProcess(ArrayList<StructureDocumentOperatorsQueueDB> documentOperatorsGroupQueuesDB) {
        if (documentOperatorsGroupQueuesDB == null || documentOperatorsGroupQueuesDB.size() <= 0) {
            finishJob();
        } else {
            groupProcesslistener = new DocumentProcessListener() {
                @Override
                public void onSuccess() {
                    documentOperatorsGroupQueuesDB.remove(0);
                    if (documentOperatorsGroupQueuesDB.size() > 0) {
                        getSubgroups(documentOperatorsGroupQueuesDB.get(0), groupProcesslistener);
                    } else {
                        onFinish();
                    }
                }

                @Override
                public void onFailed(String error) {
                    if (documentOperatorsGroupQueuesDB.size() > 0) {
                        documentOperatorsGroupQueuesDB.remove(0);
                        if (documentOperatorsGroupQueuesDB.size() > 0) {
                            getSubgroups(documentOperatorsGroupQueuesDB.get(0), groupProcesslistener);
                        } else {
                            onFinish();

                        }
                    } else {
                        onFinish();
                    }
                }

                @Override
                public void onCancel() {
                    finishJob();
                }

                @Override
                public void onFinish() {
                    finishJob();
                }
            };
            getSubgroups(documentOperatorsGroupQueuesDB.get(0), groupProcesslistener);
        }
    }

    private void getSubgroups(StructureDocumentOperatorsQueueDB documentOperatorsGroupQueueDB, DocumentProcessListener GroupProcesslistener) {
        tryCount = 0;
        int ETC;
        int EC;
        List<StructureDocumentOperatorsQueueDB> documentOperatorsSubGroupQueuesDB = new FarzinCartableQuery().getDocumentOperatorQueue(documentOperatorsGroupQueueDB.getETC(), documentOperatorsGroupQueueDB.getEC());
        if (documentOperatorsSubGroupQueuesDB == null || documentOperatorsSubGroupQueuesDB.size() <= 0) {
            finishJob();
        } else {
            ETC = documentOperatorsSubGroupQueuesDB.get(0).getETC();
            EC = documentOperatorsSubGroupQueuesDB.get(0).getEC();
            subGroupProcesslistener = new DocumentProcessListener() {
                @Override
                public void onSuccess() {

                    farzinCartableQuery.updateDocumentOperatorQueueStatus(documentOperatorsSubGroupQueuesDB.get(0).getId(), QueueStatus.SENDED);
                    documentOperatorsSubGroupQueuesDB.remove(0);
                    if (documentOperatorsSubGroupQueuesDB.size() > 0) {
                        if (documentOperatorsSubGroupQueuesDB.get(0).getQueueStatus() == QueueStatus.ERROR) {
                            GroupProcesslistener.onFailed("");
                        } else {
                            tryCount = 0;
                            Send(documentOperatorsSubGroupQueuesDB.get(0), subGroupProcesslistener);
                        }
                    } else {
                        onFinish();

                    }
                }

                @Override
                public void onFailed(String error) {

                    if (error.contains(Resorse.getString(R.string.error_permision))) {
                        tryCount = MaxTry;
                    }
                    if (tryCount >= MaxTry) {
                        tryCount = 0;
                        farzinCartableQuery.setErrorToDocumentOperatorQueue(documentOperatorsSubGroupQueuesDB.get(0).getId(), error);
                        GroupProcesslistener.onFailed("");
                    } else {
                        try {
                            if (documentOperatorsSubGroupQueuesDB.size() > 0) {
                                App.getHandlerMainThread().postDelayed(() -> tryAgain(documentOperatorsSubGroupQueuesDB.get(0), subGroupProcesslistener), FAILED_DELAY);
                            } else {
                                GroupProcesslistener.onFailed("");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            GroupProcesslistener.onFailed("");
                        }
                    }
                }

                @Override
                public void onCancel() {
                    farzinCartableQuery.updateDocumentOperatorQueueStatus(documentOperatorsSubGroupQueuesDB.get(0).getId(), QueueStatus.WAITING);
                    GroupProcesslistener.onCancel();
                }

                @Override
                public void onFinish() {
                    if (!farzinCartableQuery.isDocumentOperatorNotSendedExist(ETC, EC)) {
                        farzinCartableQuery.deletDocumentOperatorsQueue(ETC, EC);
                    }

                    GroupProcesslistener.onSuccess();
                }
            };
            if (documentOperatorsSubGroupQueuesDB.get(0).getQueueStatus() == QueueStatus.ERROR) {
                GroupProcesslistener.onFailed("");
            } else {
                Send(documentOperatorsSubGroupQueuesDB.get(0), subGroupProcesslistener);
            }
        }
    }

    private void Send(final StructureDocumentOperatorsQueueDB documentOperatorQueueDB, DocumentProcessListener SubGroupProcesslistener) {
        tryCount++;
        new FarzinDocumentOperatorsQueuePresenter().sendDataToServer(documentOperatorQueueDB, new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                if (documentOperatorQueueDB.getDocumentOpratoresTypeEnum() == DocumentOperatoresTypeEnum.SignDocument) {
                    farzinCartableQuery.deletCartableDocumentContent(documentOperatorQueueDB.getETC(), documentOperatorQueueDB.getEC());
                }
                SubGroupProcesslistener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                SubGroupProcesslistener.onFailed(message);

            }

            @Override
            public void onCancel() {
                SubGroupProcesslistener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });

    }

    private void tryAgain(final StructureDocumentOperatorsQueueDB documentOperatorQueueDB, DocumentProcessListener SubGroupProcesslistener) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                Send(documentOperatorQueueDB, SubGroupProcesslistener);
            }

            @Override
            public void disConnected() {
                tryCount++;
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    App.getHandlerMainThread().postDelayed(() -> tryAgain(documentOperatorQueueDB, SubGroupProcesslistener), FAILED_DELAY);
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
                List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueuesDB = new FarzinCartableQuery().getDocumentOperatorsQueueGroup();
                if (structureDocumentOperatorsQueuesDB != null && structureDocumentOperatorsQueuesDB.size() > 0) {
                    doGroupProcess(new ArrayList<>(structureDocumentOperatorsQueuesDB));
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
            jobServiceCartableSchedulerListener.onSuccess(CartableJobServiceNameEnum.DocumentOprator);
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