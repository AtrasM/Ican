package avida.ican.Farzin.Presenter.Queue;

import android.content.Intent;

import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.ContinueWorkFlowListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureDocumentOperatorsQueueDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureOpticalPenREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureResponseREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureSignDocumentREQ;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureWorkFlowREQ;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendPresenter;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentConfirmPresenter;
import avida.ican.Farzin.Presenter.Cartable.ContinueWorkFlowPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Cartable.OpticalPenPresenter;
import avida.ican.Farzin.Presenter.Cartable.SignDocumentPresenter;
import avida.ican.Farzin.View.Dialog.DialogShowMore;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;

import static avida.ican.Farzin.View.Enum.CartableDocumentDetailActionsEnum.TAEED;

/**
 * Created by AtrasVida on 2019-05-06 at 2:19 PM
 */
public class FarzinDocumentOperatorsQueuePresenter {

    public FarzinDocumentOperatorsQueuePresenter() {

    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorsQueueGroup() {
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueuesDB = new FarzinCartableQuery().getDocumentOperatorsQueueGroup();
        return structureDocumentOperatorsQueuesDB;
    }

    public long getDocumentOperatorQueueNotSendedCount(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        return new FarzinCartableQuery().getDocumentOperatorQueueNotSendedCount(ETC, EC, documentOpratoresTypeEnum);
    }

    public long getDocumentOperatorQueueNotSendedCount(int ETC, int EC) {
        return new FarzinCartableQuery().getDocumentOperatorQueueNotSendedCount(ETC, EC);
    }

    public long getDocumentOperatorQueueNotSendedCount() {
        return new FarzinCartableQuery().getDocumentOperatorQueueNotSendedCount();
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorQueue(int ETC, int EC) {
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueuesDB = new FarzinCartableQuery().getDocumentOperatorQueue(ETC, EC);
        return structureDocumentOperatorsQueuesDB;
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorQueue(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        List<StructureDocumentOperatorsQueueDB> structureDocumentOperatorsQueuesDB = new FarzinCartableQuery().getDocumentOperatorQueue(ETC, EC);
        return structureDocumentOperatorsQueuesDB;
    }


    public boolean deletDocumentOperatorsQueue(int ETC, int EC) {
        return new FarzinCartableQuery().deletDocumentOperatorsQueue(ETC, EC);
    }

    public boolean deletDocumentOperatorsQueue(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        return new FarzinCartableQuery().deletDocumentOperatorsQueue(ETC, EC, documentOpratoresTypeEnum);
    }

    public boolean isDocumentOperatorExist(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum) {
        return new FarzinCartableQuery().isDocumentOperatorNotSendedExist(ETC, EC, documentOpratoresTypeEnum);
    }

    public boolean isDocumentOperatorError(int ETC, int EC) {
        return new FarzinCartableQuery().isDocumentOperatorHasError(ETC, EC);
    }

    public List<StructureDocumentOperatorsQueueDB> getDocumentOperatorErrorMessage(int ETC, int EC) {
        return new FarzinCartableQuery().getDocumentOperatorErrorMessage(ETC, EC);
    }

    public void sendDataToServer(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {

        switch (documentOperatorsQueueDB.getDocumentOpratoresTypeEnum()) {
            case AddHameshOpticalPen: {
                sendHameshOpticalPen(documentOperatorsQueueDB, documentOperatorQueuesListener);
                break;
            }
            case SignDocument: {
                sendSignDocument(documentOperatorsQueueDB, documentOperatorQueuesListener);
                break;
            }
            case Append: {
                sendAppend(documentOperatorsQueueDB, documentOperatorQueuesListener);
                break;
            }
            case Response: {
                sendResponse(documentOperatorsQueueDB, documentOperatorQueuesListener);
                break;
            }
            case WorkFlow: {
                continueWorkFlow(documentOperatorsQueueDB, documentOperatorQueuesListener);
                break;
            }
        }

    }

    private void sendHameshOpticalPen(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {
        StructureOpticalPenREQ structureOpticalPenREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDB.getStrDataREQ(), StructureOpticalPenREQ.class);
        new OpticalPenPresenter().CallRequest(documentOperatorsQueueDB.getETC(), documentOperatorsQueueDB.getEC(), structureOpticalPenREQ, new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                documentOperatorQueuesListener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                documentOperatorQueuesListener.onFailed(message);
            }

            @Override
            public void onCancel() {
                documentOperatorQueuesListener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void sendSignDocument(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {
        StructureSignDocumentREQ structureSignDocumentREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDB.getStrDataREQ(), StructureSignDocumentREQ.class);
        new SignDocumentPresenter().SignDocument(documentOperatorsQueueDB.getETC(), documentOperatorsQueueDB.getEC(), structureSignDocumentREQ.getENs(), new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                documentOperatorQueuesListener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                documentOperatorQueuesListener.onFailed(message);

            }

            @Override
            public void onCancel() {
                documentOperatorQueuesListener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });
    }


    private void sendAppend(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {
        StructureAppendREQ structureAppendREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDB.getStrDataREQ(), StructureAppendREQ.class);
        new CartableDocumentAppendPresenter().AppendDocument(documentOperatorsQueueDB.getETC(), documentOperatorsQueueDB.getEC(), structureAppendREQ, new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                documentOperatorQueuesListener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                documentOperatorQueuesListener.onFailed(message);

            }

            @Override
            public void onCancel() {
                documentOperatorQueuesListener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void sendResponse(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {
        StructureResponseREQ structureResponseREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDB.getStrDataREQ(), StructureResponseREQ.class);
        new CartableDocumentConfirmPresenter().ConfirmDocument(structureResponseREQ.getReceiverCode(), new DocumentOperatorQueuesListener() {
            @Override
            public void onSuccess() {
                documentOperatorQueuesListener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                documentOperatorQueuesListener.onFailed(message);

            }

            @Override
            public void onCancel() {
                documentOperatorQueuesListener.onCancel();
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void continueWorkFlow(StructureDocumentOperatorsQueueDB documentOperatorsQueueDB, DocumentOperatorQueuesListener documentOperatorQueuesListener) {
        StructureWorkFlowREQ structureWorkFlowREQ = new CustomFunction().ConvertStringToObject(documentOperatorsQueueDB.getStrDataREQ(), StructureWorkFlowREQ.class);

        new ContinueWorkFlowPresenter().ContinueWorkFlow(structureWorkFlowREQ.getReceiverCode(), structureWorkFlowREQ.getResponse(), new ContinueWorkFlowListener() {
            @Override
            public void onSuccess() {
                documentOperatorQueuesListener.onSuccess();
            }

            @Override
            public void onFailed(String message) {
                documentOperatorQueuesListener.onFailed(message);
            }
        });

    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}


