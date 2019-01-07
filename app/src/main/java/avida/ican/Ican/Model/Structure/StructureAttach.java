package avida.ican.Ican.Model.Structure;

/**
 * Created by AtrasVida on 2018-06-02 at 11:35 AM
 */

public class StructureAttach {
    private String filePath;
    private String Name;
    private String FileExtension;
    private String Description;
    private int icon;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getFileExtension() {
        return FileExtension;
    }

    public void setFileExtension(String fileExtension) {
        FileExtension = fileExtension;
    }

    public StructureAttach(String filePath, String name, String fileExtension, int icon) {
        this.filePath = filePath;
        this.Name = name;
        this.FileExtension = fileExtension;
        this.icon = icon;
    }

    public StructureAttach(String filePath, String name, String fileExtension) {
        this.filePath = filePath;
        this.Name = name;
        this.FileExtension = fileExtension;
    }

    public StructureAttach(String filePath, String name, String fileExtension, String description) {
        this.filePath = filePath;
        this.Name = name;
        this.FileExtension = fileExtension;
        this.Description = description;
    }
}
