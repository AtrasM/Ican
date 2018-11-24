package avida.ican.Farzin.Model.Structure.Response.Cartable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2018-11-19 at 3:50 PM
 */

public class StructureCartableDocumentActionsRES {
    StructureListOfActionsResultResult GetListOfActionsResult = new StructureListOfActionsResultResult();
    private String StrErrorMsg;

    public StructureListOfActionsResultResult getGetListOfActionsResult() {
        return GetListOfActionsResult;
    }

    public void setGetListOfActionsResult(StructureListOfActionsResultResult getListOfActionsResult) {
        GetListOfActionsResult = getListOfActionsResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }


    //_________*********____________<GetListOfActionsResult>___________********____________
    public class StructureListOfActionsResultResult {
        StructureRows rows = new StructureRows();

        public StructureRows getRows() {
            return rows;
        }

        public void setRows(StructureRows rows) {
            this.rows = rows;
        }
    }


    //_________*********____________<StructureRows>___________********____________
    public class StructureRows {
        List<StructureCartableDocumentActionRES> Action = new ArrayList<>();

        public List<StructureCartableDocumentActionRES> getAction() {
            return Action;
        }

        public void setAction(List<StructureCartableDocumentActionRES> action) {
            Action = action;
        }
    }


}
