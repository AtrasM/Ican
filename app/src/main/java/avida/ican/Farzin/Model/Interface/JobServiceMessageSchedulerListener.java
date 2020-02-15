package avida.ican.Farzin.Model.Interface;

import avida.ican.Farzin.Model.Enum.MessageJobServiceNameEnum;

/**
 * Created by AtrasVida on 2019-11-16 at 11:27 AM
 */

public interface JobServiceMessageSchedulerListener {

    void onSuccess(MessageJobServiceNameEnum jobServiceNameEnum);

    void onFailed(MessageJobServiceNameEnum jobServiceNameEnum);

    void onCancel(MessageJobServiceNameEnum jobServiceNameEnum);

    void onFinish();
}
