package avida.ican.Farzin.View.Interface;

import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;

/**
 * Created by AtrasVida on 2018-05-15 at 3:56 PM
 */

public interface ListenerAdapterMessageList {
    void onDelet(StructureMessageDB structureMessageDB);
    void onItemClick(StructureDetailMessageBND structureDetailMessageBND);
}
