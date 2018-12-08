package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.ZanjireMadrakFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakListener;
import avida.ican.Farzin.Model.Interface.Cartable.ZanjireMadrakQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureZanjireMadrakFileDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureZanjireMadrakRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerGraf;
import avida.ican.Farzin.View.Interface.Cartable.ListenerZanjireMadrak;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-10-15 at 3:40 PM
 */

public class FarzinZanjireMadrakPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final ListenerZanjireMadrak listenerZanjireMadrak;
    private final int Etc;
    private final int Ec;
    private ZanjireMadrakListener zanjireMadrakListener;
    private Handler handler = new Handler();
    private GetZanjireMadrakFromServerPresenter getZanjireMadrakFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;

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
                if (structureZanjireMadrakRES.getPeyro().size() == 0 && structureZanjireMadrakRES.getDarErtebat().size() == 0 && structureZanjireMadrakRES.getAtf().size() == 0 && structureZanjireMadrakRES.getPeyvast().size() == 0) {
                    listenerZanjireMadrak.noData();
                } else {
                    SaveData(structureZanjireMadrakRES);
                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    onFailed("");
                } else {
                    reGetData();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    onFailed("");
                } else {
                    reGetData();
                }
            }
        };
    }

    public void GetZanjireMadrakFromServer() {
        getZanjireMadrakFromServerPresenter.GetZanjireMadrakList(Etc, Ec, zanjireMadrakListener);
    }

    public List<StructureZanjireMadrakFileDB> GetZanjireMadrakList(ZanjireMadrakFileTypeEnum zanjireMadrakFileTypeEnum) {
        return farzinCartableQuery.getZanjireMadrak(Etc, Ec, zanjireMadrakFileTypeEnum);
    }

    private void SaveData(StructureZanjireMadrakRES structureZanjireMadrakRES) {

        farzinCartableQuery.saveZanjireMadrak(structureZanjireMadrakRES, Etc, Ec, new ZanjireMadrakQuerySaveListener() {
            @Override
            public void onSuccess() {

                listenerZanjireMadrak.newData();
            }

            @Override
            public void onExisting() {
                listenerZanjireMadrak.noData();
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    //ShowToast("WatingForNetwork");
                    onFailed("");
                } else {
                    reGetData();
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    onCancel();
                } else {
                    reGetData();
                }
            }
        });

    }


    private void reGetData() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetZanjireMadrakFromServer();
            }
        }, DELAY);
    }


}
