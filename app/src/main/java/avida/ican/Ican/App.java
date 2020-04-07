package avida.ican.Ican;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;
import androidx.fragment.app.Fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rahul.media.model.Define;
import com.squareup.otto.Bus;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.annotation.AcraCore;
import org.acra.config.CoreConfigurationBuilder;
import org.acra.config.HttpSenderConfigurationBuilder;
import org.acra.data.StringFormat;
import org.acra.sender.HttpSender;

import java.util.HashMap;
import java.util.Stack;

import avida.ican.BuildConfig;
import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.FarzinDatabaseHelper;
import avida.ican.Farzin.Model.Interface.JobServiceCartableSchedulerListener;
import avida.ican.Farzin.Model.Interface.JobServiceMessageSchedulerListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Fragment.FragmentCartable;
import avida.ican.Farzin.View.Fragment.Message.FragmentMessageList;
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.NetWorkStatusListener;
import avida.ican.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by AtrasVida on 96-11-21 at 5:31 PM
 */

@AcraCore(buildConfigClass = BuildConfig.class)
public class App extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static App appInstance;
    @SuppressLint("StaticFieldLeak")
    public static Activity CurentActivity;
    public static boolean dialogIsShow = false;
    public static boolean canBack = true;
    public static boolean isLoading = false;
    public static boolean isTestMod = false;
    public static boolean isDocumentConfirm = false;
    public static NetWorkStatusListener netWorkStatusListener;
    public static NetworkStatus networkStatus = NetworkStatus.NoAction;
    private static Bus sBus;
    private static CurentProject curentProject;

    @SuppressLint("StaticFieldLeak")
    public static FragmentMessageList fragmentMessageList = null;
    @SuppressLint("StaticFieldLeak")
    public static FragmentCartable fragmentCartable = null;
    public static HashMap<String, Stack<Fragment>> fragmentStacks;
    public static HashMap<String, Stack<Activity>> activityStacks;
    @SuppressLint("StaticFieldLeak")
    private static FarzinBroadcastReceiver farzinBroadCastReceiver;
    @SuppressLint("StaticFieldLeak")
    public static Activity activity;
    public static int curentDocumentShowing = -1;
    @SuppressLint("StaticFieldLeak")
    public static Context serviceContext;
    //public static JobServiceCartableSchedulerListener jobServiceCartableSchedulerListener;
    //public static JobServiceMessageSchedulerListener jobServiceMessageSchedulerListener;
    public static boolean needToReGetMessageList = false;
    public static boolean needToReGetCartableDocumentList = false;
    public static ChatRoomTypeEnum hasChatRoomChanged = ChatRoomTypeEnum.NoType;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        initAcra();
        Define.MEDIA_PROVIDER = getString(R.string.IMAGE_PROVIDER);
        initializeFont();
        sBus = new Bus();
    }

    private void initAcra() {
        FarzinPrefrences farzinPrefrences = new FarzinPrefrences().init();
        String serverUrl = farzinPrefrences.getServerUrl();
        String userId = farzinPrefrences.getUserIDToken();
        String userRoleId = farzinPrefrences.getRoleIDToken();

        // The following line triggers the initialization of ACRA
        CoreConfigurationBuilder builder = new CoreConfigurationBuilder(this);
        builder.setBuildConfigClass(BuildConfig.class).setReportFormat(StringFormat.JSON);
        builder.setReportSendSuccessToast(Resorse.getString(R.string.acra_report_success_send))
                .setReportContent(
                        ReportField.ANDROID_VERSION,
                        ReportField.APP_VERSION_CODE,
                        ReportField.APP_VERSION_NAME,
                        ReportField.BRAND,
                        ReportField.FILE_PATH,
                        ReportField.PACKAGE_NAME,
                        ReportField.PHONE_MODEL,
                        ReportField.REPORT_ID,
                        ReportField.STACK_TRACE,
                        ReportField.USER_CRASH_DATE)
                .setResReportSendFailureToast(R.string.acra_report_failed_send);
        builder.getPluginConfigurationBuilder(HttpSenderConfigurationBuilder.class)
                .setUri(serverUrl + "/FarzinSoft/Common/IMSE.aspx?ICANMobileError=1&U=" + userId + "&R=" + userRoleId)
                .setHttpMethod(HttpSender.Method.POST)
                .setEnabled(true);
        ACRA.init(this, builder);
    }

    public static FarzinBroadcastReceiver initBroadcastReceiver() {
        farzinBroadCastReceiver = new FarzinBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        String BROADCAST = "avida.ican.android.action.broadcast";
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(BROADCAST);
        getAppContext().registerReceiver(farzinBroadCastReceiver, intentFilter);
        Intent intent = new Intent(BROADCAST);
        getAppContext().sendBroadcast(intent);

        setFarzinBroadCastReceiver(farzinBroadCastReceiver);
        CustomLogger.setLog("***------- initBroadcastReceiver -------***");
        return farzinBroadCastReceiver;
    }

    public static Bus bus() {
        return sBus;
    }

    /**
     * note: after init font should add this code in base activity.
     *
     * @Override protected void attachBaseContext(Context newBase) {
     * super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
     * }
     */
    private void initializeFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getFontPath())
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Context getAppContext() {
        return appInstance.getApplicationContext();
    }

    public static void setServiceContext(Context context) {
        serviceContext = context;
    }

    public static Context getServiceContext() {
        return serviceContext;
    }

    //_________*********************_________
    public static String getDefaultPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/ICANApps/OfficeAutomation";
    }

    public static String getDefaultTempPath() {
        return getDefaultPath() + "/temp";
    }

    public static String getResponsePath() {
        return getDefaultTempPath() + "/Response/";
    }

    public static String getFilePath() {
        return getDefaultPath() + "/Data";
    }

    public static String getResponseFileName() {
        return "responseData.xml";
    }
    //_________*********************_________

    public static String getFontPath() {
        return "font/sahel.ttf";
    }

    public static DisplayImageOptions getImageOption() {
        return new DisplayImageOptions.Builder().cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_warning)
                .showImageForEmptyUri(R.drawable.ic_warning)
                .showImageOnLoading(R.drawable.ic_photo)
                .showImageOnFail(R.drawable.ic_warning).build();
    }

    public static Message ShowMessage() {
        return Message.getInstance();
    }


    public static Handler getHandler() {
        return new Handler();
    }

    public static Handler getHandlerMainThread() {
        return new Handler(App.getAppContext().getMainLooper());
    }

    public static CurentProject getCurentProject() {
        return curentProject;
    }

    public static void setCurentProject(CurentProject curentProject) {
        App.curentProject = curentProject;
    }

    public static void setFarzinBroadCastReceiver(FarzinBroadcastReceiver farzinBroadCastReceiver) {
        App.farzinBroadCastReceiver = farzinBroadCastReceiver;
    }

    public static FarzinBroadcastReceiver getFarzinBroadCastReceiver() {
        return App.farzinBroadCastReceiver;
    }

    public static FarzinDatabaseHelper getFarzinDatabaseHelper() {
        return FarzinDatabaseHelper.getInstance();
    }

    public static String getAppVersionName() {
        PackageInfo pinfo = null;
        try {
            pinfo = App.getAppContext().getPackageManager().getPackageInfo(App.getAppContext().getPackageName(), 0);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
            return "";
        }

    }

    public static int getAppVersionNumber() {
        PackageInfo pinfo = null;
        try {
            pinfo = App.getAppContext().getPackageManager().getPackageInfo(App.getAppContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
        return pinfo.versionCode;
    }

    public static ImageLoader getImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getAppContext()));
        return imageLoader;
    }


}
