package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentQuerySaveListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableHameshListListener;
import avida.ican.Farzin.Model.Interface.Cartable.HameshQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentContentBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureHameshRES;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-12-05 at 10:28 AM
 */

public class CartableDocumentContentPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final ListenerCartableDocumentContent listenerCartableDocumentContent;
    private final int Etc;
    private final int Ec;
    private CartableDocumentContentListener cartableDocumentContentListener;
    private Handler handler = new Handler();
    private GetCartableDocumentContentFromServerPresenter getCartableDocumentContentFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;

    public CartableDocumentContentPresenter(int Etc, int Ec, ListenerCartableDocumentContent listenerCartableDocumentContent) {
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerCartableDocumentContent = listenerCartableDocumentContent;
        initCartableHameshListListener();
    }

    private void initCartableHameshListListener() {
        getCartableDocumentContentFromServerPresenter = new GetCartableDocumentContentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentContentListener = new CartableDocumentContentListener() {


            @Override
            public void onSuccess(String FileBinary) {
                ///listenerCartableDocumentContent.newData(FileBinary);
                SaveData(FileBinary);
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

    public void GetFromServer() {
        getCartableDocumentContentFromServerPresenter.GetContent(Etc, Ec, cartableDocumentContentListener);
    }

    public StructureCartableDocumentContentDB GetFromLocal() {
        StructureCartableDocumentContentDB cartableDocumentContentDB = new StructureCartableDocumentContentDB();
        if (farzinCartableQuery.IsDocumentContentExist(Etc, Ec)) {
            cartableDocumentContentDB = farzinCartableQuery.getDocumentContent(Etc, Ec);
        }

        return cartableDocumentContentDB;
    }


    private void SaveData(final String FileBinary) {

        StructureCartableDocumentContentBND cartableDocumentContentBND = new StructureCartableDocumentContentBND(FileBinary, Etc, Ec);
        farzinCartableQuery.saveCartableDocumentContent(cartableDocumentContentBND, new CartableDocumentContentQuerySaveListener() {

            @Override
            public void onSuccess(StructureCartableDocumentContentDB structureCartableDocumentContentDB) {
                listenerCartableDocumentContent.newData(structureCartableDocumentContentDB.getFile_binary());
            }

            @Override
            public void onExisting() {
                listenerCartableDocumentContent.noData();
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
                if (App.networkStatus != NetworkStatus.Connected || App.networkStatus != NetworkStatus.Syncing) {
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
                GetFromServer();
            }
        }, DELAY);
    }


}