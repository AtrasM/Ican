package avida.ican.Ican.Model.Interface;


import java.util.List;

/**
 * Created by AtrasVida on 2019-02-23 at 5:15 PM
 */

public interface ListenerTransportSECall {
    void onSuccess(List list);

    void onFailed(String error);
}
