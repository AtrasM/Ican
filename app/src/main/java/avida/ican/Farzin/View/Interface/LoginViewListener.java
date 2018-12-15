package avida.ican.Farzin.View.Interface;

/**
 * Created by AtrasVida on 2018-03-17 at 2:23 PM
 */

public interface LoginViewListener {
    void onSuccess();

    void onAccessDenied();

    void onFailed(String error);
}
