package avida.ican.Ican.View.Dialog;


import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Enum.ToastEnum;

/**
 * Created by AtrasVida on 2018-03-19 at 11:06 AM
 */

public class NoServerAccess {

    public void ShowDialog() {
        BaseActivity.closeKeyboard();
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                App.ShowMessage().ShowToast("server access denid", ToastEnum.TOAST_LONG_TIME);
                App.CurentActivity.recreate();

            }
        });

    }

}
