package avida.ican.Farzin.Model.Structure.Response;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by AtrasVida on 2018-07-02 at 4:16 PM
 */

@Root(name = "Message")
public class MessageRES {

    @Element
    private int ID;
    @Element(required = false)
    private String Subject;
    @Element(required = false)
    private String Description;
    @Element(required = false)
    private String SentDate;
    @ElementList
    private List<StructureReceiverRES> Receivers;

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSentDate() {
        return SentDate;
    }

    public void setSentDate(String sentDate) {
        SentDate = sentDate;
    }

    public List<StructureReceiverRES> getReceivers() {
        return Receivers;
    }

    public void setReceivers(List<StructureReceiverRES> receivers) {
        Receivers = receivers;
    }
}
