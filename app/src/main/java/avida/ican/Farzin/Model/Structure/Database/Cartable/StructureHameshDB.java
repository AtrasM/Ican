package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;

/**
 * Created by AtrasVida on 2018-09-26 at 12:15 PM
 */

public class StructureHameshDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false)
    int HameshID;
    @DatabaseField()
    String Title;
    @DatabaseField()
    String Content;
    @DatabaseField()
    String hameshType;
    @DatabaseField()
    String CreatorName;
    @DatabaseField()
    String CreatorRoleName;
    @DatabaseField()
    String CreationShamsiDate;
    @DatabaseField()
    String CreationDate;
    @DatabaseField()
    boolean IsPrivate;
    @DatabaseField()
    boolean IsHidden;
    @DatabaseField()
    String FileBinary;
    @DatabaseField()
    String FileExtens;
    @DatabaseField()
    String FileName;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    int EC;

    public StructureHameshDB() {
    }


    public StructureHameshDB(StructureHameshRES structureHameshRES, int ETC, int EC) {
        HameshID = structureHameshRES.getHameshID();
        Title = structureHameshRES.getTitle();
        Content = structureHameshRES.getContent();
        this.hameshType = structureHameshRES.getHameshType();
        CreatorName = structureHameshRES.getCreatorName();
        CreatorRoleName = structureHameshRES.getCreatorRoleName();
        CreationShamsiDate = structureHameshRES.getCreationShamsiDate();
        CreationDate = structureHameshRES.getCreationDate();
        IsPrivate = structureHameshRES.isPrivate();
        IsHidden = structureHameshRES.isHidden();
        this.ETC=ETC;
        this.EC=EC;
        if (structureHameshRES.getHameshImage() != null) {
            FileBinary = structureHameshRES.getHameshImage().getFileBinary();
            FileExtens = structureHameshRES.getHameshImage().getFileExtens();
            FileName = structureHameshRES.getHameshImage().getFileName();
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getHameshType() {
        return hameshType;
    }

    public void setHameshType(String hameshType) {
        this.hameshType = hameshType;
    }

    public String getCreatorName() {
        return CreatorName;
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public String getCreatorRoleName() {
        return CreatorRoleName;
    }

    public void setCreatorRoleName(String creatorRoleName) {
        CreatorRoleName = creatorRoleName;
    }

    public String getCreationShamsiDate() {
        return CreationShamsiDate;
    }

    public void setCreationShamsiDate(String creationShamsiDate) {
        CreationShamsiDate = creationShamsiDate;
    }

    public String getCreationDate() {
        return CreationDate;
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public boolean isPrivate() {
        return IsPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        IsPrivate = aPrivate;
    }

    public boolean isHidden() {
        return IsHidden;
    }

    public void setHidden(boolean hidden) {
        IsHidden = hidden;
    }

    public String getFileBinary() {
        return FileBinary;
    }

    public void setFileBinary(String fileBinary) {
        FileBinary = fileBinary;
    }

    public String getFileExtens() {
        return FileExtens;
    }

    public void setFileExtens(String fileExtens) {
        FileExtens = fileExtens;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
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

    public int getHameshID() {
        return HameshID;
    }

    public void setHameshID(int hameshID) {
        HameshID = hameshID;
    }
}
