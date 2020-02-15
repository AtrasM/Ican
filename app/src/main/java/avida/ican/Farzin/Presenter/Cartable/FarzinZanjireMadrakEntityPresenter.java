package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.List;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakEntityListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakEntityDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireEntityRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2019-08-04 at 10:54 AM
 */

public class FarzinZanjireMadrakEntityPresenter {
    private final long FAEILDDELAY = TimeValue.SecondsInMilli() * 5;
    private final ListenerZanjireMadrak listenerZanjireMadrak;
    private final int mainEtc;
    private final int mainEc;
    private ZanjireMadrakEntityListener zanjireMadrakEntityListener;
    private GetZanjireMadrakEntityFromServerPresenter getZanjireMadrakEntityFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private static int counterFailed = 0;
    private int MaxTry = 2;

    public FarzinZanjireMadrakEntityPresenter(int mainEtc, int mainEc, ListenerZanjireMadrak listenerZanjireMadrak) {
        this.mainEtc = mainEtc;
        this.mainEc = mainEc;
        this.listenerZanjireMadrak = listenerZanjireMadrak;
        initCartableHistoryListListener();
    }

    private void initCartableHistoryListListener() {
        getZanjireMadrakEntityFromServerPresenter = new GetZanjireMadrakEntityFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        zanjireMadrakEntityListener = new ZanjireMadrakEntityListener() {


            @Override
            public void onSuccess(StructureZanjireEntityRES structureZanjireEntityRES) {
                if (structureZanjireEntityRES.getPeyro().size() == 0 && structureZanjireEntityRES.getDarErtebat().size() == 0 && structureZanjireEntityRES.getAtf().size() == 0 && structureZanjireEntityRES.getPeyvast().size() == 0) {
                    listenerZanjireMadrak.noData();
                    counterFailed = 0;
                } else {
                    SaveData(structureZanjireEntityRES);
                }
            }

            @Override
            public void onFailed(String message) {
                if (message.contains(Resorse.getString(R.string.error_permision))) {
                    listenerZanjireMadrak.noData();
                } else {
                    if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                        listenerZanjireMadrak.noData();
                    } else {
                        reGetData();
                    }
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    listenerZanjireMadrak.noData();
                } else {
                    reGetData();
                }
            }
        };
    }

    public void getZanjireMadrakFromServer() {
        getZanjireMadrakEntityFromServerPresenter.GetZanjireEntityList(mainEtc, mainEc, zanjireMadrakEntityListener);
    }

    public List<StructureZanjireMadrakEntityDB> getZanjireMadrakEntityList(ZanjireMadrakFileTypeEnum zanjireMadrakFileTypeEnum) {
        return farzinCartableQuery.getZanjireMadrakEntity(mainEtc, mainEc, zanjireMadrakFileTypeEnum);
    }

    private void SaveData(StructureZanjireEntityRES structureZanjireEntityRES) {

        farzinCartableQuery.saveZanjireMadrakEnity(mainEtc, mainEc, structureZanjireEntityRES, new ZanjireMadrakQuerySaveListener() {
            @Override
            public void onSuccess() {

                listenerZanjireMadrak.newData();
                counterFailed = 0;
            }

            @Override
            public void onExisting() {
                listenerZanjireMadrak.noData();
                counterFailed = 0;
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    listenerZanjireMadrak.noData();
                } else {
                    reGetData();
                }

            }

            @Override
            public void onCancel() {
                listenerZanjireMadrak.noData();
            }
        });
    }


    private void reGetData() {
        counterFailed++;
        if (counterFailed >= MaxTry) {
            listenerZanjireMadrak.noData();
            counterFailed = 0;
        } else {
            App.getHandlerMainThread().postDelayed(() -> getZanjireMadrakFromServer(), FAEILDDELAY);
        }

    }

}
