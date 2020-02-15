package avida.ican.Farzin.Model.Interface;


import avida.ican.Farzin.Model.Enum.CartableJobServiceNameEnum;

/**
 * Created by AtrasVida on 2019-11-13 at 12:33 PM
 */

public interface JobServiceCartableSchedulerListener {

    void onSuccess(CartableJobServiceNameEnum jobServiceNameEnum);

    void onFailed(CartableJobServiceNameEnum jobServiceNameEnum);

    void onCancel(CartableJobServiceNameEnum jobServiceNameEnum);

    void onFinish();
}
