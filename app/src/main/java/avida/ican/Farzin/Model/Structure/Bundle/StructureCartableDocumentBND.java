package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import java.util.ArrayList;

import avida.ican.Ican.Model.Structure.StructureAttach;

/**
 * Created by AtrasVida on 2018-09-23 at 2:24 PM
 */

public class StructureCartableDocumentBND implements Serializable {
    private int actionCode;
    private String actionNAme;
    private boolean isFilter;

    public StructureCartableDocumentBND() {
    }

    public StructureCartableDocumentBND(int actionCode, String actionNAme,boolean isFilter) {
        this.actionCode = actionCode;
        this.actionNAme = actionNAme;
        this.isFilter = isFilter;
    }

    public int getActionCode() {
        return actionCode;
    }

    public void setActionCode(int actionCode) {
        this.actionCode = actionCode;
    }

    public String getActionNAme() {
        return actionNAme;
    }

    public void setActionNAme(String actionNAme) {
        this.actionNAme = actionNAme;
    }

    public boolean isFilter() {
        return isFilter;
    }

    public void setFilter(boolean filter) {
        isFilter = filter;
    }
}
