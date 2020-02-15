package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2019-08-18 at 10:38 AM
 */

public interface GetDateTimeListener {
    void onSuccess(String strDateTime);

    void onFailed(String message);

    void onCancel();
}
