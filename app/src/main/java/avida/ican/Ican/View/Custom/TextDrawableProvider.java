package avida.ican.Ican.View.Custom;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import avida.ican.Ican.App;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-06-17 at 10:13 AM
 */

 public class TextDrawableProvider {

    private static ColorGenerator mGenerator;



    public static Drawable getDrawable(String string) {
        mGenerator = ColorGenerator.MATERIAL;
        if(!string.isEmpty()){
            if(string.length()>=2){
                string=string.substring(0,2);
            }else{
                string=string.substring(0,1);
            }

        }

        string=  CustomFunction.convertArabicCharToPersianChar(string);
        return TextDrawable.builder()
                .beginConfig()
                .textColor(Resorse.getColor(R.color.color_White))
                .useFont(getFont())
                .endConfig()
                .buildRound(string, mGenerator.getColor(string));
    }

    private static Typeface getFont() {
        return Typeface.createFromAsset(App.CurentActivity.getAssets(), App.getFontPath());
    }
}
