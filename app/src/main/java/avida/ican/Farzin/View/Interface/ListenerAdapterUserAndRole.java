package avida.ican.Farzin.View.Interface;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;

/**
 * Created by AtrasVida on 2018-05-15 at 3:56 PM
 */

public interface ListenerAdapterUserAndRole {
    void onSelect(StructureUserAndRoleDB structureUserAndRoleDB);

    void unSelect(StructureUserAndRoleDB structureUserAndRoleDB);
    void showLoading();
    void hideLoading();
}
