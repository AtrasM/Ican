package avida.ican.Ican;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Farzin.View.FarzinActivityMain;
import avida.ican.Ican.View.Custom.AudioRecorder;
import avida.ican.Ican.View.Custom.ChangeAligneViewLayoutToRtl;
import avida.ican.Ican.View.Custom.FilePicker;
import avida.ican.Ican.View.Custom.MediaPicker;
import avida.ican.Ican.View.Enum.RequestCode;
import avida.ican.Ican.View.Interface.ListenerCloseActivitys;
import avida.ican.R;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by AtrasVida on 96-11-21 at 5:31 PM
 */
@SuppressLint("StaticFieldLeak")
public abstract class BaseActivity extends AppCompatActivity {

    public static DialogPlus dialog = null;
    public static DialogDataSyncing dialogDataSyncing = null;
    public static AudioRecorder audioRecorder;
    public static FilePicker filePicker;
    public static MediaPicker mediaPicker;
    public static Activity baseActivity;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        App.CurentActivity = this;
        baseActivity = this;
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.CurentActivity = this;
        baseActivity = this;
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        if (App.activityStacks != null) {
            String tag = App.CurentActivity.getClass().getSimpleName();
            if (App.activityStacks.get(tag) == null) {
                App.activityStacks.put(tag, new Stack<Activity>());
                App.activityStacks.get(tag).push(App.CurentActivity);
            }
        } else {
            App.activityStacks = new HashMap<>();
            String tag = App.CurentActivity.getClass().getSimpleName();
            App.activityStacks.put(tag, new Stack<Activity>());
            App.activityStacks.get(tag).push(App.CurentActivity);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        new ChangeAligneViewLayoutToRtl(this).execute();
    }

    private void CheckViewFocuse(View view) {
        /**
         * Example:  view=getWindow().getDecorView().findViewById(android.R.id.content);
         CheckViewFocuse(view);
         */
        if (view == null) return;
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    closeKeyboard();
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                CheckViewFocuse(innerView);
            }
        }
    }

    private void hideStatuseBar() {
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        // getSupportActionBar().hide();
    }


    public static void closeKeyboard() {
        App.getHandlerMainThread().post(() -> {
            try {
                InputMethodManager inputManager = (InputMethodManager) App.CurentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(App.CurentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void openKeyboard() {
        App.getHandlerMainThread().post(() -> {
            try {
                InputMethodManager inputManager = (InputMethodManager) App.CurentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean Finish(Activity activity) {
        closeKeyboard();
        if (App.isLoading) return false;
        if (!App.canBack) return false;
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        try {
            App.activityStacks.keySet().remove(activity.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void CloseActivitys(boolean onlyTheHomepageShouldBeOpen, ListenerCloseActivitys listenerCloseActivitys) {
        if (App.activityStacks == null || App.activityStacks.size() <= 1) {
            listenerCloseActivitys.onCancel();
        } else {
            App.canBack = false;
            HashMap<String, Stack<Activity>> tempActivityStacks = new HashMap<>();

            for (Iterator<String> iterator = App.activityStacks.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                if (key != null && !key.isEmpty()) {
                    Activity activity = App.activityStacks.get(key).lastElement();
                    if (!activity.getClass().getSimpleName().equals(baseActivity.getClass().getSimpleName())) {
                        if (!activity.getClass().getSimpleName().equals(FarzinActivityMain.class.getSimpleName())) {
                            activity.finish();
                        } else {
                            tempActivityStacks.put(key, new Stack<>());
                            tempActivityStacks.get(key).push(activity);
                        }
                    } else {
                        if (onlyTheHomepageShouldBeOpen) {
                            if (!activity.getClass().getSimpleName().equals(FarzinActivityMain.class.getSimpleName())) {
                                activity.finish();
                            } else {
                                tempActivityStacks.put(key, new Stack<>());
                                tempActivityStacks.get(key).push(activity);
                            }
                        } else {
                            tempActivityStacks.put(key, new Stack<>());
                            tempActivityStacks.get(key).push(activity);
                        }
                    }
                }
            }
            App.activityStacks.clear();
            App.activityStacks = tempActivityStacks;
            App.canBack = true;
            App.isLoading = false;
            listenerCloseActivitys.onFinish();
        }

    }

    public static void goToActivity(Class<?> cls) {
        try {
            Intent intent = new Intent(App.CurentActivity, cls);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.CurentActivity.startActivity(intent);
            //App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToActivity(Intent intent) {
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.CurentActivity.startActivity(intent);
            App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void goToActivityForResult(Intent intent) {
        App.CurentActivity.startActivityForResult(intent, 1);
        App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void goToActivityForResult(Intent intent, int requestCode) {
        App.CurentActivity.startActivityForResult(intent, requestCode);
        App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static Activity getActivityFromStackMap(String Tag) {
        if (App.activityStacks != null) {
            if (App.activityStacks.get(Tag) != null && App.activityStacks.get(Tag).size() > 0) {
                return App.activityStacks.get(Tag).lastElement();
            }
        }

        return null;
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.AudioRecordRequestCode.getValue()) {
            audioRecorder.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RequestCode.MediaPickerRequestCode.getValue()) {
            mediaPicker.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RequestCode.MediaPickerRequestCode.getValue()) {
            mediaPicker.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.AudioRecordRequestCode.getValue()) {
            if (audioRecorder != null) {
                audioRecorder.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else if (requestCode == RequestCode.FilePickerRequestCode.getValue()) {
            if (audioRecorder != null) {
                filePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } else if (requestCode == RequestCode.WRITEEXTERNALSTORAGE.getValue()) {
            if (audioRecorder != null) {
                filePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Finish(App.CurentActivity);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    protected abstract int getLayoutResource();
}

