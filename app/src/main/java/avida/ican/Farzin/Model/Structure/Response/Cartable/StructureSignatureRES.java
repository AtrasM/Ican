package avida.ican.Farzin.Model.Structure.Response.Cartable;


import org.simpleframework.xml.Attribute;

/**
 * Created by AtrasVida on 2019-06-02 at 3:25 PM
 */
public class StructureSignatureRES {
    @Attribute(required = false)
    String EN="";
    @Attribute(required = false)
    String FN="";
    int ETC;

    public String getEN() {
        return EN;
    }

    public void setEN(String EN) {
        this.EN = EN;
    }

    public String getFN() {
        return FN;
    }

    public void setFN(String FN) {
        this.FN = FN;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }
}
