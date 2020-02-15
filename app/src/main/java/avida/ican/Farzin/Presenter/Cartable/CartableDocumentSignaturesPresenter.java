package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentActionsQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentSignatureQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentSignaturesListener;
import avida.ican.Farzin.Model.Interface.Cartable.GetCartableDocumentSignaturesListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentActionsListener;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentActionsDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentSignatureDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureCartableDocumentActionRES;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureSignatureRES;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2019-05-26 at 4:21 PM
 */

public class CartableDocumentSignaturesPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final int Etc;
    private GetCartableDocumentSignaturesListener getCartableDocumentSignaturesListener;
    private GetSignatureListFromServerPresenter getSignatureListFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private CartableDocumentSignaturesListener cartableDocumentSignaturesListener;

    public CartableDocumentSignaturesPresenter(int Etc) {
        this.Etc = Etc;
        initCartableDocumentSignaturesListener();
    }

    private void initCartableDocumentSignaturesListener() {
        getSignatureListFromServerPresenter = new GetSignatureListFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        getCartableDocumentSignaturesListener = new GetCartableDocumentSignaturesListener() {


            @Override
            public void onSuccess(ArrayList<StructureSignatureRES> structureSignatureListRES) {
                if (structureSignatureListRES.size() > 0) {
                    if (!farzinCartableQuery.IsDocumentSignatureExist(Etc)) {
                        SaveData(structureSignatureListRES);
                    } else {
                        cartableDocumentSignaturesListener.onSuccess();
                    }
                } else {
                    cartableDocumentSignaturesListener.onSuccess();
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

    public void GetDocumentSignatureFromServer(CartableDocumentSignaturesListener cartableDocumentSignaturesListener) {
        this.cartableDocumentSignaturesListener = cartableDocumentSignaturesListener;
        getSignatureListFromServerPresenter.CallRequest(Etc, getCartableDocumentSignaturesListener);
    }

    public List<StructureCartableDocumentSignatureDB> GetDocumentSignatures() {
        return farzinCartableQuery.getDocumentSignatures(Etc);
    }

    public List<StructureCartableDocumentSignatureDB> GetAllDocumentSignatures() {
        return farzinCartableQuery.getAllDocumentSignatures();
    }


    private void SaveData(final ArrayList<StructureSignatureRES> structureSignaturesRES) {

        final StructureSignatureRES structureSignatureRES = structureSignaturesRES.get(0);

        farzinCartableQuery.saveDocumentSignature(structureSignatureRES, Etc, new CartableDocumentSignatureQuerySaveListener() {

            @Override
            public void onSuccess(StructureCartableDocumentSignatureDB structureCartableDocumentSignatureDB) {
                structureSignaturesRES.remove(0);
                if (structureSignaturesRES.size() > 0) {
                    SaveData(structureSignaturesRES);
                } else {
                    cartableDocumentSignaturesListener.onSuccess();
                }
            }

            @Override
            public void onExisting() {
                cartableDocumentSignaturesListener.onSuccess();
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
                GetDocumentSignatureFromServer(cartableDocumentSignaturesListener);
            }
        }, DELAY);
    }


}
