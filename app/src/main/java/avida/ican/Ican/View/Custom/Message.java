package avida.ican.Ican.View.Custom;


import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;

/**
 * Created by AtrasVida on 2018-03-13 at 11:50 AM
 */

public class Message {


    private static Message mInstance = null;

    public static Message getInstance() {

        if (mInstance == null) {
            mInstance = new Message();
        }
        return mInstance;
    }

    private Message() {
    }


    public void ShowToast(final String msg, final ToastEnum customTime) {

        App.getHandlerMainThread().post(() -> {
            // if (App.isTestMod) {
            if (customTime == ToastEnum.TOAST_LONG_TIME) {
                Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_LONG).show();
            } else if (customTime == ToastEnum.TOAST_SHORT_TIME) {
                Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show();
            }
            // }

        });

    }

    public void ShowToast(final String msg, final int customTime) {
        App.getHandlerMainThread().post(() -> Toast.makeText(App.getAppContext(), msg, customTime).show());

    }


    public void ShowSnackBar(String message, final SnackBarEnum snackBarEnum) {
        if (App.CurentActivity == null) {
            return;
        }
        View rootView = App.CurentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        //rootView.setBackgroundColor(ContextCompat.getColor(G.currentActivity, R.color.colorBlack));
        Snackbar snackbar = null;

        switch (snackBarEnum) {
            case SNACKBAR_LONG_TIME: {
                snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
                break;
            }
            case SNACKBAR_INDEFINITE: {
                snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE);
                break;
            }
            case SNACKBAR_SHORT_TIME: {
                snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
                break;
            }
        }
        mShowSnackBar(snackbar, snackBarEnum);

    }

    @SuppressLint("StaticFieldLeak")
    public void ShowSnackBar(final ArrayList<String> messageList, final SnackBarEnum snackBarEnum) {
        if (App.CurentActivity == null) {
            return;
        }
        final View rootView = App.CurentActivity.getWindow().getDecorView().findViewById(android.R.id.content);
        //rootView.setBackgroundColor(ContextCompat.getColor(G.currentActivity, R.color.colorBlack));
        final Snackbar[] snackbar = {null};
        int duration = 0;
        switch (snackBarEnum) {
            case SNACKBAR_LONG_TIME: {
                duration = Snackbar.LENGTH_LONG;
                break;
            }
            case SNACKBAR_INDEFINITE: {
                duration = Snackbar.LENGTH_INDEFINITE;
                break;
            }
            case SNACKBAR_SHORT_TIME: {
                duration = Snackbar.LENGTH_SHORT;
                break;
            }
        }
        final int finalDuration = duration;
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < (messageList.size()); i++) {
                    snackbar[0] = Snackbar.make(rootView, messageList.get(i), finalDuration);
                    mShowSnackBar(snackbar[0], snackBarEnum);
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    private void mShowSnackBar(Snackbar snackbar, final SnackBarEnum snackBarEnum) {

        final Snackbar finalSnackbar = snackbar;
        App.getHandlerMainThread().post(() -> {
            if (snackBarEnum == SnackBarEnum.SNACKBAR_INDEFINITE) {
                finalSnackbar.setAction("بستن", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalSnackbar.dismiss();
                    }
                }).show();
            } else {
                finalSnackbar.show();
            }
        });
    }


}
