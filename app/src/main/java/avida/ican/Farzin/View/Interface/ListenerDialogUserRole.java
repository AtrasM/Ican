package avida.ican.Farzin.View.Interface;

import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;

/**
 * Created by AtrasVida on 2019-07-01 at 5:19 PM
 */

public interface ListenerDialogUserRole {
    void onSuccess(StructureUserRoleDB structureUserRoleDB);

    void onCancel();

}
