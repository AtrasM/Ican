package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2018-04-18 at 5:24 PM
 */
@DatabaseTable(tableName = "userAndRoleList")
public class StructureUserAndRoleDB implements Serializable {
    final private String FIELD_NAME_ID = "id";
    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int id;
    @DatabaseField()
    private int User_ID;
    @DatabaseField()
    private String UserName;
    @DatabaseField()
    private String FirstName;
    @DatabaseField()
    private String LastName;
    @DatabaseField()
    private String Role_ParentID;
    @DatabaseField()
    private String IsDefForCardTableString;
    @DatabaseField()
    private String RoleCode;
    @DatabaseField()
    private String RoleName;
    @DatabaseField()
    private int Role_ID;
    @DatabaseField()
    private String OrganCode;
    @DatabaseField()
    private String OrganizationRoleName;
    @DatabaseField()
    private String OrganizationRole_ID;
    @DatabaseField()
    private String DepartmentID;
    @DatabaseField()
    private String Mobile;
    @DatabaseField()
    private String Gender;
    @DatabaseField()
    private String BirthDate;
    @DatabaseField()
    private String E_Mail;
    @DatabaseField()
    private String NativeID;

    private boolean Selected;


    public StructureUserAndRoleDB() {
        //empty
    }

    public StructureUserAndRoleDB(int user_ID, String userName, String firstName, String lastName, String role_ParentID, String isDefForCardTableString, String roleCode, String roleName, int role_ID, String organCode, String organizationRoleName, String organizationRole_ID, String departmentID, String mobile, String gender, String birthDate, String e_Mail, String nativeID) {
        User_ID = user_ID;
        UserName = userName;
        FirstName = firstName;
        LastName = lastName;
        Role_ParentID = role_ParentID;
        IsDefForCardTableString = isDefForCardTableString;
        RoleCode = roleCode;
        RoleName = roleName;
        Role_ID = role_ID;
        OrganCode = organCode;
        OrganizationRoleName = organizationRoleName;
        OrganizationRole_ID = organizationRole_ID;
        DepartmentID = departmentID;
        Mobile = mobile;
        Gender = gender;
        BirthDate = birthDate;
        E_Mail = e_Mail;
        NativeID = nativeID;
    }

    public StructureUserAndRoleDB(String user_name, int user_id, int role_id) {
        User_ID = user_id;
        UserName = user_name;
        Role_ID = role_id;
    }

    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public String getRole_ParentID() {
        return Role_ParentID;
    }

    public void setRole_ParentID(String role_ParentID) {
        Role_ParentID = role_ParentID;
    }

    public String getIsDefForCardTableString() {
        return IsDefForCardTableString;
    }

    public void setIsDefForCardTableString(String isDefForCardTableString) {
        IsDefForCardTableString = isDefForCardTableString;
    }

    public String getRoleCode() {
        return RoleCode;
    }

    public void setRoleCode(String roleCode) {
        RoleCode = roleCode;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public int getRole_ID() {
        return Role_ID;
    }

    public void setRole_ID(int role_ID) {
        Role_ID = role_ID;
    }

    public String getOrganCode() {
        return OrganCode;
    }

    public void setOrganCode(String organCode) {
        OrganCode = organCode;
    }

    public String getOrganizationRoleName() {
        return OrganizationRoleName;
    }

    public void setOrganizationRoleName(String organizationRoleName) {
        OrganizationRoleName = organizationRoleName;
    }

    public String getOrganizationRole_ID() {
        return OrganizationRole_ID;
    }

    public void setOrganizationRole_ID(String organizationRole_ID) {
        OrganizationRole_ID = organizationRole_ID;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public String getE_Mail() {
        return E_Mail;
    }

    public void setE_Mail(String e_Mail) {
        E_Mail = e_Mail;
    }

    public String getNativeID() {
        return NativeID;
    }

    public void setNativeID(String nativeID) {
        NativeID = nativeID;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }
}
