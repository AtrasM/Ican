package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHistoryQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableHistoryDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureGraphRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHistoryListRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerGraf;
import avida.ican.Ican.App;
import avida.ican.Ican.Model.XmlToObject;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-10-06 at 1:23 PM
 */

public class FarzinHistoryListPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final ListenerGraf listenerGraf;
    private final int Etc;
    private final int Ec;
    private CartableHistoryListListener cartableHistoryListListener;
    private Handler handler = new Handler();
    private GetCartableHistoryFromServerPresenter getCartableHistoryFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;

    public FarzinHistoryListPresenter(int Etc, int Ec, ListenerGraf listenerGraf) {
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerGraf = listenerGraf;
        initCartableHistoryListListener();
    }

    private void initCartableHistoryListListener() {
        getCartableHistoryFromServerPresenter = new GetCartableHistoryFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableHistoryListListener = new CartableHistoryListListener() {

            @Override
            public void onSuccess(ArrayList<StructureGraphRES> structureGraphRES, String xml) {
                if (structureGraphRES.size() == 0) {
                    listenerGraf.noData();
                } else {
                    SaveData(xml);
                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    },300);
                } else {
                    reGetData();
                }
            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    },300);
                } else {
                    reGetData();
                }
            }
        };
    }

    public void GetCartableHistoryFromServer() {
        getCartableHistoryFromServerPresenter.GetHistortList(Etc, Ec, cartableHistoryListListener);
    }

    public List<StructureCartableHistoryDB> GetCartableHistoryList() {
        return farzinCartableQuery.getCartableHistory(Etc, Ec, -1, -1);

    }


    private void SaveData(final String xml) {

        farzinCartableQuery.saveCartableHistory(xml, Etc, Ec, new CartableHistoryQuerySaveListener() {

            @Override
            public void onSuccess() {
                XmlToObject xmlToObject = new XmlToObject();
                StructureHistoryListRES structureHistoryListRES = xmlToObject.DeserializationGsonXml(xml, StructureHistoryListRES.class);
                ArrayList<StructureHistoryListRES> HistoryListRES = new ArrayList<>();
                HistoryListRES.add(structureHistoryListRES);
                listenerGraf.newData(HistoryListRES);
            }

            @Override
            public void onExisting() {
                listenerGraf.noData();
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    },300);
                } else {
                    reGetData();
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected&&App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    },300);
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
                GetCartableHistoryFromServer();
            }
        }, DELAY);
    }


    public StructureHistoryListRES initObject(String dataXml) {
        XmlToObject xmlToObject = new XmlToObject();
        return xmlToObject.DeserializationGsonXml(dataXml, StructureHistoryListRES.class);

    }
}
