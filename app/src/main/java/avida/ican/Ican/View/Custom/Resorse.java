package avida.ican.Ican.View.Custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import avida.ican.Ican.App;

/**
 * Created by AtrasVida on 2018-03-19 at 12:40 PM
 */

public class Resorse {
    static int getColor(int color) {
        return ContextCompat.getColor(App.getAppContext(), color);
    }

    public static Drawable getDrawable(int drawable) {
        return ContextCompat.getDrawable(App.getAppContext(), drawable);
    }

    @NonNull
    public static String getString(int resorseString) {
        return App.CurentActivity.getResources().getString(resorseString);
    }

    @NonNull
    public static String getString(Context context, int resorseString) {
        return context.getResources().getString(resorseString);
    }

    public static int getDimens(int resorseDimens) {
        return (int) Resources.getSystem().getDimension(resorseDimens);
    }

}
