package avida.ican.Ican.View.Custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import org.acra.ACRA;

import avida.ican.Ican.App;

/**
 * Created by AtrasVida on 2018-03-19 at 12:40 PM
 */

public class Resorse {
    public static int getColor(int color) {
        return ContextCompat.getColor(App.getAppContext(), color);
    }

    public static Drawable getDrawable(int drawable) {
        Context context = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //>= API 21
            context = App.getAppContext();
        } else {
            context = App.CurentActivity;
        }
        return ContextCompat.getDrawable(context, drawable);
    }

    @NonNull
    public static String getString(int resorseString) {

        return App.getAppContext().getResources().getString(resorseString);
    }

    @NonNull
    public static String getString(Context context, int resorseString) {
        return context.getResources().getString(resorseString);
    }

    public static int getDimens(int resorseDimens) {
        return (int) Resources.getSystem().getDimension(resorseDimens);
    }

}
