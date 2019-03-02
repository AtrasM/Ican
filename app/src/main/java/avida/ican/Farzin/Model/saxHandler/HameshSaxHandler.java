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
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshImageRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-01-26 at 4:11 PM
 */

public class HameshSaxHandler extends DefaultHandler {
    private List<StructureHameshRES> structureHameshs;
    private StructureHameshRES structureHamesh;
    private StructureHameshImageRES structureHameshImage;

    private String tempVal;
    private final Stack<String> tagsStack = new Stack<String>();
    private StringBuilder sb;
    private boolean isFile = false;
    private String filePath = "";
    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;

    public HameshSaxHandler() {
        structureHameshs = new ArrayList<>();
    }

    public <T> T getObject() {
        StructureHameshListRES structureHameshListRES = new StructureHameshListRES();
        structureHameshListRES.setGetHameshListResult(structureHameshs);
        structureHameshListRES.setStrErrorMsg("");
        return (T) structureHameshListRES;
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        pushTag(qName);
        sb = new StringBuilder();
        tempVal = "";
        if (qName.equalsIgnoreCase("anyType")) {
            // create a new instance of employee
            structureHamesh = new StructureHameshRES();
        } else if (qName.equalsIgnoreCase("HameshImage")) {
            // create a new instance of address
            structureHameshImage = new StructureHameshImageRES();
        }else if (qName.equalsIgnoreCase("FileBinary")) {
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
            sb.append(new String(buffer, start, length));
        }
     /*   try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String tag = peekTag();
        if (!qName.equals(tag)) {
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();

        if (qName.equalsIgnoreCase("FileBinary")) {
            closeStream();
        } else {
            tempVal = sb.toString();
        }
        if (qName.equalsIgnoreCase("anyType")) {
            // add it to the list
            structureHameshs.add(structureHamesh);
        } else if (qName.equalsIgnoreCase("HameshImage")) {
            structureHamesh.setHameshImage(structureHameshImage);
        }


        //init filed
        else if (qName.equalsIgnoreCase("HameshID")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setHameshID(Integer.parseInt(tempVal));
            }

        } else if (qName.equalsIgnoreCase("Title")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setTitle(tempVal);
            }

        } else if (qName.equalsIgnoreCase("Content")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setContent(tempVal);
            }
        } else if (qName.equalsIgnoreCase("hameshType")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setHameshType(tempVal);
            }
        } else if (qName.equalsIgnoreCase("CreatorName")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setCreatorName(tempVal);
            }
        } else if (qName.equalsIgnoreCase("CreatorRoleName")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setCreatorRoleName(tempVal);
            }
        } else if (qName.equalsIgnoreCase("CreationShamsiDate")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setCreationShamsiDate(tempVal);
            }
        } else if (qName.equalsIgnoreCase("CreationDate")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setCreationDate(tempVal);
            }
        } else if (qName.equalsIgnoreCase("IsPrivate")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setPrivate(Boolean.parseBoolean(tempVal));
            }
        } else if (qName.equalsIgnoreCase("IsHidden")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHamesh.setHidden(Boolean.parseBoolean(tempVal));
            }
        } else if (qName.equalsIgnoreCase("FileName")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHameshImage.setFileName(tempVal);
            }
        } else if (qName.equalsIgnoreCase("FileBinary")) {
            structureHameshImage.setFileAsStringBuilder(getFilePath());
        } else if (qName.equalsIgnoreCase("FileExtension")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                structureHameshImage.setFileExtension(tempVal);
            }
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
