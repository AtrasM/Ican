package avida.ican.Farzin.Model.Structure.Response.Cartable;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-10-31 at 1:06 PM
 */

public class StructureAddHameshOpticalPenRES {
    private boolean AddHameshOpticalPenResult;
    private String strErrorMsg;

    public boolean isAddHameshOpticalPenResult() {
        return AddHameshOpticalPenResult;
    }

    public void setAddHameshOpticalPenResult(boolean addHameshOpticalPenResult) {
        AddHameshOpticalPenResult = addHameshOpticalPenResult;
    }

    public String getStrErrorMsg() {
        return new ChangeXml().viewCharDecoder(strErrorMsg);
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
