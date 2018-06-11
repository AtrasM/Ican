package avida.ican.Ican.View.Enum;

import android.app.Activity;
import android.content.res.Resources;

import avida.ican.R;

/**
 * Created by AtrasVida on 2018-04-08 at 9:50 AM
 */

public enum NetworkStatus {
    WatingForNetwork(R.string.WatingForNetwork),
    WatingForConnecting(R.string.WatingForConnecting),
    Syncing(R.string.Syncing),
    Connected(R.string.Connected),
    NoAction(R.string.NoAction);
    private int mResId = -1;

    private NetworkStatus(int resId) {
        mResId = resId;
    }

    public String toLocalizedString(Activity context) {
        if (-1 != mResId) return (context.getResources().getString(mResId));
        return (this.toString());
    }

}
