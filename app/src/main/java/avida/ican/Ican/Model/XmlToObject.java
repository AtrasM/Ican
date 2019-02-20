package avida.ican.Ican.Model;

import android.util.Log;

import com.stanfy.gsonxml.GsonXml;
import com.stanfy.gsonxml.GsonXmlBuilder;
import com.stanfy.gsonxml.XmlParserCreator;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.SAXParserFactory;


/**
 * Created by AtrasVida on 2018-03-14 at 2:03 PM
 */

public class XmlToObject {
    private boolean sameNameLists = true;
    private boolean primitiveArrays = true;

    public XmlToObject setSameNameLists(boolean sameNameLists) {
        this.sameNameLists = sameNameLists;
        return this;
    }


    public XmlToObject setPrimitiveArrays(boolean primitiveArrays) {
        this.primitiveArrays = primitiveArrays;
        return this;
    }


    public <T> T DeserializationGsonXml(String xml, Class<T> tClass) {

        try {
            return (T) getGsoanXml().fromXml(xml, tClass);

        } catch (Exception e) {
            e.printStackTrace();
            return (T) tClass;
        }
    }

    public <T> T DeserializationSimpleXml(String xml, Class<T> tClass) {
        Serializer serializer = new Persister();
        try {
            return (T) serializer.read(tClass, xml, false);
        } catch (Exception e) {
            Log.i("SimpleXmlErrorAvida", "Exception= " + e.toString());
            e.printStackTrace();
            return (T) tClass;
        }
    }

    public <T> T parseXmlWithSax(String xmlFilePath, ContentHandler saxHandler) {
        File file = new File(xmlFilePath);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(fileInputStream));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser error =" + ex.toString());
            Log.d("XML", "SAXXMLParser: parse() failed");
        }

        return (T) saxHandler;
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
                .setXmlParserCreator(parserCreator)
                .setSameNameLists(sameNameLists)
                .setPrimitiveArrays(primitiveArrays)
                .create();
        return gsonXml;
    }


}

