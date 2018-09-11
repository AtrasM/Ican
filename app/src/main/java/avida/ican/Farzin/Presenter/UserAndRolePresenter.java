package avida.ican.Farzin.Presenter;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.MetaDataNameEnum;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
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
            structuresMain = new FarzinMetaDataQuery(App.CurentActivity).getUserAndRoleList();

            if (structuresMain.isEmpty()) {
                new FarzinMetaDataQuery(App.CurentActivity).Sync(new MetaDataSyncListener() {
                    @Override
                    public void onSuccess(MetaDataNameEnum metaDataNameEnum) {

                    }

                    @Override
                    public void onFailed(MetaDataNameEnum metaDataNameEnum) {

                    }

                    @Override
                    public void onCancel(MetaDataNameEnum metaDataNameEnum) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.Syncing), SnackBarEnum.SNACKBAR_SHORT_TIME);
                listenerUserAndRollDialog.onFailed();
            } else {
                if (structuresSelect.size() > 0) {
                    for (int k = 0; k < structuresSelect.size(); k++) {
                        for (int i = 0; i < structuresMain.size(); i++) {
                            if (structuresMain.get(i).getUser_ID() == structuresSelect.get(k).getUser_ID() && structuresMain.get(i).getRole_ID() == structuresSelect.get(k).getRole_ID()) {
                                structuresMain.get(i).setSelected(true);
                                break;
                            }
                        }
                    }
                    listenerUserAndRollDialog.onSuccess(structuresMain, structuresSelect);
                } else {
                    listenerUserAndRollDialog.onSuccess(structuresMain, structuresSelect);
                }

            }
        } else {
            listenerUserAndRollDialog.onSuccess(new ArrayList<>(structuresMain), new ArrayList<>(structuresSelect));
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
        query = CustomFunction.convertArabicCharToPersianChar(query);
        final String finalQuery = query;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < structuresMain.size(); i++) {
                    if (CustomFunction.convertArabicCharToPersianChar(structuresMain.get(i).getFirstName()).contains(finalQuery) || CustomFunction.convertArabicCharToPersianChar(structuresMain.get(i).getLastName()).contains(finalQuery) || CustomFunction.convertArabicCharToPersianChar(structuresMain.get(i).getRoleName()).contains(finalQuery)) {
                        structuresSearch.add(structuresMain.get(i));
                    }
                }
                listenerUserAndRollSearch.onSuccess(structuresSearch);
                return null;
            }
        }.execute();
    }

}
