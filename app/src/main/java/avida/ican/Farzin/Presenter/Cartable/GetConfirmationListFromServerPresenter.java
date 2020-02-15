package avida.ican.Farzin.Presenter.Cartable;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.ConfirmationListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureConfirmationItemRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureConfirmationListResultRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2019-06-26 at 4:22 PM
 */


public class GetConfirmationListFromServerPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MethodName = "GetConfirmationList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetConfirmationListFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetConfirmationListFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetCartableDocumentList(ConfirmationListListener confirmationListListener) {
        String startDateTime = getFarzinPrefrences().getConfirmationListDataSyncDate();
        GetCartableDocument(getSoapObject(startDateTime), confirmationListListener);
    }


    private void GetCartableDocument(SoapObject soapObject, final ConfirmationListListener confirmationListListener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, confirmationListListener);
            }

            @Override
            public void onFailed() {
                confirmationListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                confirmationListListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, ConfirmationListListener confirmationListListener) {
        Log.i("xml", "ConfirmationList xml= " + xml);
        StructureConfirmationListResultRES structureConfirmationListResultRES = xmlToObject.DeserializationSimpleXml(xml, StructureConfirmationListResultRES.class);
        if (structureConfirmationListResultRES.getStrErrorMsg() == null || structureConfirmationListResultRES.getStrErrorMsg().isEmpty()) {
            if (structureConfirmationListResultRES.getGetConfirmationListResult().size() <= 0) {
                confirmationListListener.onSuccess(new ArrayList<>());
            } else {
                ArrayList<StructureConfirmationItemRES> structureConfirmationItemRES = new ArrayList<>(structureConfirmationListResultRES.getGetConfirmationListResult());
                confirmationListListener.onSuccess(structureConfirmationItemRES);
            }
        } else {
            confirmationListListener.onFailed("" + structureConfirmationListResultRES.getStrErrorMsg());
        }
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

    private SoapObject getSoapObject(String startDateTime) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        if (!startDateTime.isEmpty()) {
            startDateTime = CustomFunction.arabicToDecimal(startDateTime);
            soapObject.addProperty("StartDateTime", startDateTime);
        }

        return soapObject;
    }

    private class processData {
        processData(WebServiceResponse webServiceResponse, DataProcessListener dataProcessListener) {
            if (!webServiceResponse.isResponse()) {
                dataProcessListener.onFailed();
                return;
            }
            String Xml = webServiceResponse.getHttpTransportSE().responseDump;
            //String Xml = new CustomFunction().readXmlResponseFromStorage();
            try {
                //Xml = changeXml.charDecoder(Xml);
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

