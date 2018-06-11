package avida.ican.Farzin.View.Interface;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;

/**
 * Created by AtrasVida on 2018-05-15 at 12:05 PM
 */

public interface ListenerUserAndRoll {
    void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect);

    void onFailed();

    void onCancel(List<StructureUserAndRoleDB> tmpItemSelect);

}
