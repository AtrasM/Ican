package avida.ican.Farzin.Model.Structure.Request;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-11-18 at 12:28 AM
 */

public class StructureSenderREQ implements KvmSerializable {
    private int roleId;
    private int sendParentCode;
    private String description;
    private boolean isLocked;
    private int viewInOutbox;


    public StructureSenderREQ() {
    }

    public StructureSenderREQ(int roleId) {
        this.roleId = roleId;
        sendParentCode = -1;
        description = "";
        isLocked = false;
        viewInOutbox = 1;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return roleId;
            case 1:
                return sendParentCode;
            case 2:
                return description;
            case 3:
                return isLocked;
            case 4:
                return viewInOutbox;
        }
        return null;
    }

    public int getPropertyCount() {
        return 4;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo propertyInfo) {
        switch (index) {
            case 0: {
                propertyInfo.name = "roleId";
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            }
            case 1: {
                propertyInfo.name = "sendParentCode";
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            }
            case 2: {
                propertyInfo.name = "description";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 3: {
                propertyInfo.name = "isLocked";
                propertyInfo.type = PropertyInfo.BOOLEAN_CLASS;
                break;
            }
            case 4: {
                propertyInfo.name = "viewInOutbox";
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            }

            default:
                break;
        }
    }

    public void setProperty(int index, Object value) {
        switch (index) {
            case 0: {
                this.roleId = Integer.parseInt(value.toString());
                break;
            }
            case 1: {
                this.sendParentCode = Integer.parseInt(value.toString());
                break;
            }
            case 2: {
                this.description = value.toString();
                break;
            }
            case 3: {
                this.isLocked = Boolean.parseBoolean(value.toString());
                break;
            }
            case 4: {
                this.viewInOutbox = Integer.parseInt(value.toString());
                break;
            }

            default:
                break;
        }
    }
}



