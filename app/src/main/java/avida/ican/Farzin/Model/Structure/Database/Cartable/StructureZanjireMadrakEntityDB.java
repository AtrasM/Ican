package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import io.reactivex.annotations.Nullable;

/**
 * Created by AtrasVida on 2019-08-04 at 11:13 AM
 */
@DatabaseTable(tableName = "zanjire_madrak_entity")
public class StructureZanjireMadrakEntityDB {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int mainETC;
    @DatabaseField()
    private int mainEC;
    @DatabaseField()
    private int ETC;
    @DatabaseField()
    private int EC;
    @DatabaseField()
    private String Title;
    @DatabaseField()
    private String EntityNumber;
    @DatabaseField()
    private String ImportEntityNumber;
    @DatabaseField()
    private String ExportEntityNumber;
    @DatabaseField()
    private String EntityFarsiName;
    @DatabaseField()
    private ZanjireMadrakFileTypeEnum fileTypeEnum;
    @DatabaseField()
    private String CreationFullName;
    @DatabaseField()
    private String CreationRoleName;
    @DatabaseField()
    private String CreationDate;


    public StructureZanjireMadrakEntityDB() {
    }

    public StructureZanjireMadrakEntityDB(int mainETC, int mainEC, int ETC, int EC, String title, String entityNumber, String importEntityNumber, String exportEntityNumber, String entityFarsiName, String creationFullName, String creationRoleName, String creationDate, ZanjireMadrakFileTypeEnum fileTypeEnum) {
        this.mainETC = mainETC;
        this.mainEC = mainEC;
        this.ETC = ETC;
        this.EC = EC;
        if (title == null) {
            title = "-";
        }
        this.Title = title;
        if (entityNumber == null) {
            entityNumber = "";
        }
        this.EntityNumber = entityNumber;
        if (importEntityNumber == null) {
            importEntityNumber = "";
        }
        this.ImportEntityNumber = importEntityNumber;
        if (exportEntityNumber == null) {
            exportEntityNumber = "";
        }
        this.ExportEntityNumber = exportEntityNumber;
        if (entityFarsiName == null) {
            entityFarsiName = "-";
        }
        this.EntityFarsiName = entityFarsiName;

        if (creationFullName == null) {
            creationFullName = "-";
        }
        this.CreationFullName = creationFullName;

        if (creationRoleName == null) {
            creationRoleName = "-";
        }
        this.CreationRoleName = creationRoleName;

        if (creationDate == null) {
            creationDate = "";
        }
        this.CreationDate = creationDate;

        this.fileTypeEnum = fileTypeEnum;
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

    public ZanjireMadrakFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(ZanjireMadrakFileTypeEnum fileTypeEnum) {
        this.fileTypeEnum = fileTypeEnum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
