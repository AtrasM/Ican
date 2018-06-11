package avida.ican.Ican.View.Interface;

import avida.ican.Ican.View.Enum.AttachEnum;

/**
 * Created by AtrasVida on 2018-05-30 at 12:44 PM
 */

public interface ListenerAttach {
    void onSuccess(AttachEnum attachEnum);

    void onCancel();
}
