package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionsQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentActionsListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-11-20 at 10:34 AM
 */

public class CartableDocumentActionsPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final int Etc;
    private CartableDocumentActionListListener cartableDocumentActionListListener;
    private GetListOfDocumentActionsFromServerPresenter getListOfDocumentActionsFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private DocumentActionsListener documentActionsFromServerListener;

    public CartableDocumentActionsPresenter(int Etc) {
        this.Etc = Etc;
        initCartableActionListListener();
    }

    private void initCartableActionListListener() {
        getListOfDocumentActionsFromServerPresenter = new GetListOfDocumentActionsFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentActionListListener = new CartableDocumentActionListListener() {

            @Override
            public void onSuccess(ArrayList<StructureCartableDocumentActionRES> cartableDocumentActionsRES) {
                if (cartableDocumentActionsRES.size() > 0) {
                    if (!farzinCartableQuery.IsDocumentActionsExist(Etc)) {
                        SaveData(cartableDocumentActionsRES);
                    } else {
                        documentActionsFromServerListener.onSuccess();
                    }
                } else {
                    documentActionsFromServerListener.onSuccess();
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
        };
    }

    public void GetDocumentActionsFromServer(DocumentActionsListener documentActionsFromServerListener) {
        this.documentActionsFromServerListener = documentActionsFromServerListener;
        getListOfDocumentActionsFromServerPresenter.CallRequest(Etc, cartableDocumentActionListListener);

    }

    public List<StructureCartableDocumentActionsDB> getDocumentActions() {
        return farzinCartableQuery.getDocumentActions(Etc);
    }


    public List<StructureCartableDocumentActionsDB> getAllDocumentActions() {
        return farzinCartableQuery.getAllDocumentActions();
    }

    public List<StructureCartableDocumentActionsDB> getAllDocumentActionsForSend(boolean isActiveForSend) {
        return farzinCartableQuery.getAllDocumentActionsForSend(isActiveForSend);
    }


    private void SaveData(final ArrayList<StructureCartableDocumentActionRES> cartableDocumentActionsRES) {

        final StructureCartableDocumentActionRES structureCartableDocumentActionRES = cartableDocumentActionsRES.get(0);

        farzinCartableQuery.saveDocumentAction(structureCartableDocumentActionRES, Etc, new CartableDocumentActionsQuerySaveListener() {
            @Override
            public void onSuccess(StructureCartableDocumentActionsDB structureCartableDocumentActionsDB) {
                cartableDocumentActionsRES.remove(0);
                if (cartableDocumentActionsRES.size() > 0) {
                    SaveData(cartableDocumentActionsRES);
                } else {
                    documentActionsFromServerListener.onSuccess();
                }
            }

            @Override
            public void onExisting() {
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
                    //ShowToast("WatingForNetwork");
                    onFailed("");
                } else {
                    reGetData();
                }
            }

        });

    }


    private void reGetData() {

        App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetDocumentActionsFromServer(documentActionsFromServerListener);
            }
        }, DELAY);
    }


}
