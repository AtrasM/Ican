package avida.ican.Farzin.Model.saxHandler;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import androidx.annotation.RequiresApi;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureGetMessageAttachmentRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureSenderRES;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-05-12 at 12:12 PM
 */

public class MessageAttachmentSaxHandler extends DefaultHandler {

    private List<StructureMessageAttachRES> MessageFiles;
    private StructureMessageAttachRES MessageFile;
    private StructureSenderRES Sender;
    private String tempVal;
    private final Stack<String> tagsStack = new Stack<String>();

    private StringBuilder sb;
    private boolean isFile = false;
    private String filePath = "";
    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;

    public MessageAttachmentSaxHandler() {
        MessageFiles = new ArrayList<>();
    }

    public <T> T getObject() {

        StructureGetMessageAttachmentRES structureGetMessageAttachmentRES = new StructureGetMessageAttachmentRES();
        structureGetMessageAttachmentRES.setGetMessageAttachmentResult(MessageFiles);
        structureGetMessageAttachmentRES.setStrErrorMsg("");
        return (T) structureGetMessageAttachmentRES;

    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset

        pushTag(qName);
        sb = new StringBuilder();
        tempVal = "";
        if (qName.equalsIgnoreCase("MessageFile")) {
            MessageFile = new StructureMessageAttachRES();
        } else if (qName.equalsIgnoreCase("FileBinary")) {
            initStream();
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
            sb.append(buffer, start, length);
        }
       /* try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String tag = peekTag();
        if (!qName.equals(tag)) {
            Log.i("endDocument", "InternalError");
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();
        //Log.i("Data", "endElement");
        if (qName.equalsIgnoreCase("FileBinary")) {
            closeStream();
        } else {
            tempVal = sb.toString();
        }
        if (qName.equalsIgnoreCase("MessageFile")) {
            MessageFiles.add(MessageFile);
        }
        //init filed
        if (qName.equalsIgnoreCase("FileName")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                MessageFile.setFileName(tempVal);
            }
        } else if (qName.equalsIgnoreCase("FileBinary")) {
            MessageFile.setFileAsStringBuilder(getFilePath());
        } else if (qName.equalsIgnoreCase("FileExtension")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                MessageFile.setFileExtension(tempVal);
            }
        }
    }

    @Override
    public void endDocument() throws SAXException {
        Log.i("endDocument", "endDocument");
        super.endDocument();
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
            //Log.i("FileBinary", "FileBinary filePath= " + filePath);

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
