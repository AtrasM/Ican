package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;

/**
 * Created by AtrasVida on 2018-09-26 at 2:42 PM
 */

public interface TaeedListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();

    void onFinish();

}
