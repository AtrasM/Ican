package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida in 2019-07-01 at 4:12 PM
 */

@DatabaseTable(tableName = "user_role")
public class StructureUserRoleDB implements Serializable {
    @DatabaseField(canBeNull = false, generatedId = true)
    private int id;
    @DatabaseField()
    private int RoleID;
    @DatabaseField()
    private String RoleIDToken;
    @DatabaseField()
    private String ActorIDToken;
    @DatabaseField()
    private String RoleName;
    @DatabaseField()
    private boolean isDef;

    private boolean isSelected;


    public StructureUserRoleDB() {

    }

    public StructureUserRoleDB(int roleID, String roleIDToken,String actorIDToken, String roleName, int isDef) {
        RoleID = roleID;
        RoleIDToken = roleIDToken;
        RoleName = roleName;
        ActorIDToken = actorIDToken;
        if (isDef == 1) {
            this.isDef = true;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public String getRoleIDToken() {
        return RoleIDToken;
    }

    public void setRoleIDToken(String roleIDToken) {
        RoleIDToken = roleIDToken;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public boolean isDef() {
        return isDef;
    }

    public void setDef(boolean def) {
        isDef = def;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getActorIDToken() {
        return ActorIDToken;
    }

    public void setActorIDToken(String actorIDToken) {
        ActorIDToken = actorIDToken;
    }
}

