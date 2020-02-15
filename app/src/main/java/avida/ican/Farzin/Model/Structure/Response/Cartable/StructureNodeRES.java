package avida.ican.Farzin.Model.Structure.Response.Cartable;

import com.google.gson.annotations.SerializedName;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-10-06 at 10:59 AM
 */

public class StructureNodeRES {
    @SerializedName("@ReceiveAction")
    String ReceiveAction;
    @SerializedName("@ReceiveActionCode")
    int ReceiveActionCode;
    @SerializedName("@ReceiveDate")
    String ReceiveDate;
    @SerializedName("@ResponseDate")
    String ResponseDate;
    @SerializedName("@ReceiveDescription")
    String ReceiveDescription;
    @SerializedName("@ReceiveDateGeorgian")
    String ReceiveDateGeorgian;
    @SerializedName("@ResponseDateGeorgian")
    String ResponseDateGeorgian;
    @SerializedName("@PriorityID_Send")
    int PriorityID_Send;
    @SerializedName("@PriorityName_Send")
    String PriorityName_Send;
    @SerializedName("@SenderRoleName")
    String SenderRoleName;
    @SerializedName("@SenderFirstName")
    String SenderFirstName;
    @SerializedName("@SenderLastName")
    String SenderLastName;
    @SerializedName("@RoleName")
    String RoleName;
    @SerializedName("@FirstName")
    String FirstName;
    @SerializedName("@LastName")
    String LastName;
    StructurePrivateHamesh PrivateHamesh = new StructurePrivateHamesh();

    public boolean isLnMoreVisible() {
        return isLnMoreVisible;
    }

    public void setLnMoreVisible(boolean lnMoreVisible) {
        isLnMoreVisible = lnMoreVisible;
    }

    private boolean isLnMoreVisible;

    public String getReceiveAction() {
        return ReceiveAction;
    }

    public void setReceiveAction(String receiveAction) {
        ReceiveAction = receiveAction;
    }

    public int getReceiveActionCode() {
        return ReceiveActionCode;
    }

    public void setReceiveActionCode(int receiveActionCode) {
        ReceiveActionCode = receiveActionCode;
    }

    public String getReceiveDate() {
        return ReceiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        ReceiveDate = receiveDate;
    }

    public int getPriorityID_Send() {
        return PriorityID_Send;
    }

    public void setPriorityID_Send(int priorityID_Send) {
        PriorityID_Send = priorityID_Send;
    }

    public String getPriorityName_Send() {
        return PriorityName_Send;
    }

    public void setPriorityName_Send(String priorityName_Send) {
        PriorityName_Send = priorityName_Send;
    }

    public String getSenderRoleName() {
        return new ChangeXml().viewCharDecoder(SenderRoleName);
    }

    public void setSenderRoleName(String senderRoleName) {
        SenderRoleName = senderRoleName;
    }

    public String getSenderFirstName() {
        return new ChangeXml().viewCharDecoder(SenderFirstName);
    }

    public void setSenderFirstName(String senderFirstName) {
        SenderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return new ChangeXml().viewCharDecoder(SenderLastName);
    }

    public void setSenderLastName(String senderLastName) {
        SenderLastName = senderLastName;
    }

    public String getRoleName() {
        return new ChangeXml().viewCharDecoder(RoleName);
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
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

    public String getResponseDate() {
        return new ChangeXml().viewCharDecoder(ResponseDate);
    }

    public void setResponseDate(String responseDate) {
        ResponseDate = responseDate;
    }


    public StructurePrivateHamesh getPrivateHamesh() {
        return PrivateHamesh;
    }

    public void setPrivateHamesh(StructurePrivateHamesh privateHamesh) {
        PrivateHamesh = privateHamesh;
    }

    public String getReceiveDateGeorgian() {
        return new ChangeXml().viewCharDecoder(ReceiveDateGeorgian);
    }

    public void setReceiveDateGeorgian(String receiveDateGeorgian) {
        ReceiveDateGeorgian = receiveDateGeorgian;
    }

    public String getResponseDateGeorgian() {
        return new ChangeXml().viewCharDecoder(ResponseDateGeorgian);
    }

    public void setResponseDateGeorgian(String responseDateGeorgian) {
        ResponseDateGeorgian = responseDateGeorgian;
    }

    public String getReceiveDescription() {
        return ReceiveDescription;
    }

    public void setReceiveDescription(String receiveDescription) {
        ReceiveDescription = receiveDescription;
    }

    //__________**************_StructurePrivateHamesh_***************_________________
    public class StructurePrivateHamesh {
        @SerializedName("@Title")
        String Title;
        @SerializedName("@Content")
        String Content;
        @SerializedName("@HameshHidden")
        String HameshHidden;
        @SerializedName("@Type")
        String Type;

        public String getTitle() {
            return new ChangeXml().viewCharDecoder(Title);
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getContent() {
            return new ChangeXml().viewCharDecoder(Content);
        }

        public void setContent(String content) {
            Content = content;
        }

        public String getHameshHidden() {
            return HameshHidden;
        }

        public void setHameshHidden(String hameshHidden) {
            HameshHidden = hameshHidden;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }
    }
}
