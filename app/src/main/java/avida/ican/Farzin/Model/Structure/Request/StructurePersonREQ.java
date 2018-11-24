package avida.ican.Farzin.Model.Structure.Request;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-11-18 at 12:28 AM
 */

public class StructurePersonREQ implements KvmSerializable {
    private int roleId;
    private int action;
    private String description;
    private String hameshTitle;
    private String hameshContent;
    private String responseUntilDate;
    private int PriorityID_Send;


    public StructurePersonREQ() {
    }

    public StructurePersonREQ(int roleId, int action, String description, String hameshContent) {
        this.roleId = roleId;
        this.action = action;
        this.description = description;
        this.hameshContent = hameshContent;
        this.hameshTitle = "";
        this.responseUntilDate = "";
        this.PriorityID_Send = 1;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return roleId;
            case 1:
                return action;
            case 2:
                return description;
            case 3:
                return hameshTitle;
            case 4:
                return hameshContent;
            case 5:
                return responseUntilDate;
            case 6:
                return PriorityID_Send;

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
                propertyInfo.name = "action";
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                break;
            }
            case 2: {
                propertyInfo.name = "description";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 3: {
                propertyInfo.name = "hameshTitle";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 4: {
                propertyInfo.name = "hameshContent";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }

            case 5: {
                propertyInfo.name = "responseUntilDate";
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                break;
            }
            case 6: {
                propertyInfo.name = "PriorityID_Send";
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
                this.action = Integer.parseInt(value.toString());
                break;
            }
            case 2: {
                this.description = value.toString();
                break;
            }
            case 3: {
                this.hameshTitle = value.toString();
                break;
            }
            case 4: {
                this.hameshContent = value.toString();
                break;
            }
            case 5: {
                this.responseUntilDate = value.toString();
                break;
            }
            case 6: {
                this.PriorityID_Send = Integer.parseInt(value.toString());
                break;
            }

            default:
                break;
        }
    }
}



