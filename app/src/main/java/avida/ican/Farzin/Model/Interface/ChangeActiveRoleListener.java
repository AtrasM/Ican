package avida.ican.Farzin.Model.Interface;


/**
 * Created by AtrasVida on 2019-07-02 at 12:18 PM
 */

public interface ChangeActiveRoleListener {
    void doProcess();
    void onSuccess();
    void onFailed(String error);
}
