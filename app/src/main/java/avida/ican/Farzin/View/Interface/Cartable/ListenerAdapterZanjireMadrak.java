package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Ican.Model.Structure.StructureAttach;

/**
 * Created by AtrasVida on 2018-10-15 at 4:26 PM
 */

public interface ListenerAdapterZanjireMadrak {
    void onClickZanjireEntity(StructureZanjireMadrakEntityDB zanjireMadrakEntityDB);
    void onOpenFile(StructureAttach structureAttach);
    void FileNotExist();

}
