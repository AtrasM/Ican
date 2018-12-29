package avida.ican.Farzin.Model.Structure.Request;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by AtrasVida in 2018-11-18 at 12:28 AM
 */

public class StructurePersonREQ  {
    private int roleId;
    private int action;
    private String description;
    private String hameshTitle;
    private String hameshContent;
    private String responseUntilDate;
    private String fullName;
    private int PriorityID_Send;


    public StructurePersonREQ() {
        this.description = "";
        this.hameshContent = "";
        this.hameshTitle = "";
        this.action = -1;
        this.roleId = -1;
        this.responseUntilDate = "";
        this.fullName = "";
        this.PriorityID_Send = 1;
    }

    public StructurePersonREQ(int roleId, int action, String description, String hameshContent,String hameshTitle) {
        this.roleId = roleId;
        this.action = action;
        this.description = description;
        this.hameshContent = hameshContent;
        this.hameshTitle = hameshTitle;
        this.responseUntilDate = "";
        this.PriorityID_Send = 1;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHameshTitle() {
        return hameshTitle;
    }

    public void setHameshTitle(String hameshTitle) {
        this.hameshTitle = hameshTitle;
    }

    public String getHameshContent() {
        return hameshContent;
    }

    public void setHameshContent(String hameshContent) {
        this.hameshContent = hameshContent;
    }

    public String getResponseUntilDate() {
        return responseUntilDate;
    }

    public void setResponseUntilDate(String responseUntilDate) {
        this.responseUntilDate = responseUntilDate;
    }

    public int getPriorityID_Send() {
        return PriorityID_Send;
    }

    public void setPriorityID_Send(int priorityID_Send) {
        PriorityID_Send = priorityID_Send;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}



