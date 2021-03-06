package avida.ican.Ican.Model;


import java.util.regex.Pattern;

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

    public String charDecoder(String xml) {
        String lineSep = System.getProperty("line.separator");
        assert lineSep != null;
        xml = xml.replaceAll("&amp;lt;", "<")
                .replaceAll("&amp;gt;", ">")
                .replaceAll("&amp;apos;", "\'")
                .replaceAll("&amp;quot;", "\"")
                .replaceAll("&amp;nbsp;", " ")
                .replaceAll("&amp;nbsp", " ")
                .replaceAll("&amp;copy;", "@")
                .replaceAll("&amp;reg;", "?")
                .replaceAll("#AvidaEnterTag#", "<br/>");
        /*
                .replaceAll("&#xD;&#xA;|&#xD;|&#xA;", "#AvidaEnterTag#")
                .replaceAll("\r\n|\r|\n", "#AvidaEnterTag#");*/

        return xml;
    }

    public String charSpaceDecoder(String xml) {
        xml = xml.replaceAll("&amp;nbsp;", " ");
        /*.replaceAll("\u0020+", " ");*/
        return xml;
    }

    public String saxCharEncoder(String xml) {
        xml = xml.replaceAll("&", "&amp;")
                .replaceAll("&amp;amp;", "&amp;")
                .replaceAll("'|\'", "&amp;quot;")
                .replaceAll("&#xD;&#xA;|&#xD;|&#xA;", "#AvidaEnterTag#")
                .replaceAll("\r\n|\r|\n", "#AvidaEnterTag#");
        return xml;
    }

    public String viewCharDecoder(String data) {
        if (data != null && !data.isEmpty()) {
            data = charDecoder(data).replaceAll("&amp;", "&")
                    .replaceAll("#AvidaEnterTag#", "<br/>");
        } else {
            data = "";
        }

        return data;
    }

}