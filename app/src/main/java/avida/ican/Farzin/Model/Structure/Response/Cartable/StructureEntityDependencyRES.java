package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Date;

/**
 * Created by AtrasVida on 2019-08-03 at 5:24 PM
 */

@Root(name = "anyType")
public class StructureEntityDependencyRES {
    @Element(required = false)
    private int ETC;
    @Element(required = false)
    private int EC;
    @Element(required = false)
    private String EntityNumber;
    @Element(required = false)
    private String ImportEntityNumber;
    @Element(required = false)
    private String ExportEntityNumber;
    @Element(required = false)
    private String Title;
    @Element(required = false)
    private String EntityFarsiName;
    @Element(required = false)
    private String CreationFullName;
    @Element(required = false)
    private String CreationRoleName;
    @Element(required = false)
    private String CreationDate;
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

    public String getEntityNumber() {
        return EntityNumber;
    }

    public void setEntityNumber(String entityNumber) {
        EntityNumber = entityNumber;
    }

    public String getImportEntityNumber() {
        return ImportEntityNumber;
    }

    public void setImportEntityNumber(String importEntityNumber) {
        ImportEntityNumber = importEntityNumber;
    }

    public String getExportEntityNumber() {
        return ExportEntityNumber;
    }

    public void setExportEntityNumber(String exportEntityNumber) {
        ExportEntityNumber = exportEntityNumber;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEntityFarsiName() {
        return EntityFarsiName;
    }

    public void setEntityFarsiName(String entityFarsiName) {
        EntityFarsiName = entityFarsiName;
    }

    public String getCreationFullName() {
        return CreationFullName;
    }

    public void setCreationFullName(String creationFullName) {
        CreationFullName = creationFullName;
    }

    public String getCreationRoleName() {
        return CreationRoleName;
    }

    public void setCreationRoleName(String creationRoleName) {
        CreationRoleName = creationRoleName;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }
}
