package avida.ican.Farzin.Presenter.Queue;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;

/**
 * Created by AtrasVida on 2019-08-06 at 2:34 PM
 */
public class FarzinCartableDocumentPublicQueuePresenter {

    public FarzinCartableDocumentPublicQueuePresenter() {

    }


    public long getDocumentOperatorQueueNotSendedCount() {
        return new FarzinCartableQuery().getDocumentOperatorQueueNotSendedCount();
    }

    public long getDocumentAttachFileQueueNotSendedCount() {
        return new FarzinCartableQuery().getDocumentAttachFileQueueQueueNotSendedCount();
    }

    public long getCreatDocumentQueueCount() {
        return new FarzinCartableQuery().getCreatDocumentQueueCount();
    }


    public boolean userHasQueue() {
        long count = 0;

        count = getDocumentOperatorQueueNotSendedCount();
        count = count + getDocumentAttachFileQueueNotSendedCount();
        count = count + getCreatDocumentQueueCount();

        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }

}


