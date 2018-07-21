package avida.ican.Farzin.Model.Structure.Response;

import java.util.List;

/**
 * Created by AtrasVida on 2018-04-18 at 5:24 PM
 */
public class StructureUserAndRoleRowsRES {
    private mGetUserAndRoleListResponse GetUserAndRoleListResult = new mGetUserAndRoleListResponse();
    private String StrErrorMsg;

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
        List<StructureUserAndRoleRES> row;

        public List<StructureUserAndRoleRES> getRowList() {
            return row;
        }

        public void setRow(List<StructureUserAndRoleRES> row) {
            this.row = row;
        }
    }
    //_______________________________</mrows>_______________________________

}
