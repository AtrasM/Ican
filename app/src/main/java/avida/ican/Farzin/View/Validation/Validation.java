package avida.ican.Farzin.View.Validation;

import android.text.TextUtils;
import android.util.Patterns;

/**
 * Created by AtrasVida on 2018-03-17 at 3:54 PM
 */

public class Validation {
    public boolean UserNameLen(String UserName) {
        return TextUtils.isEmpty(UserName);
    }

    public boolean PassWordLen(String PassWord) {
        return PassWord.length() > 4;
    }


    public boolean PassWordContent(String PassWord) {
        return PassWord.indexOf("/") > 0;
    }
    public boolean Url(String Url) {
        return Patterns.WEB_URL.matcher(Url).matches();
    }

}
