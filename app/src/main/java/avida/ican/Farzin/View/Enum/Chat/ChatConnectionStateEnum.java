package avida.ican.Farzin.View.Enum.Chat;


import avida.ican.Ican.App;
import avida.ican.R;

/**
 * Created by AtrasVida on 2020-04-18 at 16:13
 */

public enum ChatConnectionStateEnum {
    Disconnected(R.string.WatingForNetwork),
    Connecting(R.string.WatingForConnecting),
    Syncing(R.string.Syncing),
    Connected(R.string.Connected),
    NoAction(R.string.NoAction);
    private int mResId = -1;

    ChatConnectionStateEnum(int resId) {
        mResId = resId;
    }

    public String getValue() {
        if (-1 != mResId) return (App.getAppContext().getResources().getString(mResId));
        return (this.toString());
    }
}
