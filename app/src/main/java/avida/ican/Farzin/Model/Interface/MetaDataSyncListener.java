package avida.ican.Farzin.Model.Interface;


import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;

/**
 * Created by AtrasVida on 2018-04-21 at 11:25 PM
 */

public interface MetaDataSyncListener {
    void onSuccess(DataSyncingNameEnum dataSyncingNameEnum);
    void onFailed(DataSyncingNameEnum dataSyncingNameEnum);
    void onCancel(DataSyncingNameEnum dataSyncingNameEnum);
    void onFinish();
}
