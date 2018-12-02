package avida.ican.Farzin.View.Interface;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;

/**
 * Created by AtrasVida on 2018-05-15 at 12:05 PM
 */

public interface ListenerUserAndRoll {
    void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect);
    void onSuccess(StructureAppendREQ structureAppendREQ);

    void onFailed();

    void onCancel(List<StructureUserAndRoleDB> tmpItemSelect);

}
