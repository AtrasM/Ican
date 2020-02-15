package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2019-08-03 at 5:21 PM
 */

@Root(name = "GetEntityDependencyResult")
public class StructureZanjireEntityRES {
    @ElementList(required = false)
    private List<StructureEntityDependencyRES> Peyro = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureEntityDependencyRES> Peyvast = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureEntityDependencyRES> DarErtebat = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureEntityDependencyRES> Atf = new ArrayList<>();
    private int mainETC;
    private int mainEC;

    public List<StructureEntityDependencyRES> getPeyro() {
        return Peyro;
    }

    public void setPeyro(List<StructureEntityDependencyRES> peyro) {
        Peyro = peyro;
    }

    public List<StructureEntityDependencyRES> getPeyvast() {
        return Peyvast;
    }

    public void setPeyvast(List<StructureEntityDependencyRES> peyvast) {
        Peyvast = peyvast;
    }

    public List<StructureEntityDependencyRES> getDarErtebat() {
        return DarErtebat;
    }

    public void setDarErtebat(List<StructureEntityDependencyRES> darErtebat) {
        DarErtebat = darErtebat;
    }

    public List<StructureEntityDependencyRES> getAtf() {
        return Atf;
    }

    public void setAtf(List<StructureEntityDependencyRES> atf) {
        Atf = atf;
    }


    public int getMainETC() {
        return mainETC;
    }

    public void setMainETC(int mainETC) {
        this.mainETC = mainETC;
    }

    public int getMainEC() {
        return mainEC;
    }

    public void setMainEC(int mainEC) {
        this.mainEC = mainEC;
    }
}
