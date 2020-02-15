package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.ChangeActiveRoleListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureUserRoleREQ;
import avida.ican.Farzin.Model.Structure.Response.StructureChangeActiveRoleRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2019-07-02 at 12:09 PM
 */


public class ChangeActiveRolePresenter {
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "Authentication";
    private String MethodName = "ChangeActiveRole";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "OpticalPenPresenter";
    private FarzinPrefrences farzinPrefrences;

    public ChangeActiveRolePresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void CallRequest(StructureUserRoleREQ structureUserRoleREQ, final ChangeActiveRoleListener listener) {

        CallApi(MethodName, EndPoint, getSoapObject(structureUserRoleREQ), new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, listener);
            }

            @Override
            public void onFailed() {
                listener.onFailed("");
            }

            @Override
            public void onCancel() {
                listener.onFailed("cancel call request");
            }
        });

    }

    private void initStructure(String xml, ChangeActiveRoleListener listener) {
        StructureChangeActiveRoleRES changeActiveRoleRES = xmlToObject.DeserializationGsonXml(xml, StructureChangeActiveRoleRES.class);
        if (changeActiveRoleRES.getStrErrorMsg() == null || changeActiveRoleRES.getStrErrorMsg().isEmpty()) {

            if (changeActiveRoleRES.isChangeActiveRoleResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + changeActiveRoleRES.getStrErrorMsg());
            }
        } else {
            listener.onFailed("" + changeActiveRoleRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureUserRoleREQ structureUserRoleREQ) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("userName", structureUserRoleREQ.getUserName());
        soapObject.addProperty("currentRoleID", structureUserRoleREQ.getCurrentRoleID());
        soapObject.addProperty("NewRoleID", structureUserRoleREQ.getNewRoleID());

        return soapObject;
    }


    private void CallApi(String MethodName, String EndPoint, SoapObject soapObject, final DataProcessListener dataProcessListener) {
        String ServerUrl = farzinPrefrences.getServerUrl();
        String BaseUrl = farzinPrefrences.getBaseUrl();
        String SessionId = farzinPrefrences.getCookie();
        new WebService(NameSpace, MethodName, ServerUrl, BaseUrl, EndPoint)
                .setSoapObject(soapObject)
                .setSessionId(SessionId)
                .setOnListener(new WebserviceResponseListener() {

                    @Override
                    public void WebserviceResponseListener(WebServiceResponse webServiceResponse) {
                        new processData(webServiceResponse, dataProcessListener);
                    }

                    @Override
                    public void NetworkAccessDenied() {
                        dataProcessListener.onFailed();
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
                //Xml = changeXml.CharDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
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

