package avida.ican.Farzin.Model.saxHandler;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;


import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureRecieveMessageListRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureSenderRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureSentMessageListRES;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-01-14 at 4:21 PM
 */

public class MessageSaxHandler extends DefaultHandler {
    private List<StructureMessageRES> Messages;
    private StructureMessageRES Message;

    private List<StructureReceiverRES> Receivers;
    private StructureReceiverRES Receiver;
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

    public MessageSaxHandler() {
        Messages = new ArrayList<>();
        Receivers = new ArrayList<>();
        MessageFiles = new ArrayList<>();
    }

    public <T> T getObject(boolean isReciverMessage) {
        if (isReciverMessage) {
            StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();
            structureRecieveMessageListRES.setGetRecieveMessageListResult(Messages);
            structureRecieveMessageListRES.setStrErrorMsg("");
            return (T) structureRecieveMessageListRES;
        } else {
            StructureSentMessageListRES structureSentMessageListRES = new StructureSentMessageListRES();
            structureSentMessageListRES.setGetSentMessageListResult(Messages);
            structureSentMessageListRES.setStrErrorMsg("");
            return (T) structureSentMessageListRES;
        }
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset

        pushTag(qName);
        sb = new StringBuilder();
        tempVal = "";

        //Log.i("Data", "startElement");
        if (qName.equalsIgnoreCase("Message")) {
            Message = new StructureMessageRES();
        } else if (qName.equalsIgnoreCase("Receivers")) {
            Receivers = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("Receiver")) {
            Receiver = new StructureReceiverRES();
        } else if (qName.equalsIgnoreCase("Sender")) {
            Sender = new StructureSenderRES();
        } else if (qName.equalsIgnoreCase("MessageFiles")) {
            MessageFiles = new ArrayList<>();
        } else if (qName.equalsIgnoreCase("MessageFile")) {
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
        if (qName.equalsIgnoreCase("Message")) {
            // add it to the list
            Messages.add(Message);
        } else if (qName.equalsIgnoreCase("Receiver")) {
            // add it to the list
            Receivers.add(Receiver);
            Message.setReceivers(Receivers);
        } else if (qName.equalsIgnoreCase("MessageFile")) {
            MessageFiles.add(MessageFile);
            Message.setMessageFiles(MessageFiles);
        } else if (qName.equalsIgnoreCase("Sender")) {
            // add it to the list
            Message.setSender(Sender);
        }
        //init filed
        else if (qName.equalsIgnoreCase("ID")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                Message.setID(Integer.parseInt(tempVal));
            }

        } else if (qName.equalsIgnoreCase("IsRead")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                boolean isRead = Boolean.parseBoolean(tempVal);
                if (parentTag.equalsIgnoreCase("Receiver")) {
                    Receiver.setRead(isRead);
                } else if (parentTag.equalsIgnoreCase("Message")) {
                    Message.setRead(isRead);
                }
            }

        } else if (qName.equalsIgnoreCase("Subject")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                Message.setSubject(tempVal);
            }
        } else if (qName.equalsIgnoreCase("Description")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                if (parentTag.equalsIgnoreCase("MessageFile")) {
                    MessageFile.setDescription(tempVal);
                } else if (parentTag.equalsIgnoreCase("Message")) {
                    Message.setDescription(tempVal);
                }
            }
        } else if (qName.equalsIgnoreCase("SentDate")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                Message.setSentDate(tempVal);
            }
        } else if (qName.equalsIgnoreCase("ViewDate")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                if (parentTag.equalsIgnoreCase("Receiver")) {
                    Receiver.setViewDate(tempVal);
                } else if (parentTag.equalsIgnoreCase("Message")) {
                    Message.setViewDate(tempVal);
                }
            }

        } else if (qName.equalsIgnoreCase("RoleID")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                if (parentTag.equalsIgnoreCase("Sender")) {
                    Sender.setRoleID(Integer.parseInt(tempVal));
                } else if (parentTag.equalsIgnoreCase("Receiver")) {
                    Receiver.setRoleID(Integer.parseInt(tempVal));
                }
            }

        } else if (qName.equalsIgnoreCase("UserID")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                if (parentTag.equalsIgnoreCase("Sender")) {
                    Sender.setUserID(Integer.parseInt(tempVal));
                } else if (parentTag.equalsIgnoreCase("Receiver")) {
                    Receiver.setUserID(Integer.parseInt(tempVal));
                }
            }
        } else if (qName.equalsIgnoreCase("UserName")) {

            if (tempVal != null && !tempVal.isEmpty()) {
                if (parentTag.equalsIgnoreCase("Sender")) {
                    Sender.setUserName(tempVal);
                } else if (parentTag.equalsIgnoreCase("Receiver")) {
                    Receiver.setUserName(tempVal);
                }
            }
        } else if (qName.equalsIgnoreCase("FileName")) {
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
