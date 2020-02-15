package avida.ican.Farzin.Model.Structure.Response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2019-07-01 at 2:37 PM
 */
@Root(name = "Data")
public class StructureLoginDataRES {

    @Element(required = false)
    private String RoleIDToken;
    @Element(required = false)
    private String Main_ActorToken;
    @Element(required = false)
    private String Main_UserCodeToken;
    @Element(required = false)
    private String FirstName;
    @Element(required = false)
    private String LastName;
    @ElementList(required = false)
    private List<StructureUserRoleRES> ActiveRoles = new ArrayList<>();

    public String getRoleIDToken() {
        return RoleIDToken;
    }

    public void setRoleIDToken(String roleIDToken) {
        RoleIDToken = roleIDToken;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public List<StructureUserRoleRES> getActiveRoles() {
        return ActiveRoles;
    }

    public void setActiveRoles(List<StructureUserRoleRES> activeRoles) {
        ActiveRoles = activeRoles;
    }

    public String getMain_ActorToken() {
        return Main_ActorToken;
    }

    public void setMain_ActorToken(String main_ActorToken) {
        Main_ActorToken = main_ActorToken;
    }

    public String getMain_UserCodeToken() {
        return Main_UserCodeToken;
    }

    public void setMain_UserCodeToken(String main_UserCodeToken) {
        Main_UserCodeToken = main_UserCodeToken;
    }
}
