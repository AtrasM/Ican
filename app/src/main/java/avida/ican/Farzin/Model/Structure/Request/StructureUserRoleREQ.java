package avida.ican.Farzin.Model.Structure.Request;

/**
 * Created by AtrasVida on 2019-07-02 at 12:14 PM
 */

public class StructureUserRoleREQ {
    private String userName;
    private String currentRoleID;
    private String NewRoleID;
    private String ActorIDToken;
    private String MainUserCodeToken;
    private int RoleID;

    public StructureUserRoleREQ() {
    }

    public StructureUserRoleREQ(String userName, String currentRoleID, String newRoleID, int roleID,String actorIDToken,String mainUserCodeToken) {
        this.userName = userName;
        this.currentRoleID = currentRoleID;
        NewRoleID = newRoleID;
        ActorIDToken = actorIDToken;
        MainUserCodeToken = MainUserCodeToken;
        RoleID = roleID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCurrentRoleID() {
        return currentRoleID;
    }

    public void setCurrentRoleID(String currentRoleID) {
        this.currentRoleID = currentRoleID;
    }

    public String getNewRoleID() {
        return NewRoleID;
    }

    public void setNewRoleID(String newRoleID) {
        NewRoleID = newRoleID;
    }

    public int getRoleID() {
        return RoleID;
    }

    public void setRoleID(int roleID) {
        RoleID = roleID;
    }

    public String getActorIDToken() {
        return ActorIDToken;
    }

    public void setActorIDToken(String actorIDToken) {
        ActorIDToken = actorIDToken;
    }

    public String getMainUserCodeToken() {
        return MainUserCodeToken;
    }

    public void setMainUserCodeToken(String mainUserCodeToken) {
        MainUserCodeToken = mainUserCodeToken;
    }
}
