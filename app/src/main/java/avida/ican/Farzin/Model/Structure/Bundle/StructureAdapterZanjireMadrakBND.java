package avida.ican.Farzin.Model.Structure.Bundle;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.View.Enum.StructureAdapterZanjireMadrakEnum;

/**
 * Created by AtrasVida on 2019-08-04 at 12:37 PM
 */

public class StructureAdapterZanjireMadrakBND {
    private StructureZanjireMadrakFileDB zanjireMadrakFileDB;
    private StructureZanjireMadrakEntityDB zanjireMadrakEntityDB;
    private StructureZanjireMadrakHeaderBND zanjireMadrakHeaderBND;
    private StructureAdapterZanjireMadrakEnum zanjireMadrakEnum;
    private boolean isLnMoreVisible;
    private int position;

    public StructureAdapterZanjireMadrakBND(StructureZanjireMadrakFileDB zanjireMadrakFileDB, int position) {
        this.zanjireMadrakFileDB = zanjireMadrakFileDB;
        this.position = position;
        zanjireMadrakEnum = StructureAdapterZanjireMadrakEnum.ZanjireMadrakFile;
    }

    public StructureAdapterZanjireMadrakBND(StructureZanjireMadrakEntityDB zanjireMadrakEntityDB, int position) {
        this.zanjireMadrakEntityDB = zanjireMadrakEntityDB;
        this.position = position;
        zanjireMadrakEnum = StructureAdapterZanjireMadrakEnum.ZanjireMadrakEntity;
    }

    public StructureAdapterZanjireMadrakBND(StructureZanjireMadrakHeaderBND zanjireMadrakHeaderBND) {
        this.zanjireMadrakHeaderBND = zanjireMadrakHeaderBND;
        zanjireMadrakEnum = StructureAdapterZanjireMadrakEnum.Header;
    }

    public StructureZanjireMadrakFileDB getZanjireMadrakFileDB() {
        return zanjireMadrakFileDB;
    }

    public void setZanjireMadrakFileDB(StructureZanjireMadrakFileDB zanjireMadrakFileDB) {
        this.zanjireMadrakFileDB = zanjireMadrakFileDB;
    }

    public StructureZanjireMadrakEntityDB getZanjireMadrakEntityDB() {
        return zanjireMadrakEntityDB;
    }

    public void setZanjireMadrakEntityDB(StructureZanjireMadrakEntityDB zanjireMadrakEntityDB) {
        this.zanjireMadrakEntityDB = zanjireMadrakEntityDB;
    }

    public StructureAdapterZanjireMadrakEnum getZanjireMadrakEnum() {
        return zanjireMadrakEnum;
    }

    public void setZanjireMadrakEnum(StructureAdapterZanjireMadrakEnum zanjireMadrakEnum) {
        this.zanjireMadrakEnum = zanjireMadrakEnum;
    }

    public boolean isLnMoreVisible() {
        return isLnMoreVisible;
    }

    public void setLnMoreVisible(boolean lnMoreVisible) {
        isLnMoreVisible = lnMoreVisible;
    }

    public StructureZanjireMadrakHeaderBND getZanjireMadrakHeaderBND() {
        return zanjireMadrakHeaderBND;
    }

    public void setZanjireMadrakHeaderBND(StructureZanjireMadrakHeaderBND zanjireMadrakHeaderBND) {
        this.zanjireMadrakHeaderBND = zanjireMadrakHeaderBND;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
