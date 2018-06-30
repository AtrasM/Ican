package avida.ican.Farzin.Model.Structure.Input;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-06-10 at 9:45 AM
 */

public class StructureReceiverIP implements KvmSerializable {

    private String RoleID;
    private  int UserID;
    private  String UserName;
    private String NativeID;


    public StructureReceiverIP() {
    }

    public StructureReceiverIP(String roleID, int userID) {
        RoleID = roleID;
        UserID = userID;
    }

    public StructureReceiverIP(String roleID, int userID, String userName, String nativeID) {
        RoleID = roleID;
        UserID = userID;
        UserName = userName;
        NativeID = nativeID;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return RoleID;
            case 1:
                return UserID;

            case 2:
                return UserName;
            case 3:
                return NativeID;

        }
        return null;
    }

    public int getPropertyCount() {
        return 4;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo propertyInfo) {
        switch (index) {
            case 0: {
                propertyInfo.name = "RoleID";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 1: {
                propertyInfo.name = "UserID";
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            }
            case 2: {
                propertyInfo.name = "UserName";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 3: {
                propertyInfo.name = "NativeID";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }


            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0: {
                this.RoleID = value.toString();
                break;
            }
            case 1: {
                this.UserID = Integer.parseInt(value.toString());
                break;
            }
            case 2: {
                this.UserName = value.toString();
                break;
            }
            case 3: {
                this.NativeID = value.toString();
                break;
            }

            default:
                break;
        }
    }
}



