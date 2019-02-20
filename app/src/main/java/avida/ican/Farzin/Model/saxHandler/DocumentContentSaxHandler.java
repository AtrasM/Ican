package avida.ican.Farzin.Model.saxHandler;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.EmptyStackException;
import java.util.Stack;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentContentRES;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-01-14 at 4:21 PM
 */

public class DocumentContentSaxHandler extends DefaultHandler {

    private StructureCartableDocumentContentRES cartableDocumentContentRES;
    private String tempVal;
    private final Stack<String> tagsStack = new Stack<String>();
    private StringBuilder sb;
    private boolean isFile = false;
    private String filePath = "";
    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;


    public DocumentContentSaxHandler() {
        cartableDocumentContentRES = new StructureCartableDocumentContentRES();
    }

    public <T> T getObject() {
        cartableDocumentContentRES.setStrErrorMsg("");
        return (T) cartableDocumentContentRES;
    }

    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        pushTag(qName);
        sb = new StringBuilder();
        tempVal = "";
        if (qName.equalsIgnoreCase("GetContentFormAsResult")) {
            initStream();
            cartableDocumentContentRES.setGetContentFormAsResult("");
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void characters(final char[] buffer, final int start, final int length)
            throws SAXException {

        if (isFile) {
            try {
                myOutWriter.append(new String(buffer, start, length));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            sb.append(new String(buffer, start, length));
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String tag = peekTag();
        if (!qName.equals(tag)) {
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();
        if (qName.equalsIgnoreCase("GetContentFormAsResult")) {
            closeStream();
            cartableDocumentContentRES.setGetContentFormAsStringBuilder(getFilePath());
        }

    }

    private void pushTag(String tag) {
        tagsStack.push(tag);
    }

    private String popTag() {
        try {
            return tagsStack.pop();
        } catch (EmptyStackException e) {
            return "";
        }

    }

    private String peekTag() {
        try {
            return tagsStack.peek();
        } catch (EmptyStackException e) {
            return "";
        }
    }

    private void initStream() {
        filePath = new CustomFunction().initFilePath(CustomFunction.getRandomUUID());
        try {
            fOut = new FileOutputStream(filePath);
            myOutWriter = new OutputStreamWriter(fOut);
            isFile = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeStream() {
        try {
            isFile = false;
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getFilePath() {
        sb = new StringBuilder();
        sb.append(filePath);
        Log.i("FilePath", "filePath= " + sb.toString());
        return sb;
    }

}
