package avida.ican.Farzin.View;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

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

import static avida.ican.Farzin.View.Enum.SettingEnum.CUSTOM;
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
    private static int actionCode = -1;
    private FarzinPrefrences farzinPrefrences;
    private int actionCodeDefPos = 0;
    private int countOfGetDocumentDefPos = 0;
    private int settingType = -1;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_document_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingType = getIntent().getIntExtra(PutExtraEnum.Settingtype.getValue(), -1);
        farzinCartableQuery = new FarzinCartableQuery();
        farzinPrefrences = getFarzinPrefrences();
        initTollBar(Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        initView();
    }

    private void initView() {
        strStartDate = farzinPrefrences.getCartableDocumentDataSyncDate();
        txtStartDate.setText(CustomFunction.MiladyToJalaly(strStartDate));
        initDateView();
        initSpActions();

        lnAction.setOnClickListener(view -> {
            spActions.performClick();
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
        structureActionsDBS = (ArrayList<StructureCartableDocumentActionsDB>) farzinCartableQuery.getAllDocumentActions();
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
                if (strStartDate != farzinPrefrences.getCartableDocumentDataSyncDate()) {
                    farzinPrefrences.putCartableDocumentDataSyncDate(CustomFunction.JalalyToMilady(strStartDate));
                }

                farzinPrefrences.putDefultActionCode(actionCode);
                Finish(App.CurentActivity);

            } else if (settingType == CUSTOM.getValue()) {

            }

            return true;
        });
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
