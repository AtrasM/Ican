package avida.ican.Farzin.Presenter.Queue;

import java.util.List;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageQueueDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;

/**
 * Created by AtrasVida on 2019-05-06 at 2:19 PM
 */
public class FarzinQueuesPresenter {
    public FarzinQueuesPresenter() {

    }

    public List<StructureMessageQueueDB> getQueueMessageList() {
        List<StructureMessageQueueDB> structureMessagesQueueDB = new FarzinMessageQuery().getMessageQueue(getFarzinPrefrences().getUserID(), getFarzinPrefrences().getRoleID());
        return structureMessagesQueueDB;
    }

    public boolean deletMessageQueueRowWithId(int id) {
        return new FarzinMessageQuery().DeletMessageQueueRowWithId(id);
    }

    public List<StructureOpticalPenQueueDB> getQueueOpticalPenList() {
        List<StructureOpticalPenQueueDB> opticalPenListQueue = new FarzinCartableQuery().getOpticalPenListQueue();
        return opticalPenListQueue;
    }

    public boolean deletItemOpticalPenQueue(int id) {
        return new FarzinCartableQuery().deletItemOpticalPenQueue(id);
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}


