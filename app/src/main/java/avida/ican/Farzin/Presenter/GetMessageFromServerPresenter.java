package avida.ican.Farzin.Presenter;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Interface.MessageListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.OutPut.StructureMessageListResultOP;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-07-03 at 5:27 PM
 */

public class GetMessageFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "MessageSystemManagment";
    private String MetodName = "";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "SendMessageToServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private final int COUNT = 1;


    public GetMessageFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    @SuppressWarnings("SameParameterValue")
    public void GetRecieveMessageList(int page, MessageListener messageListener) {
        this.MetodName = "GetRecieveMessageList";
        GetMessage(page, messageListener);
    }


    public void GetSentMessageList(int page, MessageListener messageListener) {
        this.MetodName = "GetSentMessageList";
        GetMessage(page, messageListener);
    }

    private void GetMessage(int page, final MessageListener messageListener) {

        CallApi(MetodName, EndPoint, getSoapObject(page, COUNT), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                StructureMessageListResultOP structureMessageListResultOP = xmlToObject.XmlToObject(Xml, StructureMessageListResultOP.class);
                if (structureMessageListResultOP.getGetRecieveMessageListResult().getMessage().size() > 0) {
                    messageListener.onSuccess();
                } else {
                    messageListener.onFailed("" + structureMessageListResultOP.getStrErrorMsg());
                }
            }

            @Override
            public void onFailed() {
                messageListener.onFailed("");
            }

            @Override
            public void onCancel() {
                messageListener.onCancel();
            }
        });
    }

    private SoapObject getSoapObject(int page, int count) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        soapObject.addProperty("iPage", page);
        soapObject.addProperty("iRecordPerPage", count);
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

