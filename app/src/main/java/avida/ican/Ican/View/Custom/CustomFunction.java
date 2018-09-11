package avida.ican.Ican.View.Custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.View.Enum.ExtensionEnum;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;


/**
 * Created by AtrasVida on 2018-04-11 at 10:37 AM
 */

public class CustomFunction {
    private Activity activity;
    private Context context;
/*    String[] ImagesExtension = {".jpg", ".jpeg", ".png", ".tiff", ".bmp"};
    List<String> ImageExtensionList = Arrays.asList(ImagesExtension);
    String[] VideosExtension = {".mp4", ".mkv", ".avi", ".gif", ".flv"};
    List<String> VideosExtensionList = Arrays.asList(VideosExtension);
    String[] AudiosExtension = {".wav", ".mp3"};
    List<String> AudiosExtensionList = Arrays.asList(AudiosExtension);
    String[] TextsExtension = {".pdf", ".doc", ".txt"};
    List<String> TextsExtensionList = Arrays.asList(AudiosExtension);*/

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

    public ExtensionEnum getExtensionCategory(String fileExtension) {
        fileExtension = fileExtension.replace("waw", "wav");

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getMimeTypeFromExtension(fileExtension.replace(".", ""));
        type=type.substring(0,type.indexOf("/"));
        ExtensionEnum extensionEnum = ExtensionEnum.NONE;

        switch (type) {
            case "image": {
                extensionEnum = ExtensionEnum.IMAGE;
                break;
            }
            case "video": {
                extensionEnum = ExtensionEnum.VIDEO;
                break;
            }
            case "audio": {
                extensionEnum = ExtensionEnum.AUDIO;
                break;
            }
            case "text": {
                extensionEnum = ExtensionEnum.TEXT;
                break;
            }
        }

 /*       if (ImageExtensionList.contains(fileExtension)) {
            extensionEnum = ExtensionEnum.IMAGE;
        } else if (VideosExtensionList.contains(fileExtension)) {
            extensionEnum = ExtensionEnum.VIDEO;
        } else if (AudiosExtensionList.contains(fileExtension)) {
            extensionEnum = ExtensionEnum.AUDIO;
        } else if (TextsExtensionList.contains(fileExtension)) {
            extensionEnum = ExtensionEnum.TEXT;
        }*/
        return extensionEnum;
    }

    private String getIntentType(ExtensionEnum extensionEnum) {

        String type = "*.*";
        switch (extensionEnum) {
            case IMAGE: {
                type = "image/*";
                break;
            }
            case VIDEO: {
                type = "video/*";
                break;
            }
            case AUDIO: {
                type = "audio/*";
                break;
            }
            case TEXT: {
                type = "text/*";
                break;
            }
        }
        return type;
    }

    public File OpenFile(byte[] fileAsBytes, String fileName, String fileExtension) {
        fileExtension = fileExtension.replace("waw", "wav");
        boolean b = new CheckPermission().writeExternalStorage(1, activity);
        File file = null;
        if (b) {
            //String type = getIntentType(getExtensionCategory(fileExtension));
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String type = mime.getMimeTypeFromExtension(fileExtension.replace(".", ""));
            try {
                File dir = new File(App.DEFAULTPATH);
                file = new File(dir, fileName + fileExtension);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(fileAsBytes);
                bos.flush();
                bos.close();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(activity, "avida.ican.fileprovider", file);
                    intent.setDataAndType(contentUri, type);
                } else {
                    intent.setDataAndType(Uri.fromFile(file), type);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                activity.startActivity(intent);

           /*     App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean b1 = file.delete();
                    }
                }, 10000);*/
            } catch (
                    IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    public int getScreenOrientation() {
        return activity.getResources().getConfiguration().orientation;
    }

    /**
     * @param df example: "dd/M/yyyy hh:mm:ss"
     */
    public static String getCurentDateTimeAsStringFormat(String df) {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(df);
        return simpleDateFormat.format(c.getTime());
    }

    public static Date getCurentDateTimeAsDateFormat(String df) {
        Calendar c = Calendar.getInstance();
        Date date = null;// = new Date(c.getTime().toString());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(df);
        String strDate = simpleDateFormat.format(c.getTime());
        try {
            date = simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getCurentDateTime() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public static String MiladyToJalaly(String DateTime) {
        String JalalyDate = "";
        Date d = new Date(DateTime);
      /*  GregorianCalendar calendar=new GregorianCalendar(date.getYear(),);
        Date d=new Date(DateTime);*/
        PersianDate pdate = new PersianDate(d);
        PersianDateFormat pdformater = new PersianDateFormat("Y/m/d H:i:s");
        JalalyDate = pdformater.format(pdate);
        return JalalyDate;
    }


    public void setHtmlText(TextView myTextView, String myText) {
        ChangeXml changeXml = new ChangeXml();
        if (myText.contains("![CDATA[")) {
            myText = changeXml.RemoveTag(myText, "<![CDATA[", "]]>");
        }
        myText = changeXml.CharDecoder(myText);
        UrlImageParser p = new UrlImageParser(myTextView, activity);
        Spanned htmlSpan = Html.fromHtml(myText, p, null);
        myTextView.setText(htmlSpan);
    }

    public void setHtmlText(ExpandableTextView myExTextView, String myText) {
        ChangeXml changeXml = new ChangeXml();
        if (myText.contains("![CDATA[")) {
            myText = changeXml.RemoveTag(myText, "<![CDATA[", "]]>");
        }
        myText = changeXml.CharDecoder(myText);
        UrlImageParser p = new UrlImageParser(myExTextView, activity);
        Spanned htmlSpan = Html.fromHtml(myText, p, null);
        myExTextView.setText(htmlSpan);
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

    public static boolean isNotificationVisible(Context context, int ID, Intent notificationIntent) {
        PendingIntent test = PendingIntent.getActivity(context, ID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }

    public static void DismissNotification(Context context, int ID) {
        Log.i("Notif", "DismissNotification");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mNotificationManager.cancel(ID);
            new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
            Log.i("Notif", "UpdateAllNewMessageStatusToUnreadStatus");
        } catch (Exception e) {
            Log.i("Notif", "error update status");
            e.printStackTrace();
        }
    }

    public static String repeatChar(char c, int length) {
        char[] data = new char[length];
        Arrays.fill(data, c);
        return new String(data);
    }
}
