package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureOpticalPenQueueDB;
import avida.ican.Ican.Model.Structure.StructureAttach;

/**
 * Created by AtrasVida on 2018-09-26 at 1:22 PM
 */

public interface ListenerAdapterQueueOpticalPen {
    void onOpenFile(StructureAttach structureAttach);
    void onDelet(StructureOpticalPenQueueDB structureOpticalPenQueueDB);
}
