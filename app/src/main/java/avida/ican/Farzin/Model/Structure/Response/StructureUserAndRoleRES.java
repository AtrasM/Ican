package avida.ican.Farzin.Model.Structure.Response;

import com.google.gson.annotations.SerializedName;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-04-18 at 5:24 PM
 */
public class StructureUserAndRoleRES {
    @SerializedName("@User_ID")
    private int User_ID;
    @SerializedName("@UserName")
    private String UserName;
    @SerializedName("@FirstName")
    private String FirstName;
    @SerializedName("@LastName")
    private String LastName;
    @SerializedName("@Role_ParentID")
    private String Role_ParentID;
    @SerializedName("@IsDefForCardTable")
    private String IsDefForCardTable;
    @SerializedName("@RoleCode")
    private String RoleCode;
    @SerializedName("@RoleName")
    private String RoleName;
    @SerializedName("@Role_ID")
    private int Role_ID;
    @SerializedName("@OrganCode")
    private String OrganCode;
    @SerializedName("@OrganizationRoleName")
    private String OrganizationRoleName;
    @SerializedName("@OrganizationRole_ID")
    private String OrganizationRole_ID;
    @SerializedName("@DepartmentID")
    private String DepartmentID;
    @SerializedName("@Mobile")
    private String Mobile;
    @SerializedName("@Gender")
    private String Gender;
    @SerializedName("@BirthDate")
    private String BirthDate;
    @SerializedName("@E_Mail")
    private String E_Mail;
    @SerializedName("@NativeID")
    private String NativeID;


    public int getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(int user_ID) {
        User_ID = user_ID;
    }

    public String getUserName() {
        return new ChangeXml().viewCharDecoder(UserName);
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFirstName() {
        return new ChangeXml().viewCharDecoder(FirstName);
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return new ChangeXml().viewCharDecoder(LastName);
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

    public String getIsDefForCardTable() {
        return IsDefForCardTable;
    }

    public void setIsDefForCardTable(String isDefForCardTable) {
        IsDefForCardTable = isDefForCardTable;
    }

    public String getRoleCode() {
        return new ChangeXml().viewCharDecoder(RoleCode);
    }

    public void setRoleCode(String roleCode) {
        RoleCode = roleCode;
    }

    public String getRoleName() {
        return new ChangeXml().viewCharDecoder(RoleName);
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
        return new ChangeXml().viewCharDecoder(OrganCode);
    }

    public void setOrganCode(String organCode) {
        OrganCode = organCode;
    }

    public String getOrganizationRoleName() {
        return new ChangeXml().viewCharDecoder(OrganizationRoleName);
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
        return new ChangeXml().viewCharDecoder(BirthDate);
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
}