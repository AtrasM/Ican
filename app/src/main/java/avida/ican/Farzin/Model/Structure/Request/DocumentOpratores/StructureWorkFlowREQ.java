package avida.ican.Farzin.Model.Structure.Request.DocumentOpratores;

/**
 * Created by AtrasVida on 2020-02-05 at 10:46 AM
 */

public class StructureWorkFlowREQ {
    private int ReceiverCode;
    private int response;

    public StructureWorkFlowREQ(int receiverCode, int response) {
        ReceiverCode = receiverCode;
        this.response = response;
    }

    public int getReceiverCode() {
        return ReceiverCode;
    }

    public void setReceiverCode(int receiverCode) {
        ReceiverCode = receiverCode;
    }

    public int getResponse() {
        return response;
    }

    public void setResponse(int response) {
        this.response = response;
    }
}
