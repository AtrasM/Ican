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

import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureFileRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-01-23 at 4:21 PM
 */

public class ZanjireMadrakSaxHandler extends DefaultHandler {

    private List<StructureFileRES> Peyro;
    private List<StructureFileRES> Peyvast;
    private List<StructureFileRES> DarErtebat;
    private List<StructureFileRES> Atf;
    private List<StructureFileRES> IndicatorScanedFile;

    private boolean bPeyro;
    private boolean bPeyvast;
    private boolean bDarErtebat;
    private boolean bAtf;
    private boolean bIndicatorScanedFile;

    private StructureFileRES PeyroContent;
    private StructureFileRES PeyvastContent;
    private StructureFileRES DarErtebatContent;
    private StructureFileRES AtfContent;
    private StructureFileRES IndicatorScanedFileContent;

    private StructureZanjireMadrakRES structureZanjireMadrakRES = new StructureZanjireMadrakRES();
    private String tempVal;
    private final Stack<String> tagsStack = new Stack<String>();

    private StringBuilder sb;
    private boolean isFile = false;
    private String filePath = "";
    private FileOutputStream fOut;
    private OutputStreamWriter myOutWriter;

    public ZanjireMadrakSaxHandler() {
        Peyro = new ArrayList<>();
        Peyvast = new ArrayList<>();
        DarErtebat = new ArrayList<>();
        Atf = new ArrayList<>();
        IndicatorScanedFile = new ArrayList<>();
    }

    public <T> T getObject() {
        StructureZanjireMadrakListRES structureZanjireMadrakListRES = new StructureZanjireMadrakListRES();
        structureZanjireMadrakListRES.setGetFileDependencyResult(structureZanjireMadrakRES);
        structureZanjireMadrakListRES.setStrErrorMsg("");
        return (T) structureZanjireMadrakListRES;
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        pushTag(qName);
        sb = new StringBuilder();
        tempVal = "";
        if (qName.equalsIgnoreCase("Peyro")) {
            // create a new instance of employee
            PeyroContent = new StructureFileRES();
            setBooleanTrue("Peyro");
        } else if (qName.equalsIgnoreCase("Peyvast")) {
            // create a new instance of address
            PeyvastContent = new StructureFileRES();
            setBooleanTrue("Peyvast");
        } else if (qName.equalsIgnoreCase("DarErtebat")) {
            // create a new instance of address
            DarErtebatContent = new StructureFileRES();
            setBooleanTrue("DarErtebat");
        } else if (qName.equalsIgnoreCase("Atf")) {
            // create a new instance of address
            AtfContent = new StructureFileRES();
            setBooleanTrue("Atf");
        } else if (qName.equalsIgnoreCase("IndicatorScanedFile")) {
            // create a new instance of address
            IndicatorScanedFileContent = new StructureFileRES();
            setBooleanTrue("IndicatorScanedFile");
        } else if (qName.equalsIgnoreCase("anyType")) {

            if (bPeyro) {
                PeyroContent = new StructureFileRES();
                Peyro.add(PeyroContent);
                structureZanjireMadrakRES.setPeyro(Peyro);
            } else if (bPeyvast) {
                PeyvastContent = new StructureFileRES();
                Peyvast.add(PeyvastContent);
                structureZanjireMadrakRES.setPeyvast(Peyvast);
            } else if (bDarErtebat) {
                DarErtebatContent = new StructureFileRES();
                DarErtebat.add(DarErtebatContent);
                structureZanjireMadrakRES.setDarErtebat(DarErtebat);
            } else if (bAtf) {
                AtfContent = new StructureFileRES();
                Atf.add(AtfContent);
                structureZanjireMadrakRES.setAtf(Atf);
            } else if (bIndicatorScanedFile) {
                IndicatorScanedFileContent = new StructureFileRES();
                IndicatorScanedFile.add(IndicatorScanedFileContent);
                structureZanjireMadrakRES.setIndicatorScanedFile(IndicatorScanedFile);
            }
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
      /*  try {
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
            if (bPeyro) {
                PeyroContent.setFileAsStringBuilder(getFilePath());
            } else if (bPeyvast) {
                PeyvastContent.setFileAsStringBuilder(getFilePath());
            } else if (bDarErtebat) {
                DarErtebatContent.setFileAsStringBuilder(getFilePath());
            } else if (bAtf) {
                AtfContent.setFileAsStringBuilder(getFilePath());
            } else if (bIndicatorScanedFile) {
                IndicatorScanedFileContent.setFileAsStringBuilder(getFilePath());
            }
        } else {
            tempVal = sb.toString();
        }
        if (qName.equalsIgnoreCase("FileName")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                if (bPeyro) {
                    PeyroContent.setFileName(tempVal);
                } else if (bPeyvast) {
                    PeyvastContent.setFileName(tempVal);
                } else if (bDarErtebat) {
                    DarErtebatContent.setFileName(tempVal);
                } else if (bAtf) {
                    AtfContent.setFileName(tempVal);
                } else if (bIndicatorScanedFile) {
                    IndicatorScanedFileContent.setFileName(tempVal);
                }
            }

        } else if (qName.equalsIgnoreCase("FileExtension")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                if (bPeyro) {
                    PeyroContent.setFileExtension(tempVal);
                } else if (bPeyvast) {
                    PeyvastContent.setFileExtension(tempVal);
                } else if (bDarErtebat) {
                    DarErtebatContent.setFileExtension(tempVal);
                } else if (bAtf) {
                    AtfContent.setFileExtension(tempVal);
                } else if (bIndicatorScanedFile) {
                    IndicatorScanedFileContent.setFileExtension(tempVal);
                }
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

    private void setBooleanTrue(String tag) {

        switch (tag) {
            case "Peyro": {
                bPeyro = true;
                bPeyvast = false;
                bDarErtebat = false;
                bAtf = false;
                bIndicatorScanedFile = false;
                break;
            }
            case "Peyvast": {
                bPeyvast = true;
                bPeyro = false;
                bDarErtebat = false;
                bAtf = false;
                bIndicatorScanedFile = false;
                break;
            }
            case "DarErtebat": {
                bDarErtebat = true;
                bPeyro = false;
                bPeyvast = false;
                bAtf = false;
                bIndicatorScanedFile = false;
                break;
            }
            case "Atf": {
                bAtf = true;
                bDarErtebat = false;
                bPeyro = false;
                bPeyvast = false;
                bIndicatorScanedFile = false;
                break;
            }
            case "IndicatorScanedFile": {
                bIndicatorScanedFile = true;
                bAtf = false;
                bDarErtebat = false;
                bPeyro = false;
                bPeyvast = false;
                break;
            }
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
