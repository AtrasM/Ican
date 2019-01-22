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
import android.widget.TextView;


import com.github.barteksc.pdfviewer.PDFView;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import avida.ican.Farzin.Model.Interface.Cartable.SendListener;
import avida.ican.Farzin.Model.Interface.Message.MessageListListener;
import avida.ican.Farzin.Model.MessageSaxHandler;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureMessageRES;
import avida.ican.Farzin.Model.Structure.Response.Message.StructureRecieveMessageListRES;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendToServerPresenter;
import avida.ican.Farzin.Presenter.Message.GetMessageFromServerPresenter;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
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
    @BindView(R.id.pdf_viewer)
    PDFView pdfViewer;
    @BindView(R.id.cv_audio_recorder)
    CardView cvAudioRecorder;
    @BindView(R.id.txt_pdf_page_number)
    TextView txt_pdf_page_number;
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
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private File file;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.cv_farzin: {

                getReceiveMessage();

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

                dialogUserAndRole = new DialogUserAndRole(App.CurentActivity).setTitle(Resorse.getString(R.string.title_contacts)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(structuresMain), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(structuresSelect), UserAndRoleEnum.USERANDROLE,
                        new ListenerUserAndRoll() {
                            @Override
                            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                                structuresMain = structureUserAndRolesMain;
                                structuresSelect = structureUserAndRolesSelect;

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
                break;
            }
        }
    }

    private void getReceiveMessage() {
        GetMessageFromServerPresenter getMessageFromServerPresenter = new GetMessageFromServerPresenter();
        getMessageFromServerPresenter.GetRecieveMessageList(1, 1, new MessageListListener() {
            @Override
            public void onSuccess(ArrayList<StructureMessageRES> messageList) {
                for (int i = 0; i < messageList.size(); i++) {
                    Log.i("ReceiveMessage", "get message.getSubject i=" + i + " " + messageList.get(i).getSubject());
                    Log.i("ReceiveMessage", "get message.description i=" + i + " " + messageList.get(i).getDescription());
                    Log.i("ReceiveMessage", "get message.getSender().getUserName i=" + i + " " + messageList.get(i).getSender().getUserName());

                }

                Log.i("ReceiveMessage", "messageList.size()= " + messageList.size());
            }

            @Override
            public void onFailed(String message) {
                message.isEmpty();
            }

            @Override
            public void onCancel() {

            }
        });
   /*     String filePath = App.RESPONSEPATH + "/data_test.xml";
        StructureRecieveMessageListRES structureRecieveMessageListRES = parseXmlWithSax1(filePath);*/
    }


    private StructureRecieveMessageListRES parseXmlWithSax1(String xmlFilePath) {
        StructureRecieveMessageListRES structureRecieveMessageListRES = new StructureRecieveMessageListRES();
        List<StructureMessageRES> Messages = new ArrayList<>();
        File file = new File(xmlFilePath);

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SaxHandler
            MessageSaxHandler saxHandler = new MessageSaxHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(fileInputStream));
            // get the `Employee list`
            Messages = saxHandler.getObject();
            structureRecieveMessageListRES.setGetRecieveMessageListResult(Messages);
        } catch (Exception ex) {
            Log.d("XML", "SAXXMLParser error =" + ex.toString());
            Log.d("XML", "SAXXMLParser: parse() failed");
        }

        return structureRecieveMessageListRES;
    }

    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
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

    private void showUserAndRoleDialog() {


        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, 645, 300, -1).setTitle(Resorse.getString(R.string.title_send)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<StructureUserAndRoleDB>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
                userAndRolesMain = structureUserAndRolesMain;
                //userAndRoleDBS = structureUserAndRolesSelect;
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

            }


        });

    }


    private void CartableSend() {
        CartableDocumentAppendToServerPresenter cartableDocumentAppendToServerPresenter = new CartableDocumentAppendToServerPresenter();
        SendListener sendListener = new SendListener() {

            @Override
            public void onSuccess() {
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


        };

        cartableDocumentAppendToServerPresenter.AppendDocument(new StructureAppendREQ(), sendListener);

    }

}
