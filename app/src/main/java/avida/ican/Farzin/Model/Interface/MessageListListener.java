package avida.ican.Farzin.Model.Interface;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.MessageRES;

/**
 * Created by AtrasVida on 2018-07-16 at 16:21 PM
 */

public interface MessageListListener {
    void onSuccess(ArrayList<MessageRES> messageList);

    void onFailed(String message);

    void onCancel();

}
