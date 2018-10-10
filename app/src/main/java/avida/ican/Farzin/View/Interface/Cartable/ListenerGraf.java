package avida.ican.Farzin.View.Interface.Cartable;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;

/**
 * Created by AtrasVida on 2018-10-06 at 4:08 PM
 */

public interface ListenerGraf {
    void newData(ArrayList<StructureHistoryListRES> structureHistoryListRES);
    void noData();
}
