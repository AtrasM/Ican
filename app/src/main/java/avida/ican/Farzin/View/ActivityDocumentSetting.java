package avida.ican.Farzin.View;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.R;
import butterknife.BindView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.ArrayList;

public class ActivityDocumentSetting extends BaseToolbarActivity implements DatePickerDialog.OnDateSetListener {

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

    private String strDate = "";
    private ArrayList<StructureCartableDocumentActionsDB> structureActionsDBS = new ArrayList<>();
    private ArrayList<String> actionsList = new ArrayList<>();
    private ArrayList<String> countOfGetDocumentList = new ArrayList<>();
    private FarzinCartableQuery farzinCartableQuery;
    private int actionCode = -1;
    private FarzinPrefrences farzinPrefrences;
    private int actionCodeDefPos = 0;
    private int countOfGetDocumentDefPos = 0;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_document_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        farzinCartableQuery = new FarzinCartableQuery();
        farzinPrefrences = getFarzinPrefrences();
        initView();
    }

    private void initView() {
        strDate = farzinPrefrences.getCartableDocumentDataSyncDate();
        txtStartDate.setText(strDate.replace("T"," "));
        lnStartDate.setOnClickListener(view -> {
            PersianCalendar persianCalendar = new PersianCalendar();
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                    this,
                    persianCalendar.getPersianYear(),
                    persianCalendar.getPersianMonth(),
                    persianCalendar.getPersianDay()
            );
            datePickerDialog.show(getFragmentManager(), PutExtraEnum.Datepickerdialog.getValue());
        });
        initSpActions();
        initSpCountOfGetDocument();
        lnCountOfGetDocument.setOnClickListener(view -> {
            spCountOfGetDocument.performClick();
        });
        lnAction.setOnClickListener(view -> {
            spActions.performClick();
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
                farzinPrefrences.putDefultActionCode(actionCode);
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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        strDate = CustomFunction.convertDateToIso8601Format(year, monthOfYear, dayOfMonth);
        txtStartDate.setText(strDate.replace("T"," "));
        farzinPrefrences.putCartableDocumentDataSyncDate(strDate);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
