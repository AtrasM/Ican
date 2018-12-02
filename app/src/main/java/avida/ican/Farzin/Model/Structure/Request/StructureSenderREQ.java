package avida.ican.Farzin.Model.Structure.Request;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-11-18 at 12:28 AM
 */

public class StructureSenderREQ  {
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


    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getSendParentCode() {
        return sendParentCode;
    }

    public void setSendParentCode(int sendParentCode) {
        this.sendParentCode = sendParentCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getViewInOutbox() {
        return viewInOutbox;
    }

    public void setViewInOutbox(int viewInOutbox) {
        this.viewInOutbox = viewInOutbox;
    }


}



