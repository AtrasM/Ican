package avida.ican.Farzin.Model.Interface.Message;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageAttachRES;

/**
 * Created by AtrasVida on 2019-05-12 at 11:35 AM
 */

public interface MessageAttachmentListListener {
    void onSuccess(ArrayList<StructureMessageAttachRES> messageList);

    void onFailed(String message);

    void onCancel();

}
