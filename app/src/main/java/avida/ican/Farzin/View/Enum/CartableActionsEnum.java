package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum CartableActionsEnum {
    DstorErja("DstorErja"),
    TozihShakhsi("TozihShakhsi"),
    ListHamesh("ListHamesh"),
    DocumentFlow("GardeshMadrak"),
    TheChainOfEvidence("ZanjireMadrak"),
    Confirm("confirm"),
    InWorkFlow("InWorkFlow"),
    Send("send"),
    ConfirmSend("ConfirmSend");


    private final String name;

    private CartableActionsEnum(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String getValue() {
        return this.name;
    }
}
