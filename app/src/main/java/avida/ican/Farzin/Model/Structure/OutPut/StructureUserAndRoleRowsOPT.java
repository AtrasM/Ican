package avida.ican.Farzin.Model.Structure.OutPut;

import java.util.List;

/**
 * Created by AtrasVida on 2018-04-18 at 5:24 PM
 */
public class StructureUserAndRoleRowsOPT {
    mGetUserAndRoleListResponse GetUserAndRoleListResult = new mGetUserAndRoleListResponse();
    String StrErrorMsg;

    public mGetUserAndRoleListResponse getGetUserAndRoleListResult() {
        return GetUserAndRoleListResult;
    }

    public void setGetUserAndRoleListResult(mGetUserAndRoleListResponse getUserAndRoleListResult) {
        GetUserAndRoleListResult = getUserAndRoleListResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }

    //_______________________________<mGetUserAndRoleListResponse>_______________________________
    public class mGetUserAndRoleListResponse {
        mrows rows = new mrows();

        public mrows getRows() {
            return rows;
        }

        public void setRows(mrows rows) {
            this.rows = rows;
        }
    }
    //_______________________________</mGetUserAndRoleListResponse>_______________________________

    //_______________________________<mrows>_______________________________
    public class mrows {
        List<StructureUserAndRoleOPT> row;

        public List<StructureUserAndRoleOPT> getRowList() {
            return row;
        }

        public void setRow(List<StructureUserAndRoleOPT> row) {
            this.row = row;
        }
    }
    //_______________________________</mrows>_______________________________

}
