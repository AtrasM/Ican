package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;

/**
 * Created by AtrasVida in 2018-12-05 at 11:40 AM
 */

@DatabaseTable(tableName = "document_content")
public class StructureCartableDocumentContentDB implements Serializable {
    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;
    @DatabaseField()
    private String file_name;
    @DatabaseField()
    private String file_binary;
    @DatabaseField()
    private String file_extension;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    int EC;

    public StructureCartableDocumentContentDB() {
        this.file_name = "";
        this.file_binary = "";
        this.file_extension = "";
        this.ETC = -1;
        this.EC = -1;
    }

    public StructureCartableDocumentContentDB(String file_name, String file_binary, String file_extension, int ETC, int EC) {
        this.file_name = file_name;
        this.file_binary = file_binary;
        this.file_extension = file_extension;
        this.ETC = ETC;
        this.EC = EC;
    }

    public int getId() {
        return id;
    }


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_binary() {
        return file_binary;
    }

    public void setFile_binary(String file_binary) {
        this.file_binary = file_binary;
    }

    public String getFile_extension() {
        return file_extension;
    }

    public void setFile_extension(String file_extension) {
        this.file_extension = file_extension;
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

