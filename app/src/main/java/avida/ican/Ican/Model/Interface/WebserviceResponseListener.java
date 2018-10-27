package avida.ican.Ican.Model.Interface;

import avida.ican.Ican.Model.Structure.Output.WebServiceResponse;

/**
 * Created by AtrasVida on 2018-03-17 at 1:05 PM
 */

public interface WebserviceResponseListener {
    void WebserviceResponseListener(WebServiceResponse webServiceResponse);

    void NetworkAccessDenied();
}
