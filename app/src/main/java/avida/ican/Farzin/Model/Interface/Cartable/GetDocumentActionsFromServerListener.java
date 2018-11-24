package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-11-20 at 3:20 PM
 */

public interface GetDocumentActionsFromServerListener {
    void onSuccess();

    void onFailed(String message);

    void onCancel();
}
