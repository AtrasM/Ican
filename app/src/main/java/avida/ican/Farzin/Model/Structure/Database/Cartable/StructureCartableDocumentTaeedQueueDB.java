package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2018-10-23 at 3:49 PM
 */
@DatabaseTable(tableName = "QueueDocumentTaeed")
public class StructureCartableDocumentTaeedQueueDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    int receiveCode;

    public StructureCartableDocumentTaeedQueueDB() {
    }

    public StructureCartableDocumentTaeedQueueDB(int receiverCode) {
        this.receiveCode = receiverCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(int receiveCode) {
        this.receiveCode = receiveCode;
    }
}
