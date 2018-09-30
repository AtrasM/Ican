package avida.ican.Farzin.Model.Interface.Message;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;

/**
 * Created by AtrasVida on 2018-07-16 at 16:21 PM
 */

public interface MessageListListener {
    void onSuccess(ArrayList<StructureMessageRES> messageList);

    void onFailed(String message);

    void onCancel();

}
