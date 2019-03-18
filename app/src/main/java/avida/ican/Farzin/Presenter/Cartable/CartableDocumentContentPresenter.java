package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentContentBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
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
        initCartableDocumentContenthListListener();
    }

    private void initCartableDocumentContenthListListener() {
        getCartableDocumentContentFromServerPresenter = new GetCartableDocumentContentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentContentListener = new CartableDocumentContentListener() {


            @Override
            public void onSuccess(StringBuilder FileBinaryAsStringBuilder) {
                SaveData(FileBinaryAsStringBuilder);
            }

            @Override
            public void onFailed(String message) {
                listenerCartableDocumentContent.noData();
                /*if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onFailed("");
                        }
                    }, 300);
                } else {
                    reGetData();
                }*/
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
        getCartableDocumentContentFromServerPresenter.GetContent(Etc, Ec, cartableDocumentContentListener);
    }

    public List<StructureCartableDocumentContentDB> GetFromLocal() {
        List<StructureCartableDocumentContentDB> cartableDocumentContentDB = new ArrayList<>();
        if (farzinCartableQuery.IsDocumentContentExist(Etc, Ec)) {
            cartableDocumentContentDB = farzinCartableQuery.getDocumentContent(Etc, Ec);
        }

        return cartableDocumentContentDB;
    }


    private void SaveData(final StringBuilder fileBinaryAsStringBuilder) {

        StructureCartableDocumentContentBND cartableDocumentContentBND = new StructureCartableDocumentContentBND(fileBinaryAsStringBuilder, Etc, Ec);
        farzinCartableQuery.saveCartableDocumentContent(cartableDocumentContentBND, new CartableDocumentContentQuerySaveListener() {


            @Override
            public void onSuccess(List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB) {
                listenerCartableDocumentContent.newData(structureCartableDocumentContentsDB);

            }

            @Override
            public void onExisting() {
                listenerCartableDocumentContent.noData();
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
                if (App.networkStatus != NetworkStatus.Connected || App.networkStatus != NetworkStatus.Syncing) {
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
