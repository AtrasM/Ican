package avida.ican.Farzin.Model.Structure.Request.DocumentOpratores;

/**
 * Created by AtrasVida on 2019-07-08 at 2:33 PM
 */

public class StructureResponseREQ {
  private   int ReceiverCode;

    public StructureResponseREQ(int receiverCode) {
        ReceiverCode = receiverCode;
    }

    public int getReceiverCode() {
        return ReceiverCode;
    }

    public void setReceiverCode(int receiverCode) {
        ReceiverCode = receiverCode;
    }
}
