package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum PutExtraEnum {
    ID("ID"),
    LogOut("LogOut"),
    BundleMessage("BundleMessage"),
    BundleCartableDocument("BundleCartableDocument"),
    BundleCartableDocumentDetail("BundleCartableDocumentDetail"),
    MultyMessage("MultyMessage"),
    Notification("Notification"),
    ISFwdReplyMessage("ISFwdReplyMessage");


    private final String name;

    private PutExtraEnum(String s) {
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
