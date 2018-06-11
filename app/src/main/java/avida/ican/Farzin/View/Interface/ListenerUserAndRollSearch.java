package avida.ican.Farzin.View.Interface;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;

/**
 * Created by AtrasVida on 2018-05-15 at 12:05 PM
 */

public interface ListenerUserAndRollSearch {
    void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesSearch);

    void onFailed();

    void onCancel();

}
