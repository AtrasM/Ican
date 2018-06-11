package avida.ican.Ican.View.Custom;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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


    public void ShowToast(final String msg, final ToastEnum customTime) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (customTime == ToastEnum.TOAST_LONG_TIME) {
                    Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_LONG).show();
                } else if (customTime == ToastEnum.TOAST_SHORT_TIME) {
                    Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void ShowToast(final String msg, final int customTime) {
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(App.getAppContext(), msg, customTime).show();
            }
        });

    }


    public void ShowSnackBar(String message, final SnackBarEnum snackBarEnum) {
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
        }.execute();


    }

    private void mShowSnackBar(Snackbar snackbar, final SnackBarEnum snackBarEnum) {

        final Snackbar finalSnackbar = snackbar;
        App.CurentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
    }


}
