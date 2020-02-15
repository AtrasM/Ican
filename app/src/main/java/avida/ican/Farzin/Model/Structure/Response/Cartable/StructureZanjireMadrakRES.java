package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2018-10-14 at 10:39 AM
 */

@Root(name = "GetFileDependencyResult")
public class StructureZanjireMadrakRES {
    @ElementList(required = false)
    private List<StructureFileRES> Peyro = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureFileRES> Peyvast = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureFileRES> DarErtebat = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureFileRES> Atf = new ArrayList<>();
    @ElementList(required = false)
    private List<StructureFileRES> IndicatorScanedFile = new ArrayList<>();
    private int ETC;
    private int EC;

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

    public List<StructureFileRES> getIndicatorScanedFile() {
        return IndicatorScanedFile;
    }

    public void setIndicatorScanedFile(List<StructureFileRES> indicatorScanedFile) {
        IndicatorScanedFile = indicatorScanedFile;
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
