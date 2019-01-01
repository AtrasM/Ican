package avida.ican.Ican;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.Fragment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rahul.media.model.Define;
import com.squareup.otto.Bus;

import java.util.HashMap;
import java.util.Stack;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.FarzinDatabaseHelper;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Fragment.FragmentCartable;
import avida.ican.Farzin.View.Fragment.Message.FragmentMessageList;
import avida.ican.Ican.View.Custom.Message;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.NetWorkStatusListener;
import avida.ican.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by AtrasVida on 96-11-21 at 5:31 PM
 */

public class App extends MultiDexApplication {
    @SuppressLint("StaticFieldLeak")
    private static App appInstance;
    private static DisplayImageOptions options;
    private static SharedPreferences sharedPreferencesValue;
    private static Message message;
    private static Handler handler;
    @SuppressLint("StaticFieldLeak")
    public static Activity CurentActivity;
    public static boolean dialogIsShow = false;
    public static boolean canBack = true;
    public static boolean isLoading = false;
    public static boolean isTestMod= false;
    public static NetWorkStatusListener netWorkStatusListener;
    public static NetworkStatus networkStatus = NetworkStatus.NoAction;
    private static Bus sBus;
    // public static boolean canRecreatFragment = false;
    private static CurentProject curentProject;
    private static FarzinDatabaseHelper farzinDatabaseHelper;
    public static String fontPath = "font/iran_sans_mobile.ttf";
    @SuppressLint("StaticFieldLeak")
    private static Context serviceContext;
    public static String DEFAULTPATH = Environment.getExternalStorageDirectory() + "/ican_temp/";
    public static FragmentMessageList fragmentMessageList = null;
    public static FragmentCartable fragmentCartable = null;
    public static HashMap<String, Stack<Fragment>> fragmentStacks;
    public static HashMap<String, Stack<Activity>> activityStacks;
    private static FarzinBroadcastReceiver farzinBroadCastReceiver;
    public static Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        Define.MEDIA_PROVIDER = getString(R.string.IMAGE_PROVIDER);
        initializeImageOpteion();
        initializeSharePrefrences();
        initializeFont();
        initDatabase();
        sBus = new Bus();
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
                .setDefaultFontPath(fontPath)
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

    public static DisplayImageOptions getImageOption() {
        return options;
    }


    public static Message ShowMessage() {
        return message = new Message();
    }

    public static Handler getHandler() {
        return handler = new Handler();
    }

    public static Handler getHandlerMainThread() {
        return handler = new Handler(App.getAppContext().getMainLooper());
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

    private void initializeSharePrefrences() {
        sharedPreferencesValue = PreferenceManager.getDefaultSharedPreferences(getAppContext());
    }

    private void initializeImageOpteion() {
       /* .cacheOnDisc(true)*/
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.ic_action_photo)
                .showImageForEmptyUri(R.drawable.ic_action_photo)
                .showImageOnLoading(R.drawable.ic_action_photo)
                .showImageOnFail(R.drawable.ic_action_photo).build();
    }

    private void initDatabase() {
        farzinDatabaseHelper = new FarzinDatabaseHelper(this);
    }

    public static FarzinDatabaseHelper getFarzinDatabaseHelper() {
        return farzinDatabaseHelper;
    }

    public static ImageLoader getImageLoader() {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getAppContext()));
        return imageLoader;
    }


}
