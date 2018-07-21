package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.MessageListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureMessageFileREQ;
import avida.ican.Farzin.Model.Structure.Request.StructureReceiverREQ;
import avida.ican.Farzin.Model.Structure.Response.StructureSendMessageRES;
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
    private String MetodName = "SendMessage";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "SendMessageToServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    SendMessageToServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    void SendMessage(String Subject, String Content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureUserAndRole, final MessageListener messageListener) {

        CallApi(MetodName, EndPoint, getSoapObject(Subject, Content, structureAttaches, structureUserAndRole), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                StructureSendMessageRES structureSendMessageRES = xmlToObject.DeserializationGsonXml(Xml, StructureSendMessageRES.class);
                if (structureSendMessageRES.getSendMessageResult() > 0) {
                    messageListener.onSuccess();
                } else {
                    messageListener.onFailed("" + structureSendMessageRES.getStrErrorMsg());
                }
            }

            @Override
            public void onFailed() {
                messageListener.onFailed("");
                //App.ShowMessage().ShowToast(Resorse.getString(R.string.error_faild), ToastEnum.TOAST_LONG_TIME);
            }

            @Override
            public void onCancel() {
                messageListener.onCancel();
                //App.ShowMessage().ShowToast("کنسل", ToastEnum.TOAST_LONG_TIME);
            }
        });
    }

    private SoapObject getSoapObject(String subject, String content, ArrayList<StructureAttach> structureAttaches, List<StructureUserAndRoleDB> structureUserAndRole) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("Subject", subject);
        soapObject.addProperty("Content", content);

        //*******____________________________  AttachList  ____________________________********
        SoapObject messageFileHeader = new SoapObject(NameSpace, "messagefile");

        for (int i = 0; i < structureAttaches.size(); i++) {
            StructureMessageFileREQ structureMessageFileREQ = new StructureMessageFileREQ(structureAttaches.get(i).getName(), structureAttaches.get(i).getBase64File(), structureAttaches.get(i).getFileExtension());
            messageFileHeader.addProperty("MessageFile", structureMessageFileREQ);
        }
        //*******___________________________________________________________________________********

        //*******____________________________  UserAndRoleList  ____________________________********
        SoapObject receiverHeader = new SoapObject(NameSpace, "receiver");

        for (int i = 0; i < structureUserAndRole.size(); i++) {
            StructureReceiverREQ structureReceiverREQ = new StructureReceiverREQ(structureUserAndRole.get(i).getRole_ID(), structureUserAndRole.get(i).getUser_ID());
            receiverHeader.addProperty("Receiver", structureReceiverREQ);
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
                Xml = changeXml.CropAsResponseTag(Xml, MetodName);
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
