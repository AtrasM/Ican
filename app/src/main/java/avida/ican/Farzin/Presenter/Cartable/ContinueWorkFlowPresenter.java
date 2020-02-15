package avida.ican.Farzin.Presenter.Cartable;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.ContinueWorkFlowListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureContinueWorkFlowRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.CustomFunction;

/**
 * Created by AtrasVida on 2020-02-02 at 11:50
 */


public class ContinueWorkFlowPresenter {
    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "WorkflowManagment";
    private String MethodName = "ContinueFlow";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "ContinueWorkFlowPresenter";
    private FarzinPrefrences farzinPrefrences;

    public ContinueWorkFlowPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void ContinueWorkFlow(int receiverCode, int response, ContinueWorkFlowListener continueWorkFlowListener) {
        ContinueWorkFlow(getSoapObject(receiverCode, response), continueWorkFlowListener);
    }


    private void ContinueWorkFlow(SoapObject soapObject, final ContinueWorkFlowListener continueWorkFlowListener) {
        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, continueWorkFlowListener);
            }

            @Override
            public void onFailed() {
                continueWorkFlowListener.onFailed("");
            }

            @Override
            public void onCancel() {
                continueWorkFlowListener.onFailed("");
            }
        });

    }

    private void initStructure(String xml, ContinueWorkFlowListener continueWorkFlowListener) {
        Log.i("xml", "CartableDocumen xml= " + xml);
        StructureContinueWorkFlowRES structureContinueWorkFlowRES = xmlToObject.DeserializationSimpleXml(xml, StructureContinueWorkFlowRES.class);
        if (structureContinueWorkFlowRES.isContinueFlowResult()) {
            continueWorkFlowListener.onSuccess();
        } else {
            continueWorkFlowListener.onFailed("" + structureContinueWorkFlowRES.getStrErrorMSG());
        }
    }

    private SoapObject getSoapObject(int receiverCode, int response) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        soapObject.addProperty("receivercode", receiverCode);
        soapObject.addProperty("Response", response);
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

