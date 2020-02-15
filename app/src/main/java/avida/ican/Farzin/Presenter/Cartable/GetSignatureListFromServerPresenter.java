package avida.ican.Farzin.Presenter.Cartable;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.GetCartableDocumentSignaturesListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignatureRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignaturesRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2019-05-26 at 12:47 PM
 */


public class GetSignatureListFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "GetSignatureList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetSignatureListFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetSignatureListFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void CallRequest(int ETC, final GetCartableDocumentSignaturesListener listener) {

        CallApi(MethodName, EndPoint, getSoapObject(ETC), new DataProcessListener() {
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
                listener.onCancel();
            }
        });
    }

    private void initStructure(String xml, GetCartableDocumentSignaturesListener listener) {

        StructureSignaturesRES structureSignaturesRES = xmlToObject.DeserializationSimpleXml(xml, StructureSignaturesRES.class);
        if (structureSignaturesRES.getStrErrorMsg() == null || structureSignaturesRES.getStrErrorMsg().isEmpty()) {
            ArrayList<StructureSignatureRES> structureSignatureListRES = new ArrayList<>(structureSignaturesRES.getGetSignatureListResult().getData().getField());
            if (structureSignatureListRES.size() <= 0) {
                structureSignatureListRES.add(new StructureSignatureRES());
            }
            listener.onSuccess(structureSignatureListRES);
        } else {
            listener.onFailed("" + structureSignaturesRES.getStrErrorMsg());
        }


    }

    private SoapObject getSoapObject(int ETC) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("ETC", ETC);
        Log.i("Log","SignatureList ETC= "+ETC);
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
                //Xml = changeXml.charDecoder(Xml);
                Xml = changeXml.CropAsResponseTag(Xml, MethodName);
                Xml = changeXml.charDecoder(Xml);
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

