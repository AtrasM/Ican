package avida.ican.Farzin.Presenter.Queue;

import java.util.List;

import avida.ican.Farzin.Model.Enum.QueueStatus;
import avida.ican.Farzin.Model.Interface.Cartable.CreateDocumentListener;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureCreateDocumentQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureCreatDocumentQueueDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocIndicatorRES;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.ImportDocServerPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2019-07-30 at 11:03 AM
 */
public class FarzinCreateDocumentPresenter {

    private long FailedDelay = TimeValue.SecondsInMilli() * 3;

    public FarzinCreateDocumentPresenter() {

    }

    public List<StructureCreatDocumentQueueDB> getCreateDocumentQueueList(QueueStatus queueStatus) {
        List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS = new FarzinCartableQuery().getCreateDocumentQueueList(queueStatus);
        return structureCreatDocumentQueueDBS;
    }
    public List<StructureCreatDocumentQueueDB> getCreateDocumentQueueList() {
        List<StructureCreatDocumentQueueDB> structureCreatDocumentQueueDBS = new FarzinCartableQuery().getCreateDocumentQueueList();
        return structureCreatDocumentQueueDBS;
    }

    public boolean deletCreatDocumenQueue(int ETC, int EC) {
        return new FarzinCartableQuery().deletCreateDocumentQueue(ETC, EC);
    }



    public void sendData(StructureCreateDocumentQueueBND createDocumentQueueBND, CreateDocumentListener createDocumentListener) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            addToQueue(createDocumentQueueBND, createDocumentListener);
        } else {
            sendDataToServer(createDocumentQueueBND, createDocumentListener);
        }
    }

    public void sendDataToServer(StructureCreateDocumentQueueBND createDocumentQueueBND, CreateDocumentListener createDocumentListener) {

        new ImportDocServerPresenter().ImportDoc(createDocumentQueueBND, new CreateDocumentListener() {


            @Override
            public void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES) {
                createDocumentListener.onSuccess(importDocIndicatorRES);
            }

            @Override
            public void onSuccessAddToQueue() {

            }

            @Override
            public void onFailed(String message) {
                createDocumentListener.onFailed(message);
            }

            @Override
            public void onCancel() {
                createDocumentListener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });
    }


    public void addToQueue(StructureCreateDocumentQueueBND createDocumentQueueBND, CreateDocumentListener mainCreateDocumentListener) {

        new FarzinCartableQuery().saveCreatDocumentQueue(createDocumentQueueBND, new CreateDocumentListener() {

            @Override
            public void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES) {

            }

            @Override
            public void onSuccessAddToQueue() {
                mainCreateDocumentListener.onSuccessAddToQueue();
            }

            @Override
            public void onFailed(String message) {
                tryAddToQueue(createDocumentQueueBND, mainCreateDocumentListener);
            }

            @Override
            public void onCancel() {
                tryAddToQueue(createDocumentQueueBND, mainCreateDocumentListener);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void tryAddToQueue(StructureCreateDocumentQueueBND createDocumentQueueBND, CreateDocumentListener createDocumentListener) {
        App.getHandlerMainThread().postDelayed(() -> addToQueue(createDocumentQueueBND, createDocumentListener), FailedDelay);
    }


}


