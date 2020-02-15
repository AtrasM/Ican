package avida.ican.Farzin.Model.Structure.Database.Cartable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2018-11-20 at 11:12 PM
 */
@DatabaseTable(tableName = "cartable_document_actions")
public class StructureCartableDocumentActionsDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    int ETC;
    @DatabaseField()
    int ActionCode;
    @DatabaseField()
    String ActionName;
    @DatabaseField()
    int ActionOrder;
    @DatabaseField()
    String FarsiDescription;
    @DatabaseField()
    int IsActiveForCardTable;
    @DatabaseField()
    int IsActiveForSend;

    public StructureCartableDocumentActionsDB() {
    }

    public StructureCartableDocumentActionsDB(int ETC, int actionCode, int isActiveForCardTable, int isActiveForSend, String actionName, int actionOrder, String farsiDescription) {
        this.ETC = ETC;
        ActionCode = actionCode;
        ActionName = actionName;
        ActionOrder = actionOrder;
        IsActiveForCardTable = isActiveForCardTable;
        IsActiveForSend = isActiveForSend;
        FarsiDescription = farsiDescription;
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

    public int getActionCode() {
        return ActionCode;
    }

    public void setActionCode(int actionCode) {
        ActionCode = actionCode;
    }

    public String getActionName() {
        return ActionName;
    }

    public void setActionName(String actionName) {
        ActionName = actionName;
    }

    public int getActionOrder() {
        return ActionOrder;
    }

    public void setActionOrder(int actionOrder) {
        ActionOrder = actionOrder;
    }

    public String getFarsiDescription() {
        return FarsiDescription;
    }

    public void setFarsiDescription(String farsiDescription) {
        FarsiDescription = farsiDescription;
    }

    public int getIsActiveForCardTable() {
        return IsActiveForCardTable;
    }

    public void setIsActiveForCardTable(int isActiveForCardTable) {
        IsActiveForCardTable = isActiveForCardTable;
    }

    public int getIsActiveForSend() {
        return IsActiveForSend;
    }

    public void setIsActiveForSend(int isActiveForSend) {
        IsActiveForSend = isActiveForSend;
    }
}
