package avida.ican.Farzin.Model.Interface.Message;


/**
 * Created by AtrasVida on 2019-05-25 14:43 PM
 */

public interface MessageDataListener {
    void newData();

    void noData();

    void onFailed(String message);

}
