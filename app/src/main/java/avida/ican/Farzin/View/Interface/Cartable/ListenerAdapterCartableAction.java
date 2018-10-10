package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.StructureCartableAction;

/**
 * Created by AtrasVida on 2018-09-22 at 4:01 PM
 */

public interface ListenerAdapterCartableAction {
    void onPin(int position,StructureCartableAction structureCartableAction);
    void unPin(int position,StructureCartableAction structureCartableAction);
    void onLongClick(int position,StructureCartableAction structureCartableAction);
    void onClick(StructureCartableAction structureCartableAction);
}
