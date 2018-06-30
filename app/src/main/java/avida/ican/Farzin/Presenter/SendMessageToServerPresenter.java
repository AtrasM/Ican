package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.SendMessageListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Input.StructureMessageFileIP;
import avida.ican.Farzin.Model.Structure.Input.StructureReceiverIP;
import avida.ican.Farzin.Model.Structure.OutPut.StructureSendMessageOP;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-06-09 at 14:59 PM
 */

class SendMessageToServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "MessageSystemManagment";
    private String MetodeName = "SendMessage";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "SendMessageToServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    //_______________________***Dao***______________________________

    //private Dao<StructureUserAndRoleDB, Integer> mGetUserAndRoleListDao = null;

    //_______________________***Dao***______________________________

    SendMessageToServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
        //initDao();

    }

  /*  private void initDao() {
        try {
            mGetUserAndRoleListDao = App.getFarzinDatabaseHelper().getGetUserAndRoleListDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/


    void SendMessage(String Subject, String Content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureUserAndRole, final SendMessageListener sendMessageListener) {

        CallApi(MetodeName, EndPoint, getSoapObject(Subject, Content, structureAttaches, structureUserAndRole), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                StructureSendMessageOP structureSendMessageOP = xmlToObject.XmlToObject(Xml, StructureSendMessageOP.class);
                if (structureSendMessageOP.getSendMessageResult() > 0) {
                    sendMessageListener.onSuccess();
                } else {
                    sendMessageListener.onFailed("" + structureSendMessageOP.getStrErrorMsg());
                }
            }

            @Override
            public void onFailed() {
                sendMessageListener.onFailed("");
                //App.ShowMessage().ShowToast(Resorse.getString(R.string.error_faild), ToastEnum.TOAST_LONG_TIME);
            }

            @Override
            public void onCancel() {
                sendMessageListener.onCancel();
                //App.ShowMessage().ShowToast("کنسل", ToastEnum.TOAST_LONG_TIME);
            }
        });
    }

    private SoapObject getSoapObject(String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureUserAndRole) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodeName);
        soapObject.addProperty("Subject", subject);
        soapObject.addProperty("Content", content);

        //*******____________________________  AttachList  ____________________________********
        SoapObject messageFileHeader = new SoapObject(NameSpace, "messagefile");

        for (int i = 0; i < structureAttaches.size(); i++) {
            StructureMessageFileIP structureMessageFileIP = new StructureMessageFileIP(structureAttaches.get(i).getName(), structureAttaches.get(i).getBase64File(), structureAttaches.get(i).getFileExtension());
            messageFileHeader.addProperty("MessageFile", structureMessageFileIP);
        }
        //*******___________________________________________________________________________********

        //*******____________________________  UserAndRoleList  ____________________________********
        SoapObject receiverHeader = new SoapObject(NameSpace, "receiver");

        for (int i = 0; i < structureUserAndRole.size(); i++) {
            StructureReceiverIP structureReceiverIP = new StructureReceiverIP(structureUserAndRole.get(i).getRole_ID(), structureUserAndRole.get(i).getUser_ID());
            receiverHeader.addProperty("Receiver", structureReceiverIP);
        }
        //*******___________________________________________________________________________********

        soapObject.addSoapObject(messageFileHeader);
        soapObject.addSoapObject(receiverHeader);

        return soapObject;

    }


    private void CallApi(String MetodeName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        String SessionId = farzinPrefrences.getCookie();
        new WebService(NameSpace, MetodeName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setSessionId(SessionId)
                .setOnListener(new WebserviceResponseListener() {

                    @Override
                    public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                        new processData(webServiceResponse, dataProcessListener);
                    }
                }).execute();

    }


    private class processData {
        processData(WebServiceResponse webServiceResponse, DataProcessListener dataProcessListener) {
            if (!webServiceResponse.isResponse()) {
                dataProcessListener.onFailed();
                return;
            }
            String Xml = webServiceResponse.getHttpTransportSE().responseDump;
            try {
                Xml = changeXml.CharDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MetodeName);
                //Log.i(Tag, "XmlCropAsResponseTag= " + Xml);
                if (!Xml.isEmpty()) {
                    dataProcessListener.onSuccess(Xml);
                } else {
                    dataProcessListener.onFailed();
                }
            } catch (Exception e) {
                dataProcessListener.onFailed();
                e.printStackTrace();
            }
        }

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }
}
