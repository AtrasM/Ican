package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by AtrasVida on 2018-10-14 at 10:39 AM
 */
@Root(name = "GetFileDependencyResult")
public class StructureZanjireMadrakRES {
    @ElementList(required = false)
    private List<StructureFileRES> Peyro;
    @ElementList(required = false)
    private List<StructureFileRES> Peyvast;
    @ElementList(required = false)
    private List<StructureFileRES> DarErtebat;
    @ElementList(required = false)
    private List<StructureFileRES> Atf;
    private    int ETC;
    private   int EC;

    public List<StructureFileRES> getPeyro() {
        return Peyro;
    }

    public void setPeyro(List<StructureFileRES> peyro) {
        Peyro = peyro;
    }

    public List<StructureFileRES> getPeyvast() {
        return Peyvast;
    }

    public void setPeyvast(List<StructureFileRES> peyvast) {
        Peyvast = peyvast;
    }

    public List<StructureFileRES> getDarErtebat() {
        return DarErtebat;
    }

    public void setDarErtebat(List<StructureFileRES> darErtebat) {
        DarErtebat = darErtebat;
    }

    public List<StructureFileRES> getAtf() {
        return Atf;
    }

    public void setAtf(List<StructureFileRES> atf) {
        Atf = atf;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
    }


}