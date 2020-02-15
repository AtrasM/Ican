package avida.ican.Farzin.Model.Enum;

/**
 * Created by AtrasVida on 2019-07-06 at 3:39 AM
 */

public enum DocumentOperatoresTypeEnum {
    AddHameshOpticalPen(1),//هامش قلم نوری
    SignDocument(2),//امضاء
    Append(3),//ارجاع
    Response(4),//تایید
    WorkFlow(5);//ادامه فرایند
    private int intValue;


    DocumentOperatoresTypeEnum(int value) {
        intValue = value;
    }
    public int getIntValue() {
        return intValue;
    }

    }
