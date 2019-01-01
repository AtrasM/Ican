package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Message.MessageQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureFwdReplyBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.AdapterAttach;
import avida.ican.Ican.View.Custom.AudioRecorder;
import avida.ican.Ican.View.Custom.Base64EncodeDecodeFile;
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
import butterknife.BindDimen;
import butterknife.BindView;
import jp.wasabeef.richeditor.RichEditor;

public class FarzinActivityWriteMessage extends BaseToolbarActivity {
    @BindView(R.id.edt_subject)
    EditText edtSubject;
    @BindView(R.id.re_msg)
    RichEditor reMsg;
    @BindView(R.id.frm_add_contacts)
    FrameLayout frmAddContacts;
    @BindView(R.id.ex_txt_contacts)
    ExpandableTextView exTxtContacts;
    @BindView(R.id.ln_rcv_attach)
    LinearLayout lnRcvAttach;
    @BindView(R.id.rcv_attach)
    RecyclerView RcvAttach;
    @BindView(R.id.sp_size)
    Spinner spSize;

    private String msgContent = "";
    private DialogUserAndRole dialogUserAndRole;
    private List<StructureUserAndRoleDB> structuresMain = new ArrayList<>();
    private List<StructureUserAndRoleDB> userAndRoleDBS = new ArrayList<>();
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private AdapterAttach adapterAttach;
    private String audioExetention = ".wav";

    @BindDimen(R.dimen.padding)
    int defPading;
    @BindDimen(R.dimen.txt_editor_Size)
    int textSize;

    private Loading loading;
    private File file;
    private String message = "";
    public static StructureFwdReplyBND structureFwdReplyBND = new StructureFwdReplyBND();

