package avida.ican.Farzin.Model.Structure.Response.Cartable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2018-09-26 at 11:28 AM
 */

@Root(name = "anyType")
@Namespace( prefix = "type", reference = "DstorErja")
public class StructureHameshRES {
    @Element()
    int HameshID;
    @Element(required = false)
     String Title="";
    @Element(required = false)
    String Content="";
    @Element
    String hameshType="";
    @Element
    String CreatorName="";
    @Element
    String CreatorRoleName="";
    @Element
    String CreationShamsiDate="";
    @Element(required = false)
    String CreationDate="";
    @Element
    boolean IsPrivate;
    @Element
    boolean IsHidden;
    @Element(required = false)
    StructureHameshImageRES HameshImage;

    int ETC;
    int EC;

    public String getTitle() {
        return Title;
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

    public String getHameshType() {
        return hameshType;
    }

    public void setHameshType(String hameshType) {
        this.hameshType = hameshType;
    }

    public String getCreatorName() {
        return new ChangeXml().viewCharDecoder(CreatorName);
    }

    public void setCreatorName(String creatorName) {
        CreatorName = creatorName;
    }

    public String getCreatorRoleName() {
        return new ChangeXml().viewCharDecoder(CreatorRoleName);
    }

    public void setCreatorRoleName(String creatorRoleName) {
        CreatorRoleName = creatorRoleName;
    }

    public String getCreationShamsiDate() {
        return new ChangeXml().viewCharDecoder(CreationShamsiDate);
    }

    public void setCreationShamsiDate(String creationShamsiDate) {
        CreationShamsiDate = creationShamsiDate;
    }

    public String getCreationDate() {
        return new ChangeXml().viewCharDecoder(CreationDate);
    }

    public void setCreationDate(String creationDate) {
        CreationDate = creationDate;
    }

    public boolean isPrivate() {
        return IsPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        IsPrivate = aPrivate;
    }

    public boolean isHidden() {
        return IsHidden;
    }

    public void setHidden(boolean hidden) {
        IsHidden = hidden;
    }

    public StructureHameshImageRES getHameshImage() {
        return HameshImage;
    }

    public void setHameshImage(StructureHameshImageRES hameshImage) {
        HameshImage = hameshImage;
    }

    public int getHameshID() {
        return HameshID;
    }

    public void setHameshID(int hameshID) {
        HameshID = hameshID;
    }


    public int getETC() {
        return ETC;
    }

    public void setETC(int ETC) {
        this.ETC = ETC;
    }

    public int getEC() {
        return EC;
    }

    public void setEC(int EC) {
        this.EC = EC;
    }
}
