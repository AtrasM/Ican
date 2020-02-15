package avida.ican.Farzin.Presenter.Cartable;

import android.app.Activity;
import android.os.Handler;

import org.acra.ACRA;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentDataListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentListListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentQuerySaveListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureInboxDocumentRES;
import avida.ican.Farzin.Presenter.Notification.CartableDocumentNotificationPresenter;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;

/**
 * Created by AtrasVida on 2018-12-26 at 5:30 PM
 */

public class FarzinCartableDocumentListPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final long FAILED_DELAY = TimeValue.SecondsInMilli() * 2;
    private FarzinCartableQuery farzinCartableQuery;
    private GetCartableDocumentFromServerPresenter getCartableDocumentFromServerPresenter;
    private CartableDocumentDataListener cartableDocumentDataListener;
    private CartableDocumentListListener cartableDocumentListListenerMain;
    private Status status;
    private int Count = 200;
    private int existCont = 0;
    private int dataSize = 0;
    private boolean isGetManual = false;
    private Activity context;
    private static int newCount = 0;
    private CartableDocumentNotificationPresenter cartableDocumentNotificationPresenter;

    public FarzinCartableDocumentListPresenter(Activity context, CartableDocumentDataListener cartableDocumentDataListener) {
        this.context = context;
        cartableDocumentNotificationPresenter = new CartableDocumentNotificationPresenter(context);
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
                    App.getHandler().postDelayed(() -> invalidLogin(""), 300);
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

    public void getFromServer() {
        newCount = 0;
        getCartableDocumentFromServerPresenter.GetCartableDocumentList(Count, cartableDocumentListListenerMain);
    }

    public void getFromServer(String startDateTime, String finishDateTime, boolean isGetManual) {
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            cartableDocumentDataListener.onFailed(Resorse.getString(R.string.unable_get_data_in_ofline_mode));
        } else {
            newCount = 0;
            this.isGetManual = isGetManual;
            getCartableDocumentFromServerPresenter.GetCartableDocumentList(startDateTime, finishDateTime, cartableDocumentListListenerMain);

        }

    }

    public List<StructureInboxDocumentDB> getFromLocal(int actionCode, long Start, long Count) {
        return farzinCartableQuery.getCartableDocuments(actionCode, null, Start, Count);
    }

    public List<StructureInboxDocumentDB> getFromLocal(int actionCode, Status status, long Start, long Count) {
        return farzinCartableQuery.getCartableDocuments(actionCode, status, Start, Count);
    }

    public StructureInboxDocumentDB getLastRecord() {
        return farzinCartableQuery.getLastItemCartableDocument();
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

                if (structureInboxDocumentDB == null || structureInboxDocumentDB.getId() <= 0) {
                    existCont++;
                } else {
                    if (getFarzinPrefrences().isCartableDocumentForFirstTimeSync() && getFarzinPrefrences().isDataForFirstTimeSync()) {
                        farzinCartableQuery.updateCartableDocumentIsNewStatus(structureInboxDocumentDB.getId(), true);
                    }
                    if (!structureInboxDocumentDB.isRead()) {
                        newCount++;
                    }
                }

                try {
                    inboxCartableDocumentList.remove(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (inboxCartableDocumentList.size() == 0) {
                    callMulltiNotification();
                } else {
                    SaveData(inboxCartableDocumentList);
                }
            }

            @Override
            public void onExisting() {
                existCont++;
                inboxCartableDocumentList.remove(0);
                if (inboxCartableDocumentList.size() == 0) {
                    if (isGetManual) {
                        if (existCont == dataSize) {
                            cartableDocumentDataListener.noData();
                        } else {
                            callMulltiNotification();
                        }

                    } else {
                        if (existCont == dataSize || existCont > 50) {
                            cartableDocumentDataListener.noData();
                        } else {
                            callMulltiNotification();
                        }
                    }

                } else {
                    SaveData(inboxCartableDocumentList);
                }

            }

            @Override
            public void onFailed(final String message) {
                App.getHandlerMainThread().postDelayed(() -> {
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
                App.getHandlerMainThread().postDelayed(() -> {
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

    private void callMulltiNotification() {
        try {
            if (newCount > 0) {
                long count = new FarzinCartableQuery().getNewCartableDocumentCount();
                if (count > 0) {
                    String title = Resorse.getString(context, R.string.Cartable);
                    String message = count + " " + Resorse.getString(context, R.string.NewCartableDocument);
                    cartableDocumentNotificationPresenter.callNotification(title, "" + message, cartableDocumentNotificationPresenter.GetNotificationPendingIntent(cartableDocumentNotificationPresenter.GetNotificationManagerIntent()));
                }
            }
            newCount = 0;
            cartableDocumentDataListener.newData();
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }


    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}
