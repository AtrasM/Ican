package avida.ican.Ican.View.Custom;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import avida.ican.Ican.App;
import avida.ican.Ican.View.Enum.RequestCode;
import avida.ican.Ican.View.Interface.AudioRecorderListener;
import avida.ican.R;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;

/**
 * Created by AtrasVida on 2018-04-10 at 9:30 AM
 */

public class AudioRecorder extends AppCompatActivity {
    /**
     * ِgradle dependency is: implementation 'com.github.adrielcafe:AndroidAudioRecorder:0.3.0'
     */

    private Activity context;
    private static AudioRecorderListener audioRecorderListener;
    private  int color = -1;
    private String filePath = "";
    private boolean autoStart = false;
    private boolean keepDisplayOn = false;
    private AndroidAudioRecorder androidAudioRecorder;
    private int mRequestCode = 0;


    public AudioRecorder(Activity context) {
        this.context = context;
        initDefultValue();
    }

    private void initDefultValue() {
        color = Resorse.getColor(R.color.colorPrimaryDark);
        filePath = App.getDefaultPath() + "/recorded_audio.wav";
        autoStart = false;
        keepDisplayOn = false;
    }

    public void setOnListener(AudioRecorderListener audioRecorderListener) {
        this.audioRecorderListener = audioRecorderListener;
        CheckPermission();
    }


    public AudioRecorder setColor(int colorRes) {
        /**
         * Example R.color.ColorPrimary
         */
        this.color = Resorse.getColor(colorRes);
        return this;
    }

    public AudioRecorder setAutoStart(boolean autoStart) {
        /**
         * Desc: if autoStart is ture when run activity automatic start recording
         */
        this.autoStart = autoStart;
        return this;
    }

    public AudioRecorder setWavFilePath(String filePath) {
        /**
         * Example Environment.getExternalStorageDirectory() + "/recorded_audio.wav"
         */
        this.filePath = filePath;
        return this;
    }

    public AudioRecorder setKeepDisplayOn(boolean keepDisplayOn) {
        this.keepDisplayOn = keepDisplayOn;
        return this;
    }

    private void Record() {
        androidAudioRecorder = AndroidAudioRecorder.with(context);
        // Required
        androidAudioRecorder.setFilePath(filePath)
                .setColor(color)
                .setRequestCode(mRequestCode)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(autoStart)
                .setKeepDisplayOn(keepDisplayOn)
                .record();

    }

    private void CheckPermission() {
        mRequestCode = RequestCode.AudioRecordRequestCode.getValue();
        CheckPermission checkPermission = new CheckPermission();
        if (checkPermission.recordAudio(mRequestCode, context)) {
            if (checkPermission.writeExternalStorage(mRequestCode, context)) {
                Record();
            }
        }
    }

    //Add this method to show Dialog when the required permission has been granted to the app.

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == mRequestCode) {
            for (int a = 0; a < permissions.length; a++) {
                if (grantResults.length <= a || grantResults[a] != PackageManager.PERMISSION_GRANTED) {
                    continue;
                }
                switch (permissions[a]) {
                    case Manifest.permission.RECORD_AUDIO: {
                        CheckPermission();
                        break;
                    }

                    case Manifest.permission.WRITE_EXTERNAL_STORAGE: {
                        Record();
                        break;
                    }

                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            if (resultCode == RESULT_OK) {

                String Base64 = new Base64EncodeDecodeFile().EncodeFileToBase64(filePath);
                if (Base64.length() > 0) {
                    String FileName = new CustomFunction().getFileName(filePath);
                    audioRecorderListener.onSuccess(Base64, FileName);
                } else {
                    audioRecorderListener.onFaild();
                }
                //File file = new File()
                // Great! User has recorded and saved the audio file
            } else if (resultCode == RESULT_CANCELED) {
                audioRecorderListener.onCancel();
                // Oops! User has canceled the recording
            }
        }
    }


}
