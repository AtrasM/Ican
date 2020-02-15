package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida in 2018-07-24 at 1:25 PM
 */

@Root(name = "Sender")
public class StructureSenderRES {
    @Element
    private int RoleID;
    @Element
    private int UserID;
    @Element
    private String UserName;
    @Element(required = false)
    private String RoleName;
    @Element(required = false)
    private String FirstName;
    @Element(required = false)
    private String LastName;

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return new ChangeXml().viewCharDecoder(UserName);
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
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
}



