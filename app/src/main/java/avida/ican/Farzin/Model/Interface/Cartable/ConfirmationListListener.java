package avida.ican.Farzin.Model.Interface.Cartable;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureConfirmationItemRES;

/**
 * Created by AtrasVida on 2019-06-26 at 4:53 PM
 */

public interface ConfirmationListListener {
    void onSuccess(ArrayList<StructureConfirmationItemRES> confirmationItemRES);

    void onFailed(String message);

    void onCancel();

}
