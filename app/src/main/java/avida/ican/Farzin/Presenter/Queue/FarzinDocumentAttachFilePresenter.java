package avida.ican.Farzin.Presenter.Queue;

import java.io.File;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentAttachFileQueueDB;
import avida.ican.Farzin.Model.Structure.Request.StructureDocumentAttachFileREQ;
import avida.ican.Farzin.Presenter.Cartable.AttachFileToServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2019-07-24 at 12:39 PM
 */
public class FarzinDocumentAttachFilePresenter {

    private long FailedDelay = TimeValue.SecondsInMilli() * 3;

    public FarzinDocumentAttachFilePresenter() {

    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileQueueGroup() {
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueuesDB = new FarzinCartableQuery().getDocumentAttachFileQueueGroup();
        return structureDocumentAttachFileQueuesDB;
    }

    public long getDocumentAttachFileQueueQueueNotSendedCount(int ETC, int EC) {
        return new FarzinCartableQuery().getDocumentAttachFileQueueQueueNotSendedCount(ETC, EC);
    }

    public List<StructureDocumentAttachFileQueueDB> getDocumentAttachFileNotSendedQueue(int ETC, int EC) {
        List<StructureDocumentAttachFileQueueDB> structureDocumentAttachFileQueuesDB = new FarzinCartableQuery().getDocumentAttachFileNotSendedQueue(ETC, EC);
        return structureDocumentAttachFileQueuesDB;
    }

    public boolean deletDocumentAttachFileQueue(int ETC, int EC) {
        return new FarzinCartableQuery().deletDocumentAttachFileQueue(ETC, EC);
    }

    public boolean isDocumentAttachFileNotSendedExist(int ETC, int EC) {
        return new FarzinCartableQuery().isDocumentAttachFileNotSendedExist(ETC, EC);
    }

/*    public boolean isMoreThanOneDocumentAttachFile(int ETC, int EC) {
        return new FarzinCartableQuery().isMoreThanOneDocumentAttachFile(ETC, EC);
    }*/


    public boolean isDocumentAttachFileError(int ETC, int EC) {
        return new FarzinCartableQuery().isDocumentAttachFileHasError(ETC, EC);
    }

    public StructureDocumentAttachFileQueueDB getDocumentAttachFileErrorMessage(int ETC, int EC) {
        return new FarzinCartableQuery().getDocumentAttachFileErrorMessage(ETC, EC);
    }

    public void sendData(StructureDocumentAttachFileBND structureDocumentAttachFileBND, DocumentAttachFileListener documentAttachFileListener) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            documentAttachFileAddToQueue(structureDocumentAttachFileBND, documentAttachFileListener);
        } else {
            sendDataToServer(structureDocumentAttachFileBND, documentAttachFileListener);
        }
    }

    public void sendDataToServer(StructureDocumentAttachFileBND documentAttachFileBND, DocumentAttachFileListener documentAttachFileListener) {
        String fileAsBase64 = "";
        if (documentAttachFileBND.getFileAsStringBuilder() != null && documentAttachFileBND.getFileAsStringBuilder().length() > 0) {
            fileAsBase64 = documentAttachFileBND.getFileAsStringBuilder().toString();
        } else {
            fileAsBase64 = new CustomFunction().getFileFromStorageAsBase64(documentAttachFileBND.getFile_path());
        }


        StructureDocumentAttachFileREQ structureDocumentAttachFileREQ = new StructureDocumentAttachFileREQ(documentAttachFileBND.getETC(), documentAttachFileBND.getEC(), fileAsBase64, documentAttachFileBND.getFile_name(), documentAttachFileBND.getFile_extension(), documentAttachFileBND.getDependencyType().getIntValue(), documentAttachFileBND.getDescription());
        new AttachFileToServerPresenter().AttachFile(structureDocumentAttachFileREQ, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {
                onFinish();
            }

            @Override
            public void onSuccessAddToQueue() {

            }

            @Override
            public void onFailed(String message) {
                documentAttachFileListener.onFailed(message);
            }

            @Override
            public void onCancel() {
                documentAttachFileListener.onCancel();
            }

            @Override
            public void onFinish() {
                if (!isDocumentAttachFileNotSendedExist(documentAttachFileBND.getETC(), documentAttachFileBND.getEC())) {
                    deletDocumentAttachFileQueue(documentAttachFileBND.getETC(), documentAttachFileBND.getEC());
                }
                if (!documentAttachFileBND.getFile_path().isEmpty()) {
                    try {
                        File file = new File(documentAttachFileBND.getFile_path());
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                documentAttachFileListener.onSuccess();

            }
        });
    }



    public void documentAttachFileAddToQueue(StructureDocumentAttachFileBND structureDocumentAttachFileBND, DocumentAttachFileListener mainDocumentAttachFileListener) {

        new FarzinCartableQuery().saveDocumentAttachFileQueue(structureDocumentAttachFileBND, new DocumentAttachFileListener() {

            @Override
            public void onSuccess() {

            }

            @Override
            public void onSuccessAddToQueue() {
                mainDocumentAttachFileListener.onSuccessAddToQueue();
            }

            @Override
            public void onFailed(String message) {
                tryDocumentAttachFileAddToQueue(structureDocumentAttachFileBND, mainDocumentAttachFileListener);
            }

            @Override
            public void onCancel() {
                tryDocumentAttachFileAddToQueue(structureDocumentAttachFileBND, mainDocumentAttachFileListener);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void tryDocumentAttachFileAddToQueue(StructureDocumentAttachFileBND structureDocumentAttachFileBND, DocumentAttachFileListener documentAttachFileListener) {
        App.getHandlerMainThread().postDelayed(() -> documentAttachFileAddToQueue(structureDocumentAttachFileBND, documentAttachFileListener), FailedDelay);
    }


}


