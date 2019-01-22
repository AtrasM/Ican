package avida.ican.Ican.Model.Structure.Output;

import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.List;

import avida.ican.Ican.IcanHttpTransportSE;


/**
 * Created by AtrasVida on 2018-03-17 at 12:28 PM
 */

public class WebServiceResponse {
    private IcanHttpTransportSE httpTransportSE;
    private List headerList;
    /*private SoapSerializationEnvelope envelope;*/
    private boolean response=false;

    public IcanHttpTransportSE getHttpTransportSE() {
        return httpTransportSE;
    }

    public void setHttpTransportSE(IcanHttpTransportSE httpTransportSE) {
        this.httpTransportSE = httpTransportSE;
    }

    public List getHeaderList() {
        return headerList;
    }

    public void setHeaderList(List headerList) {
        this.headerList = headerList;
    }

/*    public SoapSerializationEnvelope getEnvelope() {
        return envelope;
    }

    public void setEnvelope(SoapSerializationEnvelope envelope) {
        this.envelope = envelope;
    }*/

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }
}
