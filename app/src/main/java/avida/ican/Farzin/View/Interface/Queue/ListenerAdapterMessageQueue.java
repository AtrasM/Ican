package avida.ican.Farzin.View.Interface.Queue;

import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Queue.StructureMessageQueueDB;

/**
 * Created by AtrasVida on 2019-05-07 at 10:38 PM
 */

public interface ListenerAdapterMessageQueue {
    void onDelet(StructureMessageQueueDB structureMessageQueueDB);
    void onTry(StructureMessageQueueDB structureMessageQueueDB);
    void onItemClick(StructureDetailMessageBND structureDetailMessageBND, int position);
}
