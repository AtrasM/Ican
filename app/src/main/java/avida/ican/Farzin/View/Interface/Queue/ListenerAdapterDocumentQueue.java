package avida.ican.Farzin.View.Interface.Queue;

import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;

/**
 * Created by AtrasVida on 2019-07-13 at 5:12 PM
 */

public interface ListenerAdapterDocumentQueue {
    void onDeletOperator(int ETC, int EC, DocumentOperatoresTypeEnum documentOpratoresTypeEnum);
    void onDeletItem(int ETC, int EC);
    void onTry(int ETC, int EC);
}
