package avida.ican.Farzin.View;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Message.MessageDataListener;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Message.FarzinGetMessagePresenter;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import static avida.ican.Farzin.View.Enum.SettingEnum.AUTOMATIC;
import static avida.ican.Farzin.View.Enum.SettingEnum.MANUALLY;
import static avida.ican.Farzin.View.Enum.SettingEnum.SYNC;

public class ActivityMessageSetting extends BaseToolbarActivity {

    @BindView(R.id.ln_count_of_get_document)
    LinearLayout lnCountOfGetDocument;
    @BindView(R.id.sp_count_of_get_document)
    Spinner spCountOfGetDocument;
    @BindView(R.id.ln_message_type)
    LinearLayout lnMessagType;
    @BindView(R.id.sp_message_type)
    Spinner spMessageType;
    @BindView(R.id.ln_start_date)
    LinearLayout lnStartDate;
    @BindView(R.id.txt_start_date)
    TextView txtStartDate;
    @BindView(R.id.ln_end_date)
    LinearLayout lnEndDate;
    @BindView(R.id.txt_end_date)
    TextView txtEndDate;
    @BindView(R.id.edt_subject)
    TextView edtSubject;
    @BindView(R.id.edt_content)
    TextView edtContent;
    @BindString(R.string.TitleActivityDocumentSetting)
    String Title;
    private static String strStartDate = "";
    private static String strEndDate = "";
    private ArrayList<String> countOfGetDocumentList = new ArrayList<>();
    private FarzinMessageQuery farzinMessageQuery;
    private FarzinPrefrences farzinPrefrences;
    private int messageTypeDefPos = 0;
    private int countOfGetDocumentDefPos = 0;
    ArrayList<Type> arrayType = new ArrayList<>();
    private Type type;
    private int settingType = -1;
    private FarzinGetMessagePresenter farzinGetMessagePresenter;
    private ArrayList<String> spList = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_message_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingType = getIntent().getIntExtra(PutExtraEnum.Settingtype.getValue(), -1);
        farzinMessageQuery = new FarzinMessageQuery();
        farzinPrefrences = getFarzinPrefrences();
        initTollBar(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initView();
    }

    private void initView() {
        if (settingType == MANUALLY.getValue()) {
            lnEndDate.setVisibility(View.VISIBLE);
            lnMessagType.setVisibility(View.VISIBLE);
            initSpMessageType();
        } else if (settingType == AUTOMATIC.getValue()) {
            lnEndDate.setVisibility(View.GONE);
            lnStartDate.setVisibility(View.GONE);
        }
        //strStartDate = farzinPrefrences.getReceiveMessageDataSyncDate();
        strStartDate = CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
        strEndDate = CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());

        txtStartDate.setText(CustomFunction.MiladyToJalaly(strStartDate));
        txtEndDate.setText(CustomFunction.MiladyToJalaly(strEndDate));
        initDateView();

