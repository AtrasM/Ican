package avida.ican.Ican.View.Custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;

import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Enum.NotificationChanelEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.ActivityImageViewer;
import avida.ican.Ican.View.Custom.Enum.CompareDateTimeEnum;
import avida.ican.Ican.View.Custom.Enum.CompareTimeEnum;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Dialog.DialogQuestion;
import avida.ican.Ican.View.Enum.ExtensionEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.ListenerQuestion;
import avida.ican.R;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

import static avida.ican.Ican.BaseActivity.goToActivity;
import static java.nio.charset.StandardCharsets.*;


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

    public URI UrlToUri(String url) {
        URI uri = null;
        try {
            new URI(url.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        return uri;
    }

    public Drawable ChengeDrawableColor(int mDrawable, int color) {
        Drawable drawable = ContextCompat.getDrawable(activity, mDrawable);
        assert drawable != null;
        drawable.setColorFilter(ContextCompat.getColor(activity, color), PorterDuff.Mode.SRC_ATOP);
        return drawable;
    }

    public void ChangeBackgroundTintColor(View view, int color) {
        ViewCompat.setBackgroundTintList(view, ContextCompat.getColorStateList(activity, color));
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
     * this function change arabic or persian ic_number Format to Engligh Format
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
        if (fileExtension == null || fileExtension.isEmpty()) {
            return ExtensionEnum.UNNOWN;
        }
        fileExtension = fileExtension.toLowerCase();
        fileExtension = fileExtension.replace("waw", "wav");

        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getMimeTypeFromExtension(fileExtension.replace(".", ""));
        ExtensionEnum extensionEnum = ExtensionEnum.NONE;
        if (type == null) {
            extensionEnum = ExtensionEnum.NONE;
        } else {
            type = type.substring(0, type.indexOf("/"));
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
                case "application": {
                    extensionEnum = ExtensionEnum.APPLICATION;
                    break;
                }
                default: {
                    extensionEnum = ExtensionEnum.UNNOWN;
                    break;
                }
            }

        }

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


    public File OpenFile(StructureAttach structureAttach) {
        if (structureAttach == null || structureAttach.getName() == null || structureAttach.getName().equals("null")) {
            return null;
        }
        String fileName = structureAttach.getName();
        String fileExtension = structureAttach.getFileExtension();
        fileExtension = fileExtension.replace("waw", "wav");
        Log.i("OpenFile", "file path = " + structureAttach.getFilePath());
        byte[] fileAsBytes = new byte[0];
        if (!canDisplayFile(activity, fileExtension)) {
            return null;
        }
        if (structureAttach.getFilePath() != null && !structureAttach.getFilePath().isEmpty()) {
            fileAsBytes = getFileFromStorageAsByte(structureAttach.getFilePath());
        } else {
            if (structureAttach.getFileAsStringBuilder() != null && structureAttach.getFileAsStringBuilder().length() > 0) {
                fileAsBytes = android.util.Base64.decode(structureAttach.getFileAsStringBuilder().toString(), android.util.Base64.NO_WRAP);
            }
        }
        Log.i("OpenFile", "fileAsBytes.length = " + fileAsBytes.length);
        File file = null;
        if (fileAsBytes != null && fileAsBytes.length > 0) {
            if (getExtensionCategory(fileExtension) == ExtensionEnum.IMAGE && !UnSupportMediaFormat(fileExtension)) {
                ActivityImageViewer.byteArray = fileAsBytes;
                goToActivity(ActivityImageViewer.class);
                return file;
            } else {
                boolean b = new CheckPermission().writeExternalStorage(1, activity);

                if (b) {
                    MimeTypeMap mime = MimeTypeMap.getSingleton();
                    String type = mime.getMimeTypeFromExtension(fileExtension.replace(".", ""));

                    try {
                        File dir = new File(App.DEFAULTPATHTEMP);
                        fileName = fileName.replace(".ican", "");
                        if (fileName.contains(fileExtension)) {
                            file = new File(dir, "/" + fileName);
                        } else {
                            file = new File(dir, fileName + fileExtension);
                        }

                        if (!file.exists()) {
                            dir.mkdirs();
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

                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.error_invalid_file), ToastEnum.TOAST_LONG_TIME);
                }
            });

        }
        return file;
    }

    private boolean UnSupportMediaFormat(String fileExtension) {
        String format = ".tiff";
        return format.contains(fileExtension);
    }

    private boolean canDisplayFile(Activity activity, String extention) {
        PackageManager pm = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getMimeTypeFromExtension(extention.replace(".", ""));
        if (type.startsWith("audio")) {
            return true;
        }
        intent.setType(type);
        List list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY | PackageManager.GET_RESOLVED_FILTER);
        if (list.size() > 0) {
            // There is something installed that can VIEW this file)
            return true;
        } else {
        /*ResolveInfo info = pm.resolveActivity(intent, 0);

        if (info != null) {
            // There is something installed that can VIEW this file)
            return true;
        } else {*/
            // Offer to download a viewer here
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.error_can_not_display_file), ToastEnum.TOAST_LONG_TIME);
                }
            });
            return false;
        }
    }


    public String saveXmlToStorage(String data) {
        byte[] fileAsBytes = null;

        String filePath = "";
        try {
            fileAsBytes = data.getBytes("UTF-8");
            filePath = saveXmlToStorage(fileAsBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public String saveXmlToStorage(InputStream is) {
        byte[] fileAsBytes = null;

        String filePath = "";
        try {
            fileAsBytes = IOUtils.toByteArray(is);

            filePath = saveXmlToStorage(fileAsBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath;
    }

    public String saveXmlToStorage(byte[] fileAsBytes) {
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(App.RESPONSEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = dir.getPath() + "/" + getRandomUUID() + App.RESPONSEFILENAME;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            objectOutputStream.writeUTF("@rasVida");
            objectOutputStream.writeUTF("Ican");
            objectOutputStream.writeInt(fileAsBytes.length);
            objectOutputStream.writeUTF("");
            objectOutputStream.write(fileAsBytes);
            objectOutputStream.writeUTF("Ican");
            objectOutputStream.writeUTF("@rasVida");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("saveFile", e.toString());
        } finally {
            try {
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("saveFile", e.toString());
            }
        }
        return filePath;
    }

    public String saveXmlResponseToStorage(String xml) {
        ObjectOutputStream objectOutputStream = null;
        File dir = new File(App.RESPONSEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = dir.getPath() + "/" + getRandomUUID() + App.RESPONSEFILENAME;
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(filePath));
            outputStreamWriter.write(xml);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        return filePath;
    }


    public String saveXmlResponseToStorage(InputStream is) {

        boolean isError = false;
        File dir = new File(App.RESPONSEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = dir.getPath() + "/" + getRandomUUID() + App.RESPONSEFILENAME;
        long megabyte = 1024 * 1024;
        int buferSize = 1024;
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(filePath);
            OutputStreamWriter myOutWriter = null;
            char[] buffer = new char[buferSize]; // or other buffer size
            InputStreamReader reader = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                reader = new InputStreamReader(is, UTF_8);
                myOutWriter = new OutputStreamWriter(fOut, UTF_8);
            } else {
                reader = new InputStreamReader(is, "UTF8");
                myOutWriter = new OutputStreamWriter(fOut, "utf-8");
            }

            int read = 0;
            while ((read = reader.read(buffer)) != -1) {
                String data = "";
                data = new String(buffer, 0, read);
                buffer = new char[buferSize];
                data = new ChangeXml().saxCharEncoder(data);
                myOutWriter.append(data);
            }

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            isError = true;
            Log.e("Exception", "File write failed: " + e.toString());
        } catch (Exception e) {
            isError = true;
            Log.e("Exception", "File write failed: " + e.toString());
            e.printStackTrace();
        }

        if (isError) {
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            File file = new File(filePath);
            file.delete();
            filePath = "";
            Log.e("isError", "error to save file");
        }
        return filePath;
    }

    public String readXmlResponseFromStorage(String filePath) {
        String xmlData = "";
        File file = new File(filePath);

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            /*BufferedReader br = new BufferedReader(new FileReader(file));*/
            BufferedReader br = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), UTF_8));
            } else {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            }
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            xmlData = text.toString();
            file.delete();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            //You'll need to add proper error handling here
        }
        return xmlData;
    }


    public String saveFileToStorage(StringBuilder stringBuilder, String name) {
        String filePath = initFilePath(name);
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(stringBuilder.toString());
            bufferedWriter.close();
            //Log.e("Log", "The file was successfully saved");
        } catch (IOException e) {
            Log.e("Log", "saveFileToStorage File write failed: " + e.toString());
        }
     /*   try {
            FileOutputStream fOut = new FileOutputStream(filePath);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            for (int i = 0; i < stringBuilder.length(); i++) {
                myOutWriter.append(stringBuilder.charAt(i));
            }
            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }*/


        return filePath;
    }

    public String getFileFromStorageAsBase64(String filePath) {
        return new Base64EncodeDecodeFile().EncodeByteArrayToString(getFileFromStorageAsByte(filePath));

    }

    public byte[] getFileFromStorageAsByte(String filePath) {

        File file = new File(filePath);
        byte[] fileAsBytes = new byte[0];
        try {
            fileAsBytes = new Base64EncodeDecodeFile().DecodeBase64ToByte(FileUtils.readFileToByteArray(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileAsBytes;
    }

/*    public byte[] getFileFromStorageAsByte(String filePath) {
        ObjectInputStream objectInputStream = null;
        //String fileAsBase64 = "";
        byte[] fileAsBytes = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(filePath));
            objectInputStream.readUTF();
            objectInputStream.readUTF();
            int fileLength = objectInputStream.readInt();
            fileAsBytes = new byte[fileLength];
            String filename = objectInputStream.readUTF();
            objectInputStream.readFully(fileAsBytes);
            objectInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("readFile", e.toString());
        } finally {
            try {
                if (objectInputStream != null) {
                    objectInputStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Log.e("readFile", e.toString());
            }
        }
        return fileAsBytes;
    }*/

    public String initFilePath(String name) {

        if (name == null || name.isEmpty()) {
            name = "";
        }

        String DirPath = getDirAsString();
        String filePath = DirPath + "/";
        name = deletExtentionAsFileName(name);
        filePath = filePath + name + ".ican";
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            //filePath="";
        }
        return filePath;
    }

    public static String deletExtentionAsFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index > 0) {
            fileName = fileName.substring(0, index);
        }
        return fileName;
    }

    public String getDirAsString() {
        File file = initDir();
        return file.getPath();
    }

    public File getDirAsFile() {
        return initDir();
    }

    private File initDir() {
        Date date = getCurentDateTime();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        String folder = year + "_" + (month + 1) + "_" + day;//+"_"+hour;
        File dir = new File(App.DEFAULTPATH + "/" + folder);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public String setSeparatorWithCommasToNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(number);
    }

    public int getScreenOrientation() {
        return activity.getResources().getConfiguration().orientation;
    }

    public static String getRandomUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    } 
 /*   public static int getRandomUUID() {
        int min = 1;
        int max = 10000;
        Random r = new Random();
        return r.nextInt(max - min) + min;
    }*/

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

    public static CompareTimeEnum compareTimeInMiliWithCurentSystemTime(long timeInMili, long deficultTimeInMili) {
        long curentTime = System.currentTimeMillis();
        long lastTime = timeInMili + deficultTimeInMili;

        CompareTimeEnum compareTimeEnum;
        if (lastTime > curentTime) {
            compareTimeEnum = CompareTimeEnum.isAfter;
        } else if (lastTime < curentTime) {
            compareTimeEnum = CompareTimeEnum.isBefore;
        } else {
            compareTimeEnum = CompareTimeEnum.isEqual;
        }
        return compareTimeEnum;
    }

    public static CompareDateTimeEnum compareDateWithCurentDate(String strLastDate, long delay) {
        try {
            Date lastDate = new Date(strLastDate);
            Date curentDate = getCurentDateTime();
            //int a = curentDate.compareTo(lastDate);
            long dificalt = curentDate.getTime() - lastDate.getTime();
            CompareDateTimeEnum compareDateTimeEnum;
            if (dificalt >= delay) {
                compareDateTimeEnum = CompareDateTimeEnum.isAfter;
            } else {
                compareDateTimeEnum = CompareDateTimeEnum.isBefore;
            }
            return compareDateTimeEnum;
        } catch (Exception e) {
            e.printStackTrace();
            return CompareDateTimeEnum.ErrorFormat;
        }

    }

    public static CompareDateTimeEnum compareDates(String DateTime1, String DateTime2) {
        long a = System.currentTimeMillis();
        Date d1 = new Date(DateTime1);
        Date d2 = new Date(DateTime2);
        CompareDateTimeEnum compareDateTimeEnum;
        if (d2.after(d1)) {
            compareDateTimeEnum = CompareDateTimeEnum.isAfter;
        } else if (d2.before(d1)) {
            compareDateTimeEnum = CompareDateTimeEnum.isBefore;
        } else {
            compareDateTimeEnum = CompareDateTimeEnum.isEqual;
        }
        return compareDateTimeEnum;
    }

    public static Date getCurentDateTime() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public static Date getLastMonthDateTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return c.getTime();//Sat Feb 16 10:25:43 GMT+03:30 2019
    }

    public static String getLastMonthDateTimeAsFormat() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue(), Locale.UK);
        Log.i("DateTime", "getLastMonthDateTimeAsFormat=" + format.format(c.getTime()));
        return format.format(c.getTime());
    }
    public static String convertDateToIso8601Format(int year, int monthOfYear, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year,monthOfYear,dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue(), Locale.UK);
        Log.i("DateTime", "getLastMonthDateTimeAsFormat=" + format.format(c.getTime()));
        return format.format(c.getTime());
    }

    public static String StandardizeTheDateFormat(String date) {
        //String dateDefault = "0001/01/01 00:00:00";
        String dateDefault = "";
        if (date != null && !date.isEmpty()) {
            try {
                date = date.replace("T", " ");
                date = date.replace("-", "/");
                int dotPos = date.indexOf(".");
                if (dotPos > 0) {
                    date = date.substring(0, dotPos);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //return null;
            }
        } else {
            return dateDefault;
        }

        return date;
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
        myText = changeXml.charDecoder(myText);
        UrlImageParser p = new UrlImageParser(myTextView, activity);
        Spanned htmlSpan = Html.fromHtml(myText, p, null);
        myTextView.setText(htmlSpan);
    }

    public void setHtmlText(ExpandableTextView myExTextView, String myText) {
        ChangeXml changeXml = new ChangeXml();
        if (myText.contains("![CDATA[")) {
            myText = changeXml.RemoveTag(myText, "<![CDATA[", "]]>");
        }
        myText = changeXml.saxCharEncoder(myText);
        myText = changeXml.charDecoder(myText);
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

    public int getWidthOrHeightColums(boolean isWhidth) {
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
        return Math.round(dpSize);
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

    public static String getFileDir(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf("/"));
    }

    public static String FileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public static String AddXmlCData(String data) {
        return "<![CDATA[" + data + "]]>";
    }

    public static boolean isNotificationVisible(Context context, int ID, Intent notificationIntent) {
        PendingIntent test = PendingIntent.getActivity(context, ID, notificationIntent, PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }

    public static void DismissNotification(Context context, NotificationChanelEnum chanelEnum) {
        Log.i("Notif", "DismissNotification");
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        try {
            mNotificationManager.cancel(chanelEnum.getValue());
            switch (chanelEnum) {
                case Message: {
                    new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
                    break;
                }
                case Cartable: {
                    new FarzinCartableQuery().UpdateAllNewCartableDocumentStatusToUnreadStatus();
                    break;
                }
            }


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

    public ArrayAdapter<String> getSpinnerAdapter(ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(App.CurentActivity, R.layout.layout_txt_spinner, list);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        return adapter;
    }

    public static byte[] ObjectToByteArray(Object object) {
        // Reference for stream of bytes
        byte[] stream = null;
        // ObjectOutputStream is used to convert a Java object into OutputStream
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            stream = baos.toByteArray();
        } catch (IOException e) {
            // Error in serialization
            e.printStackTrace();
        }
        return stream;
    }


    public <T> String ConvertObjectToString(Object mObj) {
        Gson gson = new Gson();
        try {
            return gson.toJson(mObj);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public <T> T ConvertStringToObject(String data, Class<T> mObj) {
        Gson gson = new Gson();
        try {
            if (data != null && !data.isEmpty()) {
                if (data.substring(0, 1).equals("[") && !isArray(mObj)) {
                    data = data.substring(1, data.length() - 1);
                }
            }
            return (T) gson.fromJson(data, mObj);

        } catch (Exception e) {
            e.printStackTrace();

            return (T) mObj;
        }
    }

    public <T> boolean isArray(Class<T> obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static byte[] BitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return byteArray;
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    public static void deletDirectory(File dirPath) {
        try {
            FileUtils.deleteDirectory(dirPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String reNameFile(String filePath, String newFileName) {
        String filrDir = getFileDir(filePath);
        File from = new File(filrDir, getFileName(filePath));
        File to = new File(filrDir, newFileName);
        boolean rename = from.renameTo(to);
        if (rename) {
            return to.getPath();
        } else {
            return filePath;
        }
    }

    public static void resetApp(final Activity context, final ListenerQuestion mlistenerQuestion) {
        final FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        new DialogQuestion(context).setOnListener(new ListenerQuestion() {
            @Override
            public void onSuccess() {
                farzinPrefrences.clearCatch();
                try {
                    App.getFarzinDatabaseHelper().ClearAllTable();
                    CustomFunction.deletDirectory(new File(App.DEFAULTPATH));
                    mlistenerQuestion.onSuccess();
                } catch (SQLException e) {
                    mlistenerQuestion.onCancel();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                mlistenerQuestion.onCancel();
            }
        }).setTitle(Resorse.getString(R.string.resetApp)).Show();


    }
}
