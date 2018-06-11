package avida.ican.Ican.Model;


/**
 * Created by AtrasVida on 2018-03-14 at 1:31 PM
 */

public class ChangeXml {

    public String CropAsResponseTag(String xmlString, String METHOD_NAME) {
        try {
            xmlString = xmlString.substring(xmlString.indexOf("<" + METHOD_NAME + "Response"), xmlString.indexOf("</" + METHOD_NAME + "Response>") + (11 + METHOD_NAME.length()));

        if(xmlString.contains("![CDATA[")){
            xmlString=RemoveTag(xmlString,"<![CDATA[","]]>");
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public String RemoveTag(String xmlString, String StartTag, String EndTag) {
        try {
            xmlString = xmlString.replaceAll(StartTag, "").replace(EndTag, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public String ReplaceTag(String xmlString, String StartTagTarget, String StartTagReplacement, String EndTagTarget, String EndTagReplacement) {
        try {
            xmlString = xmlString.replaceAll(StartTagTarget, StartTagReplacement).replace(EndTagTarget, EndTagReplacement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public  String CharDecoder(String xml) {

        xml = xml.replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&apos;", "\'")
                .replaceAll("&quot;", "\"")
                .replaceAll("&nbsp;", " ")
                .replaceAll("&copy;", "@")
                .replaceAll("&reg;", "?");
        return xml;
    }

}
