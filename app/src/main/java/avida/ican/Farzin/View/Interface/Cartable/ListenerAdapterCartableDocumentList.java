package avida.ican.Farzin.View.Interface.Cartable;

import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.View.Enum.CartableActionsEnum;

/**
 * Created by AtrasVida on 2018-05-15 at 3:56 PM
 */

public interface ListenerAdapterCartableDocumentList {
    void onConfirmation();

    void onLoadData();

    void onClick(StructureInboxDocumentDB structureInboxDocumentDB);

    void onAction(StructureInboxDocumentDB structureInboxDocumentDB,CartableActionsEnum cartableActionsEnum);
}
