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
import java.util.Date;

import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.GetDateTimeListener;
import avida.ican.Farzin.Model.Interface.Message.MessageDataListener;
import avida.ican.Farzin.Model.Interface.MetaDataParentSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.Message.FarzinGetMessagePresenter;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
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
    @BindView(R.id.txt_info)
    TextView txtInfo;
    @BindString(R.string.TitleActivityDocumentSetting)
    String Title;
    private static String strStartDate = "";
    private static String strEndDate = "";
    private ArrayList<String> countOfGetDocumentList = new ArrayList<>();
    private FarzinMessageQuery farzinMessageQuery;
    private FarzinPrefrences farzinPrefrences;
    private int messageTypeDefPos = 0;
    private int countOfGetDocumentDefPos = 0;
    private ArrayList<Type> arrayType = new ArrayList<>();
    private Type type;
    private int settingType = -1;
    private FarzinGetMessagePresenter farzinGetMessagePresenter;
    private ArrayList<String> spList = new ArrayList<>();
    private DatePickerDialog endDatePickerDialog;
    private DatePickerDialog startDatePickerDialog;
    private int serviceCounter = 0;


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_message_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingType = getIntent().getIntExtra(PutExtraEnum.SettingType.getValue(), -1);
        farzinMessageQuery = new FarzinMessageQuery();
        farzinPrefrences = getFarzinPrefrences();
        initTollBar(Title);
        if (settingType == SYNC.getValue()) {
            txtInfo.setVisibility(View.GONE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

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


        strStartDate = farzinPrefrences.getMessageSettingStartDate();
        strEndDate = farzinPrefrences.getMessageSettingEndDate();
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

            if (startDatePickerDialog == null) {
                Date date = CustomFunction.changeDateTimeAsDateFormat(strStartDate, SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
                if (date != null) {
                    persianCalendar.setTime(date);
                }
                startDatePickerDialog = DatePickerDialog.newInstance(
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            strStartDate = CustomFunction.convertDateToCustomFormat(year, monthOfYear + 1, dayOfMonth, 0, 0, 0, SimpleDateFormatEnum.DateTime_Y_m_d_H_i_s.getValue(), true);
                            txtStartDate.setText(strStartDate.replace("T", " "));
                            strStartDate = CustomFunction.JalalyToMilady(strStartDate);
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
            }
            startDatePickerDialog.show(getFragmentManager(), PutExtraEnum.DatepickerDialog.getValue());
        });

        lnEndDate.setOnClickListener(view -> {
            PersianCalendar persianCalendar = new PersianCalendar();
            if (endDatePickerDialog == null) {
                persianCalendar.setTime(CustomFunction.getCurentDateTimeAsDateFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue()));
                endDatePickerDialog = DatePickerDialog.newInstance(
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            strEndDate = CustomFunction.convertDateToCustomFormat(year, monthOfYear + 1, dayOfMonth, 23, 59, 59, SimpleDateFormatEnum.DateTime_Y_m_d_H_i_s.getValue(), true);
                            txtEndDate.setText(strEndDate.replace("T", " "));
                            strEndDate = CustomFunction.JalalyToMilady(strEndDate);
                        },
                        persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay()
                );
            }
            endDatePickerDialog.show(getFragmentManager(), PutExtraEnum.DatepickerDialog.getValue());
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
        spList.add("همه");
        spList.add("ارسالی");
        spList.add("دریافتی");

        arrayType.add(Type.ALL);
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

    private void getData(boolean all, int serviceCount) {
        App.canBack = false;
        callDialogDataSyncing(serviceCount);
       /* strStartDate = CustomFunction.JalalyToMilady(strStartDate);
        strEndDate = CustomFunction.JalalyToMilady(strEndDate);*/
        serviceCounter++;
        if (all) {
            type = Type.RECEIVED;
        }
        farzinGetMessagePresenter = new FarzinGetMessagePresenter(App.CurentActivity, new MessageDataListener() {
            @Override
            public void newData() {
                try {
                    serviceCounter++;
                    setSyncDate(type);
                    if (all && serviceCounter == 2) {
                        type = Type.SENDED;
                        farzinGetMessagePresenter.GetFromServer(strStartDate, strEndDate, type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void noData() {
                try {
                    serviceCounter++;
                    setSyncDate(type);
                    if (all && serviceCounter == 2) {
                        type = Type.SENDED;
                        farzinGetMessagePresenter.GetFromServer(strStartDate, strEndDate, type);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailed(String message) {
                try {
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncFailed);
                    App.ShowMessage().ShowToast(message, ToastEnum.TOAST_LONG_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        farzinGetMessagePresenter.GetFromServer(strStartDate, strEndDate, type);

    }

    private void setSyncDate(Type type) {
        doProccess(type);
    }

    private void doProccess(Type type) {
        CustomFunction.getCurentDateTimeAsStringFormat(new GetDateTimeListener() {
            @Override
            public void onSuccess(String strDateTime) {
                countinuProcess(strDateTime, type);
            }

            @Override
            public void onFailed(String message) {
                String strDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
                countinuProcess(strDate, type);

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void countinuProcess(String strDateTime, Type type) {
        try {
            if (type == Type.RECEIVED) {
                getFarzinPrefrences().putReceiveMessageDataSyncDate(strDateTime);
                BaseActivity.dialogDataSyncing.serviceGetDataFinish();
            } else {
                getFarzinPrefrences().putSendMessageDataSyncDate(strDateTime);
                BaseActivity.dialogDataSyncing.serviceGetDataFinish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callDialogDataSyncing(int serviceCount) {

        runOnUiThread(() -> {

            BaseActivity.dialogDataSyncing = new DialogDataSyncing(App.CurentActivity, serviceCount, new MetaDataParentSyncListener() {
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
        MenuItem actionDownload = menu.findItem(R.id.action_download);

        if (settingType == MANUALLY.getValue()) {
            actionDownload.setVisible(true);
            actionSuccess.setVisible(false);
        } else {
            actionDownload.setVisible(false);
            actionSuccess.setVisible(true);
        }

        CustomFunction customFunction = new CustomFunction(App.CurentActivity);
        Drawable drawable = customFunction.ChengeDrawableColor(R.drawable.ic_success, R.color.color_White);
        actionSuccess.setIcon(drawable);

        Drawable drawableDownload = customFunction.ChengeDrawableColor(R.drawable.ic_download, R.color.color_White);
        actionDownload.setIcon(drawableDownload);
        actionDownload.setOnMenuItemClickListener(menuItem -> {
            farzinPrefrences.putMessageSettingStartDate(strStartDate);
            farzinPrefrences.putMessageSettingEndDate(strEndDate);
            if (type == Type.ALL) {
                getData(true, 2);
            } else {
                getData(false, 1);
            }
            return true;
        });
        actionSuccess.setOnMenuItemClickListener(menuItem -> {
            farzinPrefrences.putMessageSettingStartDate(strStartDate);
            if (settingType == SYNC.getValue()) {
                if (strStartDate != farzinPrefrences.getSendMessageDataSyncDate()) {
                    //farzinPrefrences.putSendMessageDataSyncDate(CustomFunction.JalalyToMilady(strStartDate));
                    farzinPrefrences.putSendMessageDataSyncDate(strStartDate);
                }
                if (strStartDate != farzinPrefrences.getReceiveMessageDataSyncDate()) {
                    //farzinPrefrences.putReceiveMessageDataSyncDate(CustomFunction.JalalyToMilady(strStartDate));
                    farzinPrefrences.putReceiveMessageDataSyncDate(strStartDate);
                }

                Finish(App.CurentActivity);

            } else if (settingType == AUTOMATIC.getValue()) {
                farzinPrefrences.putMessageSettingEndDate(strEndDate);
                Finish(App.CurentActivity);
            }

            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                if (settingType != SYNC.getValue()) {
                    Finish(App.CurentActivity);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (settingType != SYNC.getValue()) {
                Finish(App.CurentActivity);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
