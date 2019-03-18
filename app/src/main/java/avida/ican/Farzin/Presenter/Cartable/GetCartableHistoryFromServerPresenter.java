package avida.ican.Farzin.Presenter.Cartable;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryListListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;

/**
 * Created by AtrasVida on 2018-10-06 at 11:43 AM
 */


public class GetCartableHistoryFromServerPresenter {

    private String strSimpleDateFormat = "";
    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MetodName = "GetHistoryList";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetCartableHistoryFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;

    public GetCartableHistoryFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetHistortList(int EntityTypeCode, int EntityCode, CartableHistoryListListener cartableHistoryListListener) {
        CallRequest(getSoapObject(EntityTypeCode, EntityCode), cartableHistoryListListener);
    }

    private void CallRequest(SoapObject soapObject, final CartableHistoryListListener cartableHistoryListListener) {

        CallApi(MetodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, cartableHistoryListListener);
            }

            @Override
            public void onFailed() {
                cartableHistoryListListener.onFailed("");
            }

            @Override
            public void onCancel() {
                cartableHistoryListListener.onCancel();
            }
        });

    }

    private void initStructure(String xml, CartableHistoryListListener cartableHistoryListListener) {
        StructureHistoryListRES structureHistoryListRES = xmlToObject.DeserializationGsonXml(xml, StructureHistoryListRES.class);
        //if (structureHistoryListRES.getStrErrorMsg() == null || structureHistoryListRES.getStrErrorMsg().isEmpty()) {

        if (structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph().size() <= 0) {
            cartableHistoryListListener.onSuccess(new ArrayList<StructureGraphRES>(), "");
        } else {
            ArrayList<StructureGraphRES> structureGraphRES = new ArrayList<>(structureHistoryListRES.getGetHistoryListResult().getGraphs().getGraph());
            // changeXml.CharDecoder(structureMessageList.get())
            cartableHistoryListListener.onSuccess(structureGraphRES, xml);
        }
    /*    } else {
        cartableHistoryListListener.onFailed("" + structureHistoryListRES.getStrErrorMsg());

    }*/
    }


    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MetodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);

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

