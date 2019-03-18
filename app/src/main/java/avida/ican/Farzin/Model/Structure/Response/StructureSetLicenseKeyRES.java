package avida.ican.Farzin.Model.Structure.Response;

/**
 * Created by AtrasVida on 2019-03-06 at 1:40 PM
 */

public class StructureSetLicenseKeyRES {
    private boolean SetLicenseKeyResult;
    private int iErrorCode;
    private String strErrorMsg;

    public boolean isSetLicenseKeyResult() {
        return SetLicenseKeyResult;
    }

    public void setSetLicenseKeyResult(boolean setLicenseKeyResult) {
        SetLicenseKeyResult = setLicenseKeyResult;
    }

    public int getiErrorCode() {
        return iErrorCode;
    }

    public void setiErrorCode(int iErrorCode) {
        this.iErrorCode = iErrorCode;
    }

    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        this.strErrorMsg = strErrorMsg;
    }
}
