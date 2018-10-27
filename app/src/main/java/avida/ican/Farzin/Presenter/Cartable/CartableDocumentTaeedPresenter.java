package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureTaeedRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-10-24 at 10:06
 */


public class CartableDocumentTaeedPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "CartableManagment";
    private String MetodName = "Response";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "CartableDocumentTaeedPresenter";
    private FarzinPrefrences farzinPrefrences;

    public CartableDocumentTaeedPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void TaeedDocument(int receiverCode, TaeedListener listener) {
        GetData(getSoapObject(receiverCode), listener);
    }

    private void GetData(SoapObject soapObject, final TaeedListener listener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
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

    private void initStructure(String xml, TaeedListener listener) {
        StructureTaeedRES structureTaeedRES = xmlToObject.DeserializationGsonXml(xml, StructureTaeedRES.class);
        if (structureTaeedRES.getStrErrorMsg() == null || structureTaeedRES.getStrErrorMsg().isEmpty()) {

            if (structureTaeedRES.isResponseResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + structureTaeedRES.getStrErrorMsg());
            }

        } else {
            listener.onFailed("" + structureTaeedRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(int receiverCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("receiverCode", receiverCode);
        soapObject.addProperty("description", "confirm from android app");
        soapObject.addProperty("responseType", "approve");

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
                Xml = changeXml.CropAsResponseTag(Xml, MetodName);
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

