package avida.ican.Farzin.Model.Structure.Response.Cartable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AtrasVida on 2018-11-19 at 4:01 PM
 */

public class StructureCartableDocumentActionRES {
    @SerializedName("@ActionCode")
    int ActionCode;
    @SerializedName("@ActionName")
    String ActionName;
    @SerializedName("@ActionOrder")
    int ActionOrder;
    @SerializedName("@FarsiDescription")
    String FarsiDescription;
    int ETC;
    public int getActionCode() {
        return ActionCode;
    }

    public void setActionCode(int actionCode) {
        ActionCode = actionCode;
    }

    public String getActionName() {
        return ActionName;
    }

    public void setActionName(String actionName) {
        ActionName = actionName;
    }

    public int getActionOrder() {
        return ActionOrder;
    }

    public void setActionOrder(int actionOrder) {
        ActionOrder = actionOrder;
    }

    public String getFarsiDescription() {
        return FarsiDescription;
    }

    public void setFarsiDescription(String farsiDescription) {
        FarsiDescription = farsiDescription;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }
}
