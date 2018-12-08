package avida.ican.Farzin.Model.Structure;

/**
 * Created by AtrasVida on 2018-09-22 at 10:31 AM
 */

public class StructureCartableAction {
    private int ActionCode;
    private String ActionName;
    private long Count;
    private boolean Pin;

    public StructureCartableAction(int actionCode, String actionName, long count, boolean isPin) {
        ActionCode = actionCode;
        ActionName = actionName;
        Count = count;
        Pin = isPin;
    }

    public StructureCartableAction() {
        Count = 0;
        Pin = false;
    }

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

    public long getCount() {
        return Count;
    }

    public void setCount(long count) {
        Count = count;
    }

    public boolean isPin() {
        return Pin;
    }

    public void setPin(boolean pin) {
        Pin = pin;
    }
}
