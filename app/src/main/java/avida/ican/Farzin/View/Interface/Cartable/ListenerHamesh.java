package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;

/**
 * Created by AtrasVida on 2018-09-29 at 12:45 PM
 */

public interface ListenerHamesh {
    void newData(StructureHameshDB structureHameshDB);
    void noData();
}
