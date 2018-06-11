package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Farzin.View.Interface.ListenerUserAndRollSearch;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-05-15 at 10:11 AM
 */

public class UserAndRolePresenter {
    private List<StructureUserAndRoleDB> structuresMain = new ArrayList<>();
    private List<StructureUserAndRoleDB> structuresSelect = new ArrayList<>();
    private List<StructureUserAndRoleDB> structuresSearch = new ArrayList<>();
    private ListenerUserAndRoll listenerUserAndRollDialog;
    private ListenerUserAndRollSearch listenerUserAndRollSearch;

    public UserAndRolePresenter(List<StructureUserAndRoleDB> structuresMain, List<StructureUserAndRoleDB> structuresSelect) {
        this.structuresMain = structuresMain;
        this.structuresSelect = structuresSelect;
    }

    public UserAndRolePresenter onListener(ListenerUserAndRoll listenerUserAndRoll) {
        this.listenerUserAndRollDialog = listenerUserAndRoll;
        return this;
    }


    public UserAndRolePresenter execute() {
        if (structuresMain.isEmpty()) {
            structuresMain = new FarzinMetaDataQuery().getUserAndRoleList();
            if (structuresMain.isEmpty()) {
                new FarzinMetaDataQuery().Sync();
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.Syncing), SnackBarEnum.SNACKBAR_SHORT_TIME);
                listenerUserAndRollDialog.onFailed();
            } else {
                listenerUserAndRollDialog.onSuccess(structuresMain, structuresSelect);
            }
        } else {
            listenerUserAndRollDialog.onSuccess(new ArrayList<StructureUserAndRoleDB>(structuresMain), new ArrayList<StructureUserAndRoleDB>(structuresSelect));
        }

        return this;
    }

    public void Search(String Query, ListenerUserAndRollSearch listenerUserAndRollSearch) {
        this.listenerUserAndRollSearch = listenerUserAndRollSearch;
        Searching(Query);
    }

    @SuppressLint("StaticFieldLeak")
    private void Searching(String query) {
        structuresSearch = new ArrayList<>();
        final CustomFunction customFunction = new CustomFunction();
        query = customFunction.convertArabicCharToPersianChar(query);
        final String finalQuery = query;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < structuresMain.size(); i++) {
                    if (customFunction.convertArabicCharToPersianChar(structuresMain.get(i).getFirstName()).contains(finalQuery) || customFunction.convertArabicCharToPersianChar(structuresMain.get(i).getLastName()).contains(finalQuery) || customFunction.convertArabicCharToPersianChar(structuresMain.get(i).getOrganizationRoleName()).contains(finalQuery)) {
                        structuresSearch.add(structuresMain.get(i));
                    }
                }
                listenerUserAndRollSearch.onSuccess(structuresSearch);
                return null;
            }
        }.execute();
    }

}
