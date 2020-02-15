package avida.ican.Farzin.Presenter.Cartable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-09-26 at 10:34 AM
 */

public class FarzinHameshListPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 10;
    private final ListenerHamesh listenerHamesh;
    private final int Etc;
    private final int Ec;
    private CartableHameshListListener cartableHameshListListener;
    private GetCartableHameshFromServerPresenter getCartableHameshFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private static int counterFailed = 0;
    private int MaxTry = 2;

    public FarzinHameshListPresenter(int Etc, int Ec, ListenerHamesh listenerHamesh) {
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerHamesh = listenerHamesh;
        initCartableHameshListListener();
    }

    private void initCartableHameshListListener() {
        getCartableHameshFromServerPresenter = new GetCartableHameshFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableHameshListListener = new CartableHameshListListener() {

            @Override
            public void onSuccess(ArrayList<StructureHameshRES> structureHameshRES) {
                if (structureHameshRES.size() == 0) {
                    listenerHamesh.noData();
                    counterFailed = 0;
                } else {
                    List<StructureHameshDB> structureHameshDBS = farzinCartableQuery.getImageHamesh(Etc, Ec);
                    for (StructureHameshDB structureHameshDB : structureHameshDBS) {
                        String filePath = structureHameshDB.getFilePath();
                        File file = new File(filePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    farzinCartableQuery.deletCartableHameshList(Etc, Ec);
                    SaveData(structureHameshRES);
                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    }, 300);
                } else {
                    reGetData();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(() -> onCancel(), 300);
                } else {
                    reGetData();
                }
            }
        };
    }

    public void GetHameshFromServer() {
        getCartableHameshFromServerPresenter.GetHameshList(Etc, Ec, cartableHameshListListener);
    }

    public List<StructureHameshDB> GetHameshList(int Start, int Count) {
        return farzinCartableQuery.getHamesh(Etc, Ec, Start, Count);
    }


    private void SaveData(final ArrayList<StructureHameshRES> structureHameshsRES) {

        final StructureHameshRES structureHameshRES = structureHameshsRES.get(0);
        farzinCartableQuery.saveHamesh(structureHameshRES, Etc, Ec, new HameshQuerySaveListener() {
            @Override
            public void onSuccess(StructureHameshDB structureHameshDB) {
                structureHameshsRES.remove(0);
                if (structureHameshsRES.size() > 0) {
                    SaveData(structureHameshsRES);
                } else {
                    listenerHamesh.newData(structureHameshDB);
                    counterFailed = 0;
                }
            }

            @Override
            public void onExisting() {
                listenerHamesh.noData();
                counterFailed = 0;
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    }, 300);
                } else {
                    reGetData();
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected || App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandlerMainThread().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    }, 300);
                } else {
                    reGetData();
                }
            }
        });

    }


    private void reGetData() {
        counterFailed++;
        if (counterFailed >= MaxTry) {
            listenerHamesh.noData();
            counterFailed = 0;
        } else {
            App.getHandlerMainThread().postDelayed(() -> GetHameshFromServer(), DELAY);
        }
    }


}
