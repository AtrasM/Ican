package avida.ican.Farzin.Model.Interface.Cartable;


/**
 * Created by AtrasVida on 2018-10-14 at 11:37 PM
 */

public interface ZanjireMadrakQuerySaveListener {
    void onSuccess();

    void onExisting();

    void onFailed(String message);

    void onCancel();

}
