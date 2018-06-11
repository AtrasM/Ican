package avida.ican.Ican.View.Custom;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import avida.ican.Ican.App;

/**
 * Created by AtrasVida on 2018-03-19 at 12:40 PM
 */

public class Resorse {
    public static   int getColor(int color) {
        return  ContextCompat.getColor(App.getAppContext() ,color);
    }
    public static Drawable getDrawable(int drawable) {
        return  ContextCompat.getDrawable(App.getAppContext() ,drawable);
    }
    public static String getString(int resorseString) {
       return App.CurentActivity.getResources().getString(resorseString);
    }
    public static int getDimens(int resorseDimens) {
       return (int) App.CurentActivity.getResources().getDimension(resorseDimens);
    }

}
