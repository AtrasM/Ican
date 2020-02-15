package avida.ican.Farzin.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import avida.ican.Farzin.Model.Enum.DependencyTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureActivityDocumentAttachFileBND;

import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.AdapterAttach;
import avida.ican.Ican.View.Custom.AudioRecorder;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.FilePicker;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.MediaPicker;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.DialogAttach;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.AttachEnum;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.Ican.View.Interface.AudioRecorderListener;
import avida.ican.Ican.View.Interface.FilePickerListener;
import avida.ican.Ican.View.Interface.ListenerAdapterAttach;
import avida.ican.Ican.View.Interface.ListenerAttach;
import avida.ican.Ican.View.Interface.MediaPickerListener;
import avida.ican.R;
import butterknife.BindView;


public class FarzinActivityDocumentAttachFile extends BaseToolbarActivity {
    @BindView(R.id.edt_description)
    EditText edtDescription;
    @BindView(R.id.ln_rcv_attach)
    LinearLayout lnRcvAttach;
    @BindView(R.id.rcv_attach)
    RecyclerView RcvAttach;
    @BindView(R.id.sp_type)
    Spinner spType;

    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private AdapterAttach adapterAttach;
    private String audioExetention = ".wav";

    private Loading loading;
    private File file;
    private String message = "";
    private StructureActivityDocumentAttachFileBND structureActivityDocumentAttachFileBND;
    private DependencyTypeEnum selectedDependencyType = DependencyTypeEnum.Peyvast;
    private StructureAttach structureAttach;
    private DialogAttach dialogAttach;

    @Override
    protected void onResume() {
        if (file != null) {
            file.delete();
        }
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_document_attach_file;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleObject = getIntent().getExtras();
        structureActivityDocumentAttachFileBND = (StructureActivityDocumentAttachFileBND) bundleObject.getSerializable(PutExtraEnum.BundleActivityDocumentAttach.getValue());
        loading = new Loading(App.CurentActivity).Creat();
        initSpinner();
        initAdapter();
        initTollBar(structureActivityDocumentAttachFileBND.getTitle());
    }

