package avida.ican.Ican.Model;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by AtrasVida on 2018-03-14 at 2:03 PM
 */

public class XmlToObject {
    public <T> T XmlToObject(String xml, Class<T> tClass) {

        try {
            return getGsoanXml().fromXml(xml, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return (T) tClass;
        }
    }

    private GsonXml getGsoanXml() {
        XmlParserCreator parserCreator = new XmlParserCreator() {
            @Override
            public XmlPullParser createParser() {
                try {
                    return XmlPullParserFactory.newInstance().newPullParser();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
        GsonXml gsonXml = new GsonXmlBuilder()
                .setSameNameLists(true)
                .setPrimitiveArrays(true)
                .setXmlParserCreator(parserCreator)
                .create();
        return gsonXml;
    }


}

