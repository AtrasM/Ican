package avida.ican.Farzin.View;

import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentDataListener;
import avida.ican.Farzin.Model.Interface.GetDateTimeListener;
import avida.ican.Farzin.Model.Interface.MetaDataParentSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableDocumentListPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

import static avida.ican.Farzin.View.Enum.SettingEnum.AUTOMATIC;
import static avida.ican.Farzin.View.Enum.SettingEnum.MANUALLY;
import static avida.ican.Farzin.View.Enum.SettingEnum.SYNC;

public class ActivityDocumentSetting extends BaseToolbarActivity {

    @BindView(R.id.ln_count_of_get_document)
    LinearLayout lnCountOfGetDocument;
    @BindView(R.id.sp_count_of_get_document)
    Spinner spCountOfGetDocument;
    @BindView(R.id.ln_action)
    LinearLayout lnAction;
    @BindView(R.id.sp_actions)
    Spinner spActions;
    @BindView(R.id.ln_start_date)
    LinearLayout lnStartDate;
    @BindView(R.id.txt_start_date)
    TextView txtStartDate;
    @BindView(R.id.ln_end_date)
    LinearLayout lnEndDate;
    @BindView(R.id.txt_end_date)
    TextView txtEndDate;
    @BindString(R.string.TitleActivityDocumentSetting)
    String Title;
    private static String strStartDate = "";
    private static String strEndDate = "";
    private ArrayList<StructureCartableDocumentActionsDB> structureActionsDBS = new ArrayList<>();
    private ArrayList<String> actionsList = new ArrayList<>();
    private ArrayList<String> countOfGetDocumentList = new ArrayList<>();
    private FarzinCartableQuery farzinCartableQuery;
    private int actionCode = -1;
    private FarzinPrefrences farzinPrefrences;
    private int actionCodeDefPos = 0;
    private int countOfGetDocumentDefPos = 0;
    private int settingType = -1;
    private FarzinCartableDocumentListPresenter farzinCartableDocumentListPresenter;
    private DatePickerDialog endDatePickerDialog;
    private DatePickerDialog startDatePickerDialog;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_document_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingType = getIntent().getIntExtra(PutExtraEnum.SettingType.getValue(), -1);
        farzinCartableQuery = new FarzinCartableQuery();
        farzinPrefrences = getFarzinPrefrences();
        initTollBar(Title);
        if (settingType == SYNC.getValue()) {
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
        } else if (settingType == AUTOMATIC.getValue()) {
            lnEndDate.setVisibility(View.GONE);
            lnStartDate.setVisibility(View.GONE);
        }


        strStartDate = farzinPrefrences.getCartableDocumentSettingStartDate();
        strEndDate = farzinPrefrences.getCartableDocumentSettingEndDate();

        txtStartDate.setText(CustomFunction.MiladyToJalaly(strStartDate));
        txtEndDate.setText(CustomFunction.MiladyToJalaly(strEndDate));

        initDateView();
        initSpActions();

        lnAction.setOnClickListener(view -> {
            spActions.performClick();
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

    private void initSpActions() {
        initSpActionsData();
        ArrayAdapter<String> adapterActions = new CustomFunction(App.CurentActivity).getSpinnerAdapter(actionsList);
        spActions.setAdapter(adapterActions);
        spActions.setSelection(actionCodeDefPos);
        spActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                actionCode = structureActionsDBS.get(i).getActionCode();
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

    private void initSpActionsData() {
        int defActionCode = farzinPrefrences.getDefultActionCode();
        StructureCartableDocumentActionsDB structureActionDB = new StructureCartableDocumentActionsDB();
        structureActionDB.setActionCode(-1);
        structureActionDB.setActionName(Resorse.getString(R.string.title_all_data));
        structureActionsDBS = (ArrayList<StructureCartableDocumentActionsDB>) farzinCartableQuery.getAllDocumentActionsForCardTable(true);
        structureActionsDBS.add(0, structureActionDB);
        for (int i = 0; i < structureActionsDBS.size(); i++) {
            if (structureActionsDBS.get(i).getActionCode() == defActionCode) {
                actionCodeDefPos = i;
            }
            actionsList.add(structureActionsDBS.get(i).getActionName());
        }
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

    private void getData(boolean isContinue) {
        App.canBack = false;
        if (!isContinue) {
            callDialogDataSyncing();
            farzinPrefrences.putDefultActionCode(actionCode);
        } else {
            StructureInboxDocumentDB inboxDocumentDB = farzinCartableDocumentListPresenter.getLastRecord();
            if (inboxDocumentDB != null) {
                strStartDate = CustomFunction.convertLongTimeToCustomFormat(inboxDocumentDB.getReceiveDate().getTime(), SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
                //CustomLogger.setLog("strStartDate in getData continue= " + strStartDate);
            }
        }

        farzinCartableDocumentListPresenter = new FarzinCartableDocumentListPresenter(App.CurentActivity, new CartableDocumentDataListener() {
            @Override
            public void newData() {
                doProccess(true);
            }

            @Override
            public void noData() {
                doProccess(false);
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
        farzinCartableDocumentListPresenter.getFromServer(strStartDate, strEndDate, true);

    }

    private void doProccess(boolean hasNewData) {
        if (hasNewData) {
            getData(true);
        } else {
            CustomFunction.getCurentDateTimeAsStringFormat(new GetDateTimeListener() {
                @Override
                public void onSuccess(String strDateTime) {
                    countinuProcess(strDateTime);
                }

                @Override
                public void onFailed(String message) {
                    String strDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
                    countinuProcess(strDate);

                }

                @Override
                public void onCancel() {

                }
            });
        }

    }

    private void countinuProcess(String strDateTime) {
        try {
            getFarzinPrefrences().putCartableDocumentDataSyncDate(strDateTime);
            farzinPrefrences.putDefultActionCode(-1);
            BaseActivity.dialogDataSyncing.serviceGetDataFinish(DataSyncingNameEnum.SyncCartableDocument);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callDialogDataSyncing() {

        runOnUiThread(() -> {

            BaseActivity.dialogDataSyncing = new DialogDataSyncing(App.CurentActivity, 1, false, new MetaDataParentSyncListener() {
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
            farzinPrefrences.putCartableDocumentSettingStartDate(strStartDate);
            farzinPrefrences.putCartableDocumentSettingEndDate(strEndDate);
            getData(false);
            return true;
        });
        actionSuccess.setOnMenuItemClickListener(menuItem -> {
            farzinPrefrences.putCartableDocumentSettingStartDate(strStartDate);

            if (settingType == SYNC.getValue()) {
                if (strStartDate != farzinPrefrences.getCartableDocumentDataSyncDate()) {
                    farzinPrefrences.putCartableDocumentDataSyncDate(strStartDate);
                }
                farzinPrefrences.putDefultActionCode(actionCode);
                Finish(App.CurentActivity);

            } else if (settingType == AUTOMATIC.getValue()) {
                farzinPrefrences.putCartableDocumentSettingEndDate(strEndDate);
                farzinPrefrences.putDefultActionCode(actionCode);
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
