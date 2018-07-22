package avida.ican.Ican.View;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import avida.ican.Farzin.Model.Interface.MessageListListener;
import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Response.StructureMessageRES;
import avida.ican.Farzin.Presenter.GetMessageFromServerPresenter;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.View.Custom.AudioRecorder;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.FilePicker;
import avida.ican.Ican.View.Custom.MediaPicker;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.AudioRecorderListener;
import avida.ican.Ican.View.Interface.FilePickerListener;
import avida.ican.Ican.View.Interface.MediaPickerListener;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cvFarzin.setOnClickListener(this);
        App.setCurentProject(CurentProject.Ican);
        cvAudioRecorder.setOnClickListener(this);
        cvFilePicker.setOnClickListener(this);
        cvMediaPicker.setOnClickListener(this);
        cvUserAndRole.setOnClickListener(this);

        ArrayList<String> strItemsize = new ArrayList<>(Arrays.asList("H 1", " H 2", " H 3", "H 4", "H 5"));
        final ArrayList<Integer> Hsize = new ArrayList<>(Arrays.asList(18, 16, 14, 12, 10));

        ArrayAdapter<String> adapterSize = new ArrayAdapter<>(App.CurentActivity, android.R.layout.simple_spinner_dropdown_item, strItemsize);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                getMessageFromServerPresenter = new GetMessageFromServerPresenter();
                getMessageFromServerPresenter.GetSentMessageList(1, new MessageListListener() {
                    @Override
                    public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                        App.ShowMessage().ShowToast("onSuccess", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailed(String message) {
                        App.ShowMessage().ShowToast("onFailed " + message, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onCancel() {
                        App.ShowMessage().ShowToast("onCancel", Toast.LENGTH_SHORT);
                    }
                });
                //goToActivity(FarzinActivityLogin.class);

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


}
