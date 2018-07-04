package avida.ican.Farzin.Model.Structure.OutPut;

import java.util.List;

/**
 * Created by AtrasVida on 2018-07-03 at 9:36 AM
 */

public class StructureMessageListResultOP {
    private StructureMessageList GetRecieveMessageListResult=new StructureMessageList();
    private StructureMessageList GetSentMessageListResult;
    private String StrErrorMsg;

    public StructureMessageList getGetRecieveMessageListResult() {
        return GetRecieveMessageListResult;
    }

    public void setGetRecieveMessageListResult(StructureMessageList getRecieveMessageListResult) {
        GetRecieveMessageListResult = getRecieveMessageListResult;
    }

    public StructureMessageList getGetSentMessageListResult() {
        return GetSentMessageListResult;
    }

    public void setGetSentMessageListResult(StructureMessageList getSentMessageListResult) {
        GetSentMessageListResult = getSentMessageListResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }


    //____________________________________________________
    public class StructureMessageList {
        List<StructureMessageListBodyOP> Message;

        public List<StructureMessageListBodyOP> getMessage() {
            return Message;
        }

        public void setMessage(List<StructureMessageListBodyOP> message) {
            Message = message;
        }
    }
}
