package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2019-03-06 at 1:45 PM
 */

public class StructureSetLicenseKeyBND implements Serializable {
    private String ApplicationName;
    private String Challenge;
    private String Random;


    public StructureSetLicenseKeyBND() {
    }

    public StructureSetLicenseKeyBND(String applicationName, String challenge, String random) {
        ApplicationName = applicationName;
        Challenge = challenge;
        Random = random;
    }

    public String getApplicationName() {
        return ApplicationName;
    }

    public void setApplicationName(String applicationName) {
        ApplicationName = applicationName;
    }

    public String getChallenge() {
        return Challenge;
    }

    public void setChallenge(String challenge) {
        Challenge = challenge;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }
}
