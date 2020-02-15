package avida.ican.Farzin.Presenter.Service.Cartable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;
import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Interface.DocumentProcessListener;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerNetwork;

/**
 * Created by AtrasVida on 2019-07-23 at 2:47 PM
 */

public class DocumentAttachFileQueueJobService extends JobService {
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 15;
    private Context context;
    private FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
    private int tryCount = 0;
    private final static int MaxTry = 3;
    private static DocumentProcessListener groupProcesslistener;
    private static DocumentProcessListener subGroupProcesslistener;
    private JobParameters job;
    private static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;


    public DocumentAttachFileQueueJobService() {
    }

    public  DocumentAttachFileQueueJobService getInstance(Context context,JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener) {
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

    private void doGroupProcess(ArrayList<StructureDocumentAttachFileQueueDB> documentAttachFileGroupQueuesDB) {
        if (documentAttachFileGroupQueuesDB == null || documentAttachFileGroupQueuesDB.size() <= 0) {
            finishJob();
        } else {
            groupProcesslistener = new DocumentProcessListener() {
                @Override
                public void onSuccess() {
                    farzinCartableQuery.setDocumentOperatorQueueLock(documentAttachFileGroupQueuesDB.get(0).getETC(), documentAttachFileGroupQueuesDB.get(0).getEC(), false);
                    documentAttachFileGroupQueuesDB.remove(0);
                    if (documentAttachFileGroupQueuesDB.size() > 0) {
                        getSubgroups(documentAttachFileGroupQueuesDB.get(0), groupProcesslistener);
                    } else {
                        onFinish();

                    }
                }

                @Override
                public void onFailed(String error) {
                    if (documentAttachFileGroupQueuesDB.size() > 0) {
                        documentAttachFileGroupQueuesDB.remove(0);
                        if (documentAttachFileGroupQueuesDB.size() > 0) {
                            getSubgroups(documentAttachFileGroupQueuesDB.get(0), groupProcesslistener);
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
            getSubgroups(documentAttachFileGroupQueuesDB.get(0), groupProcesslistener);
        }
    }

    private void getSubgroups(StructureDocumentAttachFileQueueDB documentAttachFileGroupQueueDB, DocumentProcessListener GroupProcesslistener) {
        tryCount = 0;
        int ETC;
        int EC;
        List<StructureDocumentAttachFileQueueDB> documentAttachFileSubGroupQueuesDB = new FarzinCartableQuery().getDocumentAttachFileNotSendedQueue(documentAttachFileGroupQueueDB.getETC(), documentAttachFileGroupQueueDB.getEC());
        if (documentAttachFileSubGroupQueuesDB == null || documentAttachFileSubGroupQueuesDB.size() <= 0) {
            finishJob();
        } else {
            ETC = documentAttachFileSubGroupQueuesDB.get(0).getETC();
            EC = documentAttachFileSubGroupQueuesDB.get(0).getEC();
            subGroupProcesslistener = new DocumentProcessListener() {
                @Override
                public void onSuccess() {

                    farzinCartableQuery.updateDocumentAttachFileQueueStatus(documentAttachFileSubGroupQueuesDB.get(0).getId(), QueueStatus.SENDED);
                    documentAttachFileSubGroupQueuesDB.remove(0);
                    if (documentAttachFileSubGroupQueuesDB.size() > 0) {
                        if (documentAttachFileSubGroupQueuesDB.get(0).getQueueStatus() == QueueStatus.ERROR) {
                            documentAttachFileSubGroupQueuesDB.remove(0);
                        }
                        if (documentAttachFileSubGroupQueuesDB.size() > 0) {
                            tryCount = 0;
                            Send(documentAttachFileSubGroupQueuesDB.get(0), subGroupProcesslistener);
                        } else {
                            onFinish();
                        }

                    } else {
                        onFinish();

                    }
                }

                @Override
                public void onFailed(String error) {

                    if (tryCount >= MaxTry) {
                        tryCount = 0;
                        farzinCartableQuery.setErrorToDocumentAttachFileQueue(documentAttachFileSubGroupQueuesDB.get(0).getId(), error);
                        GroupProcesslistener.onFailed("");
                    } else {
                        try {
                            if (documentAttachFileSubGroupQueuesDB.size() > 0) {
                                App.getHandlerMainThread().postDelayed(() -> tryAgain(documentAttachFileSubGroupQueuesDB.get(0), subGroupProcesslistener), FAILED_DELAY);
                            }else{
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
                    farzinCartableQuery.updateDocumentAttachFileQueueStatus(documentAttachFileSubGroupQueuesDB.get(0).getId(), QueueStatus.WAITING);
                    GroupProcesslistener.onCancel();
                }

                @Override
                public void onFinish() {
                    if (!farzinCartableQuery.isDocumentAttachFileNotSendedExist(ETC, EC)) {
                        farzinCartableQuery.deletDocumentAttachFileQueue(ETC, EC);
                    }

                    GroupProcesslistener.onSuccess();
                }
            };
            if (documentAttachFileSubGroupQueuesDB.get(0).getQueueStatus() == QueueStatus.ERROR) {
                GroupProcesslistener.onFailed("");
            } else {
                Send(documentAttachFileSubGroupQueuesDB.get(0), subGroupProcesslistener);
            }
        }
    }

    private void Send(final StructureDocumentAttachFileQueueDB documentAttachFileQueueDB, DocumentProcessListener SubGroupProcesslistener) {
        tryCount++;
        StructureDocumentAttachFileBND structureDocumentAttachFileBND = new StructureDocumentAttachFileBND(documentAttachFileQueueDB.getETC(), documentAttachFileQueueDB.getEC(), documentAttachFileQueueDB.isLock(), documentAttachFileQueueDB.getFile_name(), new StringBuilder(), documentAttachFileQueueDB.getFile_extension(), documentAttachFileQueueDB.getDependencyType(), documentAttachFileQueueDB.getDescription());
        structureDocumentAttachFileBND.setFile_path(documentAttachFileQueueDB.getFile_path());
        new FarzinDocumentAttachFilePresenter().sendDataToServer(structureDocumentAttachFileBND, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {
                SubGroupProcesslistener.onSuccess();
            }

            @Override
            public void onSuccessAddToQueue() {

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

    private void tryAgain(final StructureDocumentAttachFileQueueDB documentAttachFileQueueDB, DocumentProcessListener SubGroupProcesslistener) {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                Send(documentAttachFileQueueDB, SubGroupProcesslistener);
            }

            @Override
            public void disConnected() {
                tryCount++;
                if (tryCount >= MaxTry) {
                    tryCount = 0;
                    App.getHandlerMainThread().postDelayed(() -> finishJob(), FAILED_DELAY);
                } else {
                    App.getHandlerMainThread().postDelayed(() -> tryAgain(documentAttachFileQueueDB, SubGroupProcesslistener), FAILED_DELAY);
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
                List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueuesDB = new FarzinCartableQuery().getDocumentAttachFileQueueGroup();
                if (structureDocumentAttachFileQueuesDB != null && structureDocumentAttachFileQueuesDB.size() > 0) {
                    doGroupProcess(new ArrayList<>(structureDocumentAttachFileQueuesDB));
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
            jobServiceCartableSchedulerListener.onSuccess(CartableJobServiceNameEnum.AttachFile);
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