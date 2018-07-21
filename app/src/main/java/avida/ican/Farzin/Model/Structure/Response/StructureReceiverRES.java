package avida.ican.Farzin.Model.Structure.Response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by AtrasVida in 2018-07-08 at 15:15 PM
 */

@Root(name = "Receiver")
public class StructureReceiverRES {
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



