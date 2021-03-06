package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;

/**
 * Created by AtrasVida in 2018-10-14 at 10:56 AM
 */

@DatabaseTable(tableName = "zanjire_madrak_file")
public class StructureZanjireMadrakFileDB implements Serializable {
    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;
    @DatabaseField()
    private String file_name;
    @DatabaseField()
    private String file_path;
    @DatabaseField()
    private String file_extension;
    @DatabaseField(dataType = DataType.ENUM_INTEGER)
    private ZanjireMadrakFileTypeEnum fileTypeEnum;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    int EC;
    @DatabaseField()
    private String CreationFullName;
    @DatabaseField()
    private String CreationRoleName;
    @DatabaseField()
    private String CreationDate;

    public StructureZanjireMadrakFileDB() {
    }

    public StructureZanjireMadrakFileDB(String file_name, String file_path, String file_extension, String creationFullName, String creationRoleName, String creationDate, ZanjireMadrakFileTypeEnum fileTypeEnum, int ETC, int EC) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_extension = file_extension;
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
        this.ETC = ETC;
        this.EC = EC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
    }

    public ZanjireMadrakFileTypeEnum getFileTypeEnum() {
        return fileTypeEnum;
    }

    public void setFileTypeEnum(ZanjireMadrakFileTypeEnum fileTypeEnum) {
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

