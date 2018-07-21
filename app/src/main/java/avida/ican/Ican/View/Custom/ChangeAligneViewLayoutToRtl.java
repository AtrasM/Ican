package avida.ican.Ican.View.Custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import java.text.Bidi;
import java.util.Locale;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.ToastEnum;


/**
 * Created by AtrasVida on 2018-04-03 at 9:55 AM
 */

public class ChangeAligneViewLayoutToRtl extends AsyncTask<Void, Void, Void> {
    private String app_locale = "fa";
    @SuppressLint("StaticFieldLeak")
    private Activity context;


    public ChangeAligneViewLayoutToRtl(Activity context) {

        this.context = context;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        Locale locale = new Locale(app_locale);
        Locale.setDefault(locale);

        //Configuration to query the current layout direction.
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
        Bidi bidi = new Bidi(app_locale,
                Bidi.DIRECTION_DEFAULT_RIGHT_TO_LEFT);
        bidi.isRightToLeft();
        return null;
    }

}
