package avida.ican.Ican.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryListListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakListener;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.Presenter.Cartable.GetCartableHistoryFromServerPresenter;
import avida.ican.Farzin.Presenter.Cartable.GetZanjireMadrakFromServerPresenter;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.FarzinActivityLogin;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.Structure.StructureOpticalPen;
import avida.ican.Ican.View.Custom.AudioRecorder;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.FilePicker;
import avida.ican.Ican.View.Custom.MediaPicker;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogOpticalPen;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.AudioRecorderListener;
import avida.ican.Ican.View.Interface.FilePickerListener;
import avida.ican.Ican.View.Interface.MediaPickerListener;
import avida.ican.Ican.View.Interface.OpticalPenDialogListener;
import avida.ican.R;
import butterknife.BindView;

public class ActivityMain extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.cv_farzin)
    CardView cvFarzin;
    @BindView(R.id.cv_audio_recorder)
    CardView cvAudioRecorder;
    @BindView(R.id.cv_file_picker)
    CardView cvFilePicker;
    @BindView(R.id.cv_media_picker)
    CardView cvMediaPicker;
    @BindView(R.id.cv_user_and_role)
    CardView cvUserAndRole;
    @BindView(R.id.sp_size)
    Spinner spSize;

    private DialogUserAndRole dialogUserAndRole;

    private List<StructureUserAndRoleDB> structuresMain = new ArrayList<>();
    private List<StructureUserAndRoleDB> structuresSelect = new ArrayList<>();
    private GetMessageFromServerPresenter getMessageFromServerPresenter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        cvFarzin.setOnClickListener(this);
        App.setCurentProject(CurentProject.Ican);
        cvAudioRecorder.setOnClickListener(this);
        cvFilePicker.setOnClickListener(this);
        cvMediaPicker.setOnClickListener(this);
        cvUserAndRole.setOnClickListener(this);

        ArrayList<String> strItemsize = new ArrayList<>(Arrays.asList("H 1", " H 2", " H 3", "H 4", "H 5"));
        final ArrayList<Integer> Hsize = new ArrayList<>(Arrays.asList(18, 16, 14, 12, 10));

        ArrayAdapter<String> adapterSize = new CustomFunction(App.CurentActivity).getSpinnerAdapter(strItemsize);
        spSize.setAdapter(adapterSize);
        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                App.ShowMessage().ShowToast("" + i, ToastEnum.TOAST_SHORT_TIME);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cv_farzin: {
             /*   GetZanjireMadrakFromServerPresenter getZanjireMadrakFromServerPresenter = new GetZanjireMadrakFromServerPresenter();
                getZanjireMadrakFromServerPresenter.GetZanjireMadrakList(1183, 135, new ZanjireMadrakListener() {


                    @Override
                    public void onSuccess(StructureZanjireMadrakRES structureZanjireMadrakRES) {
                        Log.i("test", "test");
                    }

                    @Override
                    public void onFailed(String message) {
                        Log.i("test", "test");
                    }

                    @Override
                    public void onCancel() {
                        Log.i("test", "test");
                    }
                });*/
            /*    new DialogOpticalPen(App.CurentActivity).setOnListener(new OpticalPenDialogListener() {
                    @Override
                    public void onSuccess(StructureOpticalPen structureOpticalPen) {
                        structureOpticalPen.getbFile();
                    }

                    @Override
                    public void onFaild() {

                    }

                    @Override
                    public void onCancel() {

                    }
                }).Show();*/
                 goToActivity(FarzinActivityLogin.class);

                break;
            }


            case R.id.cv_audio_recorder: {
                ShowAudioRecorde();
                //App.ShowMessage().ShowToast("BPMS", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.cv_file_picker: {
                ShowFilePicker();
                //App.ShowMessage().ShowToast("BPMS", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.cv_media_picker: {
                ShowMediaPicker();
                //App.ShowMessage().ShowToast("BPMS", ToastEnum.TOAST_SHORT_TIME);
                break;
            }
            case R.id.cv_user_and_role: {

                dialogUserAndRole = new DialogUserAndRole(App.CurentActivity).setTitle(Resorse.getString(R.string.title_contacts)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(structuresMain), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(structuresSelect), new ListenerUserAndRoll() {
                    @Override
                    public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                        structuresMain = structureUserAndRolesMain;
                        structuresSelect = structureUserAndRolesSelect;

                    }

                    @Override
                    public void onFailed() {

                    }

                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onCancel(final List<StructureUserAndRoleDB> tmpItemSelect) {
                      /*  new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                for (StructureUserAndRoleDB item : tmpItemSelect) {
                                    try {
                                        Thread.sleep(10);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    int pos = structuresMain.indexOf(item);
                                    if (pos > -1) {
                                        structuresMain.get(pos).setSelected(false);
                                    }
                                }
                                return null;
                            }
                        }.execute();*/

                    }


                });
                break;
            }
        }
    }

    private void ShowAudioRecorde() {
        audioRecorder = new AudioRecorder(App.CurentActivity);
        audioRecorder.setOnListener(new AudioRecorderListener() {

            @Override
            public void onSuccess(String base64, String fileName) {
                App.ShowMessage().ShowToast("onSuccess", ToastEnum.TOAST_SHORT_TIME);
                Log.i("AudioRecorde", base64);
            }

            @Override
            public void onFaild() {
                App.ShowMessage().ShowToast("Faield", ToastEnum.TOAST_SHORT_TIME);
            }

            @Override
            public void onCancel() {
                App.ShowMessage().ShowToast("Cancel", ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }

    private void ShowFilePicker() {
        filePicker = new FilePicker(App.CurentActivity);
        filePicker.setOnListener(new FilePickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                App.ShowMessage().ShowToast("onSuccess", ToastEnum.TOAST_SHORT_TIME);
            }

            @Override
            public void onFailed() {
                App.ShowMessage().ShowToast("Faield", ToastEnum.TOAST_SHORT_TIME);
            }

            @Override
            public void onCancel() {
                App.ShowMessage().ShowToast("Cancel", ToastEnum.TOAST_SHORT_TIME);
            }
        });
    }

    private void ShowMediaPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {

            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                App.ShowMessage().ShowToast("onSuccess", ToastEnum.TOAST_SHORT_TIME);

            }

            @Override
            public void onFailed() {
                App.ShowMessage().ShowToast("Faield", ToastEnum.TOAST_SHORT_TIME);
            }

            @Override
            public void onCancel() {
                App.ShowMessage().ShowToast("Cancel", ToastEnum.TOAST_SHORT_TIME);
            }
        }).showMultyPickFromGallery();
    }

/*    void callNotification(String title, String message) {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "atrasChannel";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;

        Intent intent = new Intent(this, FarzinActivityLogin.class);
        intent.putExtra("LogOut", true);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        Intent intentCancell = new Intent(this, FarzinMessageNotificationReceiver.class);
        PendingIntent pendingIntentCancell = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intentCancell, 0);


        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        }
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setDeleteIntent(pendingIntentCancell)
                        .setSmallIcon(R.drawable.ic_album)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setChannelId(CHANNEL_ID).build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.createNotificationChannel(mChannel);
        }

        // Issue the notification.
        mNotificationManager.notify(notifyID, notification);

    }*/


}
