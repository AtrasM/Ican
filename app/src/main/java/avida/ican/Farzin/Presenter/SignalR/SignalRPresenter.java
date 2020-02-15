package avida.ican.Farzin.Presenter.SignalR;

import android.content.ComponentName;

import android.content.ServiceConnection;
import android.os.IBinder;

import avida.ican.Farzin.Presenter.Service.SignalR.SignalRService;

public class SignalRPresenter {
    private SignalRService mService;
    private boolean mBound = false;

    public SignalRPresenter() {
    }


    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    public final ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to SignalRService, cast the IBinder and get SignalRService instance
            SignalRService.LocalBinder binder = (SignalRService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
