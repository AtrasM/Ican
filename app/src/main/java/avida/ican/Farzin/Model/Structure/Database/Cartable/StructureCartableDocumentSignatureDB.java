package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2019-05-26 at 3:09 PM
 */
@DatabaseTable(tableName = "cartable_document_signature")
public class StructureCartableDocumentSignatureDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    boolean isEmpety;
    @DatabaseField()
    String EN;
    @DatabaseField()
    String FN;

    private boolean Selected;
    public StructureCartableDocumentSignatureDB() {
    }

    public StructureCartableDocumentSignatureDB(int ETC, String en, String fn) {
        this.ETC = ETC;
        EN = en;
        FN = fn;
    }
    public StructureCartableDocumentSignatureDB(int ETC,boolean isEmpety) {
        this.ETC = ETC;
        this.isEmpety = isEmpety;
        EN="";
        FN="";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public String getEN() {
        return EN;
    }

    public void setEN(String EN) {
        this.EN = EN;
    }

    public String getFN() {
        return FN;
    }

    public void setFN(String FN) {
        this.FN = FN;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public boolean isEmpety() {
        return isEmpety;
    }

    public void setEmpety(boolean empety) {
        isEmpety = empety;
    }
}
