package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentDataListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Ican.View.Custom.TimeValue;

/**
 * Created by AtrasVida on 2018-12-26 at 5:30 PM
 */

public class FarzinCartableDocumentListPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 2;
    private Handler handler = new Handler();
    private FarzinCartableQuery farzinCartableQuery;
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private CartableDocumentDataListener cartableDocumentDataListener;
    private CartableDocumentListListener cartableDocumentListListenerMain;
    private Status status;
    private int Count = 500;
    private int existCont = 0;
    private int dataSize = 0;

    public FarzinCartableDocumentListPresenter(CartableDocumentDataListener cartableDocumentDataListener) {
        this.cartableDocumentDataListener = cartableDocumentDataListener;
        initCartableDocumentListListener();
    }

    private void initCartableDocumentListListener() {
        getCartableDocumentFromServerPresenter = new GetCartableDocumentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentListListenerMain = new CartableDocumentListListener() {
            @Override
            public void onSuccess(ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {
                existCont = 0;
                dataSize = inboxCartableDocumentList.size();
                if (dataSize == 0) {
                    cartableDocumentDataListener.noData();
                } else {
                    SaveData(inboxCartableDocumentList);

                }
            }

            @Override
            public void onFailed(String message) {
                cartableDocumentDataListener.onFailed(message);
              /*  if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
                    App.getHandler().postDelayed(() -> onFailed(""), 300);
                } else {
                    reGetData();
                }*/
            }

            @Override
            public void onCancel() {
                cartableDocumentDataListener.onFailed("");

            }
        };
    }

    public void GetFromServer() {
        getCartableDocumentFromServerPresenter.GetCartableDocumentList(Count, cartableDocumentListListenerMain);
    }

    public void GetFromServer(String startDateTime, String finishDateTime) {
        getCartableDocumentFromServerPresenter.GetCartableDocumentList(startDateTime, finishDateTime, cartableDocumentListListenerMain);
    }

    public List<StructureInboxDocumentDB> GetFromLocal(int actionCode, long Start, long Count) {
        return farzinCartableQuery.getCartableDocuments(actionCode, null, Start, Count);
    }

    public List<StructureInboxDocumentDB> GetFromLocal(int actionCode, Status status, long Start, long Count) {
        return farzinCartableQuery.getCartableDocuments(actionCode, status, Start, Count);
    }

    private void SaveData(final ArrayList<StructureInboxDocumentRES> inboxCartableDocumentList) {

        final StructureInboxDocumentRES structureInboxDocumentRES = inboxCartableDocumentList.get(0);

        if (structureInboxDocumentRES.isRead()) {
            status = Status.READ;
        } else {
            if (!getFarzinPrefrences().isCartableDocumentForFirstTimeSync()) {
                status = Status.IsNew;
            } else {
                status = Status.UnRead;
            }

        }

        farzinCartableQuery.saveCartableDocument(structureInboxDocumentRES, Type.RECEIVED, status, new CartableDocumentQuerySaveListener() {

            @Override
            public void onSuccess(StructureInboxDocumentDB structureInboxDocumentDB) {
                try {
                    inboxCartableDocumentList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (inboxCartableDocumentList.size() == 0) {
                    cartableDocumentDataListener.newData();
                } else {
                    SaveData(inboxCartableDocumentList);
                }

            }

            @Override
            public void onExisting() {
                existCont++;
                inboxCartableDocumentList.remove(0);
                if (inboxCartableDocumentList.size() == 0) {
                    if (existCont == dataSize || existCont > 50) {
                        cartableDocumentDataListener.noData();
                    } else {
                        cartableDocumentDataListener.newData();
                    }

                } else {
                    SaveData(inboxCartableDocumentList);
                }

            }

            @Override
            public void onFailed(final String message) {

                handler.postDelayed(() -> {
                    if (inboxCartableDocumentList.size() == 0) {
                        cartableDocumentDataListener.onFailed(message);
                    } else {
                        try {
                            inboxCartableDocumentList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveData(inboxCartableDocumentList);
                    }

                }, FAILED_DELAY);


            }

            @Override
            public void onCancel() {
                handler.postDelayed(() -> {
                    if (inboxCartableDocumentList.size() == 0) {
                        cartableDocumentDataListener.onFailed("");
                    } else {
                        try {
                            inboxCartableDocumentList.remove(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SaveData(inboxCartableDocumentList);
                    }

                }, FAILED_DELAY);

            }
        });

    }



    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