    private void initSpinner() {
        ArrayList<String> dependencyTypename = new ArrayList<>();
        final ArrayList<DependencyTypeEnum> DependencyTypeId = new ArrayList<>(Arrays.asList(DependencyTypeEnum.Peyvast, DependencyTypeEnum.Peiro, DependencyTypeEnum.Atf, DependencyTypeEnum.DarErtebat));
        selectedDependencyType = DependencyTypeId.get(0);
        dependencyTypename.add(Resorse.getString(R.string.title_peyvast));
        dependencyTypename.add(Resorse.getString(R.string.title_peiro));
        dependencyTypename.add(Resorse.getString(R.string.title_atf));
        dependencyTypename.add(Resorse.getString(R.string.title_dar_ertebat));

        ArrayAdapter<String> adapterSize = new CustomFunction(App.CurentActivity).getSpinnerAdapter(dependencyTypename);
        spType.setAdapter(adapterSize);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDependencyType = DependencyTypeId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initAdapter() {
        GridLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        RcvAttach.setLayoutManager(linearLayoutManagerWithSmoothScroller);
        adapterAttach = new AdapterAttach(App.CurentActivity, structureAttaches, true, new ListenerAdapterAttach() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
            }

            @Override
            public void onDeletFile(StructureAttach structureAttach) {
                // structureAttaches.remove(structureAttach);
            }
        });
        RcvAttach.setAdapter(adapterAttach);
    }


    private void ShowAudioRecorde() {
        audioRecorder = new AudioRecorder(App.CurentActivity);
        audioRecorder.setOnListener(new AudioRecorderListener() {

            @Override
            public void onSuccess(String base64, String fileName) {
                addToAttach(base64, fileName, audioExetention, R.drawable.ic_voice);
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
        filePicker.setSelectionMode(false);
        filePicker.setOnListener(new FilePickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                String fileExtension = CustomFunction.FileExtension(fileNames.get(0));
                addToAttach(base64s.get(0), fileNames.get(0), fileExtension, R.drawable.ic_photo);
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

    private void ShowCameraPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                String fileExtension = CustomFunction.FileExtension(fileNames.get(0));
                addToAttach(base64s.get(0), fileNames.get(0), fileExtension, R.drawable.ic_photo);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showSinglePickFromCamera();
    }

    private void ShowGallaryPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                String fileExtension = CustomFunction.FileExtension(fileNames.get(0));
                addToAttach(base64s.get(0), fileNames.get(0), fileExtension, R.drawable.ic_photo);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showSinglePickFromGallery();
    }

    private void ShowVideoPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                String fileExtension = CustomFunction.FileExtension(fileNames.get(0));
                addToAttach(base64s.get(0), fileNames.get(0), fileExtension, R.drawable.ic_video);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showSingleVideoFromGallery();
    }

    private void ShowDialogAttach() {
        App.canBack = false;
        dialogAttach = new DialogAttach(App.CurentActivity);
        dialogAttach.setOnListener(new ListenerAttach() {
            @Override
            public void onSuccess(AttachEnum attachEnum) {
                App.canBack = true;
                dialogAttach = null;
                switch (attachEnum) {
                    case FilePicker: {
                        ShowFilePicker();
                        break;
                    }
                    case CameraPicker: {
                        ShowCameraPicker();
                        break;
                    }
                    case GallaryPicker: {
                        ShowGallaryPicker();
                        break;
                    }
                    case AudioRecorde: {
                        ShowAudioRecorde();
                        break;
                    }
                    case VideoPicker: {
                        ShowVideoPicker();
                        break;
                    }
                }
            }

            @Override
            public void onCancel() {
                App.canBack = true;
                dialogAttach = null;
            }
        }).Show();
    }

    private void addToAttach(String base64File, String name, String fileExtension, int resID) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(base64File);
        structureAttach = new StructureAttach(stringBuilder, name, fileExtension, resID);
        adapterAttach.reset(structureAttach);
    }


    private void sendData() {
        if (structureAttach == null) {
            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_no_data_for_upload), SnackBarEnum.SNACKBAR_LONG_TIME);
            return;
        }
        loading.Show();
        String description = "" + edtDescription.getText().toString();
        StructureDocumentAttachFileBND structureDocumentAttachFileBND = new StructureDocumentAttachFileBND(structureActivityDocumentAttachFileBND.getETC(), structureActivityDocumentAttachFileBND.getEC(), false, structureAttach.getName(), structureAttach.getFileAsStringBuilder(), structureAttach.getFileExtension(), selectedDependencyType, description);
        new FarzinDocumentAttachFilePresenter().sendData(structureDocumentAttachFileBND, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {
                App.getHandlerMainThread().post(() -> {
                    loading.Hide();
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.document_attach_file_send_successfull), ToastEnum.TOAST_LONG_TIME);
                    Finish(false);
                });
            }

            @Override
            public void onSuccessAddToQueue() {
                App.getHandlerMainThread().post(() -> {
                    loading.Hide();
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                    Finish(false);
                });
            }

            @Override
            public void onFailed(String message) {
                addToQueue(structureDocumentAttachFileBND);
            }

            @Override
            public void onCancel() {
                addToQueue(structureDocumentAttachFileBND);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void addToQueue(StructureDocumentAttachFileBND structureDocumentAttachFileBND) {
        new FarzinDocumentAttachFilePresenter().documentAttachFileAddToQueue(structureDocumentAttachFileBND, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onSuccessAddToQueue() {
                App.getHandlerMainThread().post(() -> {
                    loading.Hide();
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                    Finish(false);
                });
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.attach_file_toolbar_menu, menu);
        // menu.findItem(R.id.action_search).setIntent(new Intent(G.currentActivity, ActivitySearch.class));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionUpload = menu.findItem(R.id.action_erja);
        MenuItem actionAttach = menu.findItem(R.id.action_attach);
        Drawable drawable = new CustomFunction(App.CurentActivity).ChengeDrawableColor(R.drawable.ic_attach2, R.color.color_White);
        actionAttach.setIcon(drawable);
        actionUpload.setOnMenuItemClickListener(menuItem -> {
            closeKeyboard();
            if (structureAttach != null) {
                sendData();
            } else {
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_no_data_for_upload), SnackBarEnum.SNACKBAR_LONG_TIME);
            }


            return false;
        });
        actionAttach.setOnMenuItemClickListener(menuItem -> {
            ShowDialogAttach();
            return false;
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Back();
                break;
            }
        }
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Back();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Back() {
        if (App.canBack) {
            Finish(true);
        } else {
            App.canBack = true;
            try {
                if (dialogAttach != null) {
                    dialogAttach.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void Finish(boolean isCancel) {
        loading.Hide();
        App.canBack = true;
        App.isLoading = false;
        Intent returnIntent = new Intent();
        if (isCancel) {
            setResult(RESULT_CANCELED, returnIntent);
        } else {
            setResult(RESULT_OK, returnIntent);
        }

        Finish(App.CurentActivity);
    }


    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}
