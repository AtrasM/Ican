package avida.ican.Farzin.Model.Structure.Response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida on 2019-07-01 at 2:47 PM
 */
@Root(name = "Role")
public class StructureUserRoleRES {
    @Attribute(required = false)
    private int RoleID;
    @Attribute(required = false)
    private String RoleIDToken;
    @Attribute(required = false)
    private int isDef;
    @Attribute(required = false)
    private String RoleName;
    @Attribute(required = false)
    private String ActorIDToken;
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

    public int getIsDef() {
        return isDef;
    }

    public void setIsDef(int isDef) {
        this.isDef = isDef;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getActorIDToken() {
        return ActorIDToken;
    }

    public void setActorIDToken(String actorIDToken) {
        ActorIDToken = actorIDToken;
    }
}
