package avida.ican.Ican.Model;


/**
 * Created by AtrasVida on 2018-03-14 at 1:31 PM
 */

public class ChangeXml {

    public String CropAsResponseTag(String xmlString, String METHOD_NAME) {
        try {
            xmlString = xmlString.substring(xmlString.indexOf("<" + METHOD_NAME + "Response"), xmlString.indexOf("</" + METHOD_NAME + "Response>") + (11 + METHOD_NAME.length()));

            if (xmlString.contains("![CDATA[")) {
                xmlString = RemoveTag(xmlString, "<![CDATA[", "]]>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public String GetContentTag(String xmlString, String StartTag, String EndTag) {
        try {
            xmlString = xmlString.substring(xmlString.indexOf(StartTag), xmlString.indexOf(EndTag));

            if (xmlString.contains("![CDATA[")) {
                xmlString = RemoveTag(xmlString, "<![CDATA[", "]]>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;
    }

    public String RemoveContentWithTag(String xmlString, String StartTag, String EndTag) {
        String temp = xmlString;
        try {
            int index = xmlString.indexOf(StartTag);
            if (index > 0) {
                temp = xmlString.substring(0, index);
            }
            index = xmlString.lastIndexOf(EndTag);
            if (index > 0) {
                index = index + EndTag.length();
                temp = temp + "" + xmlString.substring(index, xmlString.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    public String RemoveTag(String xmlString, String StartTag, String EndTag) {
        try {
            xmlString = xmlString.replace(StartTag, "").replace(EndTag, "");
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

    public String CharDecoder(String xml) {
        if (xml.contains("![CDATA[")) {
            xml = RemoveTag(xml, "<![CDATA[", "]]>");
        }

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

    public String SaxCharDecoder(String xml) {
       /* if (xml.contains("![CDATA[")) {
            xml = RemoveTag(xml, "<![CDATA[", "]]>");
        }*/
        xml = xml.replaceAll("&", "&amp;")
                .replaceAll("&amp;amp;", "&amp;");

/*
        xml = xml.replaceAll("&amp;amp;", "&amp;")
                .replaceAll("&amp;lt;", "&lt;")
                .replaceAll("&amp;gt;", "&gt;")
                .replaceAll("&amp;apos;", "&apos;")
                .replaceAll("&amp;quot;", "&quot;")
                .replaceAll("&amp;nbsp;", "&nbsp;")
                .replaceAll("&amp;copy;", "&copy;");
*/


        return xml;
    }
}
