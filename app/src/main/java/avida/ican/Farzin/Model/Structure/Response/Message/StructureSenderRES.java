package avida.ican.Farzin.Model.Structure.Response.Message;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}



