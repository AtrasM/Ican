package avida.ican.Ican.View.Interface;

/**
 * Created by AtrasVida on 2018-03-17 at 1:05 PM
 */

public interface NetWorkStatusListener {
    void Connected();

    void WatingForNetwork();

    void WatingForConnecting();

    void Syncing();

    void DontSetStatus();
}
