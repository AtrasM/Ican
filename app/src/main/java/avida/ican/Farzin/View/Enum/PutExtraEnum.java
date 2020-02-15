package avida.ican.Farzin.View.Enum;

/**
 * Created by AtrasVida on 2018-08-07 at 15:00 AM
 */

public enum PutExtraEnum {
    ID("ID"),
    Title("Title"),
    LogOut("LogOut"),
    IsAppLock("IsAppLock"),
    BundleMessage("BundleMessage"),
    BundleCartableDocument("BundleCartableDocument"),
    BundleCartableDocumentDetail("BundleCartableDocumentDetail"),
    BundleActivityDocumentAttach("BundleActivityDocumentAttach"),
    MultyMessage("MultyMessage"),
    MultyCartableDocument("MultyCartableDocument"),
    Notification("Notification"),
    ISFwdReplyMessage("ISFwdReplyMessage"),
    IsFilter("IsFilter"),
    Extension("Extension"),
    ComeFromNotificationManager("ComeFromNotificationManager"),
    DatepickerDialog("DatepickerDialog"),
    SettingType("SettingType"),
    QueueType("QueueType");


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
