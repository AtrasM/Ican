package avida.ican.Farzin.Model.Interface;


import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;

/**
 * Created by AtrasVida on 2018-04-21 at 11:25 PM
 */

public interface MetaDataSyncListener {
    void onSuccess(MetaDataNameEnum metaDataNameEnum);
    void onFailed(MetaDataNameEnum metaDataNameEnum);
    void onCansel(MetaDataNameEnum metaDataNameEnum);
    void onFinish();
}
