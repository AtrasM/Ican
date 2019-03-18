package avida.ican.Farzin.View.Interface.Cartable;


import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;

/**
 * Created by AtrasVida on 2018-12-05 at 10:32 PM
 */

public interface ListenerCartableDocumentContent {
    void newData(List<StructureCartableDocumentContentDB> structureCartableDocumentContentDBS);
    void noData();
}
