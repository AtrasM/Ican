package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2019-07-24 at 11:29 PM
 */

public enum DependencyTypeEnum {
    Atf(1),
    Peiro(2),
    DarErtebat(3),
    Peyvast(4),
    Document(5);

    private int intValue;

    DependencyTypeEnum(int intValue) {
        this.intValue = intValue;
    }
    public int getIntValue() {
        return intValue;
    }
}
