package avida.ican.Farzin.Model.Interface;


import avida.ican.Farzin.Model.Structure.Response.StructureLoginDataRES;

/**
 * Created by AtrasVida on 2019-07-03 at 10:54 AM
 */

public interface UserRoleListener {
    void onSuccess(StructureLoginDataRES structureLoginDataRES);
    void onFailed(String error);
}
