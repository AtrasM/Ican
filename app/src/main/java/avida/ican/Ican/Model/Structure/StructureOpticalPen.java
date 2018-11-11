package avida.ican.Ican.Model.Structure;

/**
 * Created by AtrasVida on 2018-11-04 at 2:33 PM
 */

public class StructureOpticalPen {
    private String bFile;
    private String Title;
    private String FileExtension;

    public StructureOpticalPen(String bFile, String title, String fileExtension) {
        this.bFile = bFile;
        Title = title;
        FileExtension = fileExtension;
    }

    public String getbFile() {
        return bFile;
    }

    public void setbFile(String bFile) {
        this.bFile = bFile;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }
}
