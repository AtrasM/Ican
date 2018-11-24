package avida.ican.Ican;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.orhanobut.dialogplus.DialogPlus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import avida.ican.Ican.View.ActivityMain;
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

public abstract class BaseActivity extends AppCompatActivity {
    public static DialogPlus dialog = null;
    public static DialogPlus dialogMataDataSync = null;
    public static AudioRecorder audioRecorder;
    public static FilePicker filePicker;
    public static MediaPicker mediaPicker;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        App.CurentActivity = this;
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

        setContentView(getLayoutResource());
        ButterKnife.bind(this);
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
                    closeKeboard();
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


    public static void closeKeboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) App.CurentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(App.CurentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean Finish(Activity activity) {
        if (App.isLoading) return false;
        if (!App.canBack) return false;
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        App.activityStacks.values().remove(activity.getClass().getSimpleName());
        return true;
    }

    public void CloseActivitys(ListenerCloseActivitys listenerCloseActivitys) {

        if (App.activityStacks == null) {
            listenerCloseActivitys.onCancel();
        } else {
            App.canBack = false;
            Iterator mIterator = App.activityStacks.keySet().iterator();
            while (mIterator.hasNext()) {
                String key = (String) mIterator.next();
                Activity activity = App.activityStacks.get(key).lastElement();
                if (!activity.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && !activity.getClass().getSimpleName().equals(ActivityMain.class.getSimpleName())) {
                    activity.finish();
                    App.activityStacks.values().remove(key);
                }

            }
            listenerCloseActivitys.onFinish();
            App.canBack = true;
        }

    }

    public static void goToActivity(Class<?> cls) {

        Intent intent = new Intent(App.CurentActivity, cls);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.CurentActivity.startActivity(intent);
        App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

    }

    public static void goToActivity(Intent intent) {
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
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
    public static void goToActivityForResult(Intent intent,int requestCode) {
        App.CurentActivity.startActivityForResult(intent, requestCode);
        App.CurentActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static Activity getActivityFromStackMap(String Tag) {
        if (App.activityStacks.get(Tag).size() > 0) {
            return App.activityStacks.get(Tag).lastElement();
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
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RequestCode.AudioRecordRequestCode.getValue()) {
            audioRecorder.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (requestCode == RequestCode.FilePickerRequestCode.getValue()) {
            filePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

