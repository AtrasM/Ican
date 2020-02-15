package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.List;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-10-15 at 3:40 PM
 */

public class FarzinZanjireMadrakPresenter {
    private final long FAEILDDELAY = TimeValue.SecondsInMilli() * 5;
    private final ListenerZanjireMadrak listenerZanjireMadrak;
    private final int Etc;
    private final int Ec;
    private ZanjireMadrakListener zanjireMadrakListener;
    private GetZanjireMadrakFromServerPresenter getZanjireMadrakFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private static int counterFailed = 0;
    private int MaxTry = 2;

    public FarzinZanjireMadrakPresenter(int Etc, int Ec, ListenerZanjireMadrak listenerZanjireMadrak) {
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerZanjireMadrak = listenerZanjireMadrak;
        initCartableHistoryListListener();
    }

    private void initCartableHistoryListListener() {
        getZanjireMadrakFromServerPresenter = new GetZanjireMadrakFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        zanjireMadrakListener = new ZanjireMadrakListener() {
            @Override
            public void onSuccess(StructureZanjireMadrakRES structureZanjireMadrakRES) {
                if (structureZanjireMadrakRES.getPeyro().size() == 0 && structureZanjireMadrakRES.getDarErtebat().size() == 0 && structureZanjireMadrakRES.getAtf().size() == 0 && structureZanjireMadrakRES.getPeyvast().size() == 0 && structureZanjireMadrakRES.getIndicatorScanedFile().size() == 0) {
                    listenerZanjireMadrak.noData();
                    counterFailed = 0;
                } else {
                    SaveData(structureZanjireMadrakRES);
                }
            }

            @Override
            public void onFailed(String message) {
                if (message.contains(Resorse.getString(R.string.error_permision))) {
                    listenerZanjireMadrak.noData();
                }else{
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
        getZanjireMadrakFromServerPresenter.GetZanjireMadrakList(Etc, Ec, zanjireMadrakListener);
    }

    public List<StructureZanjireMadrakFileDB> getZanjireMadrakList(ZanjireMadrakFileTypeEnum zanjireMadrakFileTypeEnum) {
        return farzinCartableQuery.getZanjireMadrak(Etc, Ec, zanjireMadrakFileTypeEnum);
    }

    private void SaveData(StructureZanjireMadrakRES structureZanjireMadrakRES) {

        farzinCartableQuery.saveZanjireMadrak(structureZanjireMadrakRES, Etc, Ec, new ZanjireMadrakQuerySaveListener() {
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
