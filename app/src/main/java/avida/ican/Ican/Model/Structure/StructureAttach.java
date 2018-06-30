package avida.ican.Ican.Model.Structure;

/**
 * Created by AtrasVida on 2018-06-02 at 11:35 AM
 */

public class StructureAttach {
    private String base64File;
    private String Name;
    private  String FileExtension;
    private int icon;

    public String getBase64File() {
        return base64File;
    }

    public void setBase64File(String base64File) {
        this.base64File = base64File;
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

    public StructureAttach(String base64File, String name,String fileExtension ,int icon) {
        this.base64File = base64File;
        this. Name = name;
        this. FileExtension = fileExtension;
        this.icon = icon;
    }
    public StructureAttach(String base64File, String name,String fileExtension ) {
        this.base64File = base64File;
        this. Name = name;
        this. FileExtension = fileExtension;
    }
}