    @Override
    protected void onResume() {
        if (file != null) {
            file.delete();
        }
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_write_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean ISFwdReplyMessage = getIntent().getBooleanExtra(PutExtraEnum.ISFwdReplyMessage.getValue(), false);

        exTxtContacts.setText("");
        loading = new Loading(App.CurentActivity).Creat();
        setUpRichEditore();
        initAdapter();

        if (ISFwdReplyMessage) {
            //structureFwdReplyBND = (StructureFwdReplyBND) bundleObject.getSerializable(PutExtraEnum.ISFwdReplyMessage.getValue());
            if (structureFwdReplyBND.isReply()) {

                initTollBar(Resorse.getString(R.string.title_farzin_reply_message));
                String subject = Resorse.getString(R.string.reply_message) + " " + structureFwdReplyBND.getSubject();
                edtSubject.setText(subject);
                String divider = CustomFunction.repeatChar('-', 80);
                String content = "(" + Resorse.getString(R.string.sender) + structureFwdReplyBND.getSenderFullName() + structureFwdReplyBND.getSenderRoleName() + ") <br> " + structureFwdReplyBND.getContent() + " <br> " + divider + " <br> ";
                reMsg.setHtml(content);
                StructureUserAndRoleDB structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(structureFwdReplyBND.getSender_user_id(), structureFwdReplyBND.getSender_role_id());
                userAndRoleDBS.add(structureUserAndRoleDB);
                ShowSelectionItem(userAndRoleDBS);
                structureAttaches = structureFwdReplyBND.getStructureAttaches();
                adapterAttach.addAll(structureAttaches);
            } else {
                initTollBar(Resorse.getString(R.string.title_farzin_fwd_message));
                edtSubject.setText(structureFwdReplyBND.getSubject());
                String content = "(" + Resorse.getString(R.string.sender) + structureFwdReplyBND.getSenderFullName() + structureFwdReplyBND.getSenderRoleName() + ") <br> " + structureFwdReplyBND.getContent() + " <br> ";
                reMsg.setHtml(content);
            }


        } else {
            initTollBar(Resorse.getString(R.string.title_farzin_write_message));
        }


        frmAddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showUserAndRoleDialog();
            }
        });




    }

    private void showUserAndRoleDialog() {
        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity).setTitle(Resorse.getString(R.string.title_contacts)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(structuresMain), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRoleDBS), UserAndRoleEnum.USERANDROLE, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                structuresMain = structureUserAndRolesMain;
                userAndRoleDBS = structureUserAndRolesSelect;

                ShowSelectionItem(userAndRoleDBS);
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {

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

    }

    private void initAdapter() {
        GridLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        RcvAttach.setLayoutManager(linearLayoutManagerWithSmoothScroller);
        adapterAttach = new AdapterAttach(App.CurentActivity, structureAttaches, true, new ListenerAdapterAttach() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                byte[] aByte = new Base64EncodeDecodeFile().DecodeBase64ToByte(structureAttach.getBase64File());
                file = new CustomFunction(App.CurentActivity).OpenFile(aByte, structureAttach.getName(), structureAttach.getFileExtension());

            }
        });
        RcvAttach.setAdapter(adapterAttach);
    }

    @SuppressLint("StaticFieldLeak")
    private void ShowSelectionItem(final List<StructureUserAndRoleDB> structuresSelect) {
        exTxtContacts.setText("");
        final String[] selectedName = {""};
        loading.Show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                for (int i = 0; i < structuresSelect.size(); i++) {
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    selectedName[0] = selectedName[0] + structuresSelect.get(i).getFirstName() + " " + structuresSelect.get(i).getLastName() + " [ " + structuresSelect.get(i).getRoleName() + " ] " + " , ";
                }
                if (selectedName[0].length() > 0)
                    selectedName[0] = selectedName[0].substring(0, selectedName[0].length() - 3);
                return selectedName[0];
            }

            @Override
            protected void onPostExecute(String s) {
                exTxtContacts.setText(s);
                loading.Hide();
                super.onPostExecute(s);
            }
        }.execute();
    }

    private void setUpRichEditore() {
        //reMsg.setEditorHeight(200);
        reMsg.setEditorFontSize(textSize);
   /*     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            reMsg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }*/
        reMsg.setEditorFontColor(Color.BLACK);
        reMsg.setPadding(defPading, defPading, defPading, defPading);
        reMsg.focusEditor();
        //reMsg.setPlaceholder(Resorse.getString(R.string.editore_place_holder));
        reMsg.setHtml(Resorse.getString(R.string.editore_place_holder));
        reMsg.setAlignRight();
        reMsg.getRight();
        reMsg.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                msgContent = text;
            }
        });


        ArrayList<String> msize = new ArrayList<>();
        final ArrayList<Integer> Hsize = new ArrayList<>(Arrays.asList(14, 16, 18, 20, 22, 24, 26, 28, 30));
        msize.add("14pt");
        msize.add("16pt");
        msize.add("18pt");
        msize.add("20pt");
        msize.add("22pt");
        msize.add("24pt");
        msize.add("26pt");
        msize.add("28pt");
        msize.add("30pt");

        ArrayAdapter<String> adapterSize = new CustomFunction(App.CurentActivity).getSpinnerAdapter(msize);
        spSize.setAdapter(adapterSize);
        spSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reMsg.setEditorFontSize(Hsize.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setItalic();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setUnderline();
            }
        });


        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setAlignRight();
            }
        });


        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reMsg.setNumbers();
            }
        });

    }


    private void ShowAudioRecorde() {
        audioRecorder = new AudioRecorder(App.CurentActivity);
        audioRecorder.setOnListener(new AudioRecorderListener() {

            @Override
            public void onSuccess(String base64, String fileName) {
                //App.ShowMessage().ShowToast("onSuccess", ToastEnum.TOAST_SHORT_TIME);
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
        filePicker.setOnListener(new FilePickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                // App.ShowMessage().ShowToast("onSuccess", ToastEnum.TOAST_SHORT_TIME);
                addAllToAttach(base64s, fileNames, R.drawable.ic_attach_file);
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
                addAllToAttach(base64s, fileNames, R.drawable.ic_photo);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showMultyPickFromCamera();
    }

    private void ShowGallaryPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                addAllToAttach(base64s, fileNames, R.drawable.ic_photo);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showMultyPickFromGallery();
    }

    private void ShowVideoPicker() {
        mediaPicker = new MediaPicker(App.CurentActivity);
        mediaPicker.setOnListener(new MediaPickerListener() {


            @Override
            public void onSuccess(ArrayList<File> files, ArrayList<String> base64s, ArrayList<String> fileNames) {
                addAllToAttach(base64s, fileNames, R.drawable.ic_video);
            }

            @Override
            public void onFailed() {

            }

            @Override
            public void onCancel() {

            }
        }).showMultyVideoFromGallery();
    }

    private void ShowDialogAttach() {
        new DialogAttach(App.CurentActivity).setOnListener(new ListenerAttach() {
            @Override
            public void onSuccess(AttachEnum attachEnum) {
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

            }
        }).Show();
    }

    private void addToAttach(String base64File, String name, String fileExtension, int resID) {
        StructureAttach structureAttach = new StructureAttach(base64File, name, fileExtension, resID);
        adapterAttach.add(structureAttach);
    }

    @SuppressLint("StaticFieldLeak")
    private void addAllToAttach(final ArrayList<String> base64Files, final ArrayList<String> names, final int resID) {
        final ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
        new AsyncTask<Void, Void, ArrayList<StructureAttach>>() {
            @Override
            protected ArrayList<StructureAttach> doInBackground(Void... voids) {
                for (int i = 0; i < base64Files.size(); i++) {
                    String fileExtension = CustomFunction.FileExtension(names.get(i));
                    StructureAttach structureAttach = new StructureAttach(base64Files.get(i), names.get(i), fileExtension, resID);
                    structureAttaches.add(structureAttach);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return structureAttaches;
            }

            @Override
            protected void onPostExecute(ArrayList<StructureAttach> structureAttaches) {
                adapterAttach.addAll(structureAttaches);
                super.onPostExecute(structureAttaches);
            }
        }.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.message_toolbar_menu, menu);
        // menu.findItem(R.id.action_search).setIntent(new Intent(G.currentActivity, ActivitySearch.class));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionSend = menu.findItem(R.id.action_send_message);
        MenuItem actionAttach = menu.findItem(R.id.action_attach);

        actionSend.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (isValid()) {
                    SaveMessage();
                }

                return false;
            }
        });
        actionAttach.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                ShowDialogAttach();
                return false;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void SaveMessage() {
        loading.Show();
        String Subject = "" + edtSubject.getText().toString();
        // StructureUserAndRoleDB structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(getFarzinPrefrences().getUserName());
        int user_id = getFarzinPrefrences().getUserID();
        int role_id = getFarzinPrefrences().getRoleID();
        new FarzinMessageQuery().SaveSendLocalMessage(user_id, role_id, Subject, message, structureAttaches, userAndRoleDBS, new MessageQuerySaveListener() {

            @Override
            public void onSuccess(final StructureMessageDB structureMessageDB) {
                if (App.fragmentMessageList != null) {
                    App.CurentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.Hide();
                            App.fragmentMessageList.UpdateSendMessageData();
                            App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                            Finish(App.CurentActivity);
                        }
                    });
                }
            }

            @Override
            public void onExisting() {

            }

            @Override
            public void onFailed(final String message) {
                showSnackStatus(message);
            }

            @Override
            public void onCancel() {
                showSnackStatus(Resorse.getString(R.string.rule_cancel));

            }
        });


    }

    private void showSnackStatus(final String message) {
        App.getHandlerMainThread().post(new Runnable() {
            @Override
            public void run() {
                loading.Hide();
                App.ShowMessage().ShowSnackBar(message, SnackBarEnum.SNACKBAR_SHORT_TIME);
            }
        });

    }

    private boolean isValid() {
        boolean isValid = true;
        loading.Show();
        if (edtSubject.getText().toString().isEmpty()) {
            isValid = false;
            edtSubject.setError(Resorse.getString(R.string.error_empty_filed));
        } else {
            if (edtSubject.getText().toString().length() < 2) {
                isValid = false;
                edtSubject.setError(Resorse.getString(R.string.error_invalid_minLen));
            }
        }
        if (userAndRoleDBS.size() == 0) {
            isValid = false;
            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_empty_recivers), SnackBarEnum.SNACKBAR_LONG_TIME);
        } else {
            if (reMsg.getHtml() != null) {
                message = reMsg.getHtml();
                /*isValid = false;
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_empty_editor_filed), SnackBarEnum.SNACKBAR_LONG_TIME);*/
            }
        }
        loading.Hide();
        return isValid;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}
