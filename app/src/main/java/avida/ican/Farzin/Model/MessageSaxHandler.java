package avida.ican.Farzin.Model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureReceiverRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureRecieveMessageListRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureSenderRES;
import avida.ican.Ican.Model.ChangeXml;

/**
 * Created by AtrasVida on 2019-01-14 at 4:21 PM
 */

public class MessageSaxHandler extends DefaultHandler {
    private List<StructureMessageRES> Messages;
    private StructureMessageRES Message;
    private int ID;
    private boolean IsRead;
    private String Subject;
    private String Description;
    private String SentDate;
    private String ViewDate;
    private List<StructureReceiverRES> Receivers;
    private StructureReceiverRES Receiver;
    private List<StructureMessageAttachRES> MessageFiles;
    private StructureMessageAttachRES MessageFile;
    private StructureSenderRES Sender;
    private String tempVal;
    private final Stack<String> tagsStack = new Stack<String>();
    private ChangeXml changeXml = new ChangeXml();

    public MessageSaxHandler() {
        Messages = new ArrayList<>();
        Receivers = new ArrayList<>();
        MessageFiles = new ArrayList<>();
    }

    public <T> T getObject() {
        StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();
        structureRecieveMessageListRES.setGetRecieveMessageListResult(Messages);
        structureRecieveMessageListRES.setStrErrorMsg("");
        return (T) structureRecieveMessageListRES;
    }


    // Event Handlers
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        // reset
        pushTag(qName);
        tempVal = "";
        if (qName.equalsIgnoreCase("Message")) {
            // create a new instance of employee
            Message = new StructureMessageRES();
        } else if (qName.equalsIgnoreCase("Receiver")) {
            // create a new instance of address
            Receiver = new StructureReceiverRES();
        } else if (qName.equalsIgnoreCase("Sender")) {
            // create a new instance of address
            Sender = new StructureSenderRES();
        } else if (qName.equalsIgnoreCase("MessageFile")) {
            // create a new instance of address
            MessageFile = new StructureMessageAttachRES();
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        tempVal = new String(ch, start, length);
    }


    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        String tag = peekTag();
        if (!qName.equals(tag)) {
            throw new InternalError();
        }

        popTag();
        String parentTag = peekTag();
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
            if (tempVal != null && !tempVal.isEmpty()) {
                MessageFile.setFileBinary(tempVal);
            }
        } else if (qName.equalsIgnoreCase("FileExtension")) {
            if (tempVal != null && !tempVal.isEmpty()) {
                MessageFile.setFileExtension(tempVal);
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

}
