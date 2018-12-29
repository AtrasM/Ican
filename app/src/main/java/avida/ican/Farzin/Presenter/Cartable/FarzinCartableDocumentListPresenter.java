package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentRefreshListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-12-26 at 5:30 PM
 */

public class FarzinCartableDocumentListPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private Handler handler = new Handler();
    private FarzinCartableQuery farzinCartableQuery;
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private CartableDocumentRefreshListener cartableDocumentRefreshListener;
    private CartableDocumentListListener cartableDocumentListListenerMain;
    private Status status;
    private int Count = 10;
    private int existCont = 0;
    private int dataSize = 0;

    public FarzinCartableDocumentListPresenter(CartableDocumentRefreshListener cartableDocumentRefreshListener) {
        this.cartableDocumentRefreshListener = cartableDocumentRefreshListener;
        initCartableHameshListListener();
    }

    private void initCartableHameshListListener() {
        getCartableDocumentFromServerPresenter = new GetCartableDocumentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentListListenerMain = new CartableDocumentListListener() {
            @Override
            public void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {
                existCont = 0;
                dataSize = inboxCartableDocumentList.size();
                if (dataSize == 0) {
                    cartableDocumentRefreshListener.noData();
                } else {
                    SaveData(inboxCartableDocumentList);

                }
            }

            @Override
            public void onFailed(String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
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
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onCancel();
                        }
                    }, 300);
                } else {
                    reGetData();
                }
            }
        };
    }

    public void GetFromServer() {
        getCartableDocumentFromServerPresenter.GetCartableDocumentList(Count, cartableDocumentListListenerMain);
    }

    public List<StructureInboxDocumentDB> GetFromLocal(int actionCode, long Start, long Count) {
        return farzinCartableQuery.getCartableDocuments(actionCode, null, Start, Count);
    }

    private void SaveData(final ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        final StructureInboxDocumentRES structureInboxDocumentRES = inboxCartableDocumentList.get(0);

        if (structureInboxDocumentRES.isRead()) {
            status = Status.READ;
        } else {
            if (!getFarzinPrefrences().isDataForFirstTimeSync()) {
                status = Status.IsNew;
            } else {
                status = Status.UnRead;
            }

        }

        farzinCartableQuery.saveCartableDocument(structureInboxDocumentRES, Type.RECEIVED, status, new CartableDocumentQuerySaveListener() {

            @Override
            public void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB) {

                if (inboxCartableDocumentList.size() > 0) {
                    SaveData(inboxCartableDocumentList);
                } else {
                    cartableDocumentRefreshListener.newData();
                }
            }

            @Override
            public void onExisting() {
                existCont++;
                inboxCartableDocumentList.remove(0);
                if (inboxCartableDocumentList.size() == 0) {
                    if (existCont == dataSize) {
                        cartableDocumentRefreshListener.noData();
                    } else {
                        cartableDocumentRefreshListener.newData();
                    }

                } else {
                    SaveData(inboxCartableDocumentList);
                }

            }

            @Override
            public void onFailed(final String message) {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    cartableDocumentRefreshListener.onFailed(message);
                } else {
                    reGetData();
                }

            }

            @Override
            public void onCancel() {
                if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    cartableDocumentRefreshListener.onFailed("");
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
                GetFromServer();
            }
        }, DELAY);

    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
