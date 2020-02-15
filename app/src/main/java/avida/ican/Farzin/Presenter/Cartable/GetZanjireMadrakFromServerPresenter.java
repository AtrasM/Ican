package avida.ican.Farzin.Presenter.Cartable;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import org.ksoap2.serialization.SoapObject;

import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakListener;
import avida.ican.Farzin.Model.Interface.DataProcessListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentContentRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.Model.saxHandler.DocumentContentSaxHandler;
import avida.ican.Farzin.Model.saxHandler.ZanjireMadrakSaxHandler;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Interface.WebserviceResponseListener;
import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;
import avida.ican.Ican.Model.WebService;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-10-14 at 11:16 AM
 */


public class GetZanjireMadrakFromServerPresenter {

    private String NameSpace = "http://ICAN.ir/Farzin/WebServices/";
    private String EndPoint = "eFormManagment";
    private String MethodName = "GetFileDependency";
    private ChangeXml changeXml = new ChangeXml();
    private XmlToObject xmlToObject = new XmlToObject();
    private String Tag = "GetZanjireMadrakFromServerPresenter";
    private FarzinPrefrences farzinPrefrences;
    private AsyncTask<Void, Void, Void> task;

    public GetZanjireMadrakFromServerPresenter() {
        farzinPrefrences = getFarzinPrefrences();
    }

    public void GetZanjireMadrakList(int EntityTypeCode, int EntityCode, ZanjireMadrakListener zanjireMadrakListener) {
        CallRequest(getSoapObject(EntityTypeCode, EntityCode), zanjireMadrakListener);
    }

    private void CallRequest(SoapObject soapObject, final ZanjireMadrakListener zanjireMadrakListener) {

        CallApi(MethodName, EndPoint, soapObject, new DataProcessListener() {
            @Override
            public void onSuccess(String Xml) {
                initStructure(Xml, zanjireMadrakListener);
            }

            @Override
            public void onFailed() {
                zanjireMadrakListener.onFailed("");
            }

            @Override
            public void onCancel() {
                zanjireMadrakListener.onCancel();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void initStructure(final String data, final ZanjireMadrakListener zanjireMadrakListener) {
        final StructureZanjireMadrakListRES[] structureZanjireMadrakListRES = new StructureZanjireMadrakListRES[1];
        task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (data.contains(App.getResponsePath())) {
                        ZanjireMadrakSaxHandler zanjireMadrakSaxHandler = xmlToObject.parseXmlWithSax(data, new ZanjireMadrakSaxHandler());
                        structureZanjireMadrakListRES[0] = zanjireMadrakSaxHandler.getObject();
                        sleep();
                    } else {
                        String xml = data.replaceAll("xsi:type=\"FarzinFile\"", "");
                        structureZanjireMadrakListRES[0] = xmlToObject.DeserializationSimpleXml(xml, StructureZanjireMadrakListRES.class);
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    if (structureZanjireMadrakListRES[0].getGetFileDependencyResult() == null) {
                        if (structureZanjireMadrakListRES[0].getStrErrorMsg() == null || structureZanjireMadrakListRES[0].getStrErrorMsg().isEmpty()) {
                            zanjireMadrakListener.onSuccess(new StructureZanjireMadrakRES());
                        } else {
                            zanjireMadrakListener.onFailed("");
                        }
                    } else {
                        StructureZanjireMadrakRES structureZanjireMadrakRES = structureZanjireMadrakListRES[0].getGetFileDependencyResult();
                        // changeXml.charDecoder(structureMessageList.get())
                        zanjireMadrakListener.onSuccess(structureZanjireMadrakRES);
                    }

                } catch (Exception e) {
                    zanjireMadrakListener.onFailed("");

                    e.printStackTrace();
                }

            }
        };

        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void sleep() {
        try {
            Thread.sleep(TimeValue.SecondsInMilli() * 15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private SoapObject getSoapObject(int EntityTypeCode, int EntityCode) {
        SoapObject soapObject = new SoapObject(NameSpace, MethodName);
        //SoapObject Filter = new SoapObject(NameSpace, "filter");
        soapObject.addProperty("ETC", EntityTypeCode);
        soapObject.addProperty("EC", EntityCode);

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

