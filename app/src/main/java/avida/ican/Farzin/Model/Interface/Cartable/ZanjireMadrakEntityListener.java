package avida.ican.Farzin.Model.Interface.Cartable;


import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireEntityRES;


/**
 * Created by AtrasVida on 2019-08-03 at 11:22 AM
 */

public interface ZanjireMadrakEntityListener {
    void onSuccess(StructureZanjireEntityRES structureZanjireEntityRES);

    void onFailed(String message);

    void onCancel();

}
