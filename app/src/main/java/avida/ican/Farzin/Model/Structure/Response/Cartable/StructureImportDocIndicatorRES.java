package avida.ican.Farzin.Model.Structure.Response.Cartable;


import org.simpleframework.xml.Attribute;

/**
 * Created by AtrasVida on 2019-07-30 11:35 PM
 */
public class StructureImportDocIndicatorRES {
    @Attribute(required = false)
    int ETC;
    @Attribute(required = false)
    int EntityCode;
    @Attribute(required = false)
    int IndicatorID;
    @Attribute(required = false)
    String ImportNumber;
    @Attribute(required = false)
    int FollowID;
 @Attribute(required = false)
    int FollowPassword;

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public int getEntityCode() {
        return EntityCode;
    }

    public void setEntityCode(int entityCode) {
        EntityCode = entityCode;
    }

    public int getIndicatorID() {
        return IndicatorID;
    }

    public void setIndicatorID(int indicatorID) {
        IndicatorID = indicatorID;
    }

    public String getImportNumber() {
        return ImportNumber;
    }

    public void setImportNumber(String importNumber) {
        ImportNumber = importNumber;
    }

    public int getFollowID() {
        return FollowID;
    }

    public void setFollowID(int followID) {
        FollowID = followID;
    }

    public int getFollowPassword() {
        return FollowPassword;
    }

    public void setFollowPassword(int followPassword) {
        FollowPassword = followPassword;
    }
}
