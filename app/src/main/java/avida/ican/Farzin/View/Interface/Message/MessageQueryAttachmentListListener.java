package avida.ican.Farzin.View.Interface.Message;


import java.util.ArrayList;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;

/**
 * Created by AtrasVida on 2019-05-19 at 12:32 PM
 */

public interface MessageQueryAttachmentListListener {
    void newData(ArrayList<StructureMessageFileDB> structureMessageFilesDB);

    void noData();

    void onFailed(String message);

    void onCancel();

}
