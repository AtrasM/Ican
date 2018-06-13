package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.WriteMessageListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Input.MessageFile;
import avida.ican.Farzin.Model.Structure.Input.Receiver;
import avida.ican.Farzin.Model.Structure.OutPut.StructureSendMessage;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-06-09 at 14:59 PM
 */

public class WriteMessagePresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "MessageSystemManagment";
    private String MetodeName = "SendMessage";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "WriteMessagePresenter";
    private FarzinPrefrences farzinPrefrences;
    //_______________________***Dao***______________________________

    //private Dao<StructureUserAndRoleDB, Integer> mGetUserAndRoleListDao = null;

    //_______________________***Dao***______________________________

    public WriteMessagePresenter() {
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


    public void SendMessage(String Subject, String Content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureUserAndRole, final WriteMessageListener writeMessageListener) {

        CallApi(MetodeName, EndPoint, getSoapObject(Subject, Content, structureAttaches, structureUserAndRole), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                StructureSendMessage structureSendMessage = xmlToObject.XmlToObject(Xml, StructureSendMessage.class);
                if (structureSendMessage.getSendMessageResult() > 0) {
                    writeMessageListener.onSuccess();
                } else {
                    writeMessageListener.onFailed("" + structureSendMessage.getStrErrorMsg());
                }
            }

            @Override
            public void onFailed() {
                writeMessageListener.onFailed("");
                App.ShowMessage().ShowToast(Resorse.getString(R.string.error_faild), ToastEnum.TOAST_LONG_TIME);
            }

            @Override
            public void onCancel() {
                writeMessageListener.onCancel();
                App.ShowMessage().ShowToast("کنسل", ToastEnum.TOAST_LONG_TIME);
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
            MessageFile messageFile = new MessageFile(structureAttaches.get(i).getName(), structureAttaches.get(i).getBase64File(), structureAttaches.get(i).getFileExtension());
            messageFileHeader.addProperty("MessageFile", messageFile);
        }
        //*******___________________________________________________________________________********

        //*******____________________________  UserAndRoleList  ____________________________********
        SoapObject receiverHeader = new SoapObject(NameSpace, "receiver");

        for (int i = 0; i < structureUserAndRole.size(); i++) {
            Receiver receiver = new Receiver(structureUserAndRole.get(i).getRole_ID(), structureUserAndRole.get(i).getUser_ID());
            receiverHeader.addProperty("Receiver", receiver);
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
