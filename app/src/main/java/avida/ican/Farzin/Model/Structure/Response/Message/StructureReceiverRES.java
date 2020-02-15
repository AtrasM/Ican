package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida in 2018-07-08 at 3:15 PM
 */

@Root(name = "Receiver")
public class StructureReceiverRES {
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
    @Element
    private boolean IsRead;
    @Element(required = false)
    private String ViewDate;

    public StructureReceiverRES() {
    }

    public StructureReceiverRES(int roleID, int userID, String roleName, String firstName, String lastName, String userName, boolean isRead, String viewDate) {
        RoleID = roleID;
        UserID = userID;
        UserName = userName;
        RoleName = roleName;
        FirstName = firstName;
        LastName = lastName;
        IsRead = isRead;
        ViewDate = viewDate;
    }

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

    public boolean isRead() {
        return IsRead;
    }

    public void setRead(boolean read) {
        IsRead = read;
    }

    public String getViewDate() {
        return new ChangeXml().viewCharDecoder(ViewDate);
    }

    public void setViewDate(String viewDate) {
        ViewDate = viewDate;
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



