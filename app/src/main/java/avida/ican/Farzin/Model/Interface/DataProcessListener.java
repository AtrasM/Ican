package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2018-04-21 at 11:25 PM
 */

public interface DataProcessListener {
    void onSuccess(String Xml);
    void onFailed();
    void onCansel();
}
