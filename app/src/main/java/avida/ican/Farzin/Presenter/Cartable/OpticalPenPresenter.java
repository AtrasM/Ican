package avida.ican.Farzin.Presenter.Cartable;

import android.util.Base64;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.OpticalPenListener;
import avida.ican.Farzin.Model.Interface.Cartable.TaeedListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Request.StructureOpticalPenREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureAddHameshOpticalPenRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureTaeedRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-10-24 at 10:06
 */


public class OpticalPenPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MetodName = "AddHameshOpticalPen";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "OpticalPenPresenter";
    private FarzinPrefrences farzinPrefrences;

    public OpticalPenPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }


    public void CallRequest(StructureOpticalPenREQ opticalPenREQ, final OpticalPenListener listener) {

        CallApi(MetodName, EndPoint, getSoapObject(opticalPenREQ), new DataProcessListener() {
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

    private void initStructure(String xml, OpticalPenListener listener) {
        StructureAddHameshOpticalPenRES addHameshOpticalPenRES = xmlToObject.DeserializationGsonXml(xml, StructureAddHameshOpticalPenRES.class);
        if (addHameshOpticalPenRES.getStrErrorMsg() == null || addHameshOpticalPenRES.getStrErrorMsg().isEmpty()) {

            if (addHameshOpticalPenRES.isAddHameshOpticalPenResult()) {
                listener.onSuccess();
            } else {
                listener.onFailed("" + addHameshOpticalPenRES.getStrErrorMsg());
            }

        } else {
            listener.onFailed("" + addHameshOpticalPenRES.getStrErrorMsg());
        }
    }

    private SoapObject getSoapObject(StructureOpticalPenREQ opticalPenREQ) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("bfile", opticalPenREQ.getBfile());
        soapObject.addProperty("strExtention", opticalPenREQ.getStrExtention());
        soapObject.addProperty("ETC", opticalPenREQ.getETC());
        soapObject.addProperty("EC", opticalPenREQ.getEC());
        soapObject.addProperty("hiddenHamesh", false);
        soapObject.addProperty("Hameshtitle", opticalPenREQ.getHameshtitle());

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
