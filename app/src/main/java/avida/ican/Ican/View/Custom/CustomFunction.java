package avida.ican.Ican.View.Custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by AtrasVida on 2018-04-11 at 10:37 AM
 */

public class CustomFunction {
    private Activity activity;
    private Context context;

    public CustomFunction() {
    }

    public CustomFunction(Activity activity) {
        this.activity = activity;
    }

    public CustomFunction(Context context) {
        this.context = context;
    }

    public CustomFunction(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }


    public void SetBorderToView(int stroke, int radius, int borderColor, int backGroundColor, View view) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setStroke(stroke, ContextCompat.getColor(activity, borderColor));
        drawable.setCornerRadius(radius);
        drawable.setColor(ContextCompat.getColor(activity, backGroundColor));
        ViewCompat.setBackground(view, drawable);
    }

    /**
     * Example Used: ChengeDrawableColorAndSetToImageView(img,R.drawable.ic_x,R.color.colorPrimary);
     */
    public void ChengeDrawableColorAndSetToImageView(ImageView imageView, int mDrawable, int color) {
        Drawable drawable = ContextCompat.getDrawable(activity, mDrawable);
        assert drawable != null;
        drawable.setColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.SRC_ATOP);
        imageView.setImageDrawable(drawable);
    }


    public void Call(String phoneNumber) {
        //posted_by = "111-333-222-4";

        String uri = "tel:" + phoneNumber.trim();
        //Intent intent = new Intent(Intent.ACTION_CALL);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));
        activity.startActivity(intent);


    }


    /**
     * this function change arabic or persian number Format to Engligh Format
     * Example : Change ۱۲۳ to 123
     * private static final String arabic = "\u06f0\u06f1\u06f2\u06f3\u06f4\u06f5\u06f6\u06f7\u06f8\u06f9";
     */
    public static String arabicToDecimal(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


    public void SendSms(String content) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
        intent.putExtra("sms_body", content);
        activity.startActivity(intent);
    }

    public void InviteChoser(String subject, String content) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            String sAux = "\nLet me recommend you this application\n\n";
            sAux = sAux + content;
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            activity.startActivity(Intent.createChooser(i, "فقط یک بار"));
        } catch (Exception e) {
            //e.toString();
        }
    }


    public void SendEmail(String subject, String content, String mailto) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mailto, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        activity.startActivity(Intent.createChooser(emailIntent, "ارسال ایمیل"));
    }


    public int getScreenOrientation() {
        return activity.getResources().getConfiguration().orientation;
    }

    /**
     * @param df example: "dd/M/yyyy hh:mm:ss"
     */
    public static String getCurentDateTimeAsFormat(String df) {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(df);
        return simpleDateFormat.format(c.getTime());
    }

    public static Date getCurentDateTime() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public void setHtmlText(TextView myTextView, String myText) {
        UrlImageParser p = new UrlImageParser(myTextView, activity);
        Spanned htmlSpan = Html.fromHtml(myText, p, null);
        myTextView.setText(htmlSpan);
    }


    public int getWidthOrHeightColums(int MOD, boolean isWhidth) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = activity.getResources().getDisplayMetrics().density;
        float dpSize;
        if (isWhidth) {
            dpSize = outMetrics.widthPixels / density;
        } else {
            dpSize = outMetrics.heightPixels / density;
        }
        return Math.round(dpSize / MOD);
    }

    /**
     * This method makes a "deep clone" of any Java object it is given.
     */
    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ignore enter First space on edittext
    public static InputFilter ignoreFirstWhiteSpace() {
        return new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        if (dstart == 0)
                            return "";
                    }
                }
                return null;
            }
        };
    }

    public static String convertArabicCharToPersianChar(String input) {
        return input.replaceAll("ك", "ک").replaceAll("ي", "ی");
    }

    @SuppressWarnings("WeakerAccess")
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    @SuppressWarnings("WeakerAccess")
    public static String getFileName(String filePath) {
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    public static String getFileExtention(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public static String AddXmlCData(String data) {
        return "<![CDATA[" + data + "]]>";
    }
}