        lnMessagType.setOnClickListener(view -> {
            spMessageType.performClick();
        });
    }

    private void initDateView() {
        lnStartDate.setOnClickListener(view -> {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        strStartDate = CustomFunction.convertDateToCustomFormat(year, monthOfYear, dayOfMonth, SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
                        txtStartDate.setText(strStartDate.replace("T", " "));
                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), PutExtraEnum.Datepickerdialog.getValue());
        });

        lnEndDate.setOnClickListener(view -> {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        strEndDate = CustomFunction.convertDateToCustomFormat(year, monthOfYear, dayOfMonth, SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
                        txtEndDate.setText(strEndDate.replace("T", " "));
                    },
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), PutExtraEnum.Datepickerdialog.getValue());
        });
    }

    private void initSpMessageType() {
        initSpMessageTypeData();
        ArrayAdapter<String> adapterType = new CustomFunction(App.CurentActivity).getSpinnerAdapter(spList);
        spMessageType.setAdapter(adapterType);
        spMessageType.setSelection(messageTypeDefPos);
        spMessageType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = arrayType.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpCountOfGetDocument() {
        initSpCountOfGetDocumentData();
        ArrayAdapter<String> adapterCountOfGetDocument = new CustomFunction(App.CurentActivity).getSpinnerAdapter(countOfGetDocumentList);
        spCountOfGetDocument.setAdapter(adapterCountOfGetDocument);
        spCountOfGetDocument.setSelection(countOfGetDocumentDefPos);
        spCountOfGetDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                farzinPrefrences.putDefultCountOfGetDocument(Integer.parseInt(countOfGetDocumentList.get(i)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initSpMessageTypeData() {
        //spList.add("همه");
        spList.add("ارسالی");
        spList.add("دریافتی");

        //arrayType.add(Type.ALL);
        arrayType.add(Type.SENDED);
        arrayType.add(Type.RECEIVED);
      /*   if (defDefultMessageType.equals(Type.SENDED.getValue())) {
            messageTypeDefPos = 0;
        } else if (defDefultMessageType.equals(Type.RECEIVED.getValue())) {
            messageTypeDefPos = 1;
        }*/
        messageTypeDefPos = 0;
    }

    private void initSpCountOfGetDocumentData() {
        int defCountOfGetDocument = farzinPrefrences.getDefultCountOfGetDocument();
        countOfGetDocumentList.add("10");
        int counter = 0;
        for (int i = 50; i <= 1000; i += 50) {
            if (i == defCountOfGetDocument) {
                countOfGetDocumentDefPos = counter;
            }
            counter++;
            countOfGetDocumentList.add("" + i);
        }
    }

    private void getData() {
        App.canBack = false;
        callDialogDataSyncing();
        strStartDate = CustomFunction.JalalyToMilady(strStartDate);
        strEndDate = CustomFunction.JalalyToMilady(strEndDate);

        farzinGetMessagePresenter = new FarzinGetMessagePresenter(new MessageDataListener() {
            @Override
            public void newData() {
                String date = CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());

                if (type == Type.RECEIVED) {
                    getFarzinPrefrences().putReceiveMessageDataSyncDate(date);
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncReceiveMessage);
                } else {
                    getFarzinPrefrences().putSendMessageDataSyncDate(date);
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncSendMessage);
                }
            }

            @Override
            public void noData() {
                String date = CustomFunction.getCurentDateTimeAsStringFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());

                if (type == Type.RECEIVED) {
                    getFarzinPrefrences().putReceiveMessageDataSyncDate(date);
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncReceiveMessage);
                } else {
                    getFarzinPrefrences().putSendMessageDataSyncDate(date);
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncSendMessage);
                }
            }

            @Override
            public void onFailed(String message) {
                BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncFailed);
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_faild), SnackBarEnum.SNACKBAR_LONG_TIME);
            }
        });
        farzinGetMessagePresenter.GetFromServer(strStartDate, strEndDate, type);

    }

    private void callDialogDataSyncing() {

        runOnUiThread(() -> {

            BaseActivity.dialogDataSyncing = new DialogDataSyncing(App.CurentActivity, 1, false, new MetaDataSyncListener() {
                @Override
                public void onFinish() {
                    runOnUiThread(() -> {
                        Finish(App.CurentActivity);
                    });
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCancel() {

                }

            });
            BaseActivity.dialogDataSyncing.Creat();
        });
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.setting_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionSuccess = menu.findItem(R.id.action_success);
        CustomFunction customFunction = new CustomFunction(App.CurentActivity);
        Drawable drawable = customFunction.ChengeDrawableColor(R.drawable.ic_success, R.color.color_White);
        actionSuccess.setIcon(drawable);
        actionSuccess.setOnMenuItemClickListener(menuItem -> {

            if (settingType == SYNC.getValue()) {
                if (strStartDate != farzinPrefrences.getSendMessageDataSyncDate()) {
                    farzinPrefrences.putSendMessageDataSyncDate(CustomFunction.JalalyToMilady(strStartDate));
                }
                if (strStartDate != farzinPrefrences.getReceiveMessageDataSyncDate()) {
                    farzinPrefrences.putReceiveMessageDataSyncDate(CustomFunction.JalalyToMilady(strStartDate));
                }

                Finish(App.CurentActivity);

            } else if (settingType == MANUALLY.getValue()) {
                //Finish(App.CurentActivity);
                getData();
            } else if (settingType == AUTOMATIC.getValue()) {
                Finish(App.CurentActivity);
            }

            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Finish(App.CurentActivity);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
