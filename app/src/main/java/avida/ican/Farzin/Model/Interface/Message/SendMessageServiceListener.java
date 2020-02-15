package avida.ican.Farzin.Model.Interface.Message;


import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Queue.StructureMessageQueueDB;

/**
 * Created by AtrasVida on 2018-006-09 at 3:03 PM
 */

public interface SendMessageServiceListener {
    void onSuccess(List<StructureMessageQueueDB> structureMessageQueueDBS);

    void onFailed(String message,List<StructureMessageQueueDB> structureMessageQueueDBS);

    void onCancel(List<StructureMessageQueueDB> structureMessageQueueDBS);
    void onFinish();

}
