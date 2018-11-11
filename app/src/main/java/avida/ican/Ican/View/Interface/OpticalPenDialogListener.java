package avida.ican.Ican.View.Interface;

import avida.ican.Ican.Model.Structure.StructureOpticalPen;

/**
 * Created by AtrasVida on 2018-11-04 at 10:41 AM
 */

public interface OpticalPenDialogListener {
    void onSuccess(StructureOpticalPen structureOpticalPen);
    void onFaild();
    void onCancel();
}
